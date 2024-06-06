#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <pthread.h>
#include <sys/socket.h>
#include <arpa/inet.h>
#include <errno.h>
#include <signal.h>
#include "server.h"
#include "utils/json/json.h"
#include "utils/http/http.h"
#include "routing/router/router.h"

// Global variable to track the number of connected clients
int client_count = 0;

// Volatile flag to indicate when the server should stop
volatile sig_atomic stop_server = 0;

// Signal handler for interrupt (SIGINT) to gracefully shut down the server
void handle_interrupt(int sig) {
    stop_server = 1;
    printf("Server is shutting down\n");
}

// Function to handle each client connection in a separate thread
void* client_thread() {
    Thread* thread_data = (Thread*)arg; // There is a typo here, should be void* arg

    printf("New client connected\n");
    client_count++;
    printf("There are %d clients connected\n", client_count);
    fflush(stdout);

    // Set timeout for client socket
    struct timeval timeout;
    timeout.tv_sec = TIMEOUT_ABORT_CONNECTION;
    timeout.tv_usec = 0;
    setsockopt(thread_data->client_socket, SOL_SOCKET, SO_RCVTIMEO, (const char*)&timeout, sizeof(timeout));

    char buffer[SIZE_BUFFER];
    memset(buffer, 0, sizeof(buffer));

    // Receive data from the client
    ssize_t bytesRead = recv(thread_data->client_socket, buffer, sizeof(buffer) - 1, 0);

    // Check if the server is shutting down
    if(stop_server) {
        printf("Server is shutting down\n");
        printf("Closing thread\n");
        fflush(stdout);
        PQfinish(thread_data->connection);
        close(thread_data->client_socket);
        free(thread_data);
        pthread_exit(NULL);
    }
    // Handle timeout or receive error
    else if (bytesRead == -1) {
        if (errno == EAGAIN || errno == EWOULDBLOCK) {
            printf("Client socket timed out\n");
            printf("Closing thread\n");
            client_count--;
            printf("There are %d clients connected\n", client_count);
            fflush(stdout);
        }
        else {
            perror("Error receiving request from the client\n");
            printf("Client connection aborted");
            printf("Closing thread\n");
            client_count--;
            printf("There are %d clients connected\n", client_count);
            fflush(stdout);
        }
        PQfinish(thread_data->connection);
        close(thread_data->client_socket);
        free(thread_data);
        pthread_exit(NULL);
    }
    // Process the received HTTP request
    else {
        HttpRequest request;
        decodeHttpRequest(buffer, &request);
        printHttpRequest(&request);

        RouterParams params;
        params.thread_data = thread_data;
        params.request = request;
        routeRequest(params);

        PQfinish(thread_data->connection);
        close(thread_data->client_socket);
        free(thread_data);

        printf("Client served\n");
        printf("Closing thread\n");
        client_count--;
        printf("There are %d clients connected\n", client_count);
        fflush(stdout);

        pthread_exit(NULL);
    }
}

int main() {
    // Set up signal handler for interrupt (SIGINT)
    struct sigaction sa;
    sa.sa_handler = handle_interrupt;
    sigemptyset(&sa.sa_mask);
    sa.sa_flags = 0;

    if (sigaction(SIGINT, &sa, NULL) == -1) {
        perror("Error setting up signal handler");
        exit(EXIT_FAILURE);
    }

    // Initialize server and client socket variables
    int server_socket, client_socket;
    struct sockaddr_in server_addr, client_addr;
    socklen_t client_addr_len;

    // Array to hold PostgreSQL connections for each client
    PGconn *connections[MAX_CLIENTS];
    memset(connections, 0, sizeof(connections));

    // Array to hold thread IDs for each client thread
    pthread_t thread_pool[MAX_CLIENTS];

    // Create the server socket
    server_socket = socket(AF_INET, SOCK_STREAM, 0);
    if (server_socket == -1) {
        perror("Error creating server socket");
        exit(EXIT_FAILURE);
    }

    // Enable address reuse to quickly restart the server
    int reuse = 1;
    if (setsockopt(server_socket, SOL_SOCKET, SO_REUSEADDR, &reuse, sizeof(reuse)) == -1) {
        perror("Error setting SO_REUSEADDR option");
        exit(EXIT_FAILURE);
    }

    // Configure server address
    server_addr.sin_family = AF_INET;
    server_addr.sin_addr.s_addr = INADDR_ANY;
    server_addr.sin_port = htons(PORT);

    // Bind the server socket to the specified address
    if (bind(server_socket, (struct sockaddr *)&server_addr, sizeof(server_addr)) == -1) {
        perror("Error binding server socket");
        exit(EXIT_FAILURE);
    }

    // Listen for incoming client connections
    if (listen(server_socket, MAX_CLIENTS) == -1) {
        perror("Error listening on server socket");
        exit(EXIT_FAILURE);
    }

    printf("HTTP Server listening on port %d:\n", PORT);

    // Accept incoming client connections and create threads to handle them
    while (!stop_server) {
        // Accept a new client connection
        client_addr_len = sizeof(client_addr);
        client_socket = accept(server_socket, (struct sockaddr *)&client_addr, &client_addr_len);

        // Check if the server is shutting down
        if (stop_server) {
            printf("Server is shutting down, accept aborted.\n");
            break;
        }

        // Handle client connection error
        if (client_socket == -1) {
            perror("Error accepting client connection");
            continue;
        }

        // Find an available slot in the connections array
        int i;
        for (i = 0; i < MAX_CLIENTS; i++) {
            if (connections[i] == NULL || PQstatus(connections[i]) == CONNECTION_BAD) {
                break;
            }
        }

        // If the connections array is full, reject the connection
        if (i == MAX_CLIENTS) {
            fprintf(stderr, "Connection pool is full, cannot accept new connections\n");
            close(client_socket);
            continue;
        }

        // Connect to the PostgreSQL database
        connections[i] = PQconnectdb(DATABASE);
        if (PQstatus(connections[i]) != CONNECTION_OK) {
            fprintf(stderr, "Error connecting to the database: %s\n", PQerrorMessage(connections[i]));
            PQfinish(connections[i]);
            close(client_socket);
            continue;
        }

        // Create thread data and launch a new client thread
        Thread *thread_data = (Thread *)malloc(sizeof(Thread));
        thread_data->client_socket = client_socket;
        thread_data->connection = connections[i];

        if (pthread_create(&thread_pool[i], NULL, client_thread, (void *)thread_data) != 0) {
            fprintf(stderr, "(!) Error creating thread\n");
            PQfinish(connections[i]);
            close(client_socket);
            free(thread_data);
        }
    }

    // Clean up PostgreSQL connections
    for (int i = 0; i < MAX_CLIENTS; i++) {
        if (connections[i] != NULL) {
            if (PQstatus(connections[i]) == CONNECTION_OK) {
                PQfinish(connections[i]);
                connections[i] = NULL;
            }
        }
    }

    // Shut down and close the server socket
    shutdown(server_socket, SHUT_RDWR);
    close(server_socket);

    printf("Server stopped.\n");
    return 0;
}
