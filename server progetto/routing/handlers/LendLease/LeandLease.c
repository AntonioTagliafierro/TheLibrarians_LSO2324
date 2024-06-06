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
#include "../../../models/DAOs/order/lendleaseDao.h"

void getLendLeaseMadeByUserHandler(RouterParams params) {
    TokenPayload* token = decodeToken(params.request.authorization);
    if(token!= NULL){
        const char* decoded_email = token->email;
        int decoded_id = token->id;
        PGresult* orders_result = getLendLeaseByUser(params.thread_data->connection,decoded_id);
        if(orders_result == NULL){
            const char *response = "HTTP/1.1 404 Not Found\r\nContent-Length: 0\r\n\r\n";
            send(params.thread_data->client_socket, response, strlen(response), 0);
            return;
        }
        char* json = formatQueryResultToJson(orders_result);
        // controlla che json non superi il buffer
        if( strlen(json) > 4096 ){
            const char *response = "HTTP/1.1 500 Server Error\r\nContent-Length: 8\r\n\r\ntoo many";
            send(params.thread_data->client_socket, response, strlen(response), 0);
            free(json);
            PQclear(orders_result);
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
        PQclear(orders_result);
        free(token);
    } else {
        const char *response = "HTTP/1.1 500 Server Error\r\nContent-Length: 0\r\n\r\n";
        send(params.thread_data->client_socket, response, strlen(response), 0);
    }
}

char* formatLendLeaseResponse(LendLease* lendlease, PGresult* lend_lease_result){
    JsonProperty lendleaseProps[] = {
        {"isbn", &order->isbn, INT},
        {"id_user", &order->id_user, INT},
        {"creation_datetime", (void*)order->creation_datetime, STRING},
        {"due_datetime", (void*)lendlease->due_datetime, STRING},
        {"status", &lendlease->status, STRING}
            const char* isbn;
    int id_user;
    const char* creation_datetime;
    const char* due_datetime;
    const char* status;
    };
    int lendleasePropsCount = sizeof(lendleaseProps) / sizeof(lendleaseProps[0]);
    char* formattedLendLease = formatJsonProps(lendleaseProps, lendleasePropsCount);

    if(lend_lease_result == NULL){

        free(formattedLendLease);
        return NULL;
    }
    char* jsonListLeandLease = formatQueryResultToJson(lend_lease_result);
    char buffer[4096];
    HttpResponse response;
    response.code = "200 OK";
    response.contentType = "application/json";

    char* total_str = malloc(sizeof(char) * 10);
    snprintf(total_str, 10, "%f", total);
    const size_t dim = sizeof(char) * (strlen(jsonListLeandLease) + strlen(formattedLendLease) + 50);
    char* body = malloc(dim);
    snprintf(body, dim, "{\"books\":%s,\"in lend lease\":%s}", jsonListLeandLease, formattedLendLease);
    response.body = body;
    formatHttpResponse(buffer, sizeof(buffer), &response);
    free(formattedLendLease);
    free(jsonListLeandLease);
    free(body);
    free(total_str);
    return strdup(buffer);
}

void getLendLeaseHandler(RouterParams params){
    const char* str_order_id = getPathParameter(params.request.path);
    if(str_order_id == NULL){
        const char *response = "HTTP/1.1 400 Bad Request\r\nContent-Length: 0\r\n\r\n";
        send(params.thread_data->client_socket, response, strlen(response), 0);
        return;
    }
    TokenPayload* token = decodeToken(params.request.authorization);
    if(token!= NULL){
        const char* decoded_email = token->email;
        int decoded_id = token->id;
        LendLease* lendlease = getAllLendLease(params.thread_data->connection,atoi(str_order_id));
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

        char* response = formatLendLeaseResponse(lendlease,res);

        send(params.thread_data->client_socket, response, strlen(response), 0);
        PQclear(res);
        free(lendlease);
    } else {
        const char *response = "HTTP/1.1 500 Server Error\r\nContent-Length: 0\r\n\r\n";
        send(params.thread_data->client_socket, response, strlen(response), 0);
    }
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
    char* str_isbn = getValueFromJson(params.request.body, "isbn");
    bool delivered = deliveredLendLease(params.thread_data->connection,token->id,str_isbn);
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
