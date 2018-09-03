package com.smart.tablet.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.smart.tablet.entities.Device;
import com.smart.tablet.helpers.DatabaseHelper;
import com.smart.tablet.listeners.AsyncResultBag;

public class RetrieveDevice extends AsyncTask<Void, Void, Device> {
    private DatabaseHelper _db;
    private AsyncResultBag.Error _errorCallback;
    private AsyncResultBag.Before _beforeCallback;
    private AsyncResultBag.Success _successCallback;
    private Object error;

    public RetrieveDevice(Context context) {
        _db = DatabaseHelper.getInstance(context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if (_beforeCallback != null)
            _beforeCallback.beforeExecuting();
    }

    @Override
    protected Device doInBackground(Void... voids) {
        Device device = null;

        try {
            Device[] entities = _db.getAppDatabase().deviceDao().getAll();
            device = entities[0];
        } catch (Exception e) {
            error = e;
        }

        return device;
    }

    @Override
    protected void onPostExecute(Device device) {
        super.onPostExecute(device);

        if (error == null && _successCallback != null)
            _successCallback.onSuccess(device);

        if (error != null && _errorCallback != null)
            _errorCallback.onError(error);
    }

    public RetrieveDevice onError(AsyncResultBag.Error callback) {
        _errorCallback = callback;
        return this;
    }

    public RetrieveDevice beforeExecuting(AsyncResultBag.Before callback) {
        _beforeCallback = callback;
        return this;
    }

    public RetrieveDevice onSuccess(AsyncResultBag.Success callback) {
        _successCallback = callback;
        return this;
    }
}
