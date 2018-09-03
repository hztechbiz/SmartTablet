package com.smart.tablet.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.smart.tablet.entities.Device;
import com.smart.tablet.helpers.DatabaseHelper;
import com.smart.tablet.listeners.AsyncResultBag;

public class StoreDevice extends AsyncTask<Void, Void, Boolean> {
    private DatabaseHelper _db;
    private AsyncResultBag.Error _errorCallback;
    private AsyncResultBag.Before _beforeCallback;
    private AsyncResultBag.Success _successCallback;
    private Device _device;
    private Object error;

    public StoreDevice(Context context, Device hotel) {
        _db = DatabaseHelper.getInstance(context);
        _device = hotel;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if (_beforeCallback != null)
            _beforeCallback.beforeExecuting();
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            _db.getAppDatabase().deviceDao().insertAll(_device);
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

    public StoreDevice onError(AsyncResultBag.Error callback) {
        _errorCallback = callback;
        return this;
    }

    public StoreDevice beforeExecuting(AsyncResultBag.Before callback) {
        _beforeCallback = callback;
        return this;
    }

    public StoreDevice onSuccess(AsyncResultBag.Success callback) {
        _successCallback = callback;
        return this;
    }
}
