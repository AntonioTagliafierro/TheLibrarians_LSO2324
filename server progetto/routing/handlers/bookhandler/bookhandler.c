#include <string.h>
#include <sys/socket.h>
#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <postgresql/libpq-fe.h>
#include "../../..json/json.h"
#include "../../router/router.h"
#include "../../../jwt/jwt.h"
#include "../../../http/httpr.h"
#include "../../../file/filer.h"
#include "../../../models/models.h"
#include "../../../crypt/crypt.h"
#include "../../../date/date.h"
#include "../../../models/DAOs/book/bookDao.h"


void getBookImageHandler(RouterParams params) {
    // ottieni il parametro id dalla path
    const char* str_isbn = getPathParameter(params.request.path);
    if(str_isbn == NULL){
        const char *response = "HTTP/1.1 400 Bad Request\r\nContent-Length: 0\r\n\r\n";
        send(params.thread_data->client_socket, response, strlen(response), 0);
        return;
    }
    Book* book = getDrinkById(params.thread_data->connection,atoi(str_isbn));
    if(book == NULL){
        // 404 Drink not found
        const char *response = "HTTP/1.1 404 Not Found\r\nContent-Length: 0\r\n\r\n";
        send(params.thread_data->client_socket, response, strlen(response), 0);
        return;
    }
    const char* blobName = book->blobName;

    serveBlob(blobName,params.thread_data->client_socket);

    free(book);
}

void getBookHandler(RouterParams params) {
    const char* str_isbn = getPathParameter(params.request.path);
    if(str_isbn == NULL){
        const char *response = "HTTP/1.1 400 Bad Request\r\nContent-Length: 0\r\n\r\n";
        send(params.thread_data->client_socket, response, strlen(response), 0);
        return;
    }
    Book* book = getBookByISBN(params.thread_data->connection,strdup(str_isbn));
    if(book == NULL){
        // 404 Book not found
        const char *response = "HTTP/1.1 404 Not Found\r\nContent-Length: 0\r\n\r\n";
        send(params.thread_data->client_socket, response, strlen(response), 0);
        return;
    }
    JsonProperty props[] = {
        {"title", (void*)book->title, STRING},
        {"authors", (void*)book->authors, STRING},
        {"genre", (void*)book->genre, STRING},
        {"description", (void*)book->description, STRING},
        {"isbn", &book->isbn, STRING},
        {"blobName", (void*)book->blobName, STRING}
        {"total_copies", (void*)book->total_copies, INT},
        {"copiesonlendlease", (void*)book->copiesonlendlease, INT},
    };

    int propsCount = sizeof(props) / sizeof(props[0]);
    char* formattedJson = formatJsonProps(props, propsCount);
    char response[1024];
    snprintf(response, sizeof(response),
        "HTTP/1.1 200 OK\r\nContent-Type: application/json\r\nContent-Length: %zu\r\n\r\n%s",
        strlen(formattedJson),
        formattedJson);
    send(params.thread_data->client_socket, response, strlen(response), 0);
    free(book);
    free(formattedJson);
}

void getBooksHandler(RouterParams params) {
    PGresult* books_result = getBooks(params.thread_data->connection);
    if(books_result == NULL){
        // 404 Book not found
        const char *response = "HTTP/1.1 404 Not Found\r\nContent-Length: 0\r\n\r\n";
        send(params.thread_data->client_socket, response, strlen(response), 0);
        return;
    }
    char* json = formatQueryResultToJson(books_result);
    if( strlen(json)>= 4096) {
        // error 500 non posso mostrare tutti i books nel body della request
        const char *response = "HTTP/1.1 500 Internal Server Error\r\nContent-Length: 8\r\n\r\ntoo many";
        send(params.thread_data->client_socket, response, strlen(response), 0);
        free(json);
        PQclear(books_result);
        return;
    }
    char buffer[4096];
    HttpResponse response;
    response.code = "200 OK";
    response.contentType = "application/json";
    response.body = json;
    formatHttpResponse(buffer, sizeof(buffer), &response);
    send(params.thread_data->client_socket, buffer, strlen(buffer), 0);
    free(json);
    PQclear(books_result);
}
