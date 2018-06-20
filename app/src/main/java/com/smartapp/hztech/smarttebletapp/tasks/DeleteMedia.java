package com.smartapp.hztech.smarttebletapp.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.smartapp.hztech.smarttebletapp.dao.MediaDao;
import com.smartapp.hztech.smarttebletapp.entities.Media;
import com.smartapp.hztech.smarttebletapp.helpers.DatabaseHelper;
import com.smartapp.hztech.smarttebletapp.listeners.AsyncResultBag;

import java.io.File;

public class DeleteMedia extends AsyncTask<Void, Void, Boolean> {
    private DatabaseHelper _db;
    private AsyncResultBag.Error _errorCallback;
    private AsyncResultBag.Before _beforeCallback;
    private AsyncResultBag.Success _successCallback;
    private Media[] _media;
    private Object error;
    private int _ids[];

    public DeleteMedia(Context context, int... ids) {
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
            MediaDao mediaDao = _db.getAppDatabase().mediaDao();
            Media[] medias = mediaDao.getAll();

            for (Media media : medias) {
                File file = new File(media.getPath());
                file.delete();

                mediaDao.delete(media);
            }
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

    public DeleteMedia onError(AsyncResultBag.Error callback) {
        _errorCallback = callback;
        return this;
    }

    public DeleteMedia beforeExecuting(AsyncResultBag.Before callback) {
        _beforeCallback = callback;
        return this;
    }

    public DeleteMedia onSuccess(AsyncResultBag.Success callback) {
        _successCallback = callback;
        return this;
    }
}
