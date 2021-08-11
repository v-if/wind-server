package com.github.tkpark.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

public class DateTimeUtils {

    public static Timestamp timestampOf(LocalDateTime time) {
        return time == null ? null : Timestamp.valueOf(time);
    }

    public static LocalDateTime dateTimeOf(Timestamp timestamp) {
        return timestamp == null ? null : timestamp.toLocalDateTime();
    }

    public static String getDate() {
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat hourFormat = new SimpleDateFormat("HH");
        SimpleDateFormat minuteFormat = new SimpleDateFormat("mm");

        String date = dateFormat.format(now);
        //String time = "";
        String hour = hourFormat.format(now);
        String minute = minuteFormat.format(now);

        int h = Integer.parseInt(hour);
        int m = Integer.parseInt(minute);
        if(m <= 40) {
            h = h - 1;
            if(h < 0) {
                Calendar c = Calendar.getInstance();
                c.setTime(now);
                c.add(Calendar.DATE, -1);
                Date before = c.getTime();
                date = dateFormat.format(before);
                h = 23;
            }
        }
        //time = String.format("%02d", h) + "00";

        return date;
    }

    public static String getTime() {
        Date now = new Date();
        //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat hourFormat = new SimpleDateFormat("HH");
        SimpleDateFormat minuteFormat = new SimpleDateFormat("mm");

        //String date = dateFormat.format(now);
        String time = "";
        String hour = hourFormat.format(now);
        String minute = minuteFormat.format(now);

        int h = Integer.parseInt(hour);
        int m = Integer.parseInt(minute);
        if(m <= 40) {
            h = h - 1;
            if(h < 0) {
                Calendar c = Calendar.getInstance();
                c.setTime(now);
                c.add(Calendar.DATE, -1);
                Date before = c.getTime();
                //date = dateFormat.format(before);
                h = 23;
            }
        }
        time = String.format("%02d", h) + "00";

        return time;
    }

    public static String getPreviousDay() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        Date today = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(today);
        c.add(Calendar.DATE, -1);
        Date preDay = c.getTime();

        return sdf.format(preDay);
    }
}
