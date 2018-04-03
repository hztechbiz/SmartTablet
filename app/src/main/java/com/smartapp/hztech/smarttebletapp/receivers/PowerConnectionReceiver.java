package com.smartapp.hztech.smarttebletapp.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.smartapp.hztech.smarttebletapp.entities.Setting;
import com.smartapp.hztech.smarttebletapp.tasks.StoreSetting;

import org.json.JSONException;
import org.json.JSONObject;

public class PowerConnectionReceiver extends BroadcastReceiver {
    private String TAG = PowerConnectionReceiver.class.getName();
    private Context _context;

    @Override
    public void onReceive(Context context, Intent intent) {
        _context = context;

        int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;

        int chargePlug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
        boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;

        new StoreSetting(
                context,
                new Setting("isCharging", isCharging ? "true" : "false"),
                new Setting("isOnUsb", usbCharge ? "true" : "false"),
                new Setting("isOnAc", acCharge ? "true" : "false"))
                .execute();

        try {
            sendStatusToServer(isCharging, usbCharge, acCharge);
        } catch (Exception ex) {
            Log.d(TAG, ex.getMessage());
        }
    }

    private void sendStatusToServer(boolean isCharging, boolean usbCharge, boolean acCharge) throws JSONException {
        String url = "http://hztech.biz/smarttablet/battery.php";

        JSONObject jsonRequest = new JSONObject();

        jsonRequest.put("isCharging", isCharging ? "true" : "false");
        jsonRequest.put("usbCharge", usbCharge ? "true" : "false");
        jsonRequest.put("acCharge", acCharge ? "true" : "false");

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonRequest, null, null);

        RequestQueue queue = Volley.newRequestQueue(_context);
        queue.add(request);
    }
}
