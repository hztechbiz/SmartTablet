package com.smartapp.hztech.smarttebletapp.MarketingPartnerSection;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.app.Activity;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartapp.hztech.smarttebletapp.Constants;
import com.smartapp.hztech.smarttebletapp.HomeActivity;
import com.smartapp.hztech.smarttebletapp.R;
import com.smartapp.hztech.smarttebletapp.fragments.MainFragment;
import com.smartapp.hztech.smarttebletapp.listeners.AsyncResultBag;
import com.smartapp.hztech.smarttebletapp.listeners.FragmentListener;
import com.smartapp.hztech.smarttebletapp.tasks.RetrieveSetting;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class MarketPartner extends FragmentActivity {

    private String TAG = this.getClass().getName();
    private FrameLayout marketFragmentContainer;
    private ImageView BackgroudImage;
    private ImageView set_sginal_img, setBatteryStatus, setBgImage;
    private TextView percentage_set, set_Time, searchKey, backScreenTxt, Hometxt, aboutMP, LocationMp, MenuMP, gallaryMp, offerMP, VideoMP, TesttiMP;
    private BatteryBroadcastReceiver batteryBroadcastReceiver;
    private WifiScanReceiver wifiScanReceiver;
    private WifiManager wifiManager;
    private ImageView ttv, wifi, useTablet, info, map, region, weather, news;
    private TextView aboutPage;
    private FragmentListener mainFragmentListener;

    private FragmentListener fragmentListener = new FragmentListener() {
        @Override
        public void onUpdateFragment(Fragment uP_fragment) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            transaction.replace(marketFragmentContainer.getId(), uP_fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.market_partner);
        // View view = inflater.inflate(R.layout.market_partner, container, false);

        marketFragmentContainer = (FrameLayout) findViewById(R.id.market_fragment_container);
        BackgroudImage = (ImageView) findViewById(R.id.main_market_partner);
        set_sginal_img = (ImageView) findViewById(R.id.wifi_connect);
        setBatteryStatus = (ImageView) findViewById(R.id.bettryStatus);
        percentage_set = (TextView) findViewById(R.id.percentage_set);
        backScreenTxt = (TextView) findViewById(R.id.bckScreenTxt);
        Hometxt = (TextView) findViewById(R.id.hometxt);
        aboutMP = (TextView) findViewById(R.id.aboutPage);
        LocationMp = (TextView) findViewById(R.id.locationMp);
        MenuMP = (TextView) findViewById(R.id.MenuMP);
        gallaryMp = (TextView) findViewById(R.id.gallaryMp);
        offerMP = (TextView) findViewById(R.id.offerMP);
        VideoMP = (TextView) findViewById(R.id.videoMP);
        TesttiMP = (TextView) findViewById(R.id.testtiMP);
//      searchKey = (TextView) findViewById(R.id.searchKey);
        set_Time = (TextView) findViewById(R.id.getTime);

        if (marketFragmentContainer != null) {
            if (savedInstanceState != null) {
                return;
            }

            MainFragment checkFragment = new MainFragment();
            checkFragment.setFragmentListener(fragmentListener);

            getSupportFragmentManager().beginTransaction().add(marketFragmentContainer.getId()
                    , checkFragment).commit();
        }
        batteryBroadcastReceiver = new BatteryBroadcastReceiver();
        wifiScanReceiver = new WifiScanReceiver();
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                WifiInfo info = wifiManager.getConnectionInfo();
                final int wifi_signals_level = WifiManager.calculateSignalLevel(info.getRssi()
                        , 4);

                setSignal(wifi_signals_level);
            }
        }, 1000);


        Typeface topBarFont = ResourcesCompat.getFont(this, R.font.lato_regular);
        backScreenTxt.setTypeface(topBarFont);
        Hometxt.setTypeface(topBarFont);
        aboutMP.setTypeface(topBarFont);
        LocationMp.setTypeface(topBarFont);
        MenuMP.setTypeface(topBarFont);
        gallaryMp.setTypeface(topBarFont);
        offerMP.setTypeface(topBarFont);
        VideoMP.setTypeface(topBarFont);
        TesttiMP.setTypeface(topBarFont);


        setBranding();

    }

    private void setBranding() {
        new RetrieveSetting(this, Constants.FILE_PATH_KEY)
                .onSuccess(new AsyncResultBag.Success() {
                    @Override
                    public void onSuccess(Object result) {
                        if (result != null) {
                            String filePath = result.toString();
                            if (filePath != null) {
                                File backImg = new File(filePath + "/Background.jpg");
                                if (backImg.exists()) {
                                    Resources res = getResources();
                                    Bitmap bitmap = BitmapFactory.decodeFile(backImg.getAbsolutePath());
                                    BitmapDrawable bd = new BitmapDrawable(res, bitmap);
                                    BackgroudImage.setBackgroundDrawable(bd);
                                }
                            }
                        }
                    }
                }).execute();
    }

    protected void onStart() {
        registerReceiver(wifiScanReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        registerReceiver(batteryBroadcastReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                DateFormat df = new SimpleDateFormat("hh:mm a");
                                String date = df.format(Calendar.getInstance().getTime());
                                set_Time.setText(date);
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        t.start();

        super.onStart();
    }

    protected void onStop() {
        if (wifiScanReceiver != null)
            unregisterReceiver(wifiScanReceiver);

        if (batteryBroadcastReceiver != null)
            unregisterReceiver(batteryBroadcastReceiver);

        super.onStop();
    }

    public void setSignal(int wifi_signals_level) {

        if (wifi_signals_level == 4) {
            Bitmap btm = BitmapFactory.decodeResource(getResources(), R.drawable.wsignal);
            set_sginal_img.setImageBitmap(btm);
        } else if (wifi_signals_level == 3) {
            Bitmap btm = BitmapFactory.decodeResource(getResources(), R.drawable.wsignal3);
            set_sginal_img.setImageBitmap(btm);
        } else if (wifi_signals_level == 2) {
            Bitmap btm = BitmapFactory.decodeResource(getResources(), R.drawable.wsignal2);
            set_sginal_img.setImageBitmap(btm);
        } else if (wifi_signals_level == 1) {
            Bitmap btm = BitmapFactory.decodeResource(getResources(), R.drawable.wsignal1);
            set_sginal_img.setImageBitmap(btm);
        } else {
            Bitmap btm = BitmapFactory.decodeResource(getResources(), R.drawable.wifi_signals_in_active);
            set_sginal_img.setImageBitmap(btm);
        }
    }

    public void setBattery(float percentage) {
//        int res = R.drawable.btfull;
//
//        if (percentage < 10) {
//            res = R.drawable.batterydown;
//        } else if (percentage < 20) {
//            res = R.drawable.bt20;
//        } else if (percentage < 30) {
//            res = R.drawable.bt30;
//        } else if (percentage < 40) {
//            res = R.drawable.bt40;
//        } else if (percentage < 50) {
//            res = R.drawable.bt50;
//        } else if (percentage < 60) {
//            res = R.drawable.bt60;
//        } else if (percentage < 70) {
//            res = R.drawable.bt70;
//        } else if (percentage < 80) {
//            res = R.drawable.bt80;
//        } else if (percentage < 90) {
//            res = R.drawable.bt90;
//        } else if (percentage <= 100) {
//            res = R.drawable.btfull;
//        } int res = R.drawable.btfull;
        int res = R.drawable.battery_icon;
        if (percentage < 10) {
            res = R.drawable.batterydown;
        } else if (percentage < 20) {
            res = R.drawable.battery_icon;
        } else if (percentage < 30) {
            res = R.drawable.battery_icon;
        } else if (percentage < 40) {
            res = R.drawable.battery_icon;
        } else if (percentage < 50) {
            res = R.drawable.battery_icon;
        } else if (percentage < 60) {
            res = R.drawable.battery_icon;
        } else if (percentage < 70) {
            res = R.drawable.battery_icon;
        } else if (percentage < 80) {
            res = R.drawable.battery_icon;
        } else if (percentage < 90) {
            res = R.drawable.battery_icon;
        } else if (percentage <= 99) {
            res = R.drawable.battery_icon;
        } else if (percentage <= 100) {
            res = R.drawable.btfull1;
        }

        Bitmap bettryMap = BitmapFactory.decodeResource(getResources(), res);
        setBatteryStatus.setImageBitmap(bettryMap);
    }

    class BatteryBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);

            percentage_set.setText(level + "%");

            setBattery(level);
        }
    }

    class WifiScanReceiver extends BroadcastReceiver {
        public void onReceive(Context c, Intent intent) {
            List<ScanResult> wifiScanList = wifiManager.getScanResults();

            WifiInfo info = wifiManager.getConnectionInfo();

            final String wifiName = info.getSSID();
            final int wifi_signals_level = WifiManager.calculateSignalLevel(info.getRssi()
                    , 4);
            setSignal(wifi_signals_level);
        }
    }
}
