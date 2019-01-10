package com.smart.tablet.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.smart.tablet.AppController;
import com.smart.tablet.entities.Analytics;
import com.smart.tablet.listeners.AsyncResultBag;
import com.smart.tablet.tasks.DeleteAnalytics;
import com.smart.tablet.tasks.RetrieveAnalytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SendAnalytics extends IntentService {
    private static final String TAG = SendAnalytics.class.getName();
    private Context _context;

    public SendAnalytics() {
        super(TAG);

        _context = this;
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        new com.smart.tablet.tasks.RetrieveSetting(_context, com.smart.tablet.Constants.TOKEN_KEY)
                .onSuccess(new com.smart.tablet.listeners.AsyncResultBag.Success() {
                    @Override
                    public void onSuccess(Object result) {
                        if (result != null) {
                            Log.d("SendReport", result + "");
                            final String token = result.toString();

                            new RetrieveAnalytics(_context)
                                    .onSuccess(new AsyncResultBag.Success() {
                                        @Override
                                        public void onSuccess(Object result) {
                                            if (result != null) {
                                                Analytics[] analytics = (Analytics[]) result;

                                                String url = com.smart.tablet.Constants.GetApiUrl("analytics/track");

                                                JSONObject jsonRequest = new JSONObject();
                                                JSONArray jsonArray = new JSONArray();
                                                try {
                                                    for (int i = 0; i < analytics.length; i++) {
                                                        JSONObject obj = new JSONObject();

                                                        obj.put("data", analytics[i].getData());
                                                        obj.put("created_at", analytics[i].getCreated_at());
                                                        obj.put("updated_at", analytics[i].getUpdated_at());

                                                        jsonArray.put(obj);
                                                    }

                                                    jsonRequest.put("data", jsonArray);
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }

                                                Log.d("SendAnalytics", jsonRequest.toString());

                                                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonRequest, new Response.Listener<JSONObject>() {
                                                    @Override
                                                    public void onResponse(JSONObject response) {
                                                        Log.d("SendAnalytics", response + "");
                                                        new DeleteAnalytics(_context).execute();
                                                    }
                                                }, new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        Log.e("SendAnalytics", error + "");
                                                    }
                                                }) {
                                                    @Override
                                                    public Map<String, String> getHeaders() {
                                                        Map<String, String> params = new HashMap<String, String>();

                                                        params.put("AppKey", com.smart.tablet.Constants.APP_KEY);
                                                        params.put("Authorization", token);

                                                        return params;
                                                    }
                                                };

                                                AppController.getInstance().addToRequestQueue(request);
                                            }
                                        }
                                    })
                                    .execute();
                        }
                    }
                })
                .execute();
    }
}
