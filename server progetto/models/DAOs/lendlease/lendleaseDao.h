#ifndef LENDLEASEDAO_H
#define LENDLEASEDAO_H


bool orderLendLease(PGconn* connection, int id_user, int isbn);
PGresult* getLendLeaseByUser(PGconn* connection, int id_user);
PGresult* getAllLendLease(PGconn* connection);
bool deliveredLendLease(PGconn* connection, int id_user, const char* isbn);


#endif
