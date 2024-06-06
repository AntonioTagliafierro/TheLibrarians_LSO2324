#include "http.h"
#include <stdio.h>
#include <string.h>

// Decode the HTTP request from the given buffer
void decodeHttpRequest(const char* buffer, HttpRequest* request) {
    // Extract the method and path from the buffer
    sscanf(buffer, "%9s %1023s", request->method, request->path);

    // Check for the Authorization header and extract the Bearer token
    char* authorizationHeader = strstr(buffer, "Authorization: ");
    if (authorizationHeader != NULL) {
        sscanf(authorizationHeader, "Authorization: Bearer %255s", request->authorization);
    }

    // Find the start of the request body and copy it to the request structure
    char* bodyStart = strstr(buffer, "\r\n\r\n");
    if (bodyStart != NULL) {
        strncpy(request->body, bodyStart + 4, sizeof(request->body) - 1);
    }
}

// Print the details of the HTTP request
void printHttpRequest(const HttpRequest* request) {
    printf("Method: %s\n", request->method);
    printf("Path: %s\n", request->path);

    // Print Authorization information if available
    if (request->authorization[0] != '\0') {
        printf("Authorization (Bearer token): %s\n", request->authorization);
    } else {
        printf("Empty Authorization (Bearer token)\n");
    }

    // Print the request body if available
    if (request->body[0] != '\0') {
        printf("Body: %s\n", request->body);
    } else {
        printf("Empty Body\n");
    }
}

// Format the HTTP response into the provided response buffer
void formatHttpResponse(char* responseBuffer, size_t bufferSize, const HttpResponse* response) {
    // Create the HTTP response string with status code, content type, content length, and body
    snprintf(responseBuffer, bufferSize, "HTTP/1.1 %s\r\nContent-Type: %s\r\nContent-Length: %zu\r\n\r\n%s",
             response->code, response->contentType, strlen(response->body), response->body);
}

