package com.smart.tablet.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.smart.tablet.dao.ArrivalDao;
import com.smart.tablet.dao.MediaDao;
import com.smart.tablet.dao.OfferDao;
import com.smart.tablet.entities.Arrival;
import com.smart.tablet.entities.Offer;
import com.smart.tablet.helpers.DatabaseHelper;
import com.smart.tablet.listeners.AsyncResultBag;

public class RetrieveSingleArrival extends AsyncTask<Void, Void, Arrival> {
    private DatabaseHelper _db;
    private AsyncResultBag.Error _errorCallback;
    private AsyncResultBag.Before _beforeCallback;
    private AsyncResultBag.Success _successCallback;
    private int _id;
    private Object error;

    public RetrieveSingleArrival(Context context, int id) {
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
    protected Arrival doInBackground(Void... voids) {
        Arrival arrival = null;

        try {
            ArrivalDao arrivalDao = _db.getAppDatabase().arrivalDao();
            MediaDao mediaDao = _db.getAppDatabase().mediaDao();

            arrival = arrivalDao.get(_id);

            if (arrival.getMedia_id() > 0)
                arrival.setMedia(mediaDao.get(arrival.getMedia_id()));

        } catch (Exception e) {
            error = e;
        }

        return arrival;
    }

    @Override
    protected void onPostExecute(Arrival values) {
        super.onPostExecute(values);

        if (error == null && _successCallback != null)
            _successCallback.onSuccess(values);

        if (error != null && _errorCallback != null)
            _errorCallback.onError(error);
    }

    public com.smart.tablet.tasks.RetrieveSingleArrival onError(AsyncResultBag.Error callback) {
        _errorCallback = callback;
        return this;
    }

    public com.smart.tablet.tasks.RetrieveSingleArrival beforeExecuting(AsyncResultBag.Before callback) {
        _beforeCallback = callback;
        return this;
    }

    public com.smart.tablet.tasks.RetrieveSingleArrival onSuccess(AsyncResultBag.Success callback) {
        _successCallback = callback;
        return this;
    }
}
