#ifndef MIDDLEWARE_H
#define MIDDLEWARE_H

#include "../date/datehandler.h"
#include "middleware.h"

// Helper functions
void sendBadRequest(int client_socket);
void sendUnauthorized(int client_socket);
void handleValidToken(RouterParams params, TokenPayload* payload, void (*next)(RouterParams params));

// Main function of the authentication middleware
void requiresAuth(RouterParams params, void (*next)(RouterParams params));


#endif
