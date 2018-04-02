package com.smartapp.hztech.smarttebletapp.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.smartapp.hztech.smarttebletapp.helpers.DatabaseHelper;
import com.smartapp.hztech.smarttebletapp.listeners.AsyncResultBag;

public class RetrieveSetting extends AsyncTask<Void, Void, String> {
    private DatabaseHelper _db;
    private AsyncResultBag.Error _errorCallback;
    private AsyncResultBag.Before _beforeCallback;
    private AsyncResultBag.Success _successCallback;
    private String _key;
    private Object error;

    public RetrieveSetting(Context context, String key) {
        _db = DatabaseHelper.getInstance(context);
        _key = key;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if (_beforeCallback != null)
            _beforeCallback.beforeExecuting();
    }

    @Override
    protected String doInBackground(Void... voids) {
        String value = null;

        try {
            value = _db.getAppDatabase().settingDao().get(_key);
        } catch (Exception e) {
            error = e;
        }

        return value;
    }

    @Override
    protected void onPostExecute(String value) {
        super.onPostExecute(value);

        if (error == null && _successCallback != null)
            _successCallback.onSuccess(value);

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
