package com.smart.tablet;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.smart.tablet.entities.Setting;
import com.smart.tablet.listeners.AsyncResultBag;
import com.smart.tablet.service.SyncService;
import com.smart.tablet.tasks.RetrieveSetting;
import com.smart.tablet.tasks.StoreSetting;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SetupActivity extends Activity {

    private ProgressDialog _progressDialog;
    private String API_KEY = Constants.API_KEY;
    private String TOKEN = Constants.TOKEN_KEY;
    private String _token;
    private RelativeLayout _sync_container;
    private BroadcastReceiver syncStartReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            showSynchronizing(true);
        }
    };
    private BroadcastReceiver syncHeartBeatReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            showSynchronizing(true);
        }
    };
    private BroadcastReceiver syncReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            showSynchronizing(false);
        }
    };
    private BroadcastReceiver syncCompleteReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switchScreen(MainActivity.class);
        }
    };

    private void switchScreen(Class activityClass) {
        Intent intent = new Intent(com.smart.tablet.SetupActivity.this, activityClass);
        startActivity(intent);
        finish();

        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    private void showSynchronizing(boolean b) {
        _sync_container.setVisibility(b ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onStart() {
        registerReceiver(syncReceiver, new IntentFilter(SyncService.TRANSACTION_DONE));
        registerReceiver(syncCompleteReceiver, new IntentFilter(SyncService.TRANSACTION_COMPLETE));
        registerReceiver(syncStartReceiver, new IntentFilter(SyncService.TRANSACTION_START));
        registerReceiver(syncHeartBeatReceiver, new IntentFilter(SyncService.TRANSACTION_HEART_BEAT));

        //fetchDeviceToken();

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                _token = instanceIdResult.getToken();
                Log.d("DeviceToken", instanceIdResult + "");
            }
        }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("DeviceToken", e.getMessage());
            }
        });

        super.onStart();
    }

    private void fetchDeviceToken() {
        new RetrieveSetting(this, Constants.DEVICE_ID)
                .onSuccess(new AsyncResultBag.Success() {
                    @Override
                    public void onSuccess(Object result) {
                        if (result != null) {
                            _token = result.toString();
                        }
                    }
                })
                .execute();
    }

    private void checkIfSyncServiceRunning() {
        new RetrieveSetting(this, Constants.SYNC_SERVICE_RUNNING)
                .onSuccess(new AsyncResultBag.Success() {
                    @Override
                    public void onSuccess(Object result) {
                        showSynchronizing((result != null && result.equals("1")));
                    }
                })
                .execute();
    }

    @Override
    protected void onStop() {
        if (syncReceiver != null)
            unregisterReceiver(syncReceiver);

        if (syncStartReceiver != null)
            unregisterReceiver(syncStartReceiver);

        if (syncHeartBeatReceiver != null)
            unregisterReceiver(syncHeartBeatReceiver);

        if (syncCompleteReceiver != null)
            unregisterReceiver(syncCompleteReceiver);

        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_setup);

        _progressDialog = new ProgressDialog(this);
        _sync_container = findViewById(R.id.syncContainer);

        findViewById(R.id.btn_sync).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String key = ((EditText) findViewById(R.id.txt_key)).getText().toString();

                if (key.isEmpty()) {
                    showMessage("Please enter API key provided by the Hotel Manager");
                } else {
                    showProgressDialog("Please wait...");

                    String url = Constants.GetApiUrl("auth");
                    JSONObject jsonRequest = new JSONObject();

                    Log.d("DeviceToken", _token + "");

                    try {
                        jsonRequest.put("udid", _token);
                        jsonRequest.put("api_key", key);
                        jsonRequest.put("mac_address", getMacAddress());
                    } catch (Exception ex) {
                        showMessage(ex.getMessage());
                    }

                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonRequest, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (response.getBoolean("status")) {
                                    String token = response.getJSONObject("data").getString("token");
                                    storeSettings(new Setting(API_KEY, key), new Setting(TOKEN, token));
                                } else {
                                    hideProgressDialog();
                                    showMessage(response.getString("message"));
                                }
                            } catch (JSONException ex) {
                                showMessage(ex.getMessage());
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            showMessage("Failed to setup: " + error);
                        }
                    }) {
                        @Override
                        public Map<String, String> getHeaders() {
                            Map<String, String> params = new HashMap<String, String>();

                            params.put("AppKey", Constants.APP_KEY);

                            return params;
                        }
                    };

                    AppController.getInstance().addToRequestQueue(request);
                }
            }
        });
    }

    private String getMacAddress() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        if (wifiManager != null) {
            WifiInfo wInfo = wifiManager.getConnectionInfo();
            return wInfo != null ? wInfo.getMacAddress() : null;
        }
        return null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideProgressDialog();
    }

    /*
     * Method to store API_KEY in database
     */
    private void storeSettings(Setting... settings) {
        new StoreSetting(this, settings)
                .beforeExecuting(new AsyncResultBag.Before() {
                    @Override
                    public void beforeExecuting() {
                        //showProgressDialog("Please wait...");
                    }
                })
                .onError(new AsyncResultBag.Error() {
                    @Override
                    public void onError(Object error) {
                        Exception e = (Exception) error;

                        hideProgressDialog();
                        showMessage(e.getMessage());
                    }
                })
                .onSuccess(new AsyncResultBag.Success() {
                    @Override
                    public void onSuccess(Object result) {
                        hideProgressDialog();

                        /*
                        Intent syncActivityIntent = new Intent(SetupActivity.this, SyncActivity.class);
                        startActivity(syncActivityIntent);
                        finish();

                        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                        */
                        Intent intent = new Intent(com.smart.tablet.SetupActivity.this, SyncService.class);
                        startService(intent);
                    }
                })
                .execute();
    }

    private void showProgressDialog(String message) {
        _progressDialog.setMessage(message);
        _progressDialog.show();
    }

    private void hideProgressDialog() {
        _progressDialog.dismiss();
    }

    private void showMessage(String message) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message).setPositiveButton("Ok", null);

        builder.show();
    }
}
