package com.smartapp.hztech.smarttebletapp.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.smartapp.hztech.smarttebletapp.entities.Category;
import com.smartapp.hztech.smarttebletapp.helpers.DatabaseHelper;
import com.smartapp.hztech.smarttebletapp.listeners.AsyncResultBag;

public class RetrieveSingleCategory extends AsyncTask<Void, Void, Category> {
    private DatabaseHelper _db;
    private AsyncResultBag.Error _errorCallback;
    private AsyncResultBag.Before _beforeCallback;
    private AsyncResultBag.Success _successCallback;
    private int _id;
    private Object error;

    public RetrieveSingleCategory(Context context, int id) {
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
    protected Category doInBackground(Void... voids) {
        Category value = null;

        try {
            value = _db.getAppDatabase().categoryDao().get(_id);
        } catch (Exception e) {
            error = e;
        }

        return value;
    }

    @Override
    protected void onPostExecute(Category value) {
        super.onPostExecute(value);

        if (error == null && _successCallback != null)
            _successCallback.onSuccess(value);

        if (error != null && _errorCallback != null)
            _errorCallback.onError(error);
    }

    public RetrieveSingleCategory onError(AsyncResultBag.Error callback) {
        _errorCallback = callback;
        return this;
    }

    public RetrieveSingleCategory beforeExecuting(AsyncResultBag.Before callback) {
        _beforeCallback = callback;
        return this;
    }

    public RetrieveSingleCategory onSuccess(AsyncResultBag.Success callback) {
        _successCallback = callback;
        return this;
    }
}
