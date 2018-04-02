package com.smartapp.hztech.smarttebletapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.smartapp.hztech.smarttebletapp.entities.Hotel;
import com.smartapp.hztech.smarttebletapp.listeners.AsyncResultBag;
import com.smartapp.hztech.smarttebletapp.tasks.StoreHotel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SyncActivity extends Activity {

    private RequestQueue _queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_sync);

        _queue = Volley.newRequestQueue(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        export();
    }

    private void export() {
        String url = "http://192.168.1.105:2202/api/v1/export";

        _queue.add(new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getBoolean("status")) {
                        startExporting(response);
                    } else {
                        showMessage(response.get("message").toString());
                    }
                } catch (Exception e) {
                    showMessage("Error: " + e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showMessage("Error: " + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("AppKey", "smart-$2y$10$RdYWP.Z6T1DFDjSSunimzOUcMDGIBmyqCQ11/Vof.idVxCY14h8ky-api");
                params.put("Authorization", "h0SvovaXacjRKH2vkDk6RneNGRt13rSZA06oGX1B3Z5CsFccMbUTzU4zS66Z");

                return params;
            }
        });
    }

    private void startExporting(JSONObject response) throws JSONException {
        JSONObject data = response.getJSONObject("data");
        JSONObject hotel_obj = data.getJSONObject("hotel");

        Hotel hotel = new Hotel();

        hotel.setId(hotel_obj.getInt("id"));
        hotel.setCountry(hotel_obj.getString("country"));
        hotel.setGroup_id(hotel_obj.getInt("group_id"));
        hotel.setName(hotel_obj.getString("name"));
        hotel.setTimezone(hotel_obj.getString("timezone"));
        hotel.setMeta(hotel_obj.getJSONArray("meta").toString());

        storeHotelInfo(hotel);
    }

    private void storeHotelInfo(Hotel hotel) {
        new StoreHotel(this, hotel)
                .onSuccess(new AsyncResultBag.Success() {
                    @Override
                    public void onSuccess(Object result) {
                        //showMessage("HotelModel info stored");
                        switchScreen(MainActivity.class);
                    }
                })
                .execute();
    }

    private void switchScreen(Class activityClass) {
        Intent intent = new Intent(SyncActivity.this, activityClass);
        startActivity(intent);
        finish();

        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    private void showMessage(String message) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message).setPositiveButton("Ok", null);

        builder.show();
    }
}
