package com.smart.tablet.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.smart.tablet.entities.Hotel;
import com.smart.tablet.helpers.DatabaseHelper;
import com.smart.tablet.listeners.AsyncResultBag;
import com.smart.tablet.models.HotelModel;

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

    public com.smart.tablet.tasks.RetrieveHotel onError(AsyncResultBag.Error callback) {
        _errorCallback = callback;
        return this;
    }

    public com.smart.tablet.tasks.RetrieveHotel beforeExecuting(AsyncResultBag.Before callback) {
        _beforeCallback = callback;
        return this;
    }

    public com.smart.tablet.tasks.RetrieveHotel onSuccess(AsyncResultBag.Success callback) {
        _successCallback = callback;
        return this;
    }
}
