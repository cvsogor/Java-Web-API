package com.aditor.dashboard.spring.util;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class DateHelper {
    public static Date getDate(int year, int month, int day)
    {
        return Date.from(LocalDate.of(year, month, day).atStartOfDay(ZoneId.of("UTC")).toInstant());
    }

    public static Date fromTimestamp(Timestamp timestamp)
    {
        return Date.from(timestamp.toInstant());
    }
}