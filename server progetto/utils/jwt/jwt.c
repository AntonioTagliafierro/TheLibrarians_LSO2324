#include <string.h>
#include <stdlib.h>
#include "jwt_header.h"
#include <jwt.h>

// Encode a JWT token based on the given payload
char* encodeToken(const TokenPayload* payload) {
    jwt_t* jwt = NULL;
    jwt_new(&jwt);

    // Add payload data to the JWT
    jwt_add_grant(jwt, "email", payload->email);
    jwt_add_grant_int(jwt, "id", (long)payload->id);
    jwt_add_grant(jwt, "expire", payload->expire);

    // Set the JWT algorithm and secret
    jwt_set_alg(jwt, JWT_ALG_HS256, SECRET, strlen(SECRET));

    // Encode the JWT and retrieve the token string
    char* token = jwt_encode_str(jwt);

    // Free resources used by the JWT
    jwt_free(jwt);

    return token;
}

// Verify the validity of a JWT token
bool verifyToken(const char* token) {
    jwt_t* decoded = NULL;
    jwt_decode(&decoded, token, SECRET, strlen(SECRET));

    // Check if the token is successfully decoded
    if (decoded != NULL) {
        jwt_free(decoded);
        return true;  // Token is valid
    } else {
        return false; // Token is invalid
    }
}

// Decode a JWT token and extract the payload data
TokenPayload* decodeToken(const char* token) {
    jwt_t* decoded = NULL;
    jwt_decode(&decoded, token, SECRET, strlen(SECRET));

    // Check if the token is successfully decoded
    if (decoded != NULL) {
        TokenPayload* payload = (TokenPayload*)malloc(sizeof(TokenPayload));

        // Extract email from the decoded token
        const char* email = jwt_get_grant(decoded, "email");
        if (email != NULL) {
            payload->email = strdup(email);
        } else {
            // Handle error: Unable to retrieve the "email" attribute from the token
            free(payload);
            payload = NULL;
            jwt_free(decoded);
            return NULL;
        }

        // Extract expire from the decoded token
        const char* expire = jwt_get_grant(decoded, "expire");
        if (expire != NULL) {
            payload->expire = strdup(expire);
        } else {
            // Handle error: Unable to retrieve the "expire" attribute from the token
            free(payload->email);
            free(payload);
            payload = NULL;
            jwt_free(decoded);
            return NULL;
        }

        // Extract id from the decoded token
        long id = jwt_get_grant_int(decoded, "id");
        payload->id = (int)id;

        // Free resources used by the decoded token
        jwt_free(decoded);

        return payload;
    } else {
        return NULL; // Token decoding failed
    }
}
