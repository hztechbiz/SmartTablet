package com.smart.tablet.helpers;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.res.ResourcesCompat;

import com.smart.tablet.R;

import java.util.Date;

public class Util {
    public static Typeface getTypeFace(Context context) {
        return ResourcesCompat.getFont(context, R.font.lato_regular);
    }

    public static Typeface getBoldTypeFace(Context context) {
        return ResourcesCompat.getFont(context, R.font.lato_bold);
    }

    public static DateDifference getDateDifference(Date date1, Date date2) {
        long diff = date1.getTime() - date2.getTime();
        long seconds = diff / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;

        return new DateDifference(seconds, minutes, hours, days);
    }

    public static class DateDifference {
        long seconds, minutes, hours, days;

        public DateDifference(long seconds, long minutes, long hours, long days) {
            this.seconds = seconds;
            this.minutes = minutes;
            this.hours = hours;
            this.days = days;
        }

        @Override
        public String toString() {
            return "DateDifference{" +
                    "seconds=" + seconds +
                    ", minutes=" + minutes +
                    ", hours=" + hours +
                    ", days=" + days +
                    '}';
        }
    }
}
