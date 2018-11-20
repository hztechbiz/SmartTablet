package com.smart.tablet.helpers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.smart.tablet.Constants;
import com.smart.tablet.listeners.AsyncResultBag;
import com.smart.tablet.receivers.SyncAlarmReceiver;
import com.smart.tablet.receivers.WakeupReceiver;
import com.smart.tablet.tasks.RetrieveHotel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class ScheduledJobs {
    public static void scheduleWakeupAlarm(Context context) {
        Intent intent = new Intent(context, WakeupReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, WakeupReceiver.REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        long millis = System.currentTimeMillis();

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (alarmManager != null) {
            long interval = Constants.SCREEN_WAKEUP_WAIT;
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, millis + interval, interval, pendingIntent);
        }
    }

    public static void scheduleSyncAlarm(final Context context, String timezone) {
        scheduleSyncAlarmAt(context, timezone, "11:00:00", true);
    }

    private static void scheduleSyncAlarmAt(final Context context, String timezone, String sync_time, boolean repeating) {
        Intent intent = new Intent(context, SyncAlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, SyncAlarmReceiver.REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        long millis = System.currentTimeMillis();
        long triggerAt = millis;
        long interval = AlarmManager.INTERVAL_HOUR * 24;

        //interval = 10 * 60 * 1000;
        String datetime_format = "yyyy-MM-dd HH:mm:ss";
        String date_format = "yyyy-MM-dd";
        //sync_time = "18:49:00";

        SimpleDateFormat df = new SimpleDateFormat(date_format);
        SimpleDateFormat sdf = new SimpleDateFormat(datetime_format);

        Log.d("SyncAlarm", timezone);

        if (timezone != null && !timezone.equals("")) {
            df.setTimeZone(TimeZone.getTimeZone(timezone));
            sdf.setTimeZone(TimeZone.getTimeZone(timezone));
        }

        String current_date = df.format(millis);
        String givenDateString = current_date + " " + sync_time;

        try {
            Date mDate = sdf.parse(givenDateString);
            triggerAt = mDate.getTime();

            if (millis > triggerAt) {
                triggerAt += AlarmManager.INTERVAL_HOUR * 24;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Log.d("SyncAlarm", "Current: " + sdf.format(millis));
        Log.d("SyncAlarm", "Trigger at: " + sdf.format(triggerAt));

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (alarmManager != null) {
            if (repeating) {
                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, triggerAt, interval, pendingIntent);
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerAt, pendingIntent);
            }
        }
    }
}
