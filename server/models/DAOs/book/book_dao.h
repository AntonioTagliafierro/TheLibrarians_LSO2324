#ifndef BOOK_DADO_H
#define BOOK_DAO_H


Book* getBookByIsbn(PGconn* connection, int isbn);
PGresult* getBooks(PGconn* connection);
#endif // BOOK_DADO_H
