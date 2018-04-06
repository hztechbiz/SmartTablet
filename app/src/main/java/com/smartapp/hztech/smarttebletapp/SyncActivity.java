package com.smartapp.hztech.smarttebletapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.smartapp.hztech.smarttebletapp.entities.Category;
import com.smartapp.hztech.smarttebletapp.entities.Hotel;
import com.smartapp.hztech.smarttebletapp.entities.Service;
import com.smartapp.hztech.smarttebletapp.entities.Setting;
import com.smartapp.hztech.smarttebletapp.listeners.AsyncResultBag;
import com.smartapp.hztech.smarttebletapp.tasks.StoreCategory;
import com.smartapp.hztech.smarttebletapp.tasks.StoreHotel;
import com.smartapp.hztech.smarttebletapp.tasks.StoreService;
import com.smartapp.hztech.smarttebletapp.tasks.StoreSetting;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SyncActivity extends Activity {

    private String SYNC_DONE = "ST@SYNC_DONE";
    private boolean _isSettingsStored;
    private boolean _isHotelInfoStored;
    private boolean _isCategoriesStored;
    private boolean _isServicesStored;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _isServicesStored = _isCategoriesStored = _isSettingsStored = _isHotelInfoStored = false;

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_sync);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sync();
    }

    private void sync() {
        String url = "http://192.168.1.105:2202/api/v1/export";
        url = "http://hztech.biz/smarttablet/api.json";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getBoolean("status")) {
                        startSync(response);
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
        };
        AppController.getInstance().addToRequestQueue(request);
    }

    private void startSync(JSONObject response) throws JSONException {
        JSONObject data = response.getJSONObject("data");
        JSONObject hotel_obj = data.getJSONObject("hotel");

        Hotel hotel = new Hotel();

        hotel.setId(hotel_obj.getInt("id"));
        hotel.setCountry(hotel_obj.getString("country"));
        hotel.setGroup_id(hotel_obj.getInt("group_id"));
        hotel.setName(hotel_obj.getString("name"));
        hotel.setTimezone(hotel_obj.getString("timezone"));

        storeHotelSettings(hotel_obj.getJSONArray("meta"));
        storeHotelInfo(hotel);

        JSONArray categories_arr = data.getJSONArray("categories");
        storeCategories(categories_arr);

        JSONArray services_arr = data.getJSONArray("services");
        storeServices(services_arr);
    }

    private void storeCategories(JSONArray categories_arr) throws JSONException {
        Category[] categories = new Category[categories_arr.length()];

        for (int i = 0; i < categories_arr.length(); i++) {
            JSONObject c = categories_arr.getJSONObject(i);

            Category category = new Category();
            category.setId(c.getInt("id"));
            category.setName(c.getString("name"));
            category.setDescription(c.getString("description"));

            if (!c.isNull("parent_id"))
                category.setParent_id(c.getInt("parent_id"));

            categories[i] = category;
        }

        new StoreCategory(this, categories)
                .onSuccess(new AsyncResultBag.Success() {
                    @Override
                    public void onSuccess(Object result) {
                        _isCategoriesStored = true;
                        decide();
                    }
                })
                .execute();
    }

    private void storeServices(JSONArray services_arr) throws JSONException {
        Service[] services = new Service[services_arr.length()];

        for (int i = 0; i < services_arr.length(); i++) {
            JSONObject s = services_arr.getJSONObject(i);

            Service service = new Service();
            service.setId(s.getInt("id"));
            service.setTitle(s.getString("title"));
            service.setDescription(s.getString("description"));
            service.setCategory_id(s.getInt("category_id"));
            service.setStatus(s.getInt("status"));
            service.setHotel_id(s.getInt("hotel_id"));

            if (!s.isNull("meta"))
                service.setMeta(s.getJSONArray("meta").toString());

            services[i] = service;
        }

        new StoreService(this, services)
                .onSuccess(new AsyncResultBag.Success() {
                    @Override
                    public void onSuccess(Object result) {
                        _isServicesStored = true;
                        decide();
                    }
                })
                .execute();
    }

    private void storeHotelSettings(JSONArray meta) throws JSONException {
        Setting[] settings = new Setting[meta.length()];

        for (int i = 0; i < meta.length(); i++) {
            JSONObject m = meta.getJSONObject(i);

            Setting setting = new Setting();
            setting.setName(m.getString("meta_key"));
            setting.setValue(m.getString("meta_value"));

            settings[i] = setting;
        }

        new StoreSetting(this, settings)
                .onSuccess(new AsyncResultBag.Success() {
                    @Override
                    public void onSuccess(Object result) {
                        _isSettingsStored = true;
                        decide();
                    }
                })
                .execute();
    }

    private void storeHotelInfo(Hotel hotel) {
        new StoreHotel(this, hotel)
                .onSuccess(new AsyncResultBag.Success() {
                    @Override
                    public void onSuccess(Object result) {
                        _isHotelInfoStored = true;
                        decide();
                    }
                })
                .execute();
    }

    private void decide() {
        Boolean isDone = _isSettingsStored && _isHotelInfoStored && _isCategoriesStored && _isServicesStored;

        if (isDone) {
            new StoreSetting(this, new Setting(SYNC_DONE, "1"))
                    .onSuccess(new AsyncResultBag.Success() {
                        @Override
                        public void onSuccess(Object result) {
                            switchScreen(MainActivity.class);
                        }
                    })
                    .execute();
        }
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
