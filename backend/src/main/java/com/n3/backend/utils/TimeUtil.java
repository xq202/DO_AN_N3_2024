package com.n3.backend.utils;

import java.sql.Timestamp;

public class TimeUtil {
    public static double minusTimestamp(Timestamp start, Timestamp end){
        long diff = end.getTime() - start.getTime();
        return diff / 1000.0 / 60.0 / 60.0;
    }

}
