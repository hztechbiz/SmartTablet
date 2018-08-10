package com.smartapp.hztech.smarttebletapp.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.smartapp.hztech.smarttebletapp.entities.Media;
import com.smartapp.hztech.smarttebletapp.helpers.DatabaseHelper;
import com.smartapp.hztech.smarttebletapp.listeners.AsyncResultBag;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class StoreMedia extends AsyncTask<Void, Void, Boolean> {
    private DatabaseHelper _db;
    private AsyncResultBag.Error _errorCallback;
    private AsyncResultBag.Before _beforeCallback;
    private AsyncResultBag.Success _successCallback;
    private Media[] _media;
    private java.lang.Object error;
    private String _filepath;
    private int _totalMedia;
    private int _downloaded;

    public StoreMedia(Context context, String filepath, Media... media) {
        _db = DatabaseHelper.getInstance(context);
        _media = media;
        _filepath = filepath;
        _totalMedia = media.length;
        _downloaded = 0;
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

            int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();

            ThreadPoolExecutor executor = new ThreadPoolExecutor(
                    NUMBER_OF_CORES * 2,
                    NUMBER_OF_CORES * 2,
                    60L,
                    TimeUnit.SECONDS,
                    new LinkedBlockingQueue<Runnable>()
            );

            for (int i = 0; i < _media.length; i++) {
                executor.execute(new LongThread(i));
            }

            while (_downloaded != _totalMedia) {
                Thread.sleep(1000);
            }

            _db.getAppDatabase().mediaDao().insertAll(_media);

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

    public StoreMedia onError(AsyncResultBag.Error callback) {
        _errorCallback = callback;
        return this;
    }

    public StoreMedia beforeExecuting(AsyncResultBag.Before callback) {
        _beforeCallback = callback;
        return this;
    }

    public StoreMedia onSuccess(AsyncResultBag.Success callback) {
        _successCallback = callback;
        return this;
    }

    public class LongThread implements Runnable {
        private int index;

        public LongThread(int index) {
            this.index = index;
        }

        @Override
        public void run() {
            if (_media[index] != null) {

                String path = null;
                Log.d("StoreMedia", "downloading: " + _media[index].getUrl());

                try {
                    path = downloadImage(_media[index].getUrl());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                _media[index].setPath(path);

                Log.d("StoreMedia", "downloaded: " + _media[index].getPath());

            } else {
                Log.d("StoreMedia", "media null");
            }
            _downloaded++;
        }

        private String downloadImage(String object_url) throws IOException {
            URL url = new URL(object_url);

            InputStream in = url.openStream();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            String filename = _filepath + url.getFile();

            byte[] buf = new byte[1024];
            int n = in.read(buf);

            while (n != -1) {
                out.write(buf, 0, n);
                n = in.read(buf);
            }

            out.close();
            in.close();

            byte[] response = out.toByteArray();

            FileOutputStream fos = new FileOutputStream(filename);

            fos.write(response);
            fos.close();

            File imgFile = new File(_filepath);

            if (imgFile.exists()) {
                //return BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            }
            return filename;
        }
    }
}