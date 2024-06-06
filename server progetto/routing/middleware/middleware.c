#include <string.h>
#include <sys/socket.h>
#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <postgresql/libpq-fe.h>
#include "middleware.h"
#include "../jwt/jwt.h"
#include "../file/file.h"
#include "../date/datehandler.h"

// Helper function to send a Bad Request response
void sendBadRequest(int client_socket) {
    const char *response = "HTTP/1.1 400 Bad Request\r\nContent-Length: 0\r\n\r\n";
    send(client_socket, response, strlen(response), 0);
}

// Helper function to send an Unauthorized response
void sendUnauthorized(int client_socket) {
    serveBlobWithResponseCode("images/401.jpg", "401 Unauthorized", client_socket);
}

// Helper function to handle a valid token
void handleValidToken(RouterParams params, TokenPayload* payload, void (*next)(RouterParams params)) {
    DateTime dt = *datetime_parse(payload->expire);

    if (dt_greaterThen(datetime_now(), dt)) {
        printf("Token expired at: %s\n", payload->expire);
        free(payload);
        sendUnauthorized(params.thread_data->client_socket);
    } else {
        printf("Token will expire at: %s\n", payload->expire);
        free(payload);
        next(params);
    }
}

// Main function of the authentication middleware
void requiresAuth(RouterParams params, void (*next)(RouterParams params)) {
    if (verifyToken(params.request.authorization)) {
        TokenPayload* payload = decodeToken(params.request.authorization);

        if (payload == NULL) {
            // Send a Bad Request response if token decoding fails
            sendBadRequest(params.thread_data->client_socket);
        } else {
            // Handle a valid token
            handleValidToken(params, payload, next);
        }
    } else {
        // Send an Unauthorized response if the token is not valid
        sendUnauthorized(params.thread_data->client_socket);
    }
}

