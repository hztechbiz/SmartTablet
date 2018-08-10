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

public class RetrieveSales extends AsyncTask<Void, Void, Sale[]> {
    private DatabaseHelper _db;
    private AsyncResultBag.Error _errorCallback;
    private AsyncResultBag.Before _beforeCallback;
    private AsyncResultBag.Success _successCallback;
    private int _service_id;
    private Object error;

    public RetrieveSales(Context context, int service_id) {
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
    protected Sale[] doInBackground(Void... voids) {
        Sale[] sales = null;

        try {
            SaleDao saleDao = _db.getAppDatabase().saleDao();
            MediaDao mediaDao = _db.getAppDatabase().mediaDao();

            if (_service_id != 0)
                sales = saleDao.getAll(_service_id);
            else
                sales = saleDao.getAll();

            for (Sale sale : sales) {
                if (sale.getMedia_id() > 0)
                    sale.setMedia(mediaDao.get(sale.getMedia_id()));
            }

        } catch (Exception e) {
            error = e;
        }

        return sales;
    }

    @Override
    protected void onPostExecute(Sale[] values) {
        super.onPostExecute(values);

        if (error == null && _successCallback != null)
            _successCallback.onSuccess(values);

        if (error != null && _errorCallback != null)
            _errorCallback.onError(error);
    }

    public RetrieveSales onError(AsyncResultBag.Error callback) {
        _errorCallback = callback;
        return this;
    }

    public RetrieveSales beforeExecuting(AsyncResultBag.Before callback) {
        _beforeCallback = callback;
        return this;
    }

    public RetrieveSales onSuccess(AsyncResultBag.Success callback) {
        _successCallback = callback;
        return this;
    }
}
