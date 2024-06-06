#include "userDao.h"
#include <stdio.h>

// Function declaration
static User* executeQueryAndFetchUser(PGconn* connection, const char* query, int numParams, const char** param_values, const int* param_lengths);

// Authenticate a user by email and password
User* authenticateUser(PGconn* connection, const char* email, const char* password) {
    const char* query = "SELECT * FROM \"Users\" WHERE \"email\" = $1 AND \"password\" = $2;";
    const char* param_values[2] = {email, password};
    const int param_lengths[2] = {strlen(email), strlen(password)};

    return executeQueryAndFetchUser(connection, query, 2, param_values, param_lengths);
}

// Register a new user
User* registerUser(PGconn* connection, const char* email, const char* password, const char* name, const char* surname) {
    const char* query = "INSERT INTO \"Users\" (\"email\", \"password\", \"name\", \"surname\") VALUES ($1, $2, $3, $4) RETURNING *;";
    const char* param_values[4] = {email, password, name, surname};
    const int param_lengths[4] = {strlen(email), strlen(password), strlen(name), strlen(surname)};

    return executeQueryAndFetchUser(connection, query, 4, param_values, param_lengths);
}

// Get a user by ID
User* getUserById(PGconn* connection, int id) {
    const char* query = "SELECT * FROM \"Users\" WHERE \"id\" = $1;";
    char id_str[10];
    sprintf(id_str, "%d", id);
    const char* param_values[1] = {id_str};
    const int param_lengths[1] = {strlen(id_str)};

    return executeQueryAndFetchUser(connection, query, 1, param_values, param_lengths);
}

// Get a user by email
User* getUserByEmail(PGconn* connection, const char* email) {
    const char* query = "SELECT * FROM \"Users\" WHERE \"email\" = $1;";
    const char* param_values[1] = {email};
    const int param_lengths[1] = {strlen(email)};

    return executeQueryAndFetchUser(connection, query, 1, param_values, param_lengths);
}

// Execute a query and fetch a user based on the result
static User* executeQueryAndFetchUser(PGconn* connection, const char* query, int numParams, const char** param_values, const int* param_lengths) {
    // Set parameter formats
    const int param_formats[numParams];
    for (int i = 0; i < numParams; ++i) {
        param_formats[i] = 0;
    }

    // Execute the query
    PGresult* result = PQexecParams(connection, query, numParams, NULL, param_values, param_lengths, param_formats, 0);

    // Check if the query execution was successful
    if (PQresultStatus(result) == PGRES_TUPLES_OK) {
        int rows = PQntuples(result);
        if (rows > 0) {
            // Allocate memory for a new User struct and populate it with the result values
            User* user = malloc(sizeof(User));
            user->id = atoi(strdup(PQgetvalue(result, 0, 0)));
            user->email = strdup(PQgetvalue(result, 0, 1));
            user->password = strdup(PQgetvalue(result, 0, 2));
            user->name = strdup(PQgetvalue(result, 0, 3));
            user->surname = strdup(PQgetvalue(result, 0, 4));
            PQclear(result);
            return user;
        }
    }

    // Cleanup and return NULL if no user found
    PQclear(result);
    return NULL;
}

