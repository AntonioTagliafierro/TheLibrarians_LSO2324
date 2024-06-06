#include "router.h"
#include <string.h>
#include <stdlib.h>
#include "../handlers/basic_handlers.h"
#include "../handlers/bookhandler/bookhandler.h"
#include "../handlers/user/user.h"
#include "../handlers/bag/baghandler.h"
#include "../handlers/LendLease/LendLease.h"
#include "../middleware/middleware.h"
#include "../../utils/file/file.h"

#define NO_MIDDLEWARE NULL
#define MAX_PATH_LENGTH 1024
#define ROUTES_PREFIX "/api"

//aggiungere le routes
static Route routes[] = {

    { "POST", "/login", NO_MIDDLEWARE, loginHandler },
    { "POST", "/register", NO_MIDDLEWARE, registerHandler },
    { "GET", "/", requiresAuth, homeHandler },
    { "GET", "/user/:email", NO_MIDDLEWARE, getUserHandler },
    { "GET", "/book/image/:isbn", NO_MIDDLEWARE, getBookImageHandler },
    { "GET", "/book/:isbn", NO_MIDDLEWARE, getBookHandler },
    { "GET", "/books", NO_MIDDLEWARE, getBooksHandler },
    { "POST", "/lendlease/book", requiresAuth, lendLeaseHandler },
    { "PUT", "/lendlease/book", requiresAuth, setDeliveredLendLeaseHandler },
    { "GET", "/lendlease", requiresAuth, getLendLeaseMadeByUserHandler },
    { "GET", "/lendlease/:isbn", requiresAuth, getLendLeaseHandler },
    { "POST", "/bag/book", requiresAuth, bagHandler },
    { "PUT", "/bag/book", requiresAuth, removeBagHandler },

}





// Implementation of routeRequest
void routeRequest(RouterParams params) {
    // Determine the number of routes in the array
    int numRoutes = sizeof(routes) / sizeof(Route);

    // Iterate through the routes to find a matching one
    for (int i = 0; i < numRoutes; i++) {
        // Construct the full route path by combining the prefix and the route path
        char fullRoutePath[strlen(ROUTES_PREFIX) + strlen(routes[i].path) + 1];
        snprintf(fullRoutePath, sizeof(fullRoutePath), "%s%s", ROUTES_PREFIX, routes[i].path);

        // Check if the request's method and path match the current route
        if (params.request.method == routes[i].method && matchesPath(params.request.path, fullRoutePath)) {
            // Invoke the corresponding route handler or middleware
            if (routes[i].middleware == NO_MIDDLEWARE) {
                routes[i].handler(params);
            } else {
                routes[i].middleware(params, routes[i].handler);
            }
            return; // Exit the function after handling the request
        }
    }

    // If no matching route is found, serve a default 404 response
    serveFileWithResponseCode("images/404.jpg", "404 Not Found", params.thread_data->client_socket);
}

// Implementation of matchesPath
bool matchesPath(const char* requestPath, const char* routePath) {
    int requestPathLen = strlen(requestPath);
    int routePathLen = strlen(routePath);

    // Check if the lengths of the request and route paths match
    if (requestPathLen != routePathLen) {
        return false;
    }

    // Compare each character in the paths
    for (int i = 0; i < requestPathLen; i++) {
        if (routePath[i] == ':') {
            // If a dynamic parameter is encountered, find its end in the request path
            while (i < requestPathLen && requestPath[i] != '/') {
                i++;
            }
        } else if (requestPath[i] != routePath[i]) {
            // If characters do not match, the paths do not coincide
            return false;
        }
    }

    return true; // Paths match
}

// Implementation of getPathParameter
const char* getPathParameter(const char* path) {
    const char* paramPos = strstr(path, "/:");
    if (paramPos == NULL) {
        return NULL; // No dynamic parameter found in the path
    }

    // Find the end of the dynamic parameter in the path
    const char* paramEnd = strchr(paramPos + 2, '/');
    if (paramEnd == NULL) {
        paramEnd = path + strlen(path); // Dynamic parameter is at the end of the path
    }

    // Extract the dynamic parameter from the path
    size_t paramLen = paramEnd - (paramPos + 2);
    char* param = (char*)malloc(paramLen + 1);
    strncpy(param, paramPos + 2, paramLen);
    param[paramLen] = '\0';

    return param;
}
