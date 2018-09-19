package com.example.stefan.manifesto.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    //todo: what if date is null?
    public static String getDayMonthFormat(Date date) {
        if (date == null) return "/";

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.mm", Locale.US);
        return simpleDateFormat.format(date);
    }

}
