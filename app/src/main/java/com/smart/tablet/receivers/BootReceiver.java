package com.smart.tablet.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.smart.tablet.helpers.ScheduledJobs;

public class BootReceiver extends BroadcastReceiver {
    private static final String TAG = com.smart.tablet.receivers.BootReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive - Intent Action: " + intent.getAction());

        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            ScheduledJobs.scheduleSyncAlarm(context);
            ScheduledJobs.scheduleWakeupAlarm(context);
        }
    }
}
