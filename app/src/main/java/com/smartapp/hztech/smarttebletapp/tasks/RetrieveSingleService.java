package com.smartapp.hztech.smarttebletapp.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.smartapp.hztech.smarttebletapp.entities.Service;
import com.smartapp.hztech.smarttebletapp.helpers.DatabaseHelper;
import com.smartapp.hztech.smarttebletapp.listeners.AsyncResultBag;

public class RetrieveSingleService extends AsyncTask<Void, Void, Service> {
    private DatabaseHelper _db;
    private AsyncResultBag.Error _errorCallback;
    private AsyncResultBag.Before _beforeCallback;
    private AsyncResultBag.Success _successCallback;
    private int _id;
    private Object error;

    public RetrieveSingleService(Context context, int id) {
        _db = DatabaseHelper.getInstance(context);
        _id = id;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if (_beforeCallback != null)
            _beforeCallback.beforeExecuting();
    }

    @Override
    protected Service doInBackground(Void... voids) {
        Service value = null;

        try {
            value = _db.getAppDatabase().serviceDao().get(_id);
        } catch (Exception e) {
            error = e;
        }

        return value;
    }

    @Override
    protected void onPostExecute(Service value) {
        super.onPostExecute(value);

        if (error == null && _successCallback != null)
            _successCallback.onSuccess(value);

        if (error != null && _errorCallback != null)
            _errorCallback.onError(error);
    }

    public RetrieveSingleService onError(AsyncResultBag.Error callback) {
        _errorCallback = callback;
        return this;
    }

    public RetrieveSingleService beforeExecuting(AsyncResultBag.Before callback) {
        _beforeCallback = callback;
        return this;
    }

    public RetrieveSingleService onSuccess(AsyncResultBag.Success callback) {
        _successCallback = callback;
        return this;
    }
}
