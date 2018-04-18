package com.smartapp.hztech.smarttebletapp.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.smartapp.hztech.smarttebletapp.Constants;
import com.smartapp.hztech.smarttebletapp.listeners.AsyncResultBag;
import com.smartapp.hztech.smarttebletapp.tasks.RetrieveSetting;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NetworkChangeReceiver extends BroadcastReceiver {
    private String TAG = NetworkChangeReceiver.class.getName();
    private Context _context;
    private String _token;

    @Override
    public void onReceive(final Context context, Intent intent) {
        _context = context;

        //NetworkInfo info = getNetworkInfo(context);
        final WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                WifiInfo info = wifiManager.getConnectionInfo();
                //String mac = info.getMacAddress();

                final String wifi_name = info.getSSID();
                final int wifi_signals_level = WifiManager.calculateSignalLevel(info.getRssi(), 5);

                new RetrieveSetting(context, Constants.TOKEN_KEY)
                        .onSuccess(new AsyncResultBag.Success() {
                            @Override
                            public void onSuccess(Object result) {
                                try {
                                    if (result != null) {
                                        _token = result.toString();
                                        sendStatusToServer(wifi_name, wifi_signals_level);
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
        }, 5000);
    }

    private void sendStatusToServer(String wifiName, int signalsLevel) throws JSONException {
        String url = Constants.GetApiUrl("device/update");

        JSONObject jsonRequest = new JSONObject();

        jsonRequest.put("wifi_connected_name", wifiName);
        jsonRequest.put("wifi_signals", signalsLevel);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonRequest, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.getMessage());
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
