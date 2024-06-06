#include "handler.h"
#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include <postgresql/libpq-fe.h>
#include "../json/json.h"
#include "../jwt/jwt_header.h"
#include "../http/http.h"
#include "../file/file.h"
#include "../../models/models.h"
#include "../crypt/crypt.h"
#include "../date/datehandler.h"



// Function to send an HTTP response to the client
void sendHttpResponse(int clientSocket, const char* httpResponse) {
    send(clientSocket, httpResponse, strlen(httpResponse), 0);
}

// Function to handle the home route
void homeHandler(RouterParams params) {
    // Attempt to decode JWT from the 'authorization' field
    TokenPayload* token = decodeToken(params.request.authorization);

    if (token != NULL) {
        // Extract information from the decoded token
        const char* decoded_email = token->email;
        int decoded_id = token->id;

        // Create a formatted message
        char body[100];
        snprintf(body, sizeof(body), "Hello, %s. Your id:%d", decoded_email, decoded_id);

        // Create an HTTP response
        HttpResponse response;
        response.code = "200 OK";
        response.contentType = "application/json";
        response.body = body;

        // Format and send the HTTP response
        sendHttpResponse(params.thread_data->client_socket, &response);

        // Free the allocated memory for the token
        free(token);
    } else {
        // If token decoding fails, send a 500 Server Error response
        sendHttpResponse(params.thread_data->client_socket, "HTTP/1.1 500 Server Error\r\nContent-Length: 0\r\n\r\n");
    }
}
