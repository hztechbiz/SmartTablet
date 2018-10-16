package com.smart.tablet.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.smart.tablet.Constants;
import com.smart.tablet.helpers.ScheduledJobs;
import com.smart.tablet.listeners.AsyncResultBag;
import com.smart.tablet.models.HotelModel;
import com.smart.tablet.service.SyncService;
import com.smart.tablet.tasks.RetrieveHotel;

public class SyncAlarmReceiver extends BroadcastReceiver {
    public static final int REQUEST_CODE = 123;

    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.d("SchedulingAlarms", "executing");

        boolean isConnected = false;

        /*
        if (!isConnected) {
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

                            ScheduledJobs.scheduleSyncAlarmAt(context, timezone, "", false);
                        }
                    })
                    .execute();
        }
        */

        Intent intent1 = new Intent(context, SyncService.class);
        context.startService(intent1);
    }
}
