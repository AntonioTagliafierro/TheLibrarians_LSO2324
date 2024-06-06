#include "json.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

// Helper function to check if JSON parsing was successful
static void checkJsonParsing(struct json_object* obj, const char* errorMessage) {
    if (obj == NULL) {
        fprintf(stderr, "Error in JSON parsing: %s\n", errorMessage);
        exit(EXIT_FAILURE);
    }
}

// Helper function for secure memory allocation
static void* safeMalloc(size_t size) {
    void* ptr = malloc(size);
    if (ptr == NULL) {
        perror("Error in memory allocation");
        exit(EXIT_FAILURE);
    }
    return ptr;
}

// Helper function for secure string duplication
static char* safeStrdup(const char* str) {
    char* newStr = strdup(str);
    if (newStr == NULL) {
        perror("Error in string duplication");
        exit(EXIT_FAILURE);
    }
    return newStr;
}

// Implementation of callback function for JSON parsing (key-value map)
void jsonMap(const char* json, JSONMapCallback callback) {
    struct json_object* root = json_tokener_parse(json);
    checkJsonParsing(root, "Error during JSON parsing in jsonMap");

    enum json_type type = json_object_get_type(root);

    if (type == json_type_object) {
        json_object_object_foreach(root, key, val) {
            const char* value = json_object_get_string(val);
            callback(key, value);
        }
    }

    json_object_put(root);
}

// Implementation of callback function for JSON parsing (key-value fold)
void jsonFold(const char* json, JSONFoldCallback callback, void* accumulator) {
    struct json_object* root = json_tokener_parse(json);
    checkJsonParsing(root, "Error during JSON parsing in jsonFold");

    enum json_type type = json_object_get_type(root);

    if (type == json_type_object) {
        json_object_object_foreach(root, key, val) {
            enum json_type val_type = json_object_get_type(val);
            const char* value = NULL;
            char valueStr[64];
            char* valStr = NULL;

            switch (val_type) {
                case json_type_null:
                    value = "null";
                    break;
                case json_type_boolean:
                    value = json_object_get_boolean(val) ? "true" : "false";
                    break;
                case json_type_double:
                    snprintf(valueStr, sizeof(valueStr), "%f", json_object_get_double(val));
                    value = safeStrdup(valueStr);
                    break;
                case json_type_int:
                    snprintf(valueStr, sizeof(valueStr), "%d", json_object_get_int(val));
                    value = safeStrdup(valueStr);
                    break;
                case json_type_string:
                    value = json_object_get_string(val);
                    break;
                case json_type_array:
                    // Add array and object handling here if needed
                    break;
                case json_type_object:
                    // Add array and object handling here if needed
                    break;
                default:
                    value = "null";
                    break;
            }

            callback(key, value, accumulator);
            if (valStr != NULL) {
                free(valStr);
            }
        }
    }

    json_object_put(root);
}

// Function to format the result of a PostgreSQL query into JSON
char* formatQueryResultToJson(PGresult* result) {
    int rows = PQntuples(result);
    int columns = PQnfields(result);

    json_object* json = json_object_new_array();
    checkJsonParsing(json, "Error creating a JSON array in formatQueryResultToJson");

    for (int i = 0; i < rows; i++) {
        json_object* jsonRow = json_object_new_object();
        checkJsonParsing(jsonRow, "Error creating a JSON object in formatQueryResultToJson");

        for (int j = 0; j < columns; j++) {
            const char* columnName = PQfname(result, j);
            const char* cellValue = PQgetvalue(result, i, j);

            json_object* jsonColumn = json_object_new_string(cellValue);
            checkJsonParsing(jsonColumn, "Error creating a JSON string in formatQueryResultToJson");

            json_object_object_add(jsonRow, columnName, jsonColumn);
        }

        json_object_array_add(json, jsonRow);
    }

    const char* jsonString = json_object_to_json_string(json);
    char* formattedJsonString = safeStrdup(jsonString);

    json_object_put(json);

    return formattedJsonString;
}

// Callback function for comparing key and value during JSON fold
void compareKeyValueCallback(const char* key, const char* value, void* context) {
    JSONCompareContext* ctx = (JSONCompareContext*)context;
    if (strcmp(key, ctx->desired_key) == 0 && strcmp(value, ctx->desired_value) == 0) {
        ctx->match_found = true;
    }
}

