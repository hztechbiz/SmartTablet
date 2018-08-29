package com.smart.tablet.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.smart.tablet.entities.Media;
import com.smart.tablet.entities.Setting;
import com.smart.tablet.helpers.DatabaseHelper;
import com.smart.tablet.listeners.AsyncResultBag;

import java.util.Arrays;
import java.util.HashMap;

public class RetrieveSetting extends AsyncTask<Void, Void, HashMap<String, String>> {
    private DatabaseHelper _db;
    private AsyncResultBag.Error _errorCallback;
    private AsyncResultBag.Before _beforeCallback;
    private AsyncResultBag.Success _successCallback;
    private String[] _keys, _media_keys;
    private Object error;

    public RetrieveSetting(Context context, String... keys) {
        _db = DatabaseHelper.getInstance(context);
        _keys = keys;
    }

    public void setMediaKeys(String... keys) {
        _media_keys = keys;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if (_beforeCallback != null)
            _beforeCallback.beforeExecuting();
    }

    @Override
    protected HashMap<String, String> doInBackground(Void... voids) {
        Setting[] settings;
        HashMap<String, String> values = new HashMap<>();

        try {
            settings = _db.getAppDatabase().settingDao().getAll(_keys);

            for (int i = 0; i < settings.length; i++) {
                String value = settings[i].getValue();

                if (_media_keys != null && Arrays.asList(_media_keys).contains(settings[i].getName())) {
                    Media media = _db.getAppDatabase().mediaDao().get(Integer.parseInt(value));
                    value = media.getPath();
                }

                values.put(settings[i].getName(), value);
            }
        } catch (Exception e) {
            error = e;
        }

        return values;
    }

    @Override
    protected void onPostExecute(HashMap<String, String> values) {
        super.onPostExecute(values);

        Object val = values;

        if (values.size() == 1 && _keys.length == 1) {
            val = values.get(_keys[0]);
        }

        if (error == null && _successCallback != null)
            _successCallback.onSuccess(val);

        if (error != null && _errorCallback != null)
            _errorCallback.onError(error);
    }

    public com.smart.tablet.tasks.RetrieveSetting onError(AsyncResultBag.Error callback) {
        _errorCallback = callback;
        return this;
    }

    public com.smart.tablet.tasks.RetrieveSetting beforeExecuting(AsyncResultBag.Before callback) {
        _beforeCallback = callback;
        return this;
    }

    public com.smart.tablet.tasks.RetrieveSetting onSuccess(AsyncResultBag.Success callback) {
        _successCallback = callback;
        return this;
    }
}
