#ifndef LENDLEASEDAO_H
#define LENDLEASEDAO_H


bool orderLendLease(PGconn* connection, int id_user, int isbn);
PGresult* getLendLeaseByUser(PGconn* connection, int id_user);
LendLease* getLendLeaseById(PGconn* connection, int id);
bool deliveredLendLease(PGconn* connection, int id_user, int isbn);
PGresult* getBooksByLendLeaseId(PGconn* connection, int id, float *total);
PGresult* getAllLendLease(PGconn* connection);



#endif
