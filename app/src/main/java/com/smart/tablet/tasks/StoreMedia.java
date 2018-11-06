package com.smart.tablet.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.smart.tablet.entities.Media;
import com.smart.tablet.helpers.DatabaseHelper;
import com.smart.tablet.interfaces.RetrofitInterface;
import com.smart.tablet.listeners.AsyncResultBag;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class StoreMedia extends AsyncTask<Void, Void, Boolean> {
    private DatabaseHelper _db;
    private AsyncResultBag.Error _errorCallback;
    private AsyncResultBag.Before _beforeCallback;
    private AsyncResultBag.Success _successCallback;
    private Media[] _media;
    private Object error;
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

            while (_downloaded < (_totalMedia - 5)) {
                Log.d("StoreMedia", "" + _downloaded + " < " + _totalMedia);
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

    public com.smart.tablet.tasks.StoreMedia onError(AsyncResultBag.Error callback) {
        _errorCallback = callback;
        return this;
    }

    public com.smart.tablet.tasks.StoreMedia beforeExecuting(AsyncResultBag.Before callback) {
        _beforeCallback = callback;
        return this;
    }

    public com.smart.tablet.tasks.StoreMedia onSuccess(AsyncResultBag.Success callback) {
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
                    //downloadFile(_media[index].getUrl(), index);
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

        public String saveToDisk(ResponseBody body, URL url) {
            String filename = "";

            try {
                filename = _filepath + url.getFile();
                Log.d("StoreMedia", "file: " + filename);

                File destinationFile = new File(filename);

                InputStream is = null;
                OutputStream os = null;

                try {
                    Log.d("StoreMedia", "File Size=" + body.contentLength());

                    is = body.byteStream();
                    os = new FileOutputStream(destinationFile);

                    byte data[] = new byte[4096];
                    int count;
                    int progress = 0;
                    while ((count = is.read(data)) != -1) {
                        os.write(data, 0, count);
                        progress += count;
                        Log.d("StoreMedia", "Progress: " + progress + "/" + body.contentLength() + " >>>> " + (float) progress / body.contentLength());
                    }

                    os.flush();

                    Log.d("StoreMedia", "File saved successfully!");
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("StoreMedia", "Failed to save the file!" + e.getMessage());
                } finally {
                    if (is != null) is.close();
                    if (os != null) os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("StoreMedia", "Failed to save the file!" + e.getMessage());
            }
            return filename;
        }

        private void downloadFile(final String object_url, final int index) throws MalformedURLException {
            final URL url = new URL(object_url);
            String base_url = url.getProtocol() + "://" + url.getHost();

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            Retrofit.Builder builder = new Retrofit.Builder().baseUrl(base_url);
            Retrofit retrofit = builder.client(httpClient.build()).build();
            RetrofitInterface downloadService = retrofit.create(RetrofitInterface.class);
            Call<ResponseBody> call = downloadService.downloadFileByUrl(url.getPath());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Log.d("StoreMedia", "Got the body for the file");

                        String path = saveToDisk(response.body(), url);
                        _media[index].setPath(path);

                        Log.d("StoreMedia", "downloaded: " + _media[index].getPath());

                        _downloaded++;

                    } else {
                        Log.d("StoreMedia", "Connection failed " + response.errorBody());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    t.printStackTrace();
                    Log.e("StoreMedia", t.getMessage());
                }
            });
        }

        private String downloadImage(String object_url) throws IOException {
            URL url = new URL(object_url);

            InputStream in = url.openStream();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            String filename = _filepath + url.getFile();

            byte[] buf = new byte[4096];
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