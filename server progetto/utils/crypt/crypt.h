#ifndef CRYPT_H
#define CRYPT_H

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include <openssl/sha.h>

#define SALT_SIZE 16
#define HASH_SIZE SHA256_DIGEST_LENGTH

char* generate_random_salt();
char* encrypt(const char* data);

#endif
