package com.smartapp.hztech.smarttebletapp.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.smartapp.hztech.smarttebletapp.dao.CategoryDao;
import com.smartapp.hztech.smarttebletapp.entities.Category;
import com.smartapp.hztech.smarttebletapp.helpers.DatabaseHelper;
import com.smartapp.hztech.smarttebletapp.listeners.AsyncResultBag;

public class RetrieveCategories extends AsyncTask<Void, Void, Category[]> {
    private DatabaseHelper _db;
    private AsyncResultBag.Error _errorCallback;
    private AsyncResultBag.Before _beforeCallback;
    private AsyncResultBag.Success _successCallback;
    private int[] _ids;
    private int _parent_id;
    private Object error;

    public RetrieveCategories(Context context, int parent_id, int... ids) {
        _db = DatabaseHelper.getInstance(context);
        _ids = ids;
        _parent_id = parent_id;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if (_beforeCallback != null)
            _beforeCallback.beforeExecuting();
    }

    @Override
    protected Category[] doInBackground(Void... voids) {
        Category[] values = null;

        try {
            CategoryDao categoryDao = _db.getAppDatabase().categoryDao();

            if (_parent_id != 0)
                values = categoryDao.getAll(_parent_id);
            else if (_ids.length != 0)
                values = categoryDao.getAll(_ids);
            else
                values = categoryDao.getAll();

            for (int i = 0; i < values.length; i++) {
                int count = categoryDao.getAll(values[i].getId()).length;
                values[i].setChildren_count(count);
            }

        } catch (Exception e) {
            error = e;
        }

        return values;
    }

    @Override
    protected void onPostExecute(Category[] values) {
        super.onPostExecute(values);

        if (error == null && _successCallback != null)
            _successCallback.onSuccess(values);

        if (error != null && _errorCallback != null)
            _errorCallback.onError(error);
    }

    public RetrieveCategories onError(AsyncResultBag.Error callback) {
        _errorCallback = callback;
        return this;
    }

    public RetrieveCategories beforeExecuting(AsyncResultBag.Before callback) {
        _beforeCallback = callback;
        return this;
    }

    public RetrieveCategories onSuccess(AsyncResultBag.Success callback) {
        _successCallback = callback;
        return this;
    }
}
