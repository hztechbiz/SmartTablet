package com.smartapp.hztech.smarttebletapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.smartapp.hztech.smarttebletapp.entities.Category;
import com.smartapp.hztech.smarttebletapp.fragments.MainFragment;
import com.smartapp.hztech.smarttebletapp.fragments.NavigationFragment;
import com.smartapp.hztech.smarttebletapp.fragments.WelcomeFragment;
import com.smartapp.hztech.smarttebletapp.helpers.ImageHelper;
import com.smartapp.hztech.smarttebletapp.helpers.Util;
import com.smartapp.hztech.smarttebletapp.listeners.AsyncResultBag;
import com.smartapp.hztech.smarttebletapp.listeners.FragmentActivityListener;
import com.smartapp.hztech.smarttebletapp.listeners.FragmentListener;
import com.smartapp.hztech.smarttebletapp.models.MapMarker;
import com.smartapp.hztech.smarttebletapp.receivers.SyncAlarmReceiver;
import com.smartapp.hztech.smarttebletapp.receivers.WakeupReceiver;
import com.smartapp.hztech.smarttebletapp.service.SyncService;
import com.smartapp.hztech.smarttebletapp.tasks.RetrieveCategories;
import com.smartapp.hztech.smarttebletapp.tasks.RetrieveSetting;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class MainActivity extends FragmentActivity {

    private String TAG = this.getClass().getName();
    private FrameLayout fragmentContainer;
    private ImageView img_wifi_signals, img_battery_level, bg_image, small_logo, main_logo;
    private TextView txt_battery_percentage, txt_time, _btn_home_text, _btn_back_text, item_home_text, item_tv_text, item_wifi_text, item_how_text, item_useful_info_text, item_weather_text, item_news_text, _app_heading, _txt_copyright, _btn_guest_info_text, _btn_top_guest_info_text;
    private LinearLayout _sidebar, _btn_home, _btn_back, _time_box, small_logo_container, main_logo_container, _btn_welcome, _btn_guest_info, _bottom_bar, _btn_top_guest_info, _app_heading_container, _top_bar_right;
    private RelativeLayout _sync_container;
    private BatteryBroadcastReceiver batteryBroadcastReceiver;
    private WifiScanReceiver wifiScanReceiver;
    private WifiManager wifiManager;
    private int timerClicked;
    private boolean timerClickedTimerAdded, isServiceRunning;
    private Handler _handler;
    private Runnable _runnable;
    private ImageView item_icon_1, item_icon_2, item_icon_3, item_icon_4, item_icon_5, item_icon_6, item_icon_7, item_icon_8;
    private BroadcastReceiver syncStartReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            showSynchronizing(true);
        }
    };
    private BroadcastReceiver syncHeartBeatReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            showSynchronizing(true);
        }
    };
    private BroadcastReceiver syncReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("SchedulingAlarms", "syncreceiver");
            showSynchronizing(false);
            isServiceRunning = false;
        }
    };
    private BroadcastReceiver syncCompleteReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("SchedulingAlarms", "syncreceiver");
            showSynchronizing(false);
            isServiceRunning = false;
        }
    };
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
    private FragmentActivityListener activityListener = new FragmentActivityListener() {
        @Override
        public void receive(int message, Object arguments) {
            switch (message) {
                case R.string.msg_hide_sidebar:
                    showSideBar(false);
                    break;
                case R.string.msg_show_sidebar:
                    showSideBar(true);
                    break;
                case R.string.msg_show_home_button:
                    showHomeButton(true);
                    break;
                case R.string.msg_hide_home_button:
                    showHomeButton(false);
                    break;
                case R.string.msg_hide_back_button:
                    showBackButton(false);
                    break;
                case R.string.msg_show_back_button:
                    showBackButton(true);
                    break;
                case R.string.msg_update_background:
                    if (arguments != null)
                        setBackgroundImage(arguments.toString());
                    break;
                case R.string.msg_reset_background:
                    setBranding();
                    break;
                case R.string.msg_show_main_logo:
                    showMainLogo(true);
                    break;
                case R.string.msg_hide_main_logo:
                    showMainLogo(false);
                    break;
                case R.string.msg_show_logo_button:
                    showSmallLogo(true);
                    break;
                case R.string.msg_hide_logo_button:
                    showSmallLogo(false);
                    break;
                case R.string.msg_show_welcome_button:
                    showWelcomeButton(true);
                    break;
                case R.string.msg_hide_welcome_button:
                    showWelcomeButton(false);
                    break;
                case R.string.msg_show_guest_button:
                    showGuestButton(true);
                    break;
                case R.string.msg_hide_guest_button:
                    showGuestButton(false);
                    break;
                case R.string.msg_set_app_heading:
                    if (arguments != null)
                        _app_heading.setText(arguments.toString());
                    break;
                case R.string.msg_show_app_heading:
                    showAppHeading(true);
                    break;
                case R.string.msg_hide_app_heading:
                    showAppHeading(false);
                    break;
                case R.string.msg_show_copyright:
                    showCopyright(true);
                    break;
                case R.string.msg_hide_copyright:
                    showCopyright(false);
                    break;
                case R.string.msg_show_top_guest_button:
                    showTopGuestInfoButton(true);
                    break;
                case R.string.msg_hide_top_guest_button:
                    showTopGuestInfoButton(false);
                    break;
            }
        }
    };

    private void showCopyright(boolean b) {
        _bottom_bar.setVisibility(b ? View.VISIBLE : View.GONE);
    }

    private void showAppHeading(boolean b) {
        _app_heading_container.setVisibility(b ? View.VISIBLE : View.GONE);

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) _top_bar_right.getLayoutParams();
        layoutParams.weight = (b ? 0.3f : 0.7f);

        _top_bar_right.setLayoutParams(layoutParams);
    }

    private void showGuestButton(boolean b) {
        _btn_guest_info.setVisibility(b ? View.VISIBLE : View.GONE);
    }

    private void showWelcomeButton(boolean b) {
        _btn_welcome.setVisibility(b ? View.VISIBLE : View.GONE);
    }

    private void showTopGuestInfoButton(boolean b) {
        _btn_top_guest_info.setVisibility(b ? View.VISIBLE : View.GONE);
    }

    private void showSmallLogo(boolean b) {
        small_logo_container.setVisibility(b ? View.VISIBLE : View.GONE);
    }

    private void showMainLogo(boolean b) {
        main_logo_container.setVisibility(b ? View.VISIBLE : View.GONE);
    }

    private void showSynchronizing(boolean b) {
        _sync_container.setVisibility(b ? View.VISIBLE : View.GONE);

        if (!b) {
            setupMenuItems();
            setBranding();
        }
    }

    private void showHomeButton(boolean b) {
        _btn_home.setVisibility(b ? View.VISIBLE : View.GONE);
    }

    private void showSideBar(boolean b) {
        _sidebar.setVisibility(b ? View.VISIBLE : View.GONE);
    }

    private void showBackButton(boolean b) {
        _btn_back.setVisibility(b ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        _handler = new Handler();
        _runnable = new Runnable() {
            @Override
            public void run() {
                moveToHome();
            }
        };

        timerClicked = 0;
        timerClickedTimerAdded = isServiceRunning = false;

        fragmentContainer = findViewById(R.id.fragment_container);
        _sidebar = findViewById(R.id.sidebar);
        _top_bar_right = findViewById(R.id.top_bar_right);
        _btn_home = findViewById(R.id.btn_home);
        _btn_back = findViewById(R.id.btn_back);
        _btn_home_text = findViewById(R.id.btn_home_text);
        _btn_back_text = findViewById(R.id.btn_back_text);
        _btn_welcome = findViewById(R.id.btn_welcome);
        _btn_guest_info = findViewById(R.id.btn_guest_info);
        _btn_guest_info_text = findViewById(R.id.btn_guest_info_text);
        _btn_top_guest_info_text = findViewById(R.id.btn_top_guest_info_text);
        _btn_top_guest_info = findViewById(R.id.btn_top_guest_info);
        _time_box = findViewById(R.id.time_box);
        _sync_container = findViewById(R.id.syncContainer);
        item_home_text = findViewById(R.id.item_home_text);
        item_tv_text = findViewById(R.id.item_tv_text);
        item_wifi_text = findViewById(R.id.item_wifi_text);
        item_how_text = findViewById(R.id.item_how_text);
        item_useful_info_text = findViewById(R.id.item_useful_info_text);
        item_weather_text = findViewById(R.id.item_weather_text);
        item_news_text = findViewById(R.id.item_news_text);
        img_wifi_signals = findViewById(R.id.wifi_connect);
        img_battery_level = findViewById(R.id.bettryStatus);
        txt_battery_percentage = findViewById(R.id.percentage_set);
        txt_time = findViewById(R.id.getTime);
        _app_heading = findViewById(R.id.app_heading);
        _app_heading_container = findViewById(R.id.app_heading_container);
        _txt_copyright = findViewById(R.id.txt_copyright);
        bg_image = findViewById(R.id.main_bg_img);
        small_logo = findViewById(R.id.small_logo_img);
        main_logo = findViewById(R.id.main_logo_img);
        small_logo_container = findViewById(R.id.small_logo);
        main_logo_container = findViewById(R.id.main_logo);
        _bottom_bar = findViewById(R.id.bottom_bar);
        item_icon_1 = findViewById(R.id.tv);
        item_icon_2 = findViewById(R.id.wifi);
        item_icon_3 = findViewById(R.id.useTablet);
        item_icon_4 = findViewById(R.id.info);
        item_icon_5 = findViewById(R.id.map);
        item_icon_6 = findViewById(R.id.region);
        item_icon_7 = findViewById(R.id.weather);
        item_icon_8 = findViewById(R.id.news);

        if (fragmentContainer != null) {

            if (savedInstanceState != null) {
                return;
            }

            MainFragment firstFragment = new MainFragment();
            firstFragment.setFragmentListener(fragmentListener);
            firstFragment.setParentListener(activityListener);

            getSupportFragmentManager().beginTransaction()
                    .add(fragmentContainer.getId(), firstFragment).commit();
        }

        _btn_home_text.setTypeface(Util.getTypeFace(this));
        _btn_top_guest_info_text.setTypeface(Util.getTypeFace(this));
        _btn_guest_info_text.setTypeface(Util.getTypeFace(this));
        _btn_back_text.setTypeface(Util.getTypeFace(this));
        item_home_text.setTypeface(Util.getTypeFace(this));
        item_tv_text.setTypeface(Util.getTypeFace(this));
        item_wifi_text.setTypeface(Util.getTypeFace(this));
        item_how_text.setTypeface(Util.getTypeFace(this));
        item_useful_info_text.setTypeface(Util.getTypeFace(this));
        item_weather_text.setTypeface(Util.getTypeFace(this));
        item_news_text.setTypeface(Util.getTypeFace(this));
        _app_heading.setTypeface(Util.getTypeFace(this));

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
        scheduleAlarms();

        wakeupScreen();
    }

    private void wakeupScreen() {
        PowerManager powerManager = ((PowerManager) getSystemService(Context.POWER_SERVICE));
        if (powerManager != null) {
            PowerManager.WakeLock screenLock = powerManager.newWakeLock(
                    PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
            screenLock.acquire(10 * 60 * 1000L);
        }
    }

    private void scheduleAlarms() {
        scheduleSyncAlarm();
        scheduleWakeupAlarm();
    }

    private void scheduleWakeupAlarm() {
        Intent intent = new Intent(this, WakeupReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, WakeupReceiver.REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        long millis = System.currentTimeMillis();

        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);

        if (alarmManager != null) {
            long interval = Constants.SCREEN_WAKEUP_WAIT;
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, millis + interval, interval, pendingIntent);
        }
    }

    private void scheduleSyncAlarm() {
        Intent intent = new Intent(this, SyncAlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, SyncAlarmReceiver.REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        long millis = System.currentTimeMillis();

        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);

        if (alarmManager != null) {
            long interval = AlarmManager.INTERVAL_HALF_DAY;
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, millis + interval, interval, pendingIntent);
        }
    }

    private void setBranding() {
        RetrieveSetting setting = new RetrieveSetting(this, Constants.SETTING_BACKGROUND, Constants.SETTING_LOGO);

        setting.onSuccess(new AsyncResultBag.Success() {
            @Override
            public void onSuccess(Object result) {
                HashMap<String, String> values = result != null ? (HashMap<String, String>) result : null;

                if (values != null) {
                    String filePath = values.get(Constants.SETTING_BACKGROUND);

                    if (filePath != null) {
                        File imageFile = new File(filePath);

                        if (imageFile.exists()) {
                            Resources res = getResources();
                            Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                            bitmap = ImageHelper.getResizedBitmap(bitmap, 1000);
                            BitmapDrawable bd = new BitmapDrawable(res, bitmap);

                            bg_image.setBackgroundDrawable(bd);
                        }
                    }

                    filePath = values.get(Constants.SETTING_LOGO);

                    if (filePath != null) {
                        File imageFile = new File(filePath);

                        if (imageFile.exists()) {
                            Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                            Bitmap bitmap_small = ImageHelper.getResizedBitmap(bitmap, 500);
                            Bitmap bitmap_large = ImageHelper.getResizedBitmap(bitmap, 1000);

                            small_logo.setImageBitmap(bitmap_small);
                            main_logo.setImageBitmap(bitmap_large);
                        }
                    }
                }
            }
        });
        setting.setMediaKeys(Constants.SETTING_BACKGROUND, Constants.SETTING_LOGO);
        setting.execute();
    }

    private void setBackgroundImage(String filePath) {
        File imgBG = new File(filePath);

        if (imgBG.exists()) {
            Resources res = getResources();
            Bitmap bitmap = BitmapFactory.decodeFile(imgBG.getAbsolutePath());
            bitmap = ImageHelper.getResizedBitmap(bitmap, 1000);
            BitmapDrawable bd = new BitmapDrawable(res, bitmap);

            bg_image.setBackgroundDrawable(bd);
        }
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();

        stopHandler();
        startHandler();
    }

    @Override
    protected void onStart() {
        registerReceiver(syncReceiver, new IntentFilter(SyncService.TRANSACTION_DONE));
        registerReceiver(syncCompleteReceiver, new IntentFilter(SyncService.TRANSACTION_COMPLETE));
        registerReceiver(syncStartReceiver, new IntentFilter(SyncService.TRANSACTION_START));
        registerReceiver(syncHeartBeatReceiver, new IntentFilter(SyncService.TRANSACTION_HEART_BEAT));
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
                                txt_time.setText(date);
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    Log.e(TAG, e + "");
                }
            }
        };

        t.start();

        super.onStart();
    }

    private void checkIfSyncServiceRunning() {
        new RetrieveSetting(this, Constants.SYNC_SERVICE_RUNNING)
                .onSuccess(new AsyncResultBag.Success() {
                    @Override
                    public void onSuccess(Object result) {
                        showSynchronizing((result != null && result.equals("1")));
                    }
                })
                .execute();
    }

    @Override
    protected void onStop() {
        if (wifiScanReceiver != null)
            unregisterReceiver(wifiScanReceiver);

        if (batteryBroadcastReceiver != null)
            unregisterReceiver(batteryBroadcastReceiver);

        if (syncReceiver != null)
            unregisterReceiver(syncReceiver);

        if (syncCompleteReceiver != null)
            unregisterReceiver(syncCompleteReceiver);

        if (syncStartReceiver != null)
            unregisterReceiver(syncStartReceiver);

        if (syncHeartBeatReceiver != null)
            unregisterReceiver(syncHeartBeatReceiver);

        super.onStop();
    }

    private void setupMenuItems() {
        new RetrieveSetting(this,
                "enable_operating_the_television",
                "enable_connect_to_wifi",
                "enable_how_use_tablet",
                "enable_useful_information_category",
                "enable_local_region_category",
                "enable_local_map",
                "enable_weather",
                "enable_news",
                "operating_the_television_category",
                "connect_to_wifi_category",
                "how_use_tablet_category",
                "useful_information_category",
                "local_region_category",
                "local_map_address",
                "local_map_latitude",
                "local_map_longitude"
        ).onSuccess(new AsyncResultBag.Success() {
            @Override
            public void onSuccess(Object result) {
                HashMap<String, String> values = result != null ? (HashMap<String, String>) result : null;

                if (values != null) {
                    String enable_operating_the_television = values.containsKey("enable_operating_the_television") ? values.get("enable_operating_the_television") : "0";
                    String enable_connect_to_wifi = values.containsKey("enable_connect_to_wifi") ? values.get("enable_connect_to_wifi") : "0";
                    String enable_how_use_tablet = values.containsKey("enable_how_use_tablet") ? values.get("enable_how_use_tablet") : "0";
                    String enable_useful_information_category = values.containsKey("enable_useful_information_category") ? values.get("enable_useful_information_category") : "0";
                    String enable_local_region_category = values.containsKey("enable_local_region_category") ? values.get("enable_local_region_category") : "0";
                    String enable_local_map = values.containsKey("enable_local_map") ? values.get("enable_local_map") : "0";
                    String enable_weather = values.containsKey("enable_weather") ? values.get("enable_weather") : "0";
                    String enable_news = values.containsKey("enable_news") ? values.get("enable_news") : "0";
                    String operating_the_television_category = values.containsKey("operating_the_television_category") ? values.get("operating_the_television_category") : "0";
                    String connect_to_wifi_category = values.containsKey("connect_to_wifi_category") ? values.get("connect_to_wifi_category") : "0";
                    String how_use_tablet_category = values.containsKey("how_use_tablet_category") ? values.get("how_use_tablet_category") : "0";
                    String useful_information_category = values.containsKey("useful_information_category") ? values.get("useful_information_category") : "0";
                    String local_region_category = values.containsKey("local_region_category") ? values.get("local_region_category") : "0";
                    String local_map_address = values.containsKey("local_map_address") ? values.get("local_map_address") : null;
                    double local_map_latitude = values.containsKey("local_map_latitude") ? Double.parseDouble(values.get("local_map_latitude")) : 0;
                    double local_map_longitude = values.containsKey("local_map_longitude") ? Double.parseDouble(values.get("local_map_longitude")) : 0;

                    LinearLayout ott_linear = findViewById(R.id.ott);
                    LinearLayout wifi_linear = findViewById(R.id.itemWifi);
                    LinearLayout howTo_linear = findViewById(R.id.itemHow);
                    LinearLayout Info_linear = findViewById(R.id.itemInfo);
                    LinearLayout weather_linear = findViewById(R.id.itemWeather);
                    LinearLayout news_linear = findViewById(R.id.itemNews);
                    LinearLayout local_region = findViewById(R.id.itemLocalRegion);
                    LinearLayout local_map = findViewById(R.id.itemMap);

                    if (enable_operating_the_television.equals("1")) {
                        ott_linear.setVisibility(View.VISIBLE);
                        ott_linear.setTag(R.string.tag_value, operating_the_television_category);
                        ott_linear.setTag(R.string.tag_action, R.string.tag_action_category);
                    } else {
                        ott_linear.setVisibility(View.GONE);
                    }

                    if (enable_connect_to_wifi.equals("1")) {
                        wifi_linear.setVisibility(View.VISIBLE);
                        wifi_linear.setTag(R.string.tag_value, connect_to_wifi_category);
                        wifi_linear.setTag(R.string.tag_action, R.string.tag_action_category);
                    } else {
                        wifi_linear.setVisibility(View.GONE);
                    }

                    if (enable_how_use_tablet.equals("1")) {
                        howTo_linear.setVisibility(View.VISIBLE);
                        howTo_linear.setTag(R.string.tag_value, how_use_tablet_category);
                        howTo_linear.setTag(R.string.tag_action, R.string.tag_action_category);
                    } else {
                        howTo_linear.setVisibility(View.GONE);
                    }

                    if (enable_local_region_category.equals("1")) {
                        local_region.setVisibility(View.VISIBLE);
                        local_region.setTag(R.string.tag_value, local_region_category);
                        local_region.setTag(R.string.tag_action, R.string.tag_action_category);
                    } else {
                        local_region.setVisibility(View.GONE);
                    }

                    if (enable_local_map.equals("1") && Math.abs(local_map_latitude) > 0 && Math.abs(local_map_longitude) > 0) {
                        local_map.setVisibility(View.VISIBLE);
                        local_map.setTag(R.string.tag_value, new MapMarker(new LatLng(local_map_latitude, local_map_longitude), local_map_address));
                        local_map.setTag(R.string.tag_action, R.string.tag_action_map);
                    } else {
                        local_map.setVisibility(View.GONE);
                    }

                    if (enable_useful_information_category.equals("1")) {
                        Info_linear.setVisibility(View.VISIBLE);
                        Info_linear.setTag(R.string.tag_value, useful_information_category);
                        Info_linear.setTag(R.string.tag_action, R.string.tag_action_category);
                    } else {
                        Info_linear.setVisibility(View.GONE);
                    }

                    if (enable_weather.equals("1")) {
                        weather_linear.setVisibility(View.VISIBLE);
                    } else {
                        weather_linear.setVisibility(View.GONE);
                    }

                    if (enable_news.equals("1")) {
                        news_linear.setVisibility(View.VISIBLE);
                    } else {
                        news_linear.setVisibility(View.GONE);
                    }
                }
            }
        }).execute();
    }

    public void onNavItemClick(View view) {
        makeMenuItemActive(view, (view.getId() != R.id.itemHome && view.getId() != R.id.btn_home));

        Object action = view.getTag(R.string.tag_action);
        Object value = view.getTag(R.string.tag_value);

        Bundle bundle = new Bundle();
        MainFragment mainFragment = new MainFragment();
        mainFragment.setFragmentListener(fragmentListener);
        mainFragment.setParentListener(activityListener);

        if (action != null && value != null) {
            if (action.equals(R.string.tag_action_category)) {
                bundle.putInt(getString(R.string.param_main_category_id),
                        Integer.parseInt(value.toString()));

                MainFragment fragment = new MainFragment();
                fragment.setFragmentListener(fragmentListener);
                fragment.setParentListener(activityListener);
                fragment.setArguments(bundle);

                fragmentListener.onUpdateFragment(fragment);

            } else if (action.equals(R.string.tag_action_service)) {
                bundle.putInt(getString(R.string.param_service_id), Integer.parseInt(value.toString()));

                MainFragment fragment = new MainFragment();
                fragment.setArguments(bundle);
                fragment.setFragmentListener(fragmentListener);
                fragment.setParentListener(activityListener);

                fragmentListener.onUpdateFragment(fragment);
            } else if (action.equals(R.string.tag_action_map)) {
                if (value instanceof MapMarker) {
                    MapMarker mapMarker = (MapMarker) value;

                    bundle.putParcelable(getString(R.string.param_marker), mapMarker);

                    MainFragment fragment = new MainFragment();
                    fragment.setArguments(bundle);
                    fragment.setFragmentListener(fragmentListener);
                    fragment.setParentListener(activityListener);

                    fragmentListener.onUpdateFragment(fragment);
                }
            }
        } else {
            fragmentListener.onUpdateFragment(mainFragment);
        }
    }

    public void onWelcomeClick(View view) {
        WelcomeFragment welcomeFragment = new WelcomeFragment();
        welcomeFragment.setParentListener(activityListener);

        NavigationFragment fragment = new NavigationFragment();
        fragment.setChildFragment(welcomeFragment);
        fragment.setFragmentListener(fragmentListener);
        fragment.setParentListener(activityListener);

        fragmentListener.onUpdateFragment(fragment);
    }

    public void onGuestInfoClick(View view) {
        new RetrieveCategories(this, 0, "gsd")
                .onSuccess(new AsyncResultBag.Success() {
                    @Override
                    public void onSuccess(Object result) {
                        if (result != null) {
                            Category[] categories = (Category[]) result;

                            if (categories.length > 0) {
                                Category category = categories[0];
                                Bundle bundle = new Bundle();

                                bundle.putInt(getString(R.string.param_category_id), category.getId());
                                bundle.putBoolean(getString(R.string.param_has_children), (category.getChildren_count() > 0));
                                bundle.putString(getString(R.string.param_listing_type), "gsd");

                                NavigationFragment fragment = new NavigationFragment();
                                fragment.setFragmentListener(fragmentListener);
                                fragment.setParentListener(activityListener);
                                fragment.setArguments(bundle);

                                fragmentListener.onUpdateFragment(fragment);
                            }
                        }
                    }
                })
                .execute();
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

        if (makeActive && view != null) {
            view.setBackgroundColor(Color.parseColor("#2cb3dc"));
        }

        item_icon_1.setImageResource(R.drawable.operatingthetelevision);
        item_icon_2.setImageResource(R.drawable.connecttowifi);
        item_icon_3.setImageResource(R.drawable.usemobile);
        item_icon_4.setImageResource(R.drawable.userinformation);
        item_icon_5.setImageResource(R.drawable.localmap);
        item_icon_6.setImageResource(R.drawable.localregion);
        item_icon_7.setImageResource(R.drawable.weather);
        item_icon_8.setImageResource(R.drawable.news);

        if (view != null) {
            switch (view.getId()) {
                case R.id.ott:
                    item_icon_1.setImageResource(R.drawable.operating_the_television_black);
                    break;
                case R.id.itemWifi:
                    item_icon_2.setImageResource(R.drawable.connect_to_wifi_black);
                    break;
                case R.id.itemHow:
                    item_icon_3.setImageResource(R.drawable.how_to_use_smart_tablet_black);
                    break;
                case R.id.itemInfo:
                    item_icon_4.setImageResource(R.drawable.useful_info_black);
                    break;
                case R.id.itemMap:
                    item_icon_5.setImageResource(R.drawable.local_map_black);
                    break;
                case R.id.itemLocalRegion:
                    item_icon_6.setImageResource(R.drawable.the_local_region_black);
                    break;
                case R.id.itemWeather:
                    item_icon_7.setImageResource(R.drawable.weather_black);
                    break;
                case R.id.itemNews:
                    item_icon_8.setImageResource(R.drawable.news_black);
                    break;
            }
        }
    }

    public void onBackClick(View view) {
        onBackPressed();
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void onTimeClick(View view) {
        if (isServiceRunning)
            return;

        if (timerClicked > 3) {
            isServiceRunning = true;

            Intent intent = new Intent(this, SyncService.class);
            startService(intent);
        } else {
            timerClicked++;
        }

        if (!timerClickedTimerAdded) {
            timerClickedTimerAdded = true;

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    timerClicked = 0;
                    timerClickedTimerAdded = false;
                }
            }, 2000);
        }
    }

    @Override
    public void onBackPressed() {
        boolean handled = false;
        FragmentManager fragmentManager = getSupportFragmentManager();

        for (Fragment fragment : fragmentManager.getFragments()) {
            if (fragment.isVisible()) {
                FragmentManager childFragmentManager = fragment.getChildFragmentManager();
                if (childFragmentManager.getBackStackEntryCount() > 0) {
                    childFragmentManager.popBackStack();
                    handled = true;

                    break;
                }
            }
        }

        if (!handled)
            try {
                moveToHome();
                //super.onBackPressed();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
    }

    private void moveToHome() {
        MainFragment firstFragment = new MainFragment();
        firstFragment.setFragmentListener(fragmentListener);
        firstFragment.setParentListener(activityListener);

        fragmentListener.onUpdateFragment(firstFragment);

        makeMenuItemActive(null, false);
    }

    public void setSignal(int wifi_signals_level) {

        if (wifi_signals_level == 4) {
            Bitmap btm = BitmapFactory.decodeResource(getResources(), R.drawable.wsignal);
            img_wifi_signals.setImageBitmap(btm);
        } else if (wifi_signals_level == 3) {
            Bitmap btm = BitmapFactory.decodeResource(getResources(), R.drawable.wsignal3);
            img_wifi_signals.setImageBitmap(btm);
        } else if (wifi_signals_level == 2) {
            Bitmap btm = BitmapFactory.decodeResource(getResources(), R.drawable.wsignal2);
            img_wifi_signals.setImageBitmap(btm);
        } else if (wifi_signals_level == 1) {
            Bitmap btm = BitmapFactory.decodeResource(getResources(), R.drawable.wsignal1);
            img_wifi_signals.setImageBitmap(btm);
        } else {
            Bitmap btm = BitmapFactory.decodeResource(getResources(), R.drawable.wifi_signals_in_active);
            img_wifi_signals.setImageBitmap(btm);
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
//            Intent btryPop = new Intent(getApplicationContext(), BatteryPopUp.class);
//            startActivity(btryPop);
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
        } else if (percentage <= 100) {
            res = R.drawable.btfull1;
        }

        Bitmap battery_icon = BitmapFactory.decodeResource(getResources(), res);
        img_battery_level.setImageBitmap(battery_icon);
    }

    private void startHandler() {
        _handler.postDelayed(_runnable, Constants.BACK_TO_HOME_WAIT);
    }

    private void stopHandler() {
        _handler.removeCallbacks(_runnable);
    }

    class BatteryBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);

            txt_battery_percentage.setText(level + "%");

            setBattery(level);
        }
    }

    class WifiScanReceiver extends BroadcastReceiver {
        public void onReceive(Context c, Intent intent) {
            WifiInfo info = wifiManager.getConnectionInfo();
            final int wifi_signals_level = WifiManager.calculateSignalLevel(info.getRssi()
                    , 4);
            setSignal(wifi_signals_level);
        }
    }
}
