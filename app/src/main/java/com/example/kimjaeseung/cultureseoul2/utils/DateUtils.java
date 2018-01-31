package com.example.kimjaeseung.cultureseoul2.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by heo04 on 2017-08-26.
 */

public class DateUtils {
    public static String dateToString(Date newDate) {
        DateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
        String tempDate = sdFormat.format(newDate);
        return tempDate;
    }
}
