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

public class BatteryLevelReceiver extends BroadcastReceiver {
    private String TAG = BatteryLevelReceiver.class.getName();
    private Context _context;

    @Override
    public void onReceive(Context context, Intent intent) {
        _context = context;

        int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        float percentage = level / (float) scale;

        new StoreSetting(context, new Setting("batteryPercentage", String.valueOf(percentage)))
                .execute();

        try {
            sendStatusToServer(percentage);
        } catch (Exception ex) {
            Log.d(TAG, ex.getMessage());
        }
    }

    private void sendStatusToServer(float percentage) throws JSONException {
        String url = "http://hztech.biz/smarttablet/battery_level.php";

        JSONObject jsonRequest = new JSONObject();

        jsonRequest.put("batteryLevel", percentage);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonRequest, null, null);

        RequestQueue queue = Volley.newRequestQueue(_context);
        queue.add(request);
    }
}
