#ifndef BOOK_DADO_H
#define BOOK_DAO_H


Book* getBookByISBN(PGconn* connection, const char* isbn);
PGresult* getBooks(PGconn* connection);
#endif // BOOK_DADO_H
