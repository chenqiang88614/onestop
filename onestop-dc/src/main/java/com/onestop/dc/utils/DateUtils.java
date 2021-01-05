package com.onestop.dc.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    public static Date stringToDate(String str){
        return parse(str, Constants.YYYY_MM_DD_HH_MM_SS);
    }

    public static Date parse(String date, String format){
        try {
            return new SimpleDateFormat(format).parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
