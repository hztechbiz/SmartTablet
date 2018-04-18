package com.smartapp.hztech.smarttebletapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.smartapp.hztech.smarttebletapp.entities.Setting;
import com.smartapp.hztech.smarttebletapp.listeners.AsyncResultBag;
import com.smartapp.hztech.smarttebletapp.tasks.StoreSetting;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SetupActivity extends Activity {

    private ProgressDialog _progressDialog;
    private String API_KEY = "ST@API_KEY";
    private String TOKEN = "ST@TOKEN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_setup);

        _progressDialog = new ProgressDialog(this);

        findViewById(R.id.btn_sync).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String key = ((EditText) findViewById(R.id.txt_key)).getText().toString();

                if (key.isEmpty()) {
                    showMessage("Please enter API key provided by the HotelModel Manager");
                } else {
                    showProgressDialog("Please wait...");

                    String url = Constants.GetApiUrl("auth");
                    JSONObject jsonRequest = new JSONObject();

                    try {
                        jsonRequest.put("udid", "Test");
                        jsonRequest.put("api_key", key);
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

                        Intent syncActivityIntent = new Intent(SetupActivity.this, SyncActivity.class);
                        startActivity(syncActivityIntent);
                        finish();

                        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
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
