package com.smartapp.hztech.smarttebletapp.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.smartapp.hztech.smarttebletapp.dao.MediaDao;
import com.smartapp.hztech.smarttebletapp.dao.OfferDao;
import com.smartapp.hztech.smarttebletapp.entities.Offer;
import com.smartapp.hztech.smarttebletapp.helpers.DatabaseHelper;
import com.smartapp.hztech.smarttebletapp.listeners.AsyncResultBag;

public class RetrieveSingleOffer extends AsyncTask<Void, Void, Offer> {
    private DatabaseHelper _db;
    private AsyncResultBag.Error _errorCallback;
    private AsyncResultBag.Before _beforeCallback;
    private AsyncResultBag.Success _successCallback;
    private int _id;
    private Object error;

    public RetrieveSingleOffer(Context context, int id) {
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
    protected Offer doInBackground(Void... voids) {
        Offer offer = null;

        try {
            OfferDao offerDao = _db.getAppDatabase().offerDao();
            MediaDao mediaDao = _db.getAppDatabase().mediaDao();

            offer = offerDao.get(_id);

            if (offer.getMedia_id() > 0)
                offer.setMedia(mediaDao.get(offer.getMedia_id()));

        } catch (Exception e) {
            error = e;
        }

        return offer;
    }

    @Override
    protected void onPostExecute(Offer values) {
        super.onPostExecute(values);

        if (error == null && _successCallback != null)
            _successCallback.onSuccess(values);

        if (error != null && _errorCallback != null)
            _errorCallback.onError(error);
    }

    public RetrieveSingleOffer onError(AsyncResultBag.Error callback) {
        _errorCallback = callback;
        return this;
    }

    public RetrieveSingleOffer beforeExecuting(AsyncResultBag.Before callback) {
        _beforeCallback = callback;
        return this;
    }

    public RetrieveSingleOffer onSuccess(AsyncResultBag.Success callback) {
        _successCallback = callback;
        return this;
    }
}
