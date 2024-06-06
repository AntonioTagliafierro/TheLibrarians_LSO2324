#include <string.h>
#include <sys/socket.h>
#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <postgresql/libpq-fe.h>
#include "../../../json/json.h"
#include "../../router/router.h"
#include "../../../jwt/jwt.h"
#include "../../../http/http.h"
#include "../../../file/file.h"
#include "../../../models/models.h"
#include "../../../crypt/crypt.h"
#include "../../../date/date.h"
#include "../../../models/DAOs/order/bagDao.h"


void bagHandler(RouterParams params){
    if(!existsKeyInJson(params.request.body, "isbn")){
        const char *response = "HTTP/1.1 400 Bad Request\r\nContent-Length: 0\r\n\r\n";
        send(params.thread_data->client_socket, response, strlen(response), 0);
    }
    char* str_isbn = getValueFromJson(params.request.body, "isbn");
    int isbn = atoi(str_isbn);
    free(str_isbn);

    TokenPayload* token = decodeToken(params.request.authorization);
    if(token!= NULL){
        const char* decoded_email = token->email;
        int decoded_id = token->id;
        bool result = addToBag(params.thread_data->connection, decoded_id, isbn);
        if(result){
            const char *response = "HTTP/1.1 200 OK\r\nContent-Length: 0\r\n\r\n";
            send(params.thread_data->client_socket, response, strlen(response), 0);
        } else {
            const char *response = "HTTP/1.1 500 Server Error\r\nContent-Length: 0\r\n\r\n";
            send(params.thread_data->client_socket, response, strlen(response), 0);
        }
        free(token);
    } else {
        const char *response = "HTTP/1.1 500 Server Error\r\nContent-Length: 0\r\n\r\n";
        send(params.thread_data->client_socket, response, strlen(response), 0);
    }

}

void removeBagHandler(RouterParams params) {
    TokenPayload *token = decodeToken(params.request.authorization);
    if(token == NULL){
        const char *response = "HTTP/1.1 500 Server Error\r\nContent-Length: 0\r\n\r\n";
        send(params.thread_data->client_socket, response, strlen(response), 0);
        return;
    }
    char* str_isbn = getValueFromJson(params.request.body, "isbn");
    bool remove = removeFromBag(params.thread_data->connection,token->id,str_isbn);
    free(str_isbn);
    free(token);
    if(delivered){
        const char *response = "HTTP/1.1 200 OK\r\nContent-Length: 0\r\n\r\n";
        send(params.thread_data->client_socket, response, strlen(response), 0);
    } else {
        const char *response = "HTTP/1.1 500 Server Error\r\nContent-Length: 0\r\n\r\n";
        send(params.thread_data->client_socket, response, strlen(response), 0);
    }
}


}
