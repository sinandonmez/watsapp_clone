package com.tilda.watsapp1;

import android.content.Context;
import android.text.format.DateFormat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Util {

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    public static String getFormattedDate(long smsTimeInMilis, Context context) {
        Calendar smsTime = Calendar.getInstance();
        smsTime.setTimeInMillis(smsTimeInMilis);

        Calendar now = Calendar.getInstance();

        final String timeFormatString = "HH:mm";
        final String dateTimeFormatString = "dd.MM.yyyy";
        final long HOURS = 60 * 60 * 60;
        if (now.get(Calendar.DATE) == smsTime.get(Calendar.DATE) ) {
            //return "Today " + DateFormat.format(timeFormatString, smsTime);
            return "" + DateFormat.format(timeFormatString, smsTime);
        } else if (now.get(Calendar.DATE) - smsTime.get(Calendar.DATE) == 1  ){
            //return "Yesterday " + DateFormat.format(timeFormatString, smsTime);
            return "Dün";
        } else {
            return DateFormat.format("dd.MM.yyyy", smsTime).toString();
        }
    }


//TODO : Sil
//    public static String getTimeAgo(long time, Context ctx) {
//        if (time < 1000000000000L) {
//            // if timestamp given in seconds, convert to millis
//            time *= 1000;
//        }
//
//        long now = System.currentTimeMillis();
////        if (time > now || time <= 0) {
////            return null;
////        }
//
//        final long diff = now - time;
//        if (diff < 24 * HOUR_MILLIS) {
//
//            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
//            Calendar cal = Calendar.getInstance();
//            TimeZone timeZone = cal.getTimeZone();
//            sdf.setTimeZone(timeZone);
//            String timeString = sdf.format(new Date(time));
//
//            return timeString;
//        } else if (diff < 48 * HOUR_MILLIS) {
//            return "Dün";
//        } else {
//
//            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
//            Calendar cal = Calendar.getInstance();
//            TimeZone timeZone = cal.getTimeZone();
//            sdf.setTimeZone(timeZone);
//            String dateString = sdf.format(new Date(time));
//
//            //return diff / DAY_MILLIS + " days ago";
//            return dateString;
//        }
//    }

}
