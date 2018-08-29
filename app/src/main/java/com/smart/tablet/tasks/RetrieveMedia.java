package com.smart.tablet.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.smart.tablet.entities.Media;
import com.smart.tablet.helpers.DatabaseHelper;
import com.smart.tablet.listeners.AsyncResultBag;

import java.util.HashMap;

public class RetrieveMedia extends AsyncTask<Void, Void, HashMap<String, String>> {
    private DatabaseHelper _db;
    private AsyncResultBag.Error _errorCallback;
    private AsyncResultBag.Before _beforeCallback;
    private AsyncResultBag.Success _successCallback;
    private int[] _ids;
    private Object error;

    public RetrieveMedia(Context context, int... ids) {
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
    protected HashMap<String, String> doInBackground(Void... voids) {
        Media[] medias;
        HashMap<String, String> values = new HashMap<>();

        try {
            medias = _db.getAppDatabase().mediaDao().getAll(_ids);

            for (int i = 0; i < medias.length; i++) {
                values.put(String.valueOf(medias[i].getId()), medias[i].getPath());
            }
        } catch (Exception e) {
            error = e;
        }

        return values;
    }

    @Override
    protected void onPostExecute(HashMap<String, String> values) {
        super.onPostExecute(values);

        Object val = values;

        if (values.size() == 1 && _ids.length == 1) {
            val = values.get(String.valueOf(_ids[0]));
        }

        if (error == null && _successCallback != null)
            _successCallback.onSuccess(val);

        if (error != null && _errorCallback != null)
            _errorCallback.onError(error);
    }

    public com.smart.tablet.tasks.RetrieveMedia onError(AsyncResultBag.Error callback) {
        _errorCallback = callback;
        return this;
    }

    public com.smart.tablet.tasks.RetrieveMedia beforeExecuting(AsyncResultBag.Before callback) {
        _beforeCallback = callback;
        return this;
    }

    public com.smart.tablet.tasks.RetrieveMedia onSuccess(AsyncResultBag.Success callback) {
        _successCallback = callback;
        return this;
    }
}
