#ifndef USERS_H
#define USERS_H

#include <postgresql/libpq-fe.h>

typedef struct {
    int id;
    char* email;
    char* password;
    char* firstName;
    char* lastName;
} User;

User* authenticateUser(PGconn* connection, const char* email, const char* password);
User* registerUser(PGconn* connection, const char* email, const char* password, const char* firstName, const char* lastName);
User* getUserById(PGconn* connection, int id);
User* getUserByEmail(PGconn* connection, const char* email);

#endif
