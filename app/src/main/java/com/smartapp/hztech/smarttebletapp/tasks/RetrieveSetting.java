package com.smartapp.hztech.smarttebletapp.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.smartapp.hztech.smarttebletapp.entities.Setting;
import com.smartapp.hztech.smarttebletapp.helpers.DatabaseHelper;
import com.smartapp.hztech.smarttebletapp.listeners.AsyncResultBag;

import java.util.HashMap;

public class RetrieveSetting extends AsyncTask<Void, Void, HashMap<String, String>> {
    private DatabaseHelper _db;
    private AsyncResultBag.Error _errorCallback;
    private AsyncResultBag.Before _beforeCallback;
    private AsyncResultBag.Success _successCallback;
    private String[] _keys;
    private Object error;

    public RetrieveSetting(Context context, String... keys) {
        _db = DatabaseHelper.getInstance(context);
        _keys = keys;
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
                values.put(settings[i].getName(), settings[i].getValue());
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

    public RetrieveSetting onError(AsyncResultBag.Error callback) {
        _errorCallback = callback;
        return this;
    }

    public RetrieveSetting beforeExecuting(AsyncResultBag.Before callback) {
        _beforeCallback = callback;
        return this;
    }

    public RetrieveSetting onSuccess(AsyncResultBag.Success callback) {
        _successCallback = callback;
        return this;
    }
}
