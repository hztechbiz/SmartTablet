package com.smart.tablet.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.smart.tablet.dao.MediaDao;
import com.smart.tablet.dao.OfferDao;
import com.smart.tablet.entities.Offer;
import com.smart.tablet.helpers.DatabaseHelper;
import com.smart.tablet.listeners.AsyncResultBag;

public class RetrieveOffers extends AsyncTask<Void, Void, com.smart.tablet.entities.Offer[]> {
    private com.smart.tablet.helpers.DatabaseHelper _db;
    private com.smart.tablet.listeners.AsyncResultBag.Error _errorCallback;
    private com.smart.tablet.listeners.AsyncResultBag.Before _beforeCallback;
    private com.smart.tablet.listeners.AsyncResultBag.Success _successCallback;
    private int _service_id;
    private Object error;

    public RetrieveOffers(Context context, int service_id) {
        _db = com.smart.tablet.helpers.DatabaseHelper.getInstance(context);
        _service_id = service_id;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if (_beforeCallback != null)
            _beforeCallback.beforeExecuting();
    }

    @Override
    protected com.smart.tablet.entities.Offer[] doInBackground(Void... voids) {
        com.smart.tablet.entities.Offer[] offers = null;

        try {
            com.smart.tablet.dao.OfferDao offerDao = _db.getAppDatabase().offerDao();
            com.smart.tablet.dao.MediaDao mediaDao = _db.getAppDatabase().mediaDao();

            if (_service_id != 0)
                offers = offerDao.getAll(_service_id);
            else
                offers = offerDao.getAll();

            for (com.smart.tablet.entities.Offer offer : offers) {
                if (offer.getMedia_id() > 0)
                    offer.setMedia(mediaDao.get(offer.getMedia_id()));
            }

        } catch (Exception e) {
            error = e;
        }

        return offers;
    }

    @Override
    protected void onPostExecute(com.smart.tablet.entities.Offer[] values) {
        super.onPostExecute(values);

        if (error == null && _successCallback != null)
            _successCallback.onSuccess(values);

        if (error != null && _errorCallback != null)
            _errorCallback.onError(error);
    }

    public RetrieveOffers onError(com.smart.tablet.listeners.AsyncResultBag.Error callback) {
        _errorCallback = callback;
        return this;
    }

    public RetrieveOffers beforeExecuting(com.smart.tablet.listeners.AsyncResultBag.Before callback) {
        _beforeCallback = callback;
        return this;
    }

    public RetrieveOffers onSuccess(com.smart.tablet.listeners.AsyncResultBag.Success callback) {
        _successCallback = callback;
        return this;
    }
}
