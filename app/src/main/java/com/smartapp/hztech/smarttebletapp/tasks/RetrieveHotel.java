package com.smartapp.hztech.smarttebletapp.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.smartapp.hztech.smarttebletapp.entities.Hotel;
import com.smartapp.hztech.smarttebletapp.helpers.DatabaseHelper;
import com.smartapp.hztech.smarttebletapp.listeners.AsyncResultBag;
import com.smartapp.hztech.smarttebletapp.models.HotelModel;

public class RetrieveHotel extends AsyncTask<Void, Void, HotelModel> {
    private DatabaseHelper _db;
    private AsyncResultBag.Error _errorCallback;
    private AsyncResultBag.Before _beforeCallback;
    private AsyncResultBag.Success _successCallback;
    private Object error;

    public RetrieveHotel(Context context) {
        _db = DatabaseHelper.getInstance(context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if (_beforeCallback != null)
            _beforeCallback.beforeExecuting();
    }

    @Override
    protected HotelModel doInBackground(Void... voids) {
        HotelModel hotel = null;

        try {
            Hotel entity = _db.getAppDatabase().hotelDao().getFirst();
            hotel = new HotelModel(entity);
        } catch (Exception e) {
            error = e;
        }

        return hotel;
    }

    @Override
    protected void onPostExecute(HotelModel hotel) {
        super.onPostExecute(hotel);

        if (error == null && _successCallback != null)
            _successCallback.onSuccess(hotel);

        if (error != null && _errorCallback != null)
            _errorCallback.onError(error);
    }

    public RetrieveHotel onError(AsyncResultBag.Error callback) {
        _errorCallback = callback;
        return this;
    }

    public RetrieveHotel beforeExecuting(AsyncResultBag.Before callback) {
        _beforeCallback = callback;
        return this;
    }

    public RetrieveHotel onSuccess(AsyncResultBag.Success callback) {
        _successCallback = callback;
        return this;
    }
}
