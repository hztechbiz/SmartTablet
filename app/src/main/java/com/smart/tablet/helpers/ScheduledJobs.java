package com.smart.tablet.helpers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.smart.tablet.Constants;
import com.smart.tablet.entities.Device;
import com.smart.tablet.listeners.AsyncResultBag;
import com.smart.tablet.receivers.SyncAlarmReceiver;
import com.smart.tablet.receivers.WakeupReceiver;
import com.smart.tablet.tasks.RetrieveDevice;
import com.smart.tablet.tasks.RetrieveHotel;
import com.smart.tablet.tasks.RetrieveSetting;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

public class ScheduledJobs {
    private static String default_sync_time = "03:00:00";

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

    private static boolean got_hotel_sync_time = false;
    private static boolean got_device_sync_time = false;
    private static String hotel_sync_time = null;
    private static String device_sync_time = null;

    public static void scheduleSyncAlarm(final Context context, final String timezone) {
        new RetrieveSetting(context, Constants.SETTING_SYNC_TIME)
                .onSuccess(new AsyncResultBag.Success() {
                    @Override
                    public void onSuccess(Object result) {
                        got_hotel_sync_time = true;
                        hotel_sync_time = (result != null && !result.toString().equals("")) ? result.toString() : default_sync_time;

                        scheduleIt(context, timezone);
                    }
                })
                .onError(new AsyncResultBag.Error() {
                    @Override
                    public void onError(Object error) {
                        got_hotel_sync_time = true;
                        hotel_sync_time = default_sync_time;

                        scheduleIt(context, timezone);
                    }
                })
                .execute();

        new RetrieveDevice(context)
                .onSuccess(new AsyncResultBag.Success() {
                    @Override
                    public void onSuccess(Object result) {
                        got_device_sync_time = true;
                        device_sync_time = null;

                        Device device = result != null ? (Device) result : null;

                        if (device != null) {
                            try {
                                JSONArray metas_arr = new JSONArray(device.getMeta());

                                for (int i = 0; i < metas_arr.length(); i++) {
                                    JSONObject meta_obj = metas_arr.getJSONObject(i);
                                    String meta_key = meta_obj.getString("meta_key");
                                    String meta_value = meta_obj.getString("meta_value");

                                    if (meta_key.equals("sync_time") && meta_value != null && !meta_value.equals("")) {
                                        device_sync_time = meta_value;
                                        break;
                                    }
                                }
                            } catch (Exception e) {
                                device_sync_time = null;
                            }
                        }
                        scheduleIt(context, timezone);
                    }
                })
                .onError(new AsyncResultBag.Error() {
                    @Override
                    public void onError(Object error) {
                        got_device_sync_time = true;
                        device_sync_time = null;

                        scheduleIt(context, timezone);
                    }
                })
                .execute();
    }

    private static void scheduleIt(Context context, String timezone) {
        if (got_device_sync_time && got_hotel_sync_time) {
            String sync_time = hotel_sync_time;

            if (device_sync_time != null)
                sync_time = device_sync_time;

            scheduleSyncAlarmAt(context, timezone, sync_time, true);
        }
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

        Log.d("SyncAlarm", sync_time);

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
