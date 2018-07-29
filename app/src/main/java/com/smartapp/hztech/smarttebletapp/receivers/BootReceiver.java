package com.smartapp.hztech.smarttebletapp.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.smartapp.hztech.smarttebletapp.helpers.ScheduledJobs;

public class BootReceiver extends BroadcastReceiver {
    private static final String TAG = BootReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive - Intent Action: " + intent.getAction());

        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            ScheduledJobs.scheduleSyncAlarm(context);
            ScheduledJobs.scheduleWakeupAlarm(context);
        }
    }
}
