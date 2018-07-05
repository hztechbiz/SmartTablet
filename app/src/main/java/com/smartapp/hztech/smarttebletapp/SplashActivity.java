package com.smartapp.hztech.smarttebletapp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.smartapp.hztech.smarttebletapp.helpers.ImageHelper;
import com.smartapp.hztech.smarttebletapp.listeners.AsyncResultBag;
import com.smartapp.hztech.smarttebletapp.tasks.RetrieveSetting;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SplashActivity extends Activity {

    private String[] permissions = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    private int SPLASH_TIME_OUT = 1000;
    private Boolean _isRegistered;
    private Boolean _isSyncDone;
    private Boolean _isLoaded;
    private Boolean _isTimeout;
    private Boolean _permissionsGranted;
    private Boolean _hasEntryPage;
    private ImageView _iv_background, _iv_logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _isRegistered = _isLoaded = _isSyncDone = _isTimeout = _permissionsGranted = _hasEntryPage = false;

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
                                logo_bitmap = ImageHelper.getResizedBitmap(logo_bitmap, 500);

                                _iv_logo.setVisibility(View.VISIBLE);
                                _iv_logo.setImageBitmap(logo_bitmap);
                            }
                        }

                        if (background != null) {

                            File bg_file = new File(background);

                            if (bg_file.exists()) {
                                Resources res = getResources();
                                Bitmap bg_bitmap = BitmapFactory.decodeFile(bg_file.getAbsolutePath());
                                bg_bitmap = ImageHelper.getResizedBitmap(bg_bitmap, 1000);
                                BitmapDrawable bd = new BitmapDrawable(res, bg_bitmap);

                                _iv_background.setBackgroundDrawable(bd);
                            }
                        }
                    }
                } else {
                    Resources res1 = getResources();
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.banner);
                    bitmap = ImageHelper.getResizedBitmap(bitmap, 1000);
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
        checkPermissions();
    }

    private void checkPermissions() {
        List<String> revoked = new ArrayList<>();

        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                revoked.add(permissions[i]);
            }
        }

        if (!revoked.isEmpty()) {
            String[] request_permissions = revoked.toArray(new String[0]);
            ActivityCompat.requestPermissions(this, request_permissions, 1);
        } else {
            _permissionsGranted = true;
            decide();
        }
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
        new RetrieveSetting(this, Constants.API_KEY, Constants.SETTING_SYNC_DONE, Constants.SETTING_HAS_ENTRY_PAGE)
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
                            _hasEntryPage = values.containsKey(Constants.SETTING_HAS_ENTRY_PAGE) && values.get(Constants.SETTING_HAS_ENTRY_PAGE).equals("1");
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
        if (_isLoaded && _isTimeout && _permissionsGranted) {
            if (_isRegistered && _isSyncDone) {
                switchScreen(MainActivity.class);
            } else {
                switchScreen(SetupActivity.class);
            }
        }
    }

    private void switchScreen(Class activityClass) {
        Intent intent = new Intent(SplashActivity.this, activityClass);

        if (_hasEntryPage) {
            intent.putExtra(getString(R.string.param_has_entry_page), true);
        }

        startActivity(intent);
        finish();

        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                checkPermissions();
                break;
        }
    }
}
