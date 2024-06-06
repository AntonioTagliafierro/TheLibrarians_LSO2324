#ifndef MODELS_H
#define MODELS_H

typedef struct {
    int id_user;
    const char* name;
    const char* surname;
    const char* email;
    const char* password;
} User;

typedef struct {
    const char* isbn;
    const char* title;
    const char* authors;
    const char* description;
    const char* genre;
    const char* blobName;
    int total_copies;
    int copiesonlendlease;
} Book;

typedef struct {
    const char* isbn;
    int id_user;
    const char* creation_datetime;
    const char* due_datetime;
    const char* status;
} LendLease;

typedef struct {
    const char* isbn;
    int id_user;
} Bag;


#endif
