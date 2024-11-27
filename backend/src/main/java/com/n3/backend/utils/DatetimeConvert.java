package com.n3.backend.utils;

import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

public class DatetimeConvert {
    public static String timastampToString(Timestamp date) {
        if(date == null) return "";

        String pattern = "HH:mm:ss dd-MM-yyyy";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

        String formattedDateTime = date.toLocalDateTime().format(formatter);

        return formattedDateTime;
    }

    public static Timestamp stringToTimestamp(String date) {
        if(date.split(" ").length == 1) date += " 00:00:00";
        return (!date.isEmpty()) ? Timestamp.valueOf(date) : null;
    }

    public static String dateToString(java.sql.Date date) {
        if(date == null) return "";

        String pattern = "dd-MM-yyyy";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

        String formattedDate = date.toLocalDate().format(formatter);

        return formattedDate;
    }

    public static java.sql.Date stringToDate(String date) {
        return (!date.isEmpty()) ? java.sql.Date.valueOf(date) : null;
    }
}
