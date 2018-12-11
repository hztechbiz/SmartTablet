package com.smart.tablet.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.smart.tablet.dao.ServiceDao;
import com.smart.tablet.entities.Service;
import com.smart.tablet.helpers.DatabaseHelper;
import com.smart.tablet.listeners.AsyncResultBag;

import java.util.List;

public class RetrieveServices extends AsyncTask<Void, Void, Service[] > {
    private DatabaseHelper _db;
    private AsyncResultBag.Error _errorCallback;
    private AsyncResultBag.Before _beforeCallback;
    private AsyncResultBag.Success _successCallback;
    private int[] _ids;
    private int _category_id;
    private Object error;

    public RetrieveServices(Context context, int category_id, int... ids) {
        _db = DatabaseHelper.getInstance(context);
        _ids = ids;
        _category_id = category_id;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if (_beforeCallback != null)
            _beforeCallback.beforeExecuting();
    }

    @Override
    protected Service[]  doInBackground(Void... voids) {
        Service[]  values = null;

        try {
            ServiceDao serviceDao = _db.getAppDatabase().serviceDao();

            if (_category_id != 0)
                values = serviceDao.getAll(_category_id);
            else if (_ids.length > 0)
                values = serviceDao.getAll(_ids);
            else
                values = serviceDao.getAll();

        } catch (Exception | OutOfMemoryError e) {
            error = e;
        }

        return values;
    }

    @Override
    protected void onPostExecute(Service[]  values) {
        super.onPostExecute(values);

        if (error == null && _successCallback != null)
            _successCallback.onSuccess(values);

        if (error != null && _errorCallback != null)
            _errorCallback.onError(error);
    }

    public com.smart.tablet.tasks.RetrieveServices onError(AsyncResultBag.Error callback) {
        _errorCallback = callback;
        return this;
    }

    public com.smart.tablet.tasks.RetrieveServices beforeExecuting(AsyncResultBag.Before callback) {
        _beforeCallback = callback;
        return this;
    }

    public com.smart.tablet.tasks.RetrieveServices onSuccess(AsyncResultBag.Success callback) {
        _successCallback = callback;
        return this;
    }
}
