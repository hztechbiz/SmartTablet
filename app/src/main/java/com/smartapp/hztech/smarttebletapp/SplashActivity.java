package com.smartapp.hztech.smarttebletapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.smartapp.hztech.smarttebletapp.listeners.AsyncResultBag;
import com.smartapp.hztech.smarttebletapp.tasks.RetrieveSetting;

public class SplashActivity extends Activity {

    private int SPLASH_TIME_OUT = 1000;
    private String API_KEY = "ST@API_KEY";
    private String SYNC_DONE = "ST@SYNC_DONE";
    private Boolean _isRegistered;
    private Boolean _isSyncDone;
    private Boolean _isLoaded;
    private Boolean _isTimeout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _isRegistered = _isLoaded = _isSyncDone = _isTimeout = false;

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onResume() {
        super.onResume();

        checkSynchronized();
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
                        String[] values = result != null ? (String[]) result : null;

                        _isLoaded = true;

                        if (values != null && values.length > 0) {
                            try {
                                _isRegistered = !values[0].isEmpty();
                                _isSyncDone = !values[1].isEmpty();
                            } catch (Exception ex) {
                                // no need to handle
                            }
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
