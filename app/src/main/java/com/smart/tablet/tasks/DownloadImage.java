package com.smart.tablet.tasks;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.smart.tablet.entities.Hotel;
import com.smart.tablet.helpers.DatabaseHelper;
import com.smart.tablet.listeners.AsyncResultBag;
import com.smart.tablet.models.HotelModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

public class DownloadImage extends AsyncTask<Void, Void, Bitmap> {
    private AsyncResultBag.Error _errorCallback;
    private AsyncResultBag.Before _beforeCallback;
    private AsyncResultBag.Success _successCallback;
    private Object error;
    private String _url;
    private String _filepath;

    public DownloadImage(Context context, String url, String filepath) {
        _url = url;
        _filepath = filepath;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if (_beforeCallback != null)
            _beforeCallback.beforeExecuting();
    }

    @Override
    protected Bitmap doInBackground(Void... voids) {
        try {
            URL url = new URL(_url);
            InputStream in = url.openStream();
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            byte[] buf = new byte[1024];
            int n = in.read(buf);

            while (n != -1) {
                out.write(buf, 0, n);
                n = in.read(buf);
            }

            out.close();
            in.close();

            byte[] response = out.toByteArray();

            FileOutputStream fos = new FileOutputStream(_filepath);

            fos.write(response);
            fos.close();

            File imgFile = new File(_filepath);

            if (imgFile.exists()) {
                return BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            }
        } catch (Exception | OutOfMemoryError e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);

        if (error == null && _successCallback != null)
            _successCallback.onSuccess(bitmap);

        if (error != null && _errorCallback != null)
            _errorCallback.onError(error);
    }

    public com.smart.tablet.tasks.DownloadImage onError(AsyncResultBag.Error callback) {
        _errorCallback = callback;
        return this;
    }

    public com.smart.tablet.tasks.DownloadImage beforeExecuting(AsyncResultBag.Before callback) {
        _beforeCallback = callback;
        return this;
    }

    public com.smart.tablet.tasks.DownloadImage onSuccess(AsyncResultBag.Success callback) {
        _successCallback = callback;
        return this;
    }
}
