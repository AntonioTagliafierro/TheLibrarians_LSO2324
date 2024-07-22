#include <string.h>
#include <sys/socket.h>
#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <postgresql/libpq-fe.h>
#include "../../../utils/json_helper/json_helper.h"
#include "../../router/router.h"
#include "../../../utils/jwt_helper/jwt_helper.h"
#include "../../../utils/http_helper/http_helper.h"
#include "../../../utils/file_helper/file_helper.h"
#include "../../../models/models.h"
#include "../../../utils/crypt_helper/crypt_helper.h"
#include "../../../utils/datetime_helper/datetime_helper.h"
#include "../../../models/DAOs/bag/bag_dao.h"

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
    char* str_isbn = getValueFromJson(params.request.body, "isbn");
    if(str_isbn == NULL){ 
        const char *response = "HTTP/1.1 400 Bad Request\r\nContent-Length: 0\r\n\r\n";
        send(params.thread_data->client_socket, response, strlen(response), 0);
        return;
    }
    if(token == NULL){
        const char *response = "HTTP/1.1 500 Server Error\r\nContent-Length: 0\r\n\r\n";
        send(params.thread_data->client_socket, response, strlen(response), 0);
        return;
    }
    
    
    bool remove = removeFromBag(params.thread_data->connection,token->id,atoi(str_isbn));
    free(str_isbn);
    free(token);
    if(remove){
        const char *response = "HTTP/1.1 200 OK\r\nContent-Length: 0\r\n\r\n";
        send(params.thread_data->client_socket, response, strlen(response), 0);
    } else {
        const char *response = "HTTP/1.1 500 Server Error\r\nContent-Length: 0\r\n\r\n";
        send(params.thread_data->client_socket, response, strlen(response), 0);
    }
}