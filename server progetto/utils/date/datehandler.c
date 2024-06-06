#include "datehandler.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>

#define TIME_ZONE "CET"

// Function to calculate the difference in seconds between two DateTime objects
long int datetime_diff(const DateTime *dt1, const DateTime *dt2) {
    time_t time1, time2;
    struct tm tm1, tm2;

    // Convert DateTime dt1 to struct tm
    memset(&tm1, 0, sizeof(struct tm));
    tm1.tm_year = dt1->year - 1900;
    tm1.tm_mon = dt1->month - 1;
    tm1.tm_mday = dt1->day;
    tm1.tm_hour = dt1->hour;
    tm1.tm_min = dt1->minute;
    tm1.tm_sec = dt1->second;
    time1 = mktime(&tm1);

    // Convert DateTime dt2 to struct tm
    memset(&tm2, 0, sizeof(struct tm));
    tm2.tm_year = dt2->year - 1900;
    tm2.tm_mon = dt2->month - 1;
    tm2.tm_mday = dt2->day;
    tm2.tm_hour = dt2->hour;
    tm2.tm_min = dt2->minute;
    tm2.tm_sec = dt2->second;
    time2 = mktime(&tm2);

    // Calculate the difference in seconds
    return (long int)difftime(time2, time1);
}

// Function to check if DateTime dt1 is greater than DateTime dt2
bool dt_greaterThen(const DateTime *dt1, const DateTime *dt2) {
    long int diff = datetime_diff(dt2, dt1);
    return diff > 0;
}

// Function to check if DateTime dt1 is greater than or equal to DateTime dt2
bool dt_greaterEquals(const DateTime *dt1, const DateTime *dt2) {
    long int diff = datetime_diff(dt2, dt1);
    return diff >= 0;
}

// Function to check if DateTime dt1 is equal to DateTime dt2
bool dt_equals(const DateTime *dt1, const DateTime *dt2) {
    long int diff = datetime_diff(dt1, dt2);
    return diff == 0;
}

// Function to check if DateTime dt1 is less than DateTime dt2
bool dt_lessThen(const DateTime *dt1, const DateTime *dt2) {
    long int diff = datetime_diff(dt1, dt2);
    return diff < 0;
}

// Function to check if DateTime dt1 is less than or equal to DateTime dt2
bool dt_lessEquals(const DateTime *dt1, const DateTime *dt2) {
    long int diff = datetime_diff(dt1, dt2);
    return diff <= 0;
}

// Function to get the current local DateTime
DateTime datetime_now() {
    time_t now;
    struct tm* tm_info;
    time(&now);
    tm_info = localtime(&now);

    DateTime dt;
    dt.year = tm_info->tm_year + 1900;
    dt.month = tm_info->tm_mon + 1;
    dt.day = tm_info->tm_mday;
    dt.hour = tm_info->tm_hour;
    dt.minute = tm_info->tm_min;
    dt.second = tm_info->tm_sec;

    // Print current local time and date
    printf("Current local time and date: %s", asctime(tm_info));
    return dt;
}

// Function to format DateTime dt as a string
char* datetime_format(const DateTime *dt) {
    char* str = (char*)malloc(20 * sizeof(char)); // Assuming max length of formatted string is 20
    strftime(str, 20, "%Y-%m-%d %H:%M:%S", localtime(&(time_t){ mktime(&(struct tm){
        .tm_year = dt->year - 1900,
        .tm_mon = dt->month - 1,
        .tm_mday = dt->day,
        .tm_hour = dt->hour,
        .tm_min = dt->minute,
        .tm_sec = dt->second
    }) }));
    return str;
}

// Function to parse a string and create a DateTime object
DateTime* datetime_parse(const char* str) {
    DateTime* dt = (DateTime*)malloc(sizeof(DateTime));
    memset(dt, 0, sizeof(DateTime));

    // Attempt to parse the string
    int result = sscanf(str, "%d-%d-%d %d:%d:%d", &(dt->year), &(dt->month), &(dt->day), &(dt->hour), &(dt->minute), &(dt->second));

    // If parsing fails, free memory and return NULL
    if (result != 6) {
        free(dt);
        return NULL;
    }

    return dt;
}

// Function to add a specified number of minutes to a DateTime object
DateTime datetime_addMinutes(const DateTime *dt, int minutes) {
    time_t timestamp = mktime(&(struct tm){
        .tm_year = dt->year - 1900,
        .tm_mon = dt->month - 1,
        .tm_mday = dt->day,
        .tm_hour = dt->hour,
        .tm_min = dt->minute,
        .tm_sec = dt->second
    });

    // Add minutes to the timestamp
    timestamp += minutes * 60;

    // Convert the updated timestamp to struct tm
    struct tm* timeinfo = localtime(&timestamp);

    // Create and return a new DateTime object
    DateTime result;
    result.year = timeinfo->tm_year + 1900;
    result.month = timeinfo->tm_mon + 1;
    result.day = timeinfo->tm_mday;
    result.hour = timeinfo->tm_hour;
    result.minute = timeinfo->tm_min;
    result.second = timeinfo->tm_sec;

    return result;
}