// Function to compare key and value in a JSON
bool jsonCompare(const char* json, const char* desired_key, const char* desired_value) {
    JSONCompareContext context = {desired_key, desired_value, false};
    jsonFold(json, compareKeyValueCallback, &context);
    return context.match_found;
}

// Callback function for comparing only the key during JSON fold
void compareKeyCallback(const char* key, const char* value, void* context) {
    JSONCompareContext* ctx = (JSONCompareContext*)context;
    if (strcmp(key, ctx->desired_key) == 0) {
        ctx->desired_value = value;
        ctx->match_found = true;
    }
}

// Function to get the value associated with a key in a JSON
char* getValueFromJson(const char* json, const char* key) {
    JSONCompareContext context = {key, NULL, false};
    jsonFold(json, compareKeyCallback, &context);
    return (context.desired_value != NULL) ? safeStrdup(context.desired_value) : NULL;
}

// Function to check if a key exists in a JSON
bool existsKeyInJson(const char* json, const char* key) {
    return getValueFromJson(json, key) != NULL;
}

// Callback function for printing keys and values during JSON parsing
void mapPrint(const char* key, const char* value) {
    printf("%s: %s\n", key, value);
    fflush(stdout);
}

// Function to print keys and values of a JSON
void printJsonKeysAndValues(const char* json) {
    printf("JSON PRINT:\n");
    fflush(stdout);
    jsonMap(json, mapPrint);
}

// Function for parsing a JSON array into a list of strings
char** parseJsonStringIntoList(const char* json, int* count) {
    struct json_object* root = json_tokener_parse(json);
    checkJsonParsing(root, "Error during JSON parsing in parseJsonStringIntoList");

    enum json_type type = json_object_get_type(root);

    if (type == json_type_array) {
        int arrLen = json_object_array_length(root);
        char** list = (char**)safeMalloc(arrLen * sizeof(char*));
        *count = arrLen;

        for (int i = 0; i < arrLen; i++) {
            struct json_object* item = json_object_array_get_idx(root, i);
            const char* jsonString = json_object_to_json_string(item);
            list[i] = safeStrdup(jsonString);
        }

        json_object_put(root);
        return list;
    }

    json_object_put(root);
    *count = 0;
    return NULL;
}

// Function to extract a JSON array in string format from a JSON using a specific key
char* extractJsonListAsString(const char* json, const char* key) {
    struct json_object* root = json_tokener_parse(json);
    checkJsonParsing(root, "Error during JSON parsing in extractJsonListAsString");

    struct json_object* listObj;
    char* listString = NULL;

    if (json_object_object_get_ex(root, key, &listObj)) {
        listString = safeStrdup(json_object_to_json_string(listObj));
    }

    json_object_put(root);
    return listString;
}

// Function to get a list of strings from a JSON using a specific key
char** getListFromJson(const char* json, const char* key, int* out_count) {
    *out_count = 0;
    char* jsonStrList = extractJsonListAsString(json, key);
    if (jsonStrList != NULL) {
        return parseJsonStringIntoList(jsonStrList, out_count);
    } else {
        return NULL;
    }
}

// Function to format a JSON properties array into a JSON string
char* formatJsonProps(JsonProperty* pairs, int count) {
    json_object* json = json_object_new_object();
    checkJsonParsing(json, "Error creating a JSON object in formatJsonProps");

    for (int i = 0; i < count; i++) {
        json_object* jsonValue = NULL;
        void* value = pairs[i].value;

        if (value != NULL) {
            if (pairs[i].type == STRING) {
                jsonValue = json_object_new_string((const char*)value);
            } else if (pairs[i].type == BOOL) {
                jsonValue = json_object_new_boolean(*(bool*)value);
            } else if (pairs[i].type == FLOAT) {
                jsonValue = json_object_new_double(*(float*)value);
            } else if (pairs[i].type == INT) {
                jsonValue = json_object_new_int(*(int*)value);
            }
        }

        if (jsonValue != NULL) {
            json_object_object_add(json, pairs[i].key, jsonValue);
        }
    }

    const char* jsonString = json_object_to_json_string(json);
    char* formattedJsonString = safeStrdup(jsonString);

    json_object_put(json);

    return formattedJsonString;
}

