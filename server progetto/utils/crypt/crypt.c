#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <openssl/sha.h>
#include "crypt.h"

char* generate_random_salt() {
    char* salt = (char*)malloc((SALT_SIZE + 1) * sizeof(char));
    if (salt == NULL) {
        perror("Error allocating memory for salt");
        exit(EXIT_FAILURE);
    }

    srand(time(NULL));
    for (int i = 0; i < SALT_SIZE; i++) {
        salt[i] = 'a' + rand() % 26;
    }
    salt[SALT_SIZE] = '\0';

    return salt;
}

char* encrypt(const char* data) {
    char* salt = generate_random_salt();

    // Concatenate the data and salt to create a combined string
    char* combined_data = (char*)malloc((strlen(data) + SALT_SIZE + 1) * sizeof(char));
    if (combined_data == NULL) {
        perror("Error allocating memory for combined_data");
        exit(EXIT_FAILURE);
    }
    sprintf(combined_data, "%s%s", data, salt);

    // Hash the combined data using SHA-256
    unsigned char hash[HASH_SIZE];
    SHA256_CTX sha256;
    SHA256_Init(&sha256);
    SHA256_Update(&sha256, combined_data, strlen(combined_data));
    SHA256_Final(hash, &sha256);

    // Convert the binary hash to a hexadecimal string
    char* hex_hash = (char*)malloc((2 * HASH_SIZE + 1) * sizeof(char));
    if (hex_hash == NULL) {
        perror("Error allocating memory for hex_hash");
        exit(EXIT_FAILURE);
    }
    for (int i = 0; i < HASH_SIZE; i++) {
        sprintf(hex_hash + 2 * i, "%02x", hash[i]);
    }
    hex_hash[2 * HASH_SIZE] = '\0';

    // Free allocated memory
    free(salt);
    free(combined_data);

    return hex_hash;
}

