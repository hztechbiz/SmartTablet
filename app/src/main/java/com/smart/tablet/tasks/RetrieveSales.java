package com.smart.tablet.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.smart.tablet.dao.ArrivalDao;
import com.smart.tablet.dao.MediaDao;
import com.smart.tablet.dao.SaleDao;
import com.smart.tablet.entities.Arrival;
import com.smart.tablet.entities.Sale;
import com.smart.tablet.helpers.DatabaseHelper;
import com.smart.tablet.listeners.AsyncResultBag;

public class RetrieveSales extends AsyncTask<Void, Void, com.smart.tablet.entities.Sale[]> {
    private com.smart.tablet.helpers.DatabaseHelper _db;
    private com.smart.tablet.listeners.AsyncResultBag.Error _errorCallback;
    private com.smart.tablet.listeners.AsyncResultBag.Before _beforeCallback;
    private com.smart.tablet.listeners.AsyncResultBag.Success _successCallback;
    private int _service_id;
    private Object error;

    public RetrieveSales(Context context, int service_id) {
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
    protected com.smart.tablet.entities.Sale[] doInBackground(Void... voids) {
        com.smart.tablet.entities.Sale[] sales = null;

        try {
            com.smart.tablet.dao.SaleDao saleDao = _db.getAppDatabase().saleDao();
            com.smart.tablet.dao.MediaDao mediaDao = _db.getAppDatabase().mediaDao();

            if (_service_id != 0)
                sales = saleDao.getAll(_service_id);
            else
                sales = saleDao.getAll();

            for (com.smart.tablet.entities.Sale sale : sales) {
                if (sale.getMedia_id() > 0)
                    sale.setMedia(mediaDao.get(sale.getMedia_id()));
            }

        } catch (Exception e) {
            error = e;
        }

        return sales;
    }

    @Override
    protected void onPostExecute(com.smart.tablet.entities.Sale[] values) {
        super.onPostExecute(values);

        if (error == null && _successCallback != null)
            _successCallback.onSuccess(values);

        if (error != null && _errorCallback != null)
            _errorCallback.onError(error);
    }

    public RetrieveSales onError(com.smart.tablet.listeners.AsyncResultBag.Error callback) {
        _errorCallback = callback;
        return this;
    }

    public RetrieveSales beforeExecuting(com.smart.tablet.listeners.AsyncResultBag.Before callback) {
        _beforeCallback = callback;
        return this;
    }

    public RetrieveSales onSuccess(com.smart.tablet.listeners.AsyncResultBag.Success callback) {
        _successCallback = callback;
        return this;
    }
}
