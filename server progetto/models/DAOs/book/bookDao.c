#include <postgresql/libpq-fe.h>
#include <stdlib.h>
#include <stdbool.h>
#include <string.h>
#include "../models.h"



Book* getBookByISBN(PGconn* connection, const char* isbn) {
    const char* query = "SELECT * FROM \"Books\" WHERE \"isbn\" = $1;";
    const char* param_values[1] = { isbn };
    const int param_lengths[1] = { strlen(isbn) };
    const int param_formats[1] = { 0 };

    PGresult* result = PQexecParams(connection, query, 1, NULL, param_values, param_lengths, param_formats, 0);

    if (PQresultStatus(result) == PGRES_TUPLES_OK) {
        int rows = PQntuples(result);
        if (rows == 0) {
            PQclear(result);
            return NULL;
        }

        Book* book = malloc(sizeof(Book));
        book->isbn = strdup(PQgetvalue(result, 0, 0));
        book->title = strdup(PQgetvalue(result, 0, 1));
        book->authors = strdup(PQgetvalue(result, 0, 2));
        book->description = strdup(PQgetvalue(result, 0, 3));
        book->genre = strdup(PQgetvalue(result, 0, 4));
        book->image_url = strdup(PQgetvalue(result, 0, 5));
        book->total_copies = atoi(strdup(PQgetvalue(result, 0, 6)));
        book->copiesonlendlease = atoi(strdup(PQgetvalue(result, 0, 7)));
        PQclear(result);
        return book;
    } else {
        PQclear(result);
        return NULL;
    }
}

PGresult* getBooks(PGconn* connection) {
    const char* query = "SELECT * FROM books;";
    PGresult* result = PQexec(connection, query);
    return result;
}
