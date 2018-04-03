package com.smartapp.hztech.smarttebletapp.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.smartapp.hztech.smarttebletapp.helpers.DatabaseHelper;
import com.smartapp.hztech.smarttebletapp.listeners.AsyncResultBag;

public class RetrieveSetting extends AsyncTask<Void, Void, String[]> {
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
    protected String[] doInBackground(Void... voids) {
        String[] values = null;

        try {
            values = _db.getAppDatabase().settingDao().getAll(_keys);
        } catch (Exception e) {
            error = e;
        }

        return values;
    }

    @Override
    protected void onPostExecute(String[] values) {
        super.onPostExecute(values);

        Object val = values;

        if (values != null && values.length == 1)
            val = values[0];

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
