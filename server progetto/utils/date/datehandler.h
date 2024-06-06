#ifndef DATETIME_HANDLER_H
#define DATETIME_HANDLER_H

#include <stdbool.h>

typedef struct {
    int year;
    int month;
    int day;
    int hour;
    int minute;
    int second;
} DateTime;

long int datetime_diff(const DateTime *dt1, const DateTime *dt2);

bool dt_greaterThen(const DateTime *dt1, const DateTime *dt2);

bool dt_greaterEquals(const DateTime *dt1, const DateTime *dt2);

bool dt_equals(const DateTime *dt1, const DateTime *dt2);

bool dt_lessThen(const DateTime *dt1, const DateTime *dt2);

bool dt_lessEquals(const DateTime *dt1, const DateTime *dt2);

DateTime datetime_now();

char* datetime_format(const DateTime *dt);

DateTime* datetime_parse(const char* str);

DateTime datetime_addMinutes(const DateTime *dt, int minutes);

#endif
