package com.drinkbird.restaurant.reservations.api.domain;

import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DateId {
    private final DateTime date;

    public DateId(DateTime date) {
        this.date = date;
    }

    public static DateId parse(String dateIdStr) {
        DateTime parsedDate = DateTime.parse(dateIdStr, fmt);
        return new DateId(parsedDate);
    }

    public DateTime getDate() {
        return date;
    }

    private static final DateTimeFormatter fmt =
        DateTimeFormat.forPattern("yyyy-MM-dd");

    @Override
    public String toString() {
        return date.toString(fmt);
    }

    public boolean isEarlierThan(DateTime otherDate) {
        return DateTimeComparator
            .getDateOnlyInstance()
            .compare(getDate(), otherDate) < 0;
    }
}
