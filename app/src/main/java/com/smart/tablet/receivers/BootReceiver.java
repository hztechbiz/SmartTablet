package com.smart.tablet.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.smart.tablet.Constants;
import com.smart.tablet.helpers.ScheduledJobs;
import com.smart.tablet.listeners.AsyncResultBag;
import com.smart.tablet.models.HotelModel;
import com.smart.tablet.tasks.RetrieveHotel;
import com.smart.tablet.tasks.RetrieveSetting;

public class BootReceiver extends BroadcastReceiver {
    private static final String TAG = com.smart.tablet.receivers.BootReceiver.class.getSimpleName();

    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.d(TAG, "onReceive - Intent Action: " + intent.getAction());

        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            new RetrieveHotel(context)
                    .onSuccess(new AsyncResultBag.Success() {
                        @Override
                        public void onSuccess(Object result) {
                            String timezone = Constants.DEFAULT_TIMEZONE;

                            if (result != null) {
                                HotelModel hotel = (HotelModel) result;

                                if (hotel.getTimezone() != null && !hotel.getTimezone().equals(""))
                                    timezone = hotel.getTimezone();
                            }

                            ScheduledJobs.scheduleSyncAlarm(context, timezone);
                        }
                    })
                    .execute();

            ScheduledJobs.scheduleWakeupAlarm(context);
        }
    }
}
