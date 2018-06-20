package com.smartapp.hztech.smarttebletapp.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.smartapp.hztech.smarttebletapp.entities.Testimonial;
import com.smartapp.hztech.smarttebletapp.helpers.DatabaseHelper;
import com.smartapp.hztech.smarttebletapp.listeners.AsyncResultBag;

public class StoreTestimonial extends AsyncTask<Void, Void, Boolean> {
    private DatabaseHelper _db;
    private AsyncResultBag.Error _errorCallback;
    private AsyncResultBag.Before _beforeCallback;
    private AsyncResultBag.Success _successCallback;
    private Testimonial[] _testimonials;
    private Object error;

    public StoreTestimonial(Context context, Testimonial... testimonials) {
        _db = DatabaseHelper.getInstance(context);
        _testimonials = testimonials;
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
            _db.getAppDatabase().testimonialDao().insertAll(_testimonials);
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

    public StoreTestimonial onError(AsyncResultBag.Error callback) {
        _errorCallback = callback;
        return this;
    }

    public StoreTestimonial beforeExecuting(AsyncResultBag.Before callback) {
        _beforeCallback = callback;
        return this;
    }

    public StoreTestimonial onSuccess(AsyncResultBag.Success callback) {
        _successCallback = callback;
        return this;
    }
}
