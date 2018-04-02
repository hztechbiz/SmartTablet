package com.smartapp.hztech.smarttebletapp.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.smartapp.hztech.smarttebletapp.entities.Setting;
import com.smartapp.hztech.smarttebletapp.helpers.DatabaseHelper;
import com.smartapp.hztech.smarttebletapp.listeners.AsyncResultBag;

public class StoreSetting extends AsyncTask<Void, Void, Boolean> {
    private DatabaseHelper _db;
    private AsyncResultBag.Error _errorCallback;
    private AsyncResultBag.Before _beforeCallback;
    private AsyncResultBag.Success _successCallback;
    private String _key;
    private String _value;
    private Object error;

    public StoreSetting(Context context, String key, String value) {
        _db = DatabaseHelper.getInstance(context);
        _key = key;
        _value = value;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if (_beforeCallback != null)
            _beforeCallback.beforeExecuting();
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        Setting setting = new Setting();
        setting.setName(_key);
        setting.setValue(_value);

        try {
            _db.getAppDatabase().settingDao().insertAll(setting);
        } catch (Exception e) {
            error = e;
            return false;
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);

        if (error == null && _successCallback != null)
            _successCallback.onSuccess(result);

        if (error != null && _errorCallback != null)
            _errorCallback.onError(error);
    }

    public StoreSetting onError(AsyncResultBag.Error callback) {
        _errorCallback = callback;
        return this;
    }

    public StoreSetting beforeExecuting(AsyncResultBag.Before callback) {
        _beforeCallback = callback;
        return this;
    }

    public StoreSetting onSuccess(AsyncResultBag.Success callback) {
        _successCallback = callback;
        return this;
    }
}
