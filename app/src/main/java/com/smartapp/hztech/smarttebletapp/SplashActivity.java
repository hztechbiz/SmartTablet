package com.smartapp.hztech.smarttebletapp;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.smartapp.hztech.smarttebletapp.listeners.AsyncResultBag;
import com.smartapp.hztech.smarttebletapp.tasks.RetrieveSetting;

import java.io.File;
import java.util.HashMap;

public class SplashActivity extends Activity {

    private int SPLASH_TIME_OUT = 1000;
    private Boolean _isRegistered;
    private Boolean _isSyncDone;
    private Boolean _isLoaded;
    private Boolean _isTimeout;
    private ImageView _iv_background, _iv_logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _isRegistered = _isLoaded = _isSyncDone = _isTimeout = false;

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);

        _iv_background = findViewById(R.id.splsh_background);
        _iv_logo = findViewById(R.id.splsh_Logo);
    }

    private void setSplashImage() {
        RetrieveSetting retrieveSetting = new RetrieveSetting(this, Constants.SETTING_LOGO, Constants.SETTING_BACKGROUND, Constants.SETTING_SYNC_DONE);

        retrieveSetting.setMediaKeys(Constants.SETTING_LOGO, Constants.SETTING_BACKGROUND);
        retrieveSetting.onSuccess(new AsyncResultBag.Success() {
            @Override
            public void onSuccess(Object result) {
                HashMap<String, String> values = result != null ? (HashMap<String, String>) result : null;

                if (values != null) {
                    String is_synced = values.containsKey(Constants.SETTING_SYNC_DONE) ? values.get(Constants.SETTING_SYNC_DONE) : "1";
                    String logo = values.containsKey(Constants.SETTING_LOGO) ? values.get(Constants.SETTING_LOGO) : null;
                    String background = values.containsKey(Constants.SETTING_BACKGROUND) ? values.get(Constants.SETTING_BACKGROUND) : null;

                    if (is_synced.equals("1")) {
                        if (logo != null) {

                            File logo_file = new File(logo);

                            if (logo_file.exists()) {
                                Bitmap logo_bitmap = BitmapFactory.decodeFile(logo_file.getAbsolutePath());

                                _iv_logo.setVisibility(View.VISIBLE);
                                _iv_logo.setImageBitmap(logo_bitmap);
                            }
                        }

                        if (background != null) {

                            File bg_file = new File(background);

                            if (bg_file.exists()) {
                                Resources res = getResources();
                                Bitmap bg_bitmap = BitmapFactory.decodeFile(bg_file.getAbsolutePath());
                                BitmapDrawable bd = new BitmapDrawable(res, bg_bitmap);

                                _iv_background.setBackgroundDrawable(bd);
                            }
                        }
                    }
                } else {
                    Resources res1 = getResources();
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.banner);
                    BitmapDrawable bd = new BitmapDrawable(res1, bitmap);

                    _iv_background.setBackgroundDrawable(bd);
                }

            }
        });
        retrieveSetting.execute();
    }

    @Override
    protected void onResume() {
        super.onResume();

        checkSynchronized();
        setSplashImage();
        waitSplash();
    }

    private void waitSplash() {
        new Handler()
                .postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        _isTimeout = true;
                        decide();
                    }
                }, SPLASH_TIME_OUT);
    }

    /*
     * Asynchronous method to check if Constants.API_KEY exists in database
     */
    private void checkSynchronized() {
        new RetrieveSetting(this, Constants.API_KEY, Constants.SETTING_SYNC_DONE)
                .onError(new AsyncResultBag.Error() {
                    @Override
                    public void onError(Object error) {
                        switchScreen(SetupActivity.class);
                    }
                })
                .onSuccess(new AsyncResultBag.Success() {
                    @Override
                    public void onSuccess(Object result) {
                        HashMap<String, String> values = result != null ? (HashMap<String, String>) result : null;

                        _isLoaded = true;

                        if (values != null) {
                            _isRegistered = values.containsKey(Constants.API_KEY) && !values.get(Constants.API_KEY).isEmpty();
                            _isSyncDone = values.containsKey(Constants.SETTING_SYNC_DONE) && !values.get(Constants.SETTING_SYNC_DONE).isEmpty();
                        }

                        decide();
                    }
                })
                .execute();
    }

    /*
     * Method to decide which screen to show next
     * after splash screen
     */
    private void decide() {
        if (_isLoaded && _isTimeout) {
            if (_isRegistered && _isSyncDone) {
                switchScreen(MainActivity.class);
            } else {
                switchScreen(SetupActivity.class);
            }
        }
    }

    private void switchScreen(Class activityClass) {
        Intent intent = new Intent(SplashActivity.this, activityClass);
        startActivity(intent);
        finish();

        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }
}
