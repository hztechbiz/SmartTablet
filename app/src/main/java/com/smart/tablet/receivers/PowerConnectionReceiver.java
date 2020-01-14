package com.smart.tablet.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.smart.tablet.Constants;
import com.smart.tablet.entities.Setting;
import com.smart.tablet.helpers.Util;
import com.smart.tablet.listeners.AsyncResultBag;
import com.smart.tablet.tasks.RetrieveSetting;
import com.smart.tablet.tasks.StoreSetting;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PowerConnectionReceiver extends BroadcastReceiver {
    public static final String POWER_CONNECTION_CHANGE = "power_connection_change";
    private String TAG = com.smart.tablet.receivers.PowerConnectionReceiver.class.getName();
    private Context _context;
    private String _token;

    @Override
    public void onReceive(Context context, Intent intent) {
        _context = context;

        int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        final boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;

        int chargePlug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        final boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
        final boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;

        Intent i = new Intent(POWER_CONNECTION_CHANGE);
        i.putExtra("isCharging", isCharging);
        i.putExtra("isOnUsb", usbCharge);
        i.putExtra("isOnAc", acCharge);

        context.sendBroadcast(i);

        new StoreSetting(
                context,
                new Setting("isCharging", isCharging ? "true" : "false"),
                new Setting("isOnUsb", usbCharge ? "true" : "false"),
                new Setting("isOnAc", acCharge ? "true" : "false"))
                .execute();

        new RetrieveSetting(context, Constants.TOKEN_KEY)
                .onSuccess(new AsyncResultBag.Success() {
                    @Override
                    public void onSuccess(Object result) {
                        try {
                            if (result != null) {
                                _token = result.toString();
                                sendStatusToServer(isCharging, usbCharge, acCharge);
                            } else {
                                Log.e(TAG, "Token not found");
                            }
                        } catch (Exception ex) {
                            Log.d(TAG, ex.getMessage());
                        }
                    }
                })
                .onError(new AsyncResultBag.Error() {
                    @Override
                    public void onError(Object error) {
                        Log.e(TAG, ((Exception) error).getMessage());
                    }
                })
                .execute();
    }

    private void sendStatusToServer(boolean isCharging, boolean usbCharge, boolean acCharge) throws JSONException {
        String url = Constants.GetApiUrl("device/update");

        JSONObject jsonRequest = new JSONObject();
        String version_name = Util.getVersionName(_context);

        jsonRequest.put("is_charging", isCharging ? "true" : "false");
        jsonRequest.put("usb_charge", usbCharge ? "true" : "false");
        jsonRequest.put("ac_charge", acCharge ? "true" : "false");
        jsonRequest.put("app_version", version_name);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonRequest, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error + ", " + _token);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("AppKey", Constants.APP_KEY);
                params.put("Authorization", _token);

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(_context);
        queue.add(request);
    }
}
