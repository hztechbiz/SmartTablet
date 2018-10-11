package com.smart.tablet.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.smart.tablet.entities.Analytics;
import com.smart.tablet.entities.Arrival;
import com.smart.tablet.helpers.DatabaseHelper;
import com.smart.tablet.listeners.AsyncResultBag;

public class StoreAnalytics extends AsyncTask<Void, Void, Boolean> {
    private DatabaseHelper _db;
    private AsyncResultBag.Error _errorCallback;
    private AsyncResultBag.Before _beforeCallback;
    private AsyncResultBag.Success _successCallback;
    private Analytics[] _analytics;
    private Object error;

    public StoreAnalytics(Context context, Analytics... analytics) {
        _db = DatabaseHelper.getInstance(context);
        _analytics = analytics;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if (_beforeCallback != null)
            _beforeCallback.beforeExecuting();
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            _db.getAppDatabase().analyticsDao().insertAll(_analytics);
        } catch (Exception e) {
            error = e;
            return false;
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);

        if (error == null && _successCallback != null)
            _successCallback.onSuccess(result);

        if (error != null && _errorCallback != null)
            _errorCallback.onError(error);
    }

    public StoreAnalytics onError(AsyncResultBag.Error callback) {
        _errorCallback = callback;
        return this;
    }

    public StoreAnalytics beforeExecuting(AsyncResultBag.Before callback) {
        _beforeCallback = callback;
        return this;
    }

    public StoreAnalytics onSuccess(AsyncResultBag.Success callback) {
        _successCallback = callback;
        return this;
    }
}
