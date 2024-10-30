package com.n3.backend.config;

import java.sql.Timestamp;

public class DatetimeConvert {
    public static String timastampToString(Timestamp date) {
        return (date != null) ? date.toLocalDateTime().toString() : "";
    }

    public static Timestamp stringToTimastamp(String date) {
        return (!date.isEmpty()) ? Timestamp.valueOf(date) : null;
    }

    public static String dateToString(java.sql.Date date) {
        return (date != null) ? date.toLocalDate().toString() : "";
    }

    public static java.sql.Date stringToDate(String date) {
        return (!date.isEmpty()) ? java.sql.Date.valueOf(date) : null;
    }
}
