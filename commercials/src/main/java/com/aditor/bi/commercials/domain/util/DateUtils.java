package com.aditor.bi.commercials.domain.util;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class DateUtils {
    public static Date parseDate(String date) {
        String[] dateComponents = date.split("-");
        int year = Integer.parseInt(dateComponents[0]);
        int month = Integer.parseInt(dateComponents[1]);
        int day = Integer.parseInt(dateComponents[2]);
        return Date.from(LocalDate.of(year, month, day)
               .atStartOfDay(ZoneId.of("UTC"))
               .toInstant());
    }

    public static String formatDate(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd")
                .format(date);
    }
}
