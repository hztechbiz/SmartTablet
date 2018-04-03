package com.smartapp.hztech.smarttebletapp.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.smartapp.hztech.smarttebletapp.entities.Hotel;
import com.smartapp.hztech.smarttebletapp.helpers.DatabaseHelper;
import com.smartapp.hztech.smarttebletapp.listeners.AsyncResultBag;

public class StoreHotel extends AsyncTask<Void, Void, Boolean> {
    private DatabaseHelper _db;
    private AsyncResultBag.Error _errorCallback;
    private AsyncResultBag.Before _beforeCallback;
    private AsyncResultBag.Success _successCallback;
    private Hotel _hotel;
    private Object error;

    public StoreHotel(Context context, Hotel hotel) {
        _db = DatabaseHelper.getInstance(context);
        _hotel = hotel;
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
            _db.getAppDatabase().hotelDao().insertAll(_hotel);
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

    public StoreHotel onError(AsyncResultBag.Error callback) {
        _errorCallback = callback;
        return this;
    }

    public StoreHotel beforeExecuting(AsyncResultBag.Before callback) {
        _beforeCallback = callback;
        return this;
    }

    public StoreHotel onSuccess(AsyncResultBag.Success callback) {
        _successCallback = callback;
        return this;
    }
}