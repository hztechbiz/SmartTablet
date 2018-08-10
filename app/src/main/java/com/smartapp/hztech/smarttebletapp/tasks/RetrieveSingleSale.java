package com.smartapp.hztech.smarttebletapp.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.smartapp.hztech.smarttebletapp.dao.ArrivalDao;
import com.smartapp.hztech.smarttebletapp.dao.MediaDao;
import com.smartapp.hztech.smarttebletapp.dao.SaleDao;
import com.smartapp.hztech.smarttebletapp.entities.Arrival;
import com.smartapp.hztech.smarttebletapp.entities.Sale;
import com.smartapp.hztech.smarttebletapp.helpers.DatabaseHelper;
import com.smartapp.hztech.smarttebletapp.listeners.AsyncResultBag;

public class RetrieveSingleSale extends AsyncTask<Void, Void, Sale> {
    private DatabaseHelper _db;
    private AsyncResultBag.Error _errorCallback;
    private AsyncResultBag.Before _beforeCallback;
    private AsyncResultBag.Success _successCallback;
    private int _id;
    private Object error;

    public RetrieveSingleSale(Context context, int id) {
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
    protected Sale doInBackground(Void... voids) {
        Sale sale = null;

        try {
            SaleDao arrivalDao = _db.getAppDatabase().saleDao();
            MediaDao mediaDao = _db.getAppDatabase().mediaDao();

            sale = arrivalDao.get(_id);

            if (sale.getMedia_id() > 0)
                sale.setMedia(mediaDao.get(sale.getMedia_id()));

        } catch (Exception e) {
            error = e;
        }

        return sale;
    }

    @Override
    protected void onPostExecute(Sale values) {
        super.onPostExecute(values);

        if (error == null && _successCallback != null)
            _successCallback.onSuccess(values);

        if (error != null && _errorCallback != null)
            _errorCallback.onError(error);
    }

    public RetrieveSingleSale onError(AsyncResultBag.Error callback) {
        _errorCallback = callback;
        return this;
    }

    public RetrieveSingleSale beforeExecuting(AsyncResultBag.Before callback) {
        _beforeCallback = callback;
        return this;
    }

    public RetrieveSingleSale onSuccess(AsyncResultBag.Success callback) {
        _successCallback = callback;
        return this;
    }
}
