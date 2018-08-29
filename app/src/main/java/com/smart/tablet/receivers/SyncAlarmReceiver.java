package com.smart.tablet.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.smart.tablet.service.SyncService;

public class SyncAlarmReceiver extends BroadcastReceiver {
    public static final int REQUEST_CODE = 123;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("SchedulingAlarms", "executing");
        Intent intent1 = new Intent(context, SyncService.class);
        context.startService(intent1);
    }
}
