#include <postgresql/libpq-fe.h>
#include <stdlib.h>
#include <stdbool.h>
#include <string.h>
#include "../../models.h"


bool orderLendLease(PGconn* connection, int id_user, int isbn) {
    const char* query = "SELECT order_lendlease($1, $2);";
    const char* param_values[2];
    char id_user_str[11]; // 10 caratteri per il massimo numero di cifre di un int, pi� 1 per il terminatore di stringa
    snprintf(id_user_str, sizeof(id_user_str), "%d", id_user);
    param_values[0] = id_user_str;
    char isbn_str[11]; // 10 caratteri per il massimo numero di cifre di un int, pi� 1 per il terminatore di stringa
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


PGresult* getLendLeaseByUser(PGconn* connection, int id_user) {
    const char* query = (
        "SELECT \"LendLease\".*, \"Books\".\"isbn\" , \"Books\".\"title\", \
         \"Books\".\"image_url\", \"Books\".\"genre\" , \"Books\".\"authors\" \
        FROM \"LendLeases\" \
        INNER JOIN \"Books\" ON \"LendLeases\".\"isbn\" = \"Books\".\"isbn\" \
        WHERE \"id_user\" = $1;"

    );
    const char* param_values[1];
    char id_user_str[10];
    sprintf(id_user_str, "%d", id_user);
    param_values[0] = id_user_str;
    const int param_lengths[1] = { strlen(id_user_str) };
    const int param_formats[1] = { 0 };

    PGresult* result = PQexecParams(connection, query, 1, NULL, param_values, param_lengths, param_formats, 0);
    return result;
}



PGresult* getBooksByLendLeaseId(PGconn* connection, int id, float *total) {
    // bisogna selezionare in questo modo per evitare di avere attributi con lo stesso nome
    const char* query = (
        "SELECT \"LendLease\".*, \"Books\".\"isbn\" , \"Books\".\"title\", \
         \"Books\".\"image_url\", \"Books\".\"genre\" , \"Books\".\"authors\" \
        FROM \"LendLeases\" \
        INNER JOIN \"Books\" ON \"LendLeases\".\"isbn\" = \"Books\".\"isbn\" \
        WHERE \"id\" = $1;"

    );
    const char* param_values[1];
    char id_str[10];
    sprintf(id_str, "%d", id);
    param_values[0] = id_str;
    const int param_lengths[1] = { strlen(id_str) };
    const int param_formats[1] = { 0 };

    PGresult* result = PQexecParams(connection, query, 1, NULL, param_values, param_lengths, param_formats, 0);
    *total = 0;
    for (int i = 0; i < PQntuples(result); i++) {
        //printf("price = %s\n", PQgetvalue(result, i, 8));
        //printf("quantity = %s\n", PQgetvalue(result, i, 3));
        *total += atoi(PQgetvalue(result,i,3))*atof(PQgetvalue(result, i, 8));
    }
    return result;
}


LendLease* getLendLeaseById(PGconn* connection, int id) {
    const char* query = "SELECT * FROM \"LendLease\" WHERE \"id\" = $1;";
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
        
        LendLease* lendlease = malloc(sizeof(LendLease));
        lendlease->isbn = atoi(strdupPQgetvalue(result, 0, 5));
        lendlease->id = atoi(strdup(PQgetvalue(result, 0, 3)));
        lendlease->id_user = atoi(strdup(PQgetvalue(result, 0, 0)));
        lendlease->creation_datetime = strdup(PQgetvalue(result, 0, 1));
        lendlease->due_datetime = strdup(PQgetvalue(result, 0, 2));
        lendlease->delivered = (strcmp(PQgetvalue(result, 0, 4),"t") == 0 ? true : false);
        PQclear(result);
        return lendlease;
    } else {
        return NULL; 
    }
}

PGresult* getAllLendLease(PGconn* connection) {
    const char* query = (
        "SELECT \"LendLease\".*, \"Books\".\"isbn\" , \"Books\".\"title\", \
         \"Books\".\"image_url\", \"Books\".\"genre\" , \"Books\".\"authors\" \
        FROM \"LendLeases\" \
        INNER JOIN \"Books\" ON \"LendLeases\".\"isbn\" = \"Books\".\"isbn\" ;"

    );
    PGresult* result = PQexec(connection, query);
    return result;
}



bool deliveredLendLease(PGconn* connection, int id_user, int id_lendlease){
    const char* query = "UPDATE delivered_lendlease($1,$2);";
    const char* param_values[2];
    char id_user_str[10];
    sprintf(id_user_str, "%d", id_user);
    param_values[0] = id_user_str;
    char id_lendlease_str[10];
    sprintf(id_lendlease_str, "%d", id_lendlease);
    param_values[1] = id_lendlease_str;
    const int param_lengths[2] = { strlen(id_user_str), strlen(id_lendlease_str) };
    const int param_formats[2] = { 0 , 0 };

    PGresult* result = PQexecParams(connection, query, 2, NULL, param_values, param_lengths, param_formats, 0);
    if(PQresultStatus(result) == PGRES_TUPLES_OK){
        PQclear(result);
        return true;
    } else{
        PQclear(result);
        return false;
    }
}