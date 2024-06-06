#include <postgresql/libpq-fe.h>
#include <stdlib.h>
#include <stdbool.h>
#include <string.h>
#include "../../models.h"


bool addToBag(PGconn* connection, int id_user, int isbn) {
    const char* query = "SELECT add_Bag($1, $2);";
    const char* param_values[2];
    char id_user_str[11]; // 10 caratteri per il massimo numero di cifre di un int, più 1 per il terminatore di stringa
    snprintf(id_user_str, sizeof(id_user_str), "%d", id_user);
    param_values[0] = id_user_str;
    char isbn_str[11]; // 10 caratteri per il massimo numero di cifre di un int, più 1 per il terminatore di stringa
    snprintf(isbn_str, sizeof(isbn_str), "%d", isbn);
    param_values[1] = isbn_str;
    const int param_lengths[2] = { strlen(id_user_str), strlen(isbn_str) };
    const int param_formats[2] = { 0, 0 };

    PGresult* result = PQexecParams(connection, query, 2, NULL, param_values, param_lengths, param_formats, 0);
    if (PQresultStatus(result) == PGRES_TUPLES_OK) {
        PQclear(result);
        return true;
    } else {
        PQclear(result);
        return false;
    }
}

bool removeFromBag(PGconn* connection, int id_user, int isbn) {
    const char* query = "SELECT remove_Bag($1, $2);";
    const char* param_values[2];
    char id_user_str[11]; // 10 caratteri per il massimo numero di cifre di un int, più 1 per il terminatore di stringa
    snprintf(id_user_str, sizeof(id_user_str), "%d", id_user);
    param_values[0] = id_user_str;
    char isbn_str[11]; // 10 caratteri per il massimo numero di cifre di un int, più 1 per il terminatore di stringa
    snprintf(isbn_str, sizeof(isbn_str), "%d", isbn);
    param_values[1] = isbn_str;
    const int param_lengths[2] = { strlen(id_user_str), strlen(isbn_str) };
    const int param_formats[2] = { 0, 0 };

    PGresult* result = PQexecParams(connection, query, 2, NULL, param_values, param_lengths, param_formats, 0);
    if (PQresultStatus(result) == PGRES_TUPLES_OK) {
        PQclear(result);
        return true;
    } else {
        PQclear(result);
        return false;
    }
}

PGresult* getBagByUser(PGconn* connection, int id_user) {
    const char* query = "SELECT * FROM bag WHERE id_user = $1;";
    const char* param_values[1];
    char id_str[11]; // 10 caratteri per il massimo numero di cifre di un int, più 1 per il terminatore di stringa
    snprintf(id_str, sizeof(id_str), "%d", id_user);
    param_values[0] = id_str;
    const int param_lengths[1] = { strlen(id_str) };
    const int param_formats[1] = { 0 };

    PGresult* result = PQexecParams(connection, query, 1, NULL, param_values, param_lengths, param_formats, 0);
    if (PQresultStatus(result) != PGRES_TUPLES_OK) {
        fprintf(stderr, "Query failed: %s", PQerrorMessage(connection));
        PQclear(result);
        return NULL;
    }

    return result;
}

