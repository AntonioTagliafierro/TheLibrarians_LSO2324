#include <stdbool.h>
#include <string.h>
#include <sys/socket.h>
#include "router.h"
#include "../handlers/basic_handlers.h"
#include "../handlers/user/user_handlers.h"
#include "../handlers/book/book_handlers.h"
#include "../handlers/lendlease/lendlease_handlers.h"
#include "../handlers/bag/bag_handlers.h"
#include "../middleware/middleware.h"
#include "../../utils/file_helper/file_helper.h"
#define NO_MIDDLEWARE NULL
#define MAX_PATH_LENGTH 1024
#define ROUTES_PREFIX "/api"

Route routes[] = {

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
};

void routeRequest(RouterParams params) {
    int numRoutes = sizeof(routes) / sizeof(Route);
    for (int i = 0; i < numRoutes; i++) {
        char routePath[MAX_PATH_LENGTH];
        snprintf(routePath, sizeof(routePath), "%s%s", ROUTES_PREFIX, routes[i].path);
        if (strcmp(params.request.method, routes[i].method) == 0 && matchesPath(params.request.path, routePath)) {
            if (routes[i].middleware == NO_MIDDLEWARE) {
                // Se non � presente il middleware, chiama direttamente la funzione handler
                routes[i].handler(params);
            } else {
                // Altrimenti esegue il middleware
                routes[i].middleware(params, routes[i].handler);
            }
            return;
        }
    }
    // Se nessuna route corrispondente � stata trovata, invia una risposta 404 Not Found al client
    serveFileWithResponseCode( "images/404.jpg", "404 Not Found",params.thread_data->client_socket);
}

// match per routes con sintassi "basepath/:param"
// esempio: /user/:id deve matchare con la request /user/1
bool matchesPath(const char* requestPath, const char* routePath) {
    int requestPathLen = strlen(requestPath);
    int routePathLen = strlen(routePath);

    // slash count di requestPath
    int requestPathSlashCount = 0;
    for (int i = 0; i < requestPathLen; i++) {
        if (requestPath[i] == '/') {
            requestPathSlashCount++;
        }
    }
    // slash count di routePath
    int routePathSlashCount = 0;
    for (int i = 0; i < routePathLen; i++) {
        if (routePath[i] == '/') {
            routePathSlashCount++;
        }
    }
    if(requestPathSlashCount != routePathSlashCount) {
        return false;
    }
    // Controlla se il routePath ha un parametro dinamico
    const char* paramPos = strstr(routePath, "/:");
    if (paramPos == NULL) {
        return strcmp(requestPath, routePath) == 0;  // Nessun parametro dinamico nel routePath, confronta i percorsi direttamente
    }

    // Controlla se il routePath corrisponde al percorso della richiesta con un parametro dinamico
    int paramIdx = paramPos - routePath + 1;  // Indice del primo carattere del parametro (ignorando "/")
    int staticPartLen = paramIdx - 1;  // Lunghezza della parte statica del percorso (ignorando "/")
    bool match = false;

    if (requestPathLen >= staticPartLen) {
        // Verifica se la parte statica corrisponde
        if (strncmp(requestPath, routePath, staticPartLen) == 0) {
            match = true;
            // Verifica se la parte dinamica corrisponde
            const char* requestDynamicPart = requestPath + staticPartLen;
            const char* nextSlashPos = strchr(requestDynamicPart, '/');
            if (nextSlashPos != NULL) {
                int dynamicPartLen = nextSlashPos - requestDynamicPart;
                const char* routeDynamicPart = routePath + paramIdx;
                int remainingLen = requestPathLen - staticPartLen - dynamicPartLen;
                if (remainingLen > 0 && strncmp(requestDynamicPart, routeDynamicPart, dynamicPartLen) != 0) {
                    match = false;
                }
            }
        }
    }

    return match;
}


const char* getPathParameter(const char* path) {
    if (path == NULL || *path == '\0') {
        return NULL;
    }
    const char* lastSlash = strrchr(path, '/');
    if (lastSlash != NULL && *(lastSlash + 1) != '\0') {
        return lastSlash + 1;
    } else {
        return NULL;
    }
}
