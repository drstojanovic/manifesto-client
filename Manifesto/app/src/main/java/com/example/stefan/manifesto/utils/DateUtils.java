package com.example.stefan.manifesto.utils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    public static String getDayMonthFormat(Date date) {
        if (date == null) return "/";

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM", Locale.US);
        return simpleDateFormat.format(date);
    }

    public static String getHourMinuteFormat(Date date) {
        if (date == null) return "/";

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.US);
        return simpleDateFormat.format(date);
    }

    public static Date getProperDateFormat(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US);
        Date date1 = null;
        try {
            date1 = simpleDateFormat.parse(date.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date1;
    }
}
