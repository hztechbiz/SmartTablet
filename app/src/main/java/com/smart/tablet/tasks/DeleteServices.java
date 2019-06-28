package com.smart.tablet.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.smart.tablet.helpers.DatabaseHelper;
import com.smart.tablet.listeners.AsyncResultBag;

import java.util.List;

public class DeleteServices extends AsyncTask<Void, Void, Boolean> {
    private DatabaseHelper _db;
    private AsyncResultBag.Error _errorCallback;
    private AsyncResultBag.Before _beforeCallback;
    private AsyncResultBag.Success _successCallback;
    private List<Integer> _ids;
    private Object error;

    public DeleteServices(Context context) {
        _db = DatabaseHelper.getInstance(context);
    }

    public DeleteServices(Context context, List<Integer> ids) {
        _db = DatabaseHelper.getInstance(context);
        _ids = ids;
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
            if (_ids != null) {
                int[] ids = new int[_ids.size()];

                for (int i = 0; i < _ids.size(); i++) {
                    ids[i] = _ids.get(i);
                }

                if (ids.length > 0) {
                    _db.getAppDatabase().serviceDao().deleteAll(ids);
                }

            } else {
                _db.getAppDatabase().serviceDao().deleteAll();
            }
        } catch (Exception e) {
            error = e;
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

    public DeleteServices onError(AsyncResultBag.Error callback) {
        _errorCallback = callback;
        return this;
    }

    public DeleteServices beforeExecuting(AsyncResultBag.Before callback) {
        _beforeCallback = callback;
        return this;
    }

    public DeleteServices onSuccess(AsyncResultBag.Success callback) {
        _successCallback = callback;
        return this;
    }
}
