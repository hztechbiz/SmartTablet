package com.smart.tablet.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.smart.tablet.Constants;
import com.smart.tablet.MessagePopupActivity;
import com.smart.tablet.R;
import com.smart.tablet.helpers.ScheduledJobs;
import com.smart.tablet.listeners.AsyncResultBag;
import com.smart.tablet.models.HotelModel;
import com.smart.tablet.service.SyncService;
import com.smart.tablet.tasks.RetrieveHotel;
import com.smart.tablet.tasks.RetrieveSetting;

public class SyncAlarmReceiver extends BroadcastReceiver {
    public static final int REQUEST_CODE = 123;
    public static final String TRANSACTION_BEFORE_SYNC = SyncAlarmReceiver.class.getName() + ":BEFORE";

    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.d("SchedulingAlarms", "executing");

        new RetrieveSetting(context, Constants.SETTING_SYNC_ENABLE)
                .onSuccess(new AsyncResultBag.Success() {
                    @Override
                    public void onSuccess(Object result) {
                        Log.d("SyncAlarmReceiver", result + "");

                        if (result == null)
                            result = "1";

                        if (result.equals("1")) {
                            Intent intent1 = new Intent(context, SyncService.class);
                            intent1.putExtra(context.getString(R.string.param_sync_wait), Constants.SYNC_BEFORE_WAIT);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                context.startForegroundService(intent1);
                            } else {
                                context.startService(intent1);
                            }

                            Intent i = new Intent(TRANSACTION_BEFORE_SYNC);
                            context.sendBroadcast(i);
                        }
                    }
                })
                .onError(new AsyncResultBag.Error() {
                    @Override
                    public void onError(Object error) {
                        Log.e("SyncAlarmReceiver", error + "");
                    }
                })
                .execute();
    }
}
