#include <postgresql/libpq-fe.h>
#include <stdlib.h>
#include <stdbool.h>
#include <string.h>
#include "../../models.h"

User* authenticateUser(PGconn* connection, const char* email, const char* password) {
    const char* query = "SELECT * FROM \"Users\" WHERE \"email\" = $1 AND \"password\" = $2;";
    const char* param_values[2];
    param_values[0] = email;
    param_values[1] = password;
    const int param_lengths[2] = { strlen(email), strlen(password) };
    const int param_formats[2] = { 0, 0 };

    PGresult* result = PQexecParams(connection, query, 2, NULL, param_values, param_lengths, param_formats, 0);

    if (PQresultStatus(result) == PGRES_TUPLES_OK) {
        int rows = PQntuples(result);
        if (rows == 0) {
            return NULL;
        }
        
        User* user = malloc(sizeof(User));
        user->id = atoi(strdup(PQgetvalue(result, 0, 0)));
        user->password = strdup(PQgetvalue(result, 0, 1));
        user->email = strdup(PQgetvalue(result, 0, 2));
        PQclear(result);
        return user;
    } else {
        return NULL; 
    }
}

User* registerUser(PGconn* connection, const char* email, const char* password) {
    const char* query = "INSERT INTO \"Users\" (\"email\", \"password\") VALUES ($1, $2) RETURNING *;";
    const char* param_values[2];
    param_values[0] = email;
    param_values[1] = password;
    const int param_lengths[2] = { strlen(email), strlen(password) };
    const int param_formats[2] = { 0, 0 };

    PGresult* result = PQexecParams(connection, query, 2, NULL, param_values, param_lengths, param_formats, 0);

    if (PQresultStatus(result) == PGRES_TUPLES_OK) {
        int rows = PQntuples(result);
        if (rows == 0) {
            return NULL;
        }
        
        User* user = malloc(sizeof(User));
        user->id = atoi(strdup(PQgetvalue(result, 0, 0)));
        user->email = strdup(PQgetvalue(result, 0, 1));
        user->password = strdup(PQgetvalue(result, 0, 2));
        PQclear(result);
        return user;
    } else {
        return NULL; 
    }
}

User* getUserById(PGconn* connection, int id) {
    const char* query = "SELECT * FROM \"Users\" WHERE \"id\" = $1;";
    const char* param_values[1];
    char id_str[10];
    sprintf(id_str, "%d", id);
    param_values[0] = id_str;
    const int param_lengths[1] = { strlen(id_str) };
    const int param_formats[1] = { 0 };

    PGresult* result = PQexecParams(connection, query, 1, NULL, param_values, param_lengths, param_formats, 0);

    if (PQresultStatus(result) == PGRES_TUPLES_OK) {
        int rows = PQntuples(result);
        if (rows == 0) {
            return NULL;
        }
        
        User* user = malloc(sizeof(User));
        user->id = atoi(strdup(PQgetvalue(result, 0, 0)));
        user->password = strdup(PQgetvalue(result, 0, 1));
        user->email = strdup(PQgetvalue(result, 0, 2));
        PQclear(result);
        return user;
    } else {
        return NULL; 
    }
}

User* getUserByEmail(PGconn* connection, const char* email) {
    const char* query = "SELECT * FROM \"Users\" WHERE \"email\" = $1;";
    const char* param_values[1];
    param_values[0] = email;
    const int param_lengths[1] = { strlen(email) };
    const int param_formats[1] = { 0 };

    PGresult* result = PQexecParams(connection, query, 1, NULL, param_values, param_lengths, param_formats, 0);

    if (PQresultStatus(result) == PGRES_TUPLES_OK) {
        int rows = PQntuples(result);
        if (rows == 0) {
            return NULL;
        }
        
        User* user = malloc(sizeof(User));
        user->id = atoi(strdup(PQgetvalue(result, 0, 0)));
        user->password = strdup(PQgetvalue(result, 0, 1));
        user->email = strdup(PQgetvalue(result, 0, 2));
        PQclear(result);
        return user;
    } else {
        return NULL; 
    }
}