package com.smart.tablet.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;

public class WakeupReceiver extends BroadcastReceiver {
    public static final int REQUEST_CODE = 124;
    private static final String TAG = WakeupReceiver.class.getName();

    @Override
    public void onReceive(Context context, Intent intent) {
        PowerManager powerManager = ((PowerManager) context.getSystemService(Context.POWER_SERVICE));

        if (powerManager != null) {
            PowerManager.WakeLock screenLock = powerManager.newWakeLock(
                    PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, TAG);
            screenLock.acquire(10 * 60 * 1000L);
        }
    }
}
