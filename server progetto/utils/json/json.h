#ifndef JSON_H
#define JSON_H

#include <postgresql/libpq-fe.h>
#include <stdbool.h>

// JSON parsing callback functions
typedef void (*JSONMapCallback)(const char* key, const char* value);
typedef void (*JSONFoldCallback)(const char* key, const char* value, void* accumulator);

// Structure for comparing keys and values in a JSON
typedef struct {
    const char* desired_key;
    const char* desired_value;
    bool match_found;
} JSONCompareContext;

// JSON manipulation functions
void jsonMap(const char* json, JSONMapCallback callback);
void jsonFold(const char* json, JSONFoldCallback callback, void* accumulator);

// Function to format the result of a PostgreSQL query into JSON
char* formatQueryResultToJson(PGresult* result);

// Functions for comparing keys and values in a JSON
bool jsonCompare(const char* json, const char* desired_key, const char* desired_value);
char* getValueFromJson(const char* json, const char* key);
bool existsKeyInJson(const char* json, const char* key);

// Functions for JSON manipulation and printing
void printJsonKeysAndValues(const char* json);
char** parseJsonStringIntoList(const char* json, int* count);
char* extractJsonListAsString(const char* json, const char* key);
char** getListFromJson(const char* json, const char* key, int* out_count);

// Structure to represent a JSON property
typedef struct {
    const char* key;
    void* value;
    int type;
} JsonProperty;

// Function to format an array of JSON properties into a JSON string
char* formatJsonProps(JsonProperty* pairs, int count);

#endif  // JSON_H

