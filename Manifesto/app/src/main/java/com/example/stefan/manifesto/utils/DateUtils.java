package com.example.stefan.manifesto.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    public static String getDayMonthFormat(Date date) {
        if (date == null) return "/";

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM", Locale.US);
        return simpleDateFormat.format(date);
    }

    public static String getHourMinutFormat(Date date) {
        if (date == null) return "/";

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.US);
        return simpleDateFormat.format(date);
    }
}
