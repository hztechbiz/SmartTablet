package com.smart.tablet.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.smart.tablet.dao.AnalyticsDao;
import com.smart.tablet.dao.TestimonialDao;
import com.smart.tablet.entities.Analytics;
import com.smart.tablet.entities.Testimonial;
import com.smart.tablet.helpers.DatabaseHelper;
import com.smart.tablet.listeners.AsyncResultBag;

public class RetrieveAnalytics extends AsyncTask<Void, Void, Analytics[]> {
    private DatabaseHelper _db;
    private AsyncResultBag.Error _errorCallback;
    private AsyncResultBag.Before _beforeCallback;
    private AsyncResultBag.Success _successCallback;
    private Object error;

    public RetrieveAnalytics(Context context) {
        _db = DatabaseHelper.getInstance(context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if (_beforeCallback != null)
            _beforeCallback.beforeExecuting();
    }

    @Override
    protected Analytics[] doInBackground(Void... voids) {
        Analytics[] analytics = null;

        try {
            AnalyticsDao analyticsDao = _db.getAppDatabase().analyticsDao();
            analytics = analyticsDao.getAll();

        } catch (Exception e) {
            error = e;
        }

        return analytics;
    }

    @Override
    protected void onPostExecute(Analytics[] values) {
        super.onPostExecute(values);

        if (error == null && _successCallback != null)
            _successCallback.onSuccess(values);

        if (error != null && _errorCallback != null)
            _errorCallback.onError(error);
    }

    public RetrieveAnalytics onError(AsyncResultBag.Error callback) {
        _errorCallback = callback;
        return this;
    }

    public RetrieveAnalytics beforeExecuting(AsyncResultBag.Before callback) {
        _beforeCallback = callback;
        return this;
    }

    public RetrieveAnalytics onSuccess(AsyncResultBag.Success callback) {
        _successCallback = callback;
        return this;
    }
}
