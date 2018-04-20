package com.smartapp.hztech.smarttebletapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.method.ArrowKeyMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartapp.hztech.smarttebletapp.fragments.CategoryFragment;
import com.smartapp.hztech.smarttebletapp.fragments.MainFragment;
import com.smartapp.hztech.smarttebletapp.fragments.ServiceFragment;
import com.smartapp.hztech.smarttebletapp.listeners.AsyncResultBag;
import com.smartapp.hztech.smarttebletapp.listeners.FragmentListener;
import com.smartapp.hztech.smarttebletapp.tasks.RetrieveSetting;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends FragmentActivity {

    private String TAG = this.getClass().getName();
    private FrameLayout fragmentContainer;

    private ImageView set_sginal_img, setBatteryStatus, setBgImage;
    private TextView percentage_set, set_Time, searchKey;
    private BatteryBroadcastReceiver batteryBroadcastReceiver;
    private WifiScanReceiver wifiScanReceiver;
    private WifiManager wifiManager;
    private ImageView ttv, wifi, useTablet, info, map, region, weather, news;

    private FragmentListener fragmentListener = new FragmentListener() {
        @Override
        public void onUpdateFragment(Fragment newFragment) {
            Log.d("FragmentUpdated", "From: MainActivity, Fragment: " + newFragment.getClass().getName());
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            transaction.replace(fragmentContainer.getId(), newFragment);
            transaction.addToBackStack(null);

            transaction.commit();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        fragmentContainer = findViewById(R.id.fragment_container);

        set_sginal_img = (ImageView) findViewById(R.id.wifi_connect);
        setBatteryStatus = (ImageView) findViewById(R.id.bettryStatus);
        percentage_set = (TextView) findViewById(R.id.percentage_set);
//        searchKey = (TextView) findViewById(R.id.searchKey);
        set_Time = (TextView) findViewById(R.id.getTime);
        setBgImage = (ImageView) findViewById(R.id.main_bg_img);
        ttv = (ImageView) findViewById(R.id.tv);
        wifi = (ImageView) findViewById(R.id.wifi);
        useTablet = (ImageView) findViewById(R.id.useTablet);
        info = (ImageView) findViewById(R.id.info);
        map = (ImageView) findViewById(R.id.map);
        region = (ImageView) findViewById(R.id.region);
        weather = (ImageView) findViewById(R.id.weather);
        news = (ImageView) findViewById(R.id.news);


//        searchKey.setTextIsSelectable(false);
//        searchKey.setFocusable(true);
//        searchKey.setFocusableInTouchMode(true);
//        searchKey.setClickable(true);
//        searchKey.setLongClickable(true);
//        searchKey.setMovementMethod(ArrowKeyMovementMethod.getInstance());
//        searchKey.setText(searchKey.getText(), TextView.BufferType.SPANNABLE);
        if (fragmentContainer != null) {

            if (savedInstanceState != null) {
                return;
            }

            MainFragment firstFragment = new MainFragment();
            firstFragment.setFragmentListener(fragmentListener);

            getSupportFragmentManager().beginTransaction()
                    .add(fragmentContainer.getId(), firstFragment).commit();
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

        setupMenuItems();
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
                                File imgBG = new File(filePath + "/Background.jpg");
                                if (imgBG.exists()) {
                                    Resources res = getResources();
                                    Bitmap bitmap = BitmapFactory.decodeFile(imgBG.getAbsolutePath());
                                    BitmapDrawable bd = new BitmapDrawable(res, bitmap);
                                    setBgImage.setBackgroundDrawable(bd);
                                }
                            }
                        }
                    }
                })
                .execute();
    }

    @Override
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

    @Override
    protected void onStop() {
        if (wifiScanReceiver != null)
            unregisterReceiver(wifiScanReceiver);

        if (batteryBroadcastReceiver != null)
            unregisterReceiver(batteryBroadcastReceiver);

        super.onStop();
    }

    private void setupMenuItems() {
        new RetrieveSetting(this,
                "enable_operating_the_television",
                "enable_connect_to_wifi",
                "enable_how_use_tablet",
                "enable_useful_information_category",
                "enable_weather",
                "enable_news",
                "operating_the_television_service",
                "connect_to_wifi_service",
                "how_use_tablet_category",
                "useful_information_category"
        ).onSuccess(new AsyncResultBag.Success() {
            @Override
            public void onSuccess(Object result) {
                HashMap<String, String> values = result != null ? (HashMap<String, String>) result : null;

                if (values != null) {
                    String enable_operating_the_television = values.containsKey("enable_operating_the_television") ? values.get("enable_operating_the_television") : "1";
                    String enable_connect_to_wifi = values.containsKey("enable_connect_to_wifi") ? values.get("enable_connect_to_wifi") : "1";
                    String enable_how_use_tablet = values.containsKey("enable_how_use_tablet") ? values.get("enable_how_use_tablet") : "1";
                    String enable_useful_information_category = values.containsKey("enable_useful_information_category") ? values.get("enable_useful_information_category") : "1";
                    String enable_weather = values.containsKey("enable_weather")
                            ? values.get("enable_weather") : "1";
                    String enable_news = values.containsKey("enable_news") ? values.get("enable_news") : "1";
                    String operating_the_television_service = values.containsKey("operating_the_television_service") ? values.get("operating_the_television_service") : "0";
                    String connect_to_wifi_service = values.containsKey("connect_to_wifi_service") ? values.get("connect_to_wifi_service") : "0";
                    String how_use_tablet_category = values.containsKey("how_use_tablet_category") ? values.get("how_use_tablet_category") : "0";
                    String useful_information_category = values.containsKey("useful_information_category") ? values.get("useful_information_category") : "0";

                    LinearLayout ott_linear = findViewById(R.id.ott);
                    LinearLayout wifi_linear = findViewById(R.id.itemWifi);
                    LinearLayout howTo_linear = findViewById(R.id.itemHow);
                    LinearLayout Info_linear = findViewById(R.id.itemInfo);
                    //LinearLayout map_linear = findViewById(R.id.itemMap);
                    //LinearLayout localReg_linear = findViewById(R.id.itemLocalRegion);
                    LinearLayout weather_linear = findViewById(R.id.itemWeather);
                    LinearLayout news_linear = findViewById(R.id.itemNews);

                    if (enable_operating_the_television.equals("1")) {
                        ott_linear.setVisibility(View.VISIBLE);
                        ott_linear.setTag(R.string.tag_value, operating_the_television_service);
                        ott_linear.setTag(R.string.tag_action, R.string.tag_action_service);
                    } else {
                        ott_linear.setVisibility(View.INVISIBLE);
                    }

                    if (enable_connect_to_wifi.equals("1")) {
                        wifi_linear.setVisibility(View.VISIBLE);
                        wifi_linear.setTag(R.string.tag_value, connect_to_wifi_service);
                        wifi_linear.setTag(R.string.tag_action, R.string.tag_action_service);
                    } else {
                        wifi_linear.setVisibility(View.INVISIBLE);
                    }

                    if (enable_how_use_tablet.equals("1")) {
                        howTo_linear.setVisibility(View.VISIBLE);
                        howTo_linear.setTag(R.string.tag_value, how_use_tablet_category);
                        howTo_linear.setTag(R.string.tag_action, R.string.tag_action_category);
                    } else {
                        howTo_linear.setVisibility(View.INVISIBLE);
                    }

                    if (enable_useful_information_category.equals("1")) {
                        Info_linear.setVisibility(View.VISIBLE);
                        Info_linear.setTag(R.string.tag_value, useful_information_category);
                        Info_linear.setTag(R.string.tag_action, R.string.tag_action_category);
                    } else {
                        Info_linear.setVisibility(View.INVISIBLE);
                    }

                    if (enable_weather.equals("1")) {
                        weather_linear.setVisibility(View.VISIBLE);
                    } else {
                        weather_linear.setVisibility(View.INVISIBLE);
                    }

                    if (enable_news.equals("1")) {
                        news_linear.setVisibility(View.VISIBLE);
                    } else {
                        news_linear.setVisibility(View.INVISIBLE);
                    }
                }
            }
        }).execute();
    }

    public void onNavItemClick(View view) {
        makeMenuItemActive(view, (view.getId() != R.id.itemHome));

        Object action = view.getTag(R.string.tag_action);
        Object value = view.getTag(R.string.tag_value);

        Bundle bundle = new Bundle();
        MainFragment mainFragment = new MainFragment();
        mainFragment.setFragmentListener(fragmentListener);

        if (action != null && value != null) {
            if (action.equals(R.string.tag_action_category)) {
                bundle.putInt(getString(R.string.param_category_id), Integer.parseInt(value.toString()));

                CategoryFragment fragment = new CategoryFragment();
                fragment.setFragmentListener(fragmentListener);
                fragment.setArguments(bundle);

                mainFragment.setChildFragment(fragment);

            } else if (action.equals(R.string.tag_action_service)) {
                bundle.putInt(getString(R.string.param_service_id), Integer.parseInt(value.toString()));

                ServiceFragment fragment = new ServiceFragment();
                fragment.setArguments(bundle);

                mainFragment.setChildFragment(fragment);
            }
        }

        fragmentListener.onUpdateFragment(mainFragment);
    }

    public void makeMenuItemActive(View view, Boolean makeActive) {
        LinearLayout ott_linear = findViewById(R.id.ott);
        LinearLayout wifi_linear = findViewById(R.id.itemWifi);
        LinearLayout howTo_linear = findViewById(R.id.itemHow);
        LinearLayout Info_linear = findViewById(R.id.itemInfo);
        LinearLayout map_linear = findViewById(R.id.itemMap);
        LinearLayout localReg_linear = findViewById(R.id.itemLocalRegion);
        LinearLayout weather_linear = findViewById(R.id.itemWeather);
        LinearLayout news_linear = findViewById(R.id.itemNews);

        LinearLayout[] all_items = new LinearLayout[]{ott_linear, wifi_linear, howTo_linear, Info_linear, map_linear, localReg_linear,
                weather_linear, news_linear};

        for (LinearLayout all_item : all_items) {
            all_item.setBackgroundColor(0);

        }

        if (makeActive) {
            view.setBackgroundColor(Color.parseColor("#2cb3dc"));
            // view.setBackground(R.drawable.sidemenu_gradient_bg);
        }
        ttv.setImageResource(R.drawable.operatingthetelevision);
        wifi.setImageResource(R.drawable.connecttowifi);
        useTablet.setImageResource(R.drawable.usemobile);
        info.setImageResource(R.drawable.userinformation);
        map.setImageResource(R.drawable.localmap);
        region.setImageResource(R.drawable.localregion);
        weather.setImageResource(R.drawable.weather);
        news.setImageResource(R.drawable.news);


        switch (view.getId()) {
            case R.id.ott:
                ttv.setImageResource(R.drawable.operating_the_television_black);
                break;
            case R.id.itemWifi:
                wifi.setImageResource(R.drawable.connect_to_wifi_black);
                break;
            case R.id.itemHow:
                useTablet.setImageResource(R.drawable.how_to_use_smart_tablet_black);
                break;
            case R.id.itemInfo:
                info.setImageResource(R.drawable.useful_info_black);
                break;
            case R.id.itemMap:
                map.setImageResource(R.drawable.local_map_black);
                break;
            case R.id.itemLocalRegion:
                region.setImageResource(R.drawable.the_local_region_black);
                break;
            case R.id.itemWeather:
                weather.setImageResource(R.drawable.weather_black);
                break;
            case R.id.itemNews:
                news.setImageResource(R.drawable.news_black);
                break;

        }
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
