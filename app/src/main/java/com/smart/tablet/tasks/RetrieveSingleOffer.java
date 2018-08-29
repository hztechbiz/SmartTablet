package com.smart.tablet.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.smart.tablet.dao.MediaDao;
import com.smart.tablet.dao.OfferDao;
import com.smart.tablet.entities.Offer;
import com.smart.tablet.helpers.DatabaseHelper;
import com.smart.tablet.listeners.AsyncResultBag;

public class RetrieveSingleOffer extends AsyncTask<Void, Void, com.smart.tablet.entities.Offer> {
    private com.smart.tablet.helpers.DatabaseHelper _db;
    private com.smart.tablet.listeners.AsyncResultBag.Error _errorCallback;
    private com.smart.tablet.listeners.AsyncResultBag.Before _beforeCallback;
    private com.smart.tablet.listeners.AsyncResultBag.Success _successCallback;
    private int _id;
    private Object error;

    public RetrieveSingleOffer(Context context, int id) {
        _db = com.smart.tablet.helpers.DatabaseHelper.getInstance(context);
        _id = id;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if (_beforeCallback != null)
            _beforeCallback.beforeExecuting();
    }

    @Override
    protected com.smart.tablet.entities.Offer doInBackground(Void... voids) {
        com.smart.tablet.entities.Offer offer = null;

        try {
            com.smart.tablet.dao.OfferDao offerDao = _db.getAppDatabase().offerDao();
            com.smart.tablet.dao.MediaDao mediaDao = _db.getAppDatabase().mediaDao();

            offer = offerDao.get(_id);

            if (offer.getMedia_id() > 0)
                offer.setMedia(mediaDao.get(offer.getMedia_id()));

        } catch (Exception e) {
            error = e;
        }

        return offer;
    }

    @Override
    protected void onPostExecute(com.smart.tablet.entities.Offer values) {
        super.onPostExecute(values);

        if (error == null && _successCallback != null)
            _successCallback.onSuccess(values);

        if (error != null && _errorCallback != null)
            _errorCallback.onError(error);
    }

    public RetrieveSingleOffer onError(com.smart.tablet.listeners.AsyncResultBag.Error callback) {
        _errorCallback = callback;
        return this;
    }

    public RetrieveSingleOffer beforeExecuting(com.smart.tablet.listeners.AsyncResultBag.Before callback) {
        _beforeCallback = callback;
        return this;
    }

    public RetrieveSingleOffer onSuccess(com.smart.tablet.listeners.AsyncResultBag.Success callback) {
        _successCallback = callback;
        return this;
    }
}
