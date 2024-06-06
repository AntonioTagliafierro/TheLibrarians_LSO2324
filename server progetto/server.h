#ifndef SERVER_H
#define SERVER_H

#include <postgresql/libpq-fe.h>

#define DATABASE= "TODO"
#define PORT="TODO"
#define MAX_CLIENTS 100;
#define TIMEOUT_ABORT_CONNECTION 10;
#define SIZE_BUFFER 1024;

typedef struct {
    int client_socket;
    PGconn* connection;
}Thread;

#endif
