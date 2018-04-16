package com.smartapp.hztech.smarttebletapp;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.smartapp.hztech.smarttebletapp.listeners.AsyncResultBag;
import com.smartapp.hztech.smarttebletapp.tasks.RetrieveSetting;

import java.io.File;
import java.util.HashMap;

public class SplashActivity extends Activity {

    private int SPLASH_TIME_OUT = 1000;
    private String API_KEY = "ST@API_KEY";
    private String SYNC_DONE = "ST@SYNC_DONE";
    private Boolean _isRegistered;
    private Boolean _isSyncDone;
    private Boolean _isLoaded;
    private Boolean _isTimeout;
    private ImageView _splshBackground, _splshLogo;
    private String FILE_PATH = "ST@FILE_PATH";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _isRegistered = _isLoaded = _isSyncDone = _isTimeout = false;

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);

        _splshBackground = findViewById(R.id.splsh_background);
        _splshLogo = findViewById(R.id.splsh_Logo);
    }

    private void setSplashImage() {
        new RetrieveSetting(this, FILE_PATH, SYNC_DONE)
                .onSuccess(new AsyncResultBag.Success() {
                    @Override
                    public void onSuccess(Object result) {
                        HashMap<String, String> values = result != null ? (HashMap<String, String>) result : null;

                        if (values != null) {
                            String is_synced = values.containsKey(SYNC_DONE) ? values.get(SYNC_DONE) : "1";
                            String file_path = values.containsKey(FILE_PATH) ? values.get(FILE_PATH) : null;

                            if (is_synced.equals("1")) {//image foran load horae hai ya delay ata hai? ho to foran rahi h per image
                                // background cover nhi kar rahi. side or top bottom se choti h.
                                if (file_path != null) {

                                    File splshBG = new File(file_path + "/Background.jpg");
                                    File splshLogo = new File(file_path + "/Logo.jpg");

                                    if (splshBG.exists()) {
                                        // Bitmap splshBitmap = BitmapFactory.decodeFile(splshBG.getAbsolutePath());
                                        // _splshBackground.setImageBitmap(splshBitmap);

                                        Resources res = getResources();
                                        Bitmap bitmap = BitmapFactory.decodeFile(splshBG.getAbsolutePath());
                                        BitmapDrawable bd = new BitmapDrawable(res, bitmap);
                                        _splshBackground.setBackgroundDrawable(bd);
                                    }
                                    if (splshLogo.exists()) {

                                        Bitmap splshLogoBitmap = BitmapFactory.decodeFile(splshLogo.getAbsolutePath());
                                        _splshLogo.setVisibility(View.VISIBLE);
                                        _splshLogo.setImageBitmap(splshLogoBitmap);
                                    }
                                }
                            }
                        } else {

                            Resources res1 = getResources();
                            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bgbg);
                            BitmapDrawable bd = new BitmapDrawable(res1, bitmap);
                            _splshBackground.setBackgroundDrawable(bd);
                        }

                    }
                }).execute();
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
     * Asynchronous method to check if API_KEY exists in database
     */
    private void checkSynchronized() {
        new RetrieveSetting(this, API_KEY, SYNC_DONE)
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
                            _isRegistered = values.containsKey(API_KEY) && !values.get(API_KEY).isEmpty();
                            _isSyncDone = values.containsKey(SYNC_DONE) && !values.get(SYNC_DONE).isEmpty();
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
