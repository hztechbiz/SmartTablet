package com.smartapp.hztech.smarttebletapp.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.smartapp.hztech.smarttebletapp.Constants;
import com.smartapp.hztech.smarttebletapp.entities.Category;
import com.smartapp.hztech.smarttebletapp.entities.Hotel;
import com.smartapp.hztech.smarttebletapp.entities.Media;
import com.smartapp.hztech.smarttebletapp.entities.Offer;
import com.smartapp.hztech.smarttebletapp.entities.Service;
import com.smartapp.hztech.smarttebletapp.entities.Setting;
import com.smartapp.hztech.smarttebletapp.entities.Testimonial;
import com.smartapp.hztech.smarttebletapp.listeners.AsyncResultBag;
import com.smartapp.hztech.smarttebletapp.tasks.DeleteMedia;
import com.smartapp.hztech.smarttebletapp.tasks.RetrieveSetting;
import com.smartapp.hztech.smarttebletapp.tasks.StoreCategory;
import com.smartapp.hztech.smarttebletapp.tasks.StoreHotel;
import com.smartapp.hztech.smarttebletapp.tasks.StoreMedia;
import com.smartapp.hztech.smarttebletapp.tasks.StoreOffer;
import com.smartapp.hztech.smarttebletapp.tasks.StoreService;
import com.smartapp.hztech.smarttebletapp.tasks.StoreSetting;
import com.smartapp.hztech.smarttebletapp.tasks.StoreTestimonial;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

public class SyncService extends IntentService {

    public static final String TRANSACTION_DONE = SyncService.class.getName() + ":DONE";
    public static final String TRANSACTION_COMPLETE = SyncService.class.getName() + ":COMPLETE";
    public static final String TRANSACTION_START = SyncService.class.getName() + ":START";
    public static final String TRANSACTION_HEART_BEAT = SyncService.class.getName() + ":HEART_BEAT";
    private static final String TAG = SyncService.class.getName();
    private boolean isRunning = false;
    private String SYNC_DONE = "ST@SYNC_DONE";
    private String TOKEN = "ST@TOKEN";
    private String FILE_PATH = "ST@FILE_PATH";
    private boolean _isSettingsStored;
    private boolean _isHotelInfoStored;
    private boolean _isCategoriesStored;
    private boolean _isServicesStored;
    private boolean _isFilesDownloaded;
    private boolean _isOffersStored;
    private boolean _imageProcessing;
    private String _token;
    private int _extraFieldsLength;
    private int _indexesFilled;
    private boolean _hasError;
    private Object _error;
    private int _totalMedia = 0;
    private int _processedMedia = 0;

    public SyncService() {
        super(TAG);
    }

    private void notifyFinish() {
        isRunning = false;

        new StoreSetting(this, new Setting(Constants.SYNC_SERVICE_RUNNING, "0"))
                .onSuccess(new AsyncResultBag.Success() {
                    @Override
                    public void onSuccess(Object result) {
                        sendFinishBroadcast();
                    }
                })
                .onError(new AsyncResultBag.Error() {
                    @Override
                    public void onError(Object error) {
                        Log.e(TAG, "notifyFinish: " + error);
                        sendFinishBroadcast();
                    }
                })
                .execute();
    }

    private void notifyComplete() {
        isRunning = false;

        new StoreSetting(this, new Setting(Constants.SYNC_SERVICE_RUNNING, "0"))
                .onSuccess(new AsyncResultBag.Success() {
                    @Override
                    public void onSuccess(Object result) {
                        sendCompleteBroadcast();
                    }
                })
                .onError(new AsyncResultBag.Error() {
                    @Override
                    public void onError(Object error) {
                        Log.e(TAG, "notifyComplete: " + error);
                        sendFinishBroadcast();
                    }
                })
                .execute();
    }

    private void sendFinishBroadcast() {
        Intent i = new Intent(TRANSACTION_DONE);
        sendBroadcast(i);
    }

    private void sendCompleteBroadcast() {
        Intent i = new Intent(TRANSACTION_COMPLETE);
        sendBroadcast(i);
    }

    private void sendStartBroadcast() {
        Intent i = new Intent(TRANSACTION_START);
        sendBroadcast(i);
    }

