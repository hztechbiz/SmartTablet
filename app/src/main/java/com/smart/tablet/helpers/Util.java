package com.smart.tablet.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;

import androidx.core.content.res.ResourcesCompat;

import android.text.Html;

import com.smart.tablet.Constants;
import com.smart.tablet.R;

import java.util.Date;

public class Util {
    public static void setLanguage(Context context, String language_code) {
        SharedPreferences sharedPref = context.getApplicationContext().getSharedPreferences(context.getString(R.string.sp_language), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(context.getString(R.string.sp_language), language_code);
        editor.commit();
    }

    public static String getLanguage(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.sp_language), Context.MODE_PRIVATE);

        return sharedPref.getString(context.getString(R.string.sp_language), Constants.DEFAULT_LANG);
    }

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

    public static String stripHtml(String html) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY).toString();
        } else {
            return Html.fromHtml(html).toString();
        }
    }

    public static class DateDifference {
        long seconds, minutes, hours, days;

        public DateDifference(long seconds, long minutes, long hours, long days) {
            this.seconds = seconds;
            this.minutes = minutes;
            this.hours = hours;
            this.days = days;
        }

        public long getSeconds() {
            return seconds;
        }

        public long getMinutes() {
            return minutes;
        }

        public long getHours() {
            return hours;
        }

        public long getDays() {
            return days;
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
