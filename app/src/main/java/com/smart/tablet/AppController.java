package com.smart.tablet;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.smart.tablet.listeners.AsyncResultBag;
import com.smart.tablet.tasks.RetrieveSetting;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AppController extends Application {

    public static final String TAG = com.smart.tablet.AppController.class.getSimpleName();
    private static com.smart.tablet.AppController mInstance;
    private RequestQueue mRequestQueue;
    private String _token;
    private boolean _syncDone;
    private String _wifiName;
    private int _wifiLevel;
    private int _batteryLevel;
    private boolean _isCharging, _usbCharge, _acCharge;
    private BroadcastReceiver wifiStatusReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

            if (wifiManager != null) {
                WifiInfo info = wifiManager.getConnectionInfo();

                _wifiName = info.getSSID();
                _wifiLevel = WifiManager.calculateSignalLevel(info.getRssi(), 4);

                sendStatusToServer();
            }
        }
    };
    private BroadcastReceiver batteryStatusReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            _batteryLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);

            int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            _isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                    status == BatteryManager.BATTERY_STATUS_FULL;

            int chargePlug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
            _usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
            _acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;

            sendStatusToServer();
        }
    };
    private BroadcastReceiver powerStatusReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            _isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                    status == BatteryManager.BATTERY_STATUS_FULL;

            int chargePlug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
            _usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
            _acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;

            sendStatusToServer();
        }
    };

    public static synchronized com.smart.tablet.AppController getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        registerReceiver(wifiStatusReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        registerReceiver(batteryStatusReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        registerReceiver(powerStatusReceiver, new IntentFilter(Intent.ACTION_POWER_CONNECTED));
        registerReceiver(powerStatusReceiver, new IntentFilter(Intent.ACTION_POWER_DISCONNECTED));

        fetchSettings();
    }

    @Override
    public void onTerminate() {
        unregisterReceiver(batteryStatusReceiver);
        unregisterReceiver(wifiStatusReceiver);
        unregisterReceiver(powerStatusReceiver);

        super.onTerminate();
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    private void sendStatusToServer() {

        if (!_syncDone)
            return;

        String url = Constants.GetApiUrl("device/update");

        JSONObject jsonRequest = new JSONObject();

        try {
            jsonRequest.put("battery_percentage", _batteryLevel);
            jsonRequest.put("wifi_signals", _wifiLevel);
            jsonRequest.put("wifi_connected_name", _wifiName);
            jsonRequest.put("is_charging", _isCharging ? "true" : "false");
            jsonRequest.put("usb_charge", _usbCharge ? "true" : "false");
            jsonRequest.put("ac_charge", _acCharge ? "true" : "false");
            Log.d(TAG, jsonRequest.toString());
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonRequest, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                Log.d(TAG, "Token: " + _token);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, error + "");
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

        if (_token != null) {
            com.smart.tablet.AppController.getInstance().addToRequestQueue(request);
        }
    }

    private void fetchSettings() {
        new RetrieveSetting(this, Constants.TOKEN_KEY, Constants.SETTING_SYNC_DONE)
                .onSuccess(new AsyncResultBag.Success() {
                    @Override
                    public void onSuccess(Object result) {
                        try {
                            if (result != null) {
                                HashMap<String, String> settings = (HashMap<String, String>) result;

                                _token = settings.get(Constants.TOKEN_KEY);
                                _syncDone = settings.get(Constants.SETTING_SYNC_DONE).equals("1");

                                sendStatusToServer();
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
}