    private void sendHeartBeat() {
        Log.d("SchedulingAlarms", "heart beat sent");
        Intent i = new Intent(TRANSACTION_HEART_BEAT);
        sendBroadcast(i);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        _hasError = false;
        _isServicesStored = _isCategoriesStored = _isSettingsStored = _isHotelInfoStored = _isFilesDownloaded = false;
        _isOffersStored = true;
        _extraFieldsLength = 1;
        _indexesFilled = 0;

        if (!isRunning) {

            isRunning = true;

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    notifyFinish();
                }
            }, Constants.SYNC_WAIT);

            new StoreSetting(this, new Setting(Constants.SYNC_SERVICE_RUNNING, "1"))
                    .onSuccess(new AsyncResultBag.Success() {
                        @Override
                        public void onSuccess(Object result) {
                            retrieveTokenAndStartSync();
                            sendStartBroadcast();
                        }
                    })
                    .onError(new AsyncResultBag.Error() {
                        @Override
                        public void onError(Object error) {
                            Log.e(TAG, "init: " + error);

                            _hasError = true;
                            _error = error;

                            decide();
                        }
                    })
                    .execute();

            //scheduleHeartBeat();
        }
    }

    private void scheduleHeartBeat() {
        Log.d("SchedulingAlarms", "heart beat scheduled");
        try {
            Thread.sleep(1000);
            sendHeartBeat();
        } catch (InterruptedException e) {
            Log.e(TAG, "scheduleHeartBeat: " + e.getMessage());
            e.printStackTrace();
        }

        if (isRunning)
            scheduleHeartBeat();
        else
            notifyFinish();
    }

    private void retrieveTokenAndStartSync() {
        new RetrieveSetting(this, TOKEN)
                .onSuccess(new AsyncResultBag.Success() {
                    @Override
                    public void onSuccess(Object result) {
                        if (result != null) {
                            _token = result.toString();
                            Log.d("SchedulingAlarms", "token: " + _token);
                            sync();
                        }
                    }
                })
                .onError(new AsyncResultBag.Error() {
                    @Override
                    public void onError(Object error) {
                        Log.e(TAG, "retrieveToken: " + error);

                        _hasError = true;
                        _error = error;

                        decide();
                    }
                })
                .execute();
    }

    @Override
    public void onDestroy() {
        isRunning = false;
        super.onDestroy();
    }

    private void sync() {
        String url = Constants.GetApiUrl("export");
        Log.d("SchedulingAlarms", "sync");

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getBoolean("status")) {
                        startSync(response);
                    } else {
                        _hasError = true;
                        _error = response.get("message").toString();

                        decide();
                    }
                } catch (Exception e) {
                    Log.e(TAG, "sync: " + e.getMessage());

                    _hasError = true;
                    _error = e;

                    decide();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "sync: " + error.getMessage());

                _hasError = true;
                _error = error;

                decide();
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

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    private void startSync(JSONObject response) throws JSONException {
        Log.d("SchedulingAlarms", "sync started");

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

        final JSONArray media_arr = data.getJSONArray("objects");

        new DeleteMedia(this)
                .onSuccess(new AsyncResultBag.Success() {
                    @Override
                    public void onSuccess(Object result) {
                        try {
                            storeMedias(media_arr);
                        } catch (JSONException e) {
                            Log.e(TAG, "deleteMedia: " + e.getMessage());

                            _hasError = true;
                            _error = e;

                            decide();
                        }
                    }
                })
                .execute();
    }

    private void storeMedias(JSONArray media_arr) throws JSONException {
        Media[] medias = new Media[media_arr.length()];

        if (media_arr.length() > 0) {
            _totalMedia = media_arr.length();

            Log.d("MediaFile", "total medias: " + _totalMedia);

            for (int i = 0; i < _totalMedia; i++) {
                JSONObject m = media_arr.getJSONObject(i);

                Media media = new Media();
                media.setId(m.getInt("id"));
                media.setUrl(m.getString("url"));

                medias[i] = media;
            }
        }
        if (medias.length > 0) {
            new StoreMedia(this, getFilePath(null), medias)
                    .onSuccess(new AsyncResultBag.Success() {
                        @Override
                        public void onSuccess(Object result) {
                            _isFilesDownloaded = true;
                            decide();
                        }
                    })
                    .onError(new AsyncResultBag.Error() {
                        @Override
                        public void onError(Object error) {
                            Log.e(TAG, "storeMedia: " + error);

                            _hasError = true;
                            _error = error;

                            decide();
                        }
                    })
                    .execute();
        } else {
            _isFilesDownloaded = true;
        }
    }

    private void storeCategories(JSONArray categories_arr) throws JSONException {
        Category[] categories = new Category[categories_arr.length()];

        for (int i = 0; i < categories_arr.length(); i++) {
            JSONObject c = categories_arr.getJSONObject(i);

            Category category = new Category();
            category.setId(c.getInt("id"));
            category.setName(c.getString("name"));
            category.setDescription(c.getString("description"));
            category.setIs_marketing_partner((c.getInt("is_marketing_partner") == 1));

            if (!c.isNull("embed_url"))
                category.setEmbed_url(c.getString("embed_url"));

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
                .onError(new AsyncResultBag.Error() {
                    @Override
                    public void onError(Object error) {
                        Log.e(TAG, "storeCategory: " + error);

                        _hasError = true;
                        _error = error;

                        decide();
                    }
                })
                .execute();
    }

    private void storeServices(JSONArray services_arr) throws JSONException {
        Service[] services = new Service[services_arr.length()];

        for (int i = 0; i < services_arr.length(); i++) {
            JSONObject s = services_arr.getJSONObject(i);
            JSONArray offers = s.getJSONArray("offers");
            JSONArray testimonials = s.getJSONArray("testimonials");

            Service service = new Service();
            service.setId(s.getInt("id"));
            service.setTitle(s.getString("title"));
            service.setDescription(s.getString("description"));
            service.setCategory_id(s.getInt("category_id"));
            service.setStatus(s.getInt("status"));
            service.setHotel_id(s.getInt("hotel_id"));
            service.setIs_marketing_partner((s.getInt("is_marketing_partner") == 1));

            if (!s.isNull("meta"))
                service.setMeta(s.getJSONArray("meta").toString());

            if (offers.length() > 0) {
                storeOffers(offers);
            }

            if (testimonials.length() > 0) {
                storeTestimonials(testimonials);
            }

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
                .onError(new AsyncResultBag.Error() {
                    @Override
                    public void onError(Object error) {
                        Log.e(TAG, "storeService: " + error);

                        _hasError = true;
                        _error = error;

                        decide();
                    }
                })
                .execute();
    }

    private void storeTestimonials(JSONArray testimonials_arr) throws JSONException {
        Testimonial[] testimonials = new Testimonial[testimonials_arr.length()];

        for (int i = 0; i < testimonials_arr.length(); i++) {
            JSONObject o = testimonials_arr.getJSONObject(i);

            Testimonial testimonial = new Testimonial();

            testimonial.setId(o.getInt("id"));
            testimonial.setCite(o.getString("cite"));
            testimonial.setContent(o.getString("content"));
            testimonial.setService_id(o.getInt("service_id"));

            testimonials[i] = testimonial;
        }

        if (testimonials.length > 0) {
            new StoreTestimonial(this, testimonials).execute();
        }
    }

    private void storeOffers(JSONArray offers_arr) throws JSONException {
        Offer[] offers = new Offer[offers_arr.length()];

        for (int i = 0; i < offers_arr.length(); i++) {
            JSONObject o = offers_arr.getJSONObject(i);

            Offer offer = new Offer();

            offer.setId(o.getInt("id"));
            offer.setTitle(o.getString("title"));
            offer.setDescription(o.getString("description"));
            offer.setMedia_id(o.getInt("media_id"));
            offer.setService_id(o.getInt("service_id"));

            offers[i] = offer;
        }

        if (offers.length > 0) {
            new StoreOffer(this, offers).execute();
        }
    }

    private void storeHotelSettings(JSONArray meta) throws JSONException {
        int length = meta.length() + _extraFieldsLength;
        Setting[] settings = new Setting[length];

        for (int i = 0; i < meta.length(); i++) {
            JSONObject m = meta.getJSONObject(i);

            Setting setting = new Setting();
            setting.setName(m.getString("meta_key"));
            setting.setValue(m.getString("meta_value"));

            settings[i] = setting;

            _indexesFilled++;
        }

        settings[_indexesFilled++] = new Setting(FILE_PATH, getFilePath(null));

        new StoreSetting(this, settings)
                .onSuccess(new AsyncResultBag.Success() {
                    @Override
                    public void onSuccess(Object result) {
                        _isSettingsStored = true;
                        decide();
                    }
                })
                .onError(new AsyncResultBag.Error() {
                    @Override
                    public void onError(Object error) {
                        Log.e(TAG, "storeSetting: " + error);

                        _hasError = true;
                        _error = error;

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
                .onError(new AsyncResultBag.Error() {
                    @Override
                    public void onError(Object error) {
                        Log.e(TAG, "storeHotelInfo: " + error);

                        _hasError = true;
                        _error = error;

                        decide();
                    }
                })
                .execute();
    }

    private void decide() {
        Boolean isDone = _isSettingsStored && _isHotelInfoStored && _isCategoriesStored && _isServicesStored && _isFilesDownloaded;
        Log.d("SchedulingAlarms", "all done: " + (isDone ? "yes" : "no"));

        if (isDone) {
            new StoreSetting(this, new Setting(SYNC_DONE, "1"))
                    .onSuccess(new AsyncResultBag.Success() {
                        @Override
                        public void onSuccess(Object result) {
                            notifyComplete();
                        }
                    })
                    .onError(new AsyncResultBag.Error() {
                        @Override
                        public void onError(Object error) {
                            _hasError = true;
                            _error = error;

                            decide();
                        }
                    })
                    .execute();
        } else if (_hasError) {
            if (_error instanceof ConnectException || _error instanceof UnknownHostException || _error instanceof NetworkError) {
                _error = "Connection failed, please try again later";
            }

            notifyFinish();
            showMessage(_error + "");
        }
    }

    private void showMessage(String message) {
        Log.d("SYNCMESSAGE", message);
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private String getFilePath(String filename) {
        String path = getFilesDir().getPath();

        if (filename == null)
            return path;

        return path + File.separator + filename;
    }
}
