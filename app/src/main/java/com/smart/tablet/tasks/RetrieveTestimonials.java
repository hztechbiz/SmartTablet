package com.smart.tablet.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.smart.tablet.dao.TestimonialDao;
import com.smart.tablet.entities.Testimonial;
import com.smart.tablet.helpers.DatabaseHelper;
import com.smart.tablet.listeners.AsyncResultBag;

public class RetrieveTestimonials extends AsyncTask<Void, Void, Testimonial[]> {
    private DatabaseHelper _db;
    private AsyncResultBag.Error _errorCallback;
    private AsyncResultBag.Before _beforeCallback;
    private AsyncResultBag.Success _successCallback;
    private int _service_id;
    private Object error;

    public RetrieveTestimonials(Context context, int service_id) {
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
    protected Testimonial[] doInBackground(Void... voids) {
        Testimonial[] testimonials = null;

        try {
            TestimonialDao testimonialDao = _db.getAppDatabase().testimonialDao();

            if (_service_id != 0)
                testimonials = testimonialDao.getAll(_service_id);
            else
                testimonials = testimonialDao.getAll();

        } catch (Exception e) {
            error = e;
        }

        return testimonials;
    }

    @Override
    protected void onPostExecute(Testimonial[] values) {
        super.onPostExecute(values);

        if (error == null && _successCallback != null)
            _successCallback.onSuccess(values);

        if (error != null && _errorCallback != null)
            _errorCallback.onError(error);
    }

    public com.smart.tablet.tasks.RetrieveTestimonials onError(AsyncResultBag.Error callback) {
        _errorCallback = callback;
        return this;
    }

    public com.smart.tablet.tasks.RetrieveTestimonials beforeExecuting(AsyncResultBag.Before callback) {
        _beforeCallback = callback;
        return this;
    }

    public com.smart.tablet.tasks.RetrieveTestimonials onSuccess(AsyncResultBag.Success callback) {
        _successCallback = callback;
        return this;
    }
}
