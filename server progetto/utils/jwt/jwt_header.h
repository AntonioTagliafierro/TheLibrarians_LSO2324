#ifndef JWT_HEADER_H
#define JWT_HEADER_H

#include <stdbool.h>

#define SECRET "NON_CI_RESTA_CHE_PIANGERE"
#define EXPIRE_MINUTES 60
typedef struct {
    int id;
    const char* email;
    const char* expire;
} TokenPayload;

char* encodeToken(const TokenPayload* payload);
bool verifyToken(const char* token);
TokenPayload* decodeToken(const char* token);

#endif
