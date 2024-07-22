#ifndef JSON_HELPER_H
#define JSON_HELPER_H
#include <stdbool.h>
enum JSON_PROPERTY_TYPE {
    STRING,
    BOOL,
    FLOAT,
    INT
};

typedef struct {
    const char* key;
    void* value;
    enum JSON_PROPERTY_TYPE type;
} JsonProperty;

// Funzione per comparare chiave e valore
bool jsonCompare(const char* json, const char* desired_key, const char* desired_value);

typedef void (*JSONFoldCallback)(const char* key, const char* value, void* accumulator);

// Funzione per il parsing di un JSON e la fold dei valori
void jsonFold(const char* json, JSONFoldCallback callback, void* accumulator);

typedef void (*JSONMapCallback)(const char* key, const char* value);

// Funzione per il parsing di un JSON e la map dei valori
void jsonMap(const char* json, JSONMapCallback callback);

char* formatQueryResultToJson(PGresult* result);

char* getValueFromJson(const char* json, const char* key);
char** getListFromJson(const char* json, const char* key, int* out_count);

bool existsKeyInJson(const char* json,const char* key);

void printJsonKeysAndValues(const char* json);

char* formatJsonProps(JsonProperty* pairs, int count);

#endif