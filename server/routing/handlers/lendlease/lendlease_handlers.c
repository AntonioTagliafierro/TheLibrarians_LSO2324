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
#include "../../../models/DAOs/lendlease/lendlease_dao.h"


void getLendLeaseMadeByUserHandler(RouterParams params) {
    TokenPayload* token = decodeToken(params.request.authorization);
    if(token!= NULL){
        const char* decoded_email = token->email;
        int decoded_id = token->id;
        PGresult* lendleases_result = getLendLeaseByUser(params.thread_data->connection,decoded_id);
        if(lendleases_result == NULL){
            const char *response = "HTTP/1.1 404 Not Found\r\nContent-Length: 0\r\n\r\n";
            send(params.thread_data->client_socket, response, strlen(response), 0);
            return;
        }
        char* json = formatQueryResultToJson(lendleases_result);
        // controlla che json non superi il buffer
        if( strlen(json) > 4096 ){
            const char *response = "HTTP/1.1 500 Server Error\r\nContent-Length: 8\r\n\r\ntoo many";
            send(params.thread_data->client_socket, response, strlen(response), 0);
            free(json);
            PQclear(lendleases_result);
            free(token);
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
        PQclear(lendleases_result);
        free(token);
    } else {
        const char *response = "HTTP/1.1 500 Server Error\r\nContent-Length: 0\r\n\r\n";
        send(params.thread_data->client_socket, response, strlen(response), 0);
    }
}

char* formatLendLeaseResponse(LendLease* lendlease, PGresult* lendlease_result,float total){
    JsonProperty lendleaseProps[] = {
        {"isbn", &lendlease->isbn, INT},
        {"id", &lendlease->id, INT},
        {"id_user", &lendlease->id_user, INT},
        {"creation_datetime", (void*)lendlease->creation_datetime, STRING},
        {"due_datetime", (void*)lendlease->due_datetime, STRING},
        {"delivered", &lendlease->delivered, BOOL},

    };
    int lendleasePropsCount = sizeof(lendleaseProps) / sizeof(lendleaseProps[0]);
    char* formattedLendLease = formatJsonProps(lendleaseProps, lendleasePropsCount);

    if(lendlease_result == NULL){

        free(formattedLendLease);
        return NULL;
    }
    char* jsonListLeandLease = formatQueryResultToJson(lendlease_result);
    char buffer[4096];
    HttpResponse response;
    response.code = "200 OK";
    response.contentType = "application/json";

    char* total_str = malloc(sizeof(char) * 10);
    snprintf(total_str, 10, "%f", total);
    const size_t dim = sizeof(char) * (strlen(jsonListLeandLease) + strlen(formattedLendLease) + strlen(total_str) + 50);
    char* body = malloc(dim);
    snprintf(body, dim, "{\"books\":%s,\"in lend lease\":%s,\"total\":%s}", jsonListLeandLease, formattedLendLease, total_str);
    response.body = body;
    formatHttpResponse(buffer, sizeof(buffer), &response);
    free(formattedLendLease);
    free(jsonListLeandLease);
    free(body);
    free(total_str);
    return strdup(buffer);
}

void getLendLeaseHandler(RouterParams params){
    const char* str_lendlease_id = getPathParameter(params.request.path);
    if(str_lendlease_id == NULL){
        const char *response = "HTTP/1.1 400 Bad Request\r\nContent-Length: 0\r\n\r\n";
        send(params.thread_data->client_socket, response, strlen(response), 0);
        return;
    }
    TokenPayload* token = decodeToken(params.request.authorization);
    if(token!= NULL){
        const char* decoded_email = token->email;
        int decoded_id = token->id;
        LendLease* lendlease = getLendLeaseById(params.thread_data->connection,atoi(str_lendlease_id));
        free(token);
        if (lendlease == NULL){
            const char *response = "HTTP/1.1 404 Not Found\r\nContent-Length: 0\r\n\r\n";
            send(params.thread_data->client_socket, response, strlen(response), 0);
            return;
        }
        if (lendlease->id_user != decoded_id){
            const char *response = "HTTP/1.1 403 Forbidden\r\nContent-Length: 0\r\n\r\n";
            send(params.thread_data->client_socket, response, strlen(response), 0);
            free(lendlease);
            return;
        }
        float total;
        PGresult* res = getBooksByLendLeaseId(params.thread_data->connection,lendlease->id,&total);
        if(res == NULL){
            const char *response = "HTTP/1.1 500 Server Error\r\nContent-Length: 0\r\n\r\n";
            send(params.thread_data->client_socket, response, strlen(response), 0);
            free(lendlease);
            free(token);
            return;
        }
        char* response = formatLendLeaseResponse(lendlease,res,total);

        send(params.thread_data->client_socket, response, strlen(response), 0);
        PQclear(res);
        free(lendlease);
    } else {
        const char *response = "HTTP/1.1 500 Server Error\r\nContent-Length: 0\r\n\r\n";
        send(params.thread_data->client_socket, response, strlen(response), 0);
    }
}

void getAllLendLeaseHandler(RouterParams params){



        PGresult* lendleases_result = getAllLendLease(params.thread_data->connection);
        if(lendleases_result == NULL){
            const char *response = "HTTP/1.1 404 Not Found\r\nContent-Length: 0\r\n\r\n";
            send(params.thread_data->client_socket, response, strlen(response), 0);
            return;
        }
        char* json = formatQueryResultToJson(lendleases_result);
        // controlla che json non superi il buffer
        if( strlen(json) > 4096 ){
            const char *response = "HTTP/1.1 500 Server Error\r\nContent-Length: 8\r\n\r\ntoo many";
            send(params.thread_data->client_socket, response, strlen(response), 0);
            free(json);
            PQclear(lendleases_result);
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
        PQclear(lendleases_result);
 
  
}

void lendLeaseHandler(RouterParams params){
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
        bool result = orderLendLease(params.thread_data->connection, decoded_id, isbn);
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

void setDeliveredLendLeaseHandler(RouterParams params) {
    TokenPayload *token = decodeToken(params.request.authorization);
    if(token == NULL){
        const char *response = "HTTP/1.1 500 Server Error\r\nContent-Length: 0\r\n\r\n";
        send(params.thread_data->client_socket, response, strlen(response), 0);
        return;
    }
    char* str_id_lendlease = getValueFromJson(params.request.body, "id_lendlease");
    bool delivered = deliveredLendLease(params.thread_data->connection,token->id,atoi(str_id_lendlease));
    free(str_id_lendlease);
    free(token);
    if(delivered){
        const char *response = "HTTP/1.1 200 OK\r\nContent-Length: 0\r\n\r\n";
        send(params.thread_data->client_socket, response, strlen(response), 0);
    } else {
        const char *response = "HTTP/1.1 500 Server Error\r\nContent-Length: 0\r\n\r\n";
        send(params.thread_data->client_socket, response, strlen(response), 0);
    }
}



