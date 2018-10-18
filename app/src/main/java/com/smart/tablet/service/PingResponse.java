package com.smart.tablet.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.smart.tablet.Constants;
import com.smart.tablet.listeners.AsyncResultBag;
import com.smart.tablet.tasks.RetrieveSetting;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PingResponse extends IntentService {
    private static final String TAG = PingResponse.class.getName();
    private Context _context;

    public PingResponse() {
        super(TAG);

        _context = this;
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        if (intent == null)
            return;

        Log.d("Ping", "Getting Token");

        new RetrieveSetting(_context, Constants.TOKEN_KEY)
                .onSuccess(new AsyncResultBag.Success() {
                    @Override
                    public void onSuccess(Object result) {
                        if (result != null) {
                            final String token = result.toString();

                            Log.d("Ping", "Started with token: " + token);

                            String url = Constants.GetApiUrl("device/update");

                            JSONObject jsonRequest = new JSONObject();
                            try {
                                jsonRequest.put("ping", true);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonRequest, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.d(TAG, response.toString());
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.d(TAG, error.getMessage());
                                }
                            }) {
                                @Override
                                public Map<String, String> getHeaders() {
                                    Map<String, String> params = new HashMap<String, String>();

                                    params.put("AppKey", Constants.APP_KEY);
                                    params.put("Authorization", token);

                                    return params;
                                }
                            };

                            RequestQueue queue = Volley.newRequestQueue(_context);
                            queue.add(request);
                        }
                    }
                })
                .execute();
    }
}
