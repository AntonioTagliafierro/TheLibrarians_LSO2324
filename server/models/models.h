#ifndef MODELS_H
#define MODELS_H

typedef struct {
    int id;
    const char* email;
    const char* password;
} User;

typedef struct {
    int isbn;
    const char* title;
    const char* authors;
    const char* genre;
    const char* image_url;
    int total_copies;
    int copiesonlendlease;
} Book;

typedef struct {
    int id;
    int id_user;
    int isbn;
    const char* creation_datetime;
    const char* due_datetime;
    bool delivered;
} LendLease;


typedef struct {
    const char* isbn;
    int id_user;
} Bag;


#endif
