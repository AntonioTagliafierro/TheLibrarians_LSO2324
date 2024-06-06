#ifndef ROUTER_H
#define ROUTER_H

#include <stdbool.h>
#include <sys/socket.h>



typedef enum {
    HTTP_GET,
    HTTP_POST,
    HTTP_PUT,
    HTTP_DELETE
} HttpMethod;


typedef struct {
    HttpMethod method;
    const char* path;
    Middleware middleware;
    Handler handler;
} Route;


typedef struct {
    RequestData request;
    Thread* thread_data;
} RouterParams;

// Funzione per instradare la richiesta alla route corretta
void routeRequest(RouterParams params);



#endif
