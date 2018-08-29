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

public class RetrieveArrivals extends AsyncTask<Void, Void, Arrival[]> {
    private DatabaseHelper _db;
    private AsyncResultBag.Error _errorCallback;
    private AsyncResultBag.Before _beforeCallback;
    private AsyncResultBag.Success _successCallback;
    private int _service_id;
    private Object error;

    public RetrieveArrivals(Context context, int service_id) {
        _db = DatabaseHelper.getInstance(context);
        _service_id = service_id;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if (_beforeCallback != null)
            _beforeCallback.beforeExecuting();
    }

    @Override
    protected Arrival[] doInBackground(Void... voids) {
        Arrival[] arrivals = null;

        try {
            ArrivalDao arrivalDao = _db.getAppDatabase().arrivalDao();
            MediaDao mediaDao = _db.getAppDatabase().mediaDao();

            if (_service_id != 0)
                arrivals = arrivalDao.getAll(_service_id);
            else
                arrivals = arrivalDao.getAll();

            for (Arrival arrival : arrivals) {
                if (arrival.getMedia_id() > 0)
                    arrival.setMedia(mediaDao.get(arrival.getMedia_id()));
            }

        } catch (Exception e) {
            error = e;
        }

        return arrivals;
    }

    @Override
    protected void onPostExecute(Arrival[] values) {
        super.onPostExecute(values);

        if (error == null && _successCallback != null)
            _successCallback.onSuccess(values);

        if (error != null && _errorCallback != null)
            _errorCallback.onError(error);
    }

    public com.smart.tablet.tasks.RetrieveArrivals onError(AsyncResultBag.Error callback) {
        _errorCallback = callback;
        return this;
    }

    public com.smart.tablet.tasks.RetrieveArrivals beforeExecuting(AsyncResultBag.Before callback) {
        _beforeCallback = callback;
        return this;
    }

    public com.smart.tablet.tasks.RetrieveArrivals onSuccess(AsyncResultBag.Success callback) {
        _successCallback = callback;
        return this;
    }
}
