#ifndef BAGDAO_H
#define BAGDAO_H


bool addToBag(PGconn* connection, int id_user, int isbn);
bool removeFromBag(PGconn* connection, int id_user, int isbn);
PGresult* getBagByUser(PGconn* connection, int id_user);



#endif