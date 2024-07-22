#ifndef SERVER_H
#define SERVER_H

#include <postgresql/libpq-fe.h>

#define DATABASE "host=DBpostgres port=5432 dbname=thelibrarians user=docker password=thelibrarians"
#define PORT 4040
#define MAX_CLIENTS 100
#define TIMEOUT_SECONDS_ABORT_CONNECTION 5
#define SIZE_BUFFER 1024

typedef struct {
    int client_socket;
    PGconn* connection;
}ThreadData;

#endif
