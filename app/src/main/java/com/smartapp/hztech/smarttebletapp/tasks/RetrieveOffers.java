package com.smartapp.hztech.smarttebletapp.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.smartapp.hztech.smarttebletapp.dao.MediaDao;
import com.smartapp.hztech.smarttebletapp.dao.OfferDao;
import com.smartapp.hztech.smarttebletapp.entities.Offer;
import com.smartapp.hztech.smarttebletapp.helpers.DatabaseHelper;
import com.smartapp.hztech.smarttebletapp.listeners.AsyncResultBag;

public class RetrieveOffers extends AsyncTask<Void, Void, Offer[]> {
    private DatabaseHelper _db;
    private AsyncResultBag.Error _errorCallback;
    private AsyncResultBag.Before _beforeCallback;
    private AsyncResultBag.Success _successCallback;
    private int _service_id;
    private Object error;

    public RetrieveOffers(Context context, int service_id) {
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
    protected Offer[] doInBackground(Void... voids) {
        Offer[] offers = null;

        try {
            OfferDao offerDao = _db.getAppDatabase().offerDao();
            MediaDao mediaDao = _db.getAppDatabase().mediaDao();

            if (_service_id != 0)
                offers = offerDao.getAll(_service_id);
            else
                offers = offerDao.getAll();

            for (Offer offer : offers) {
                if (offer.getMedia_id() > 0)
                    offer.setMedia(mediaDao.get(offer.getMedia_id()));
            }

        } catch (Exception e) {
            error = e;
        }

        return offers;
    }

    @Override
    protected void onPostExecute(Offer[] values) {
        super.onPostExecute(values);

        if (error == null && _successCallback != null)
            _successCallback.onSuccess(values);

        if (error != null && _errorCallback != null)
            _errorCallback.onError(error);
    }

    public RetrieveOffers onError(AsyncResultBag.Error callback) {
        _errorCallback = callback;
        return this;
    }

    public RetrieveOffers beforeExecuting(AsyncResultBag.Before callback) {
        _beforeCallback = callback;
        return this;
    }

    public RetrieveOffers onSuccess(AsyncResultBag.Success callback) {
        _successCallback = callback;
        return this;
    }
}
