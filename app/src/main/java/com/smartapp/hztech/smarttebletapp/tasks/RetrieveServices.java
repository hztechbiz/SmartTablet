package com.smartapp.hztech.smarttebletapp.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.smartapp.hztech.smarttebletapp.dao.ServiceDao;
import com.smartapp.hztech.smarttebletapp.entities.Service;
import com.smartapp.hztech.smarttebletapp.helpers.DatabaseHelper;
import com.smartapp.hztech.smarttebletapp.listeners.AsyncResultBag;

import java.util.List;

public class RetrieveServices extends AsyncTask<Void, Void, List<Service>> {
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
    protected List<Service> doInBackground(Void... voids) {
        List<Service> values = null;

        try {
            ServiceDao serviceDao = _db.getAppDatabase().serviceDao();

            if (_category_id != 0)
                values = serviceDao.getAll(_category_id);
            else if (_ids != null)
                values = serviceDao.getAll(_ids);
            else
                values = serviceDao.getAll();

        } catch (Exception e) {
            error = e;
        }

        return values;
    }

    @Override
    protected void onPostExecute(List<Service> values) {
        super.onPostExecute(values);

        if (error == null && _successCallback != null)
            _successCallback.onSuccess(values);

        if (error != null && _errorCallback != null)
            _errorCallback.onError(error);
    }

    public RetrieveServices onError(AsyncResultBag.Error callback) {
        _errorCallback = callback;
        return this;
    }

    public RetrieveServices beforeExecuting(AsyncResultBag.Before callback) {
        _beforeCallback = callback;
        return this;
    }

    public RetrieveServices onSuccess(AsyncResultBag.Success callback) {
        _successCallback = callback;
        return this;
    }
}
