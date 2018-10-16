package com.smart.tablet;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.smart.tablet.entities.Category;
import com.smart.tablet.entities.Device;
import com.smart.tablet.fragments.MainFragment;
import com.smart.tablet.fragments.NavigationFragment;
import com.smart.tablet.fragments.WelcomeFragment;
import com.smart.tablet.helpers.AnalyticsHelper;
import com.smart.tablet.helpers.Common;
import com.smart.tablet.helpers.ImageHelper;
import com.smart.tablet.helpers.ScheduledJobs;
import com.smart.tablet.helpers.Util;
import com.smart.tablet.listeners.AsyncResultBag;
import com.smart.tablet.listeners.FragmentActivityListener;
import com.smart.tablet.listeners.FragmentListener;
import com.smart.tablet.models.ActivityAction;
import com.smart.tablet.models.HotelModel;
import com.smart.tablet.models.MapMarker;
import com.smart.tablet.receivers.AdminReceiver;
import com.smart.tablet.receivers.BootReceiver;
import com.smart.tablet.receivers.PowerConnectionReceiver;
import com.smart.tablet.service.MyFirebaseMessagingService;
import com.smart.tablet.service.SyncService;
import com.smart.tablet.tasks.RetrieveCategories;
import com.smart.tablet.tasks.RetrieveDevice;
import com.smart.tablet.tasks.RetrieveHotel;
import com.smart.tablet.tasks.RetrieveSetting;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

public class MainActivity extends FragmentActivity {

    private String TAG = this.getClass().getName();
    private String entryPageStart, entryPageEnd, kioskPassword, sleepTime, timezone;
    private FrameLayout _fragment_container;
    private ImageView img_wifi_signals, img_battery_level, entry_page_img_wifi_signals, entry_page_img_battery_level, bg_image, small_logo, main_logo, entry_logo, entry_bg_img, img_electric, img_electric_2;
    private TextView txt_battery_percentage, entry_page_txt_battery_percentage, txt_time, entry_page_txt_time, _btn_home_text, _btn_back_text, item_home_text, item_tv_text, item_wifi_text, item_how_text, item_useful_info_text, item_weather_text, item_news_text, item_transport_text, item_partner_text, _app_heading, _txt_copyright, _btn_guest_info_text, _btn_top_guest_info_text, _btn_welcome_2_text;
    private LinearLayout _sidebar, _btn_home, _btn_back, _time_box, small_logo_container, main_logo_container, _btn_welcome, _btn_guest_info, _bottom_bar, _btn_top_guest_info, _app_heading_container, _top_bar_right, _top_bar_left, _top_right_buttons;
    private Button _btn_night_mode, _btn_night_mode_2;
    private RelativeLayout _sync_container, _entry_page_container, _night_mode_container, _main_activity;
    private BatteryBroadcastReceiver batteryBroadcastReceiver;
    private WifiScanReceiver wifiScanReceiver;
    private WifiManager wifiManager;
    private int timerClicked, _btn_kiosk_clicks;
    private boolean timerClickedTimerAdded, isServiceRunning, _hasEntryPage, _isActivityUp, _isCheckingToShowEntryPage, _battery_low_popup;
    private Handler _activeScreenHandler, _entryPageHandler;
    private Runnable _activeScreenRunnable, _entryPageRunnable;
    private ImageButton _btn_kiosk;
    private ImageView item_icon_1, item_icon_2, item_icon_3, item_icon_4, item_icon_5, item_icon_6, item_icon_7, item_icon_8, item_icon_9, item_icon_10;
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
    private BroadcastReceiver firebaseReceiver = new BatteryBroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String title = intent.getStringExtra("title");
            String message = intent.getStringExtra("body");

            showPopupMessage(title, message);
        }
    };
    private FragmentListener fragmentListener = new FragmentListener() {
        @Override
        public void onUpdateFragment(Fragment newFragment) {
            Log.d("FragmentUpdated", "From: MainActivity, Fragment: " + newFragment.getClass().getName());
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(_fragment_container.getId(), newFragment);
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
                case R.string.msg_show_top_right_buttons:
                    showTopRightButtons(true);
                    break;
                case R.string.msg_hide_top_right_buttons:
                    showTopRightButtons(false);
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
                case R.string.msg_show_night_mode_button:
                    showNightModeButton(true);
                    break;
                case R.string.msg_hide_night_mode_button:
                    showNightModeButton(false);
                    break;
            }
        }
    };
    private BroadcastReceiver activityListener1 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ArrayList<ActivityAction> actions = intent.getParcelableArrayListExtra(getString(R.string.param_activity_actions));

            if (actions != null) {
                for (ActivityAction action :
                        actions) {
                    activityListener.receive(action.getKey(), action.getData());
                }
            }
        }
    };
    private BroadcastReceiver powerConnectionReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean isCharging = intent.getBooleanExtra("isCharging", false);
            boolean isOnUsb = intent.getBooleanExtra("isOnUsb", false);
            boolean isOnAc = intent.getBooleanExtra("isOnAc", false);

            showElectricImage(isCharging || isOnAc || isOnUsb);
        }
    };
    private boolean inKioskMode = false;
    private DevicePolicyManager dpm;
    private ComponentName deviceAdmin;
    private boolean _isNightMode;

    private void showPopupMessage(String title, String message) {
        Intent intent = new Intent(this, MessagePopupActivity.class);
        intent.putExtra(getString(R.string.param_message_title), title);
        intent.putExtra(getString(R.string.param_message_body), message);

        startActivity(intent);
    }

    private void showElectricImage(boolean b) {
        img_electric.setVisibility(b ? View.VISIBLE : View.GONE);
        img_electric_2.setVisibility(b ? View.VISIBLE : View.GONE);
    }

    private void showCopyright(boolean b) {
        _bottom_bar.setVisibility(b ? View.VISIBLE : View.GONE);
    }

    private void showAppHeading(boolean b) {
        _app_heading_container.setVisibility(b ? View.VISIBLE : View.GONE);

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) _top_bar_left.getLayoutParams();
        layoutParams.weight = (b ? 0.35f : 0.45f);

        LinearLayout.LayoutParams layoutParams1 = (LinearLayout.LayoutParams) _app_heading_container.getLayoutParams();
        layoutParams1.weight = (b ? 0.3f : 0.3f);

        LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) _top_bar_right.getLayoutParams();
        layoutParams2.weight = (b ? 0.35f : 0.55f);

        _top_bar_left.setLayoutParams(layoutParams);
        _app_heading_container.setLayoutParams(layoutParams1);
        _top_bar_right.setLayoutParams(layoutParams2);
    }

    private void showNightModeButton(boolean b) {
        _btn_night_mode.setVisibility(b ? View.VISIBLE : View.GONE);
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

    private void showTopRightButtons(boolean b) {
        _top_right_buttons.setVisibility(b ? View.VISIBLE : View.GONE);
    }

    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        deviceAdmin = new ComponentName(this, AdminReceiver.class);
        dpm = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);

        _hasEntryPage = getIntent().getBooleanExtra(getString(R.string.param_has_entry_page), false);
        _activeScreenHandler = new Handler();
        _entryPageHandler = new Handler();
        _activeScreenRunnable = new Runnable() {
            @Override
            public void run() {
                if (_isActivityUp)
                    moveToHome();
            }
        };
        _entryPageRunnable = new Runnable() {
            @Override
            public void run() {
                showEntryPage(true);
            }
        };

        timerClicked = 0;
        timerClickedTimerAdded = isServiceRunning = _isNightMode = _isCheckingToShowEntryPage = false;
        _isActivityUp = true;
        inKioskMode = dpm.isLockTaskPermitted(this.getPackageName());

        _fragment_container = findViewById(R.id.fragment_container);
        _main_activity = findViewById(R.id.main_activity);
        _sidebar = findViewById(R.id.sidebar);
        _top_right_buttons = findViewById(R.id.topRightButtons);
        _top_bar_right = findViewById(R.id.top_bar_right);
        _top_bar_left = findViewById(R.id.top_bar_left);
        _btn_home = findViewById(R.id.btn_home);
        _btn_back = findViewById(R.id.btn_back);
        _btn_home_text = findViewById(R.id.btn_home_text);
        _btn_back_text = findViewById(R.id.btn_back_text);
        _btn_welcome = findViewById(R.id.btn_welcome);
        _btn_guest_info = findViewById(R.id.btn_guest_info);
        _btn_guest_info_text = findViewById(R.id.btn_guest_info_text);
        _btn_welcome_2_text = findViewById(R.id.btn_welcome_2_text);
        _btn_top_guest_info_text = findViewById(R.id.btn_top_guest_info_text);
        _btn_top_guest_info = findViewById(R.id.btn_top_guest_info);
        _btn_night_mode = findViewById(R.id.btn_night_mode);
        _btn_night_mode_2 = findViewById(R.id.btn_night_mode_2);
        _btn_kiosk = findViewById(R.id.btn_kiosk);
        _time_box = findViewById(R.id.time_box);
        _sync_container = findViewById(R.id.syncContainer);
        _entry_page_container = findViewById(R.id.entryPageContainer);
        _night_mode_container = findViewById(R.id.nightModeContainer);
        item_home_text = findViewById(R.id.item_home_text);
        item_tv_text = findViewById(R.id.item_tv_text);
        item_wifi_text = findViewById(R.id.item_wifi_text);
        item_how_text = findViewById(R.id.item_how_text);
        item_useful_info_text = findViewById(R.id.item_useful_info_text);
        item_weather_text = findViewById(R.id.item_weather_text);
        item_news_text = findViewById(R.id.item_news_text);
        item_transport_text = findViewById(R.id.item_transport_text);
        item_partner_text = findViewById(R.id.item_partner_text);
        img_wifi_signals = findViewById(R.id.wifi_connect);
        img_electric = findViewById(R.id.img_electric);
        img_electric_2 = findViewById(R.id.img_electric_2);
        entry_page_img_wifi_signals = findViewById(R.id.entry_page_wifi_connect);
        img_battery_level = findViewById(R.id.bettryStatus);
        entry_page_img_battery_level = findViewById(R.id.entry_page_bettryStatus);
        txt_battery_percentage = findViewById(R.id.percentage_set);
        entry_page_txt_battery_percentage = findViewById(R.id.entry_page_percentage_set);
        txt_time = findViewById(R.id.getTime);
        entry_page_txt_time = findViewById(R.id.entry_page_getTime);
        _app_heading = findViewById(R.id.app_heading);
        _app_heading_container = findViewById(R.id.app_heading_container);
        _txt_copyright = findViewById(R.id.txt_copyright);
        bg_image = findViewById(R.id.main_bg_img);
        entry_bg_img = findViewById(R.id.entry_bg_img);
        small_logo = findViewById(R.id.small_logo_img);
        main_logo = findViewById(R.id.main_logo_img);
        entry_logo = findViewById(R.id.entryLogo);
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
        item_icon_9 = findViewById(R.id.transport);
        item_icon_10 = findViewById(R.id.partner);

        if (_fragment_container != null) {

            if (savedInstanceState != null) {
                return;
            }

            MainFragment firstFragment = new MainFragment();
            firstFragment.setFragmentListener(fragmentListener);
            firstFragment.setParentListener(activityListener);

            getSupportFragmentManager().beginTransaction()
                    .add(_fragment_container.getId(), firstFragment).commit();
        }

        _btn_home_text.setTypeface(Util.getTypeFace(this));
        _btn_top_guest_info_text.setTypeface(Util.getTypeFace(this));
        _btn_welcome_2_text.setTypeface(Util.getTypeFace(this));
        _btn_guest_info_text.setTypeface(Util.getTypeFace(this));
        _btn_back_text.setTypeface(Util.getTypeFace(this));
        _btn_night_mode.setTypeface(Util.getTypeFace(this));
        _btn_night_mode_2.setTypeface(Util.getTypeFace(this));
        item_home_text.setTypeface(Util.getTypeFace(this));
        item_tv_text.setTypeface(Util.getTypeFace(this));
        item_wifi_text.setTypeface(Util.getTypeFace(this));
        item_how_text.setTypeface(Util.getTypeFace(this));
        item_useful_info_text.setTypeface(Util.getTypeFace(this));
        item_weather_text.setTypeface(Util.getTypeFace(this));
        item_news_text.setTypeface(Util.getTypeFace(this));
        item_transport_text.setTypeface(Util.getTypeFace(this));
        item_partner_text.setTypeface(Util.getTypeFace(this));
        _app_heading.setTypeface(Util.getTypeFace(this));

        batteryBroadcastReceiver = new BatteryBroadcastReceiver();
        wifiScanReceiver = new WifiScanReceiver();
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        _btn_kiosk.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                toggleKioskMode(v);
                return false;
            }
        });

        _btn_kiosk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _btn_kiosk_clicks++;

                if (_btn_kiosk_clicks > 1) {
                    _btn_kiosk_clicks = 0;
                    openDeviceInformationDialog(v);
                }
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                WifiInfo info = wifiManager.getConnectionInfo();
                final int wifi_signals_level = WifiManager.calculateSignalLevel(info.getRssi()
                        , 4);

                setSignal(wifi_signals_level);
            }
        }, 1000);

        getHotelInformation();
    }

    private void init() {
        getTimeSettings();
        getKioskPassword();
        setupMenuItems();
        setBranding();
        scheduleAlarms();

        wakeupScreen();

        if (_hasEntryPage) {
            showEntryPage(true);
        }
    }

    private void getHotelInformation() {
        timezone = Constants.DEFAULT_TIMEZONE;

        new RetrieveHotel(this)
                .onSuccess(new AsyncResultBag.Success() {
                    @Override
                    public void onSuccess(Object result) {
                        if (result != null) {
                            HotelModel hotel = (HotelModel) result;

                            if (hotel.getTimezone() != null && !hotel.getTimezone().equals(""))
                                timezone = hotel.getTimezone();
                        }

                        init();
                    }
                })
                .execute();
    }

    private void getKioskPassword() {
        kioskPassword = "test";
        new RetrieveSetting(this, Constants.SETTING_PASSWORD)
                .onSuccess(new AsyncResultBag.Success() {
                    @Override
                    public void onSuccess(Object result) {
                        if (result != null)
                            kioskPassword = result.toString();
                    }
                })
                .execute();
    }

    private void getTimeSettings() {
        new RetrieveSetting(this, Constants.SETTING_ENTRY_PAGE_START_TIME,
                Constants.SETTING_ENTRY_PAGE_END_TIME,
                Constants.SETTING_SLEEP_TIME)
                .onSuccess(new AsyncResultBag.Success() {
                    @Override
                    public void onSuccess(Object result) {
                        HashMap<String, String> values = result != null ? (HashMap<String, String>) result : null;

                        if (values != null) {
                            entryPageStart = values.containsKey("entry_page_start_time") ? values.get("entry_page_start_time") : null;
                            entryPageEnd = values.containsKey("entry_page_end_time") ? values.get("entry_page_end_time") : null;
                            sleepTime = values.containsKey("sleep_time") ? values.get("sleep_time") : null;
                        }
                    }
                })
                .execute();
    }

    private void showEntryPage(boolean b) {
        _entry_page_container.setVisibility(b ? View.VISIBLE : View.GONE);
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
        ScheduledJobs.scheduleSyncAlarm(this, timezone);
        ScheduledJobs.scheduleWakeupAlarm(this);

        setBootReceiverEnabled(PackageManager.COMPONENT_ENABLED_STATE_ENABLED);
    }

    private void setBootReceiverEnabled(int componentEnabledState) {
        ComponentName componentName = new ComponentName(this, BootReceiver.class);
        PackageManager packageManager = getPackageManager();
        packageManager.setComponentEnabledSetting(componentName,
                componentEnabledState,
                PackageManager.DONT_KILL_APP);
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
                            entry_bg_img.setBackgroundDrawable(bd);
                        }
                    }

                    filePath = values.get(Constants.SETTING_LOGO);

                    if (filePath != null) {
                        File imageFile = new File(filePath);

                        if (imageFile.exists()) {
                            try {
                                Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                                Bitmap bitmap_small = ImageHelper.getResizedBitmap(bitmap, 500);
                                Bitmap bitmap_large = ImageHelper.getResizedBitmap(bitmap, 1000);

                                small_logo.setImageBitmap(bitmap_small);
                                main_logo.setImageBitmap(bitmap_large);
                                entry_logo.setImageBitmap(bitmap_large);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
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

        stopCheckingActiveScreen();
        stopCheckingToShowEntryPage();

        startCheckingActiveScreen();
        //startCheckingToShowEntryPage();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        _isActivityUp = false;
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        _isActivityUp = true;
    }

    @Override
    protected void onStart() {
        registerReceiver(syncReceiver, new IntentFilter(SyncService.TRANSACTION_DONE));
        registerReceiver(syncCompleteReceiver, new IntentFilter(SyncService.TRANSACTION_COMPLETE));
        registerReceiver(syncStartReceiver, new IntentFilter(SyncService.TRANSACTION_START));
        registerReceiver(syncHeartBeatReceiver, new IntentFilter(SyncService.TRANSACTION_HEART_BEAT));
        registerReceiver(wifiScanReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        registerReceiver(batteryBroadcastReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        registerReceiver(firebaseReceiver, new IntentFilter(MyFirebaseMessagingService.MESSAGE_RECEIVED));
        registerReceiver(activityListener1, new IntentFilter(getString(R.string.param_activity_action)));
        registerReceiver(powerConnectionReceiver, new IntentFilter(PowerConnectionReceiver.POWER_CONNECTION_CHANGE));

        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                DateFormat df = new SimpleDateFormat("hh:mm a");

                                if (timezone != null && !timezone.equals("")) {
                                    df.setTimeZone(TimeZone.getTimeZone(timezone));
                                }

                                String date = df.format(Calendar.getInstance().getTime());

                                txt_time.setText(date);
                                entry_page_txt_time.setText(date);

                                checkEntryPageConditions();
                                checkSleepPageConditions();
                            }
                        });
                        Thread.sleep((1000 * 60));
                    }
                } catch (InterruptedException e) {
                    Log.e(TAG, e + "");
                }
            }
        };

        t.start();

        if (!dpm.isAdminActive(deviceAdmin)) {
            showToast("This app is not a device admin!");
        }

        if (dpm.isDeviceOwnerApp(getPackageName())) {
            dpm.setLockTaskPackages(deviceAdmin,
                    new String[]{getPackageName()});
            setKioskMode(true);
        } else {
            showToast("This app is not the device owner!");
        }

        super.onStart();
    }

    private void checkSleepPageConditions() {
        if (sleepTime == null || sleepTime.isEmpty())
            return;

        DateFormat df = new SimpleDateFormat("HH:mm");

        if (timezone != null && !timezone.equals("")) {
            df.setTimeZone(TimeZone.getTimeZone(timezone));
        }

        String date = df.format(Calendar.getInstance().getTime());
        Date sleepTimeDate = null;

        try {
            sleepTimeDate = df.parse(this.sleepTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        sleepTime = df.format(sleepTimeDate.getTime());

        if (date.equals(this.sleepTime)) {
            showNightMode(true);
        }
    }

    private void checkEntryPageConditions() {
        if (entryPageStart == null || entryPageEnd == null || entryPageStart.isEmpty() || entryPageEnd.isEmpty())
            return;

        DateFormat time_format = new SimpleDateFormat("HH:mm");
        DateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat datetime_format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        if (timezone != null && !timezone.equals("")) {
            time_format.setTimeZone(TimeZone.getTimeZone(timezone));
            date_format.setTimeZone(TimeZone.getTimeZone(timezone));
            datetime_format.setTimeZone(TimeZone.getTimeZone(timezone));
        }

        Date now = Calendar.getInstance().getTime();
        String date = time_format.format(now);

        String entryPageStartDateTime = date_format.format(now) + " " + entryPageStart;
        String entryPageEndDateTime = date_format.format(now) + " " + entryPageEnd;

        try {
            Date d1 = datetime_format.parse(entryPageStartDateTime);
            Date d2 = datetime_format.parse(entryPageEndDateTime);

            Util.DateDifference diff = Util.getDateDifference(d1, now);
            boolean is_started = diff.getHours() < 0 || diff.getMinutes() < 0 || diff.getSeconds() < 0;

            diff = Util.getDateDifference(d2, now);
            boolean is_ended = diff.getHours() < 0 || diff.getMinutes() < 0 || diff.getSeconds() < 0;

            if (!is_ended && is_started) {
                if (!isCheckingToShowEntryPage())
                    startCheckingToShowEntryPage();
            } else {
                stopCheckingToShowEntryPage();
            }

            //Log.d("entryPageDate", "\n" + d1 + "\n" + d2 + "\n" + now);
            //Log.d("entryPageDate", diff.toString());
            //Log.d("entryPageDate", isCheckingToShowEntryPage() ? "checking" : "not checking");
            //Log.d("entryPageDate", "started: " + (is_started ? "Yes" : "No") + ", ended: " + (is_ended ? "Yes" : "No"));
        } catch (ParseException e) {
            Log.e("entryPageDate", e.getMessage());
            e.printStackTrace();
        }
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

        if (firebaseReceiver != null)
            unregisterReceiver(firebaseReceiver);

        if (activityListener1 != null)
            unregisterReceiver(activityListener1);

        if (powerConnectionReceiver != null)
            unregisterReceiver(powerConnectionReceiver);

        stopCheckingActiveScreen();
        stopCheckingToShowEntryPage();

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
                "enable_transport_options",
                "enable_partner_offers",
                "operating_the_television_category",
                "connect_to_wifi_category",
                "how_use_tablet_category",
                "useful_information_category",
                "local_region_category",
                "transport_options_category",
                "partner_offers_category",
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
                    String enable_transport_options = values.containsKey("enable_transport_options") ? values.get("enable_transport_options") : "0";
                    String enable_partner_offers = values.containsKey("enable_partner_offers") ? values.get("enable_partner_offers") : "0";
                    String operating_the_television_category = values.containsKey("operating_the_television_category") ? values.get("operating_the_television_category") : "0";
                    String connect_to_wifi_category = values.containsKey("connect_to_wifi_category") ? values.get("connect_to_wifi_category") : "0";
                    String how_use_tablet_category = values.containsKey("how_use_tablet_category") ? values.get("how_use_tablet_category") : "0";
                    String useful_information_category = values.containsKey("useful_information_category") ? values.get("useful_information_category") : "0";
                    String local_region_category = values.containsKey("local_region_category") ? values.get("local_region_category") : "0";
                    String transport_options_category = values.containsKey("transport_options_category") ? values.get("transport_options_category") : "0";
                    String partner_offers_category = values.containsKey("partner_offers_category") ? values.get("partner_offers_category") : "0";
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
                    LinearLayout transport_options = findViewById(R.id.itemTransport);
                    LinearLayout partner_offers = findViewById(R.id.itemPartner);

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

                    if (enable_transport_options.equals("1")) {
                        transport_options.setVisibility(View.VISIBLE);
                        transport_options.setTag(R.string.tag_value, transport_options_category);
                        transport_options.setTag(R.string.tag_action, R.string.tag_action_category);
                    } else {
                        transport_options.setVisibility(View.GONE);
                    }

                    if (enable_partner_offers.equals("1")) {
                        partner_offers.setVisibility(View.VISIBLE);
                        partner_offers.setTag(R.string.tag_value, partner_offers_category);
                        partner_offers.setTag(R.string.tag_action, R.string.tag_action_category);
                    } else {
                        partner_offers.setVisibility(View.GONE);
                    }

                    if (enable_weather.equals("1")) {
                        weather_linear.setVisibility(View.VISIBLE);
                        weather_linear.setTag(R.string.tag_action, R.string.tag_action_weather);
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

    public void onEntryPageEnter(View view) {
        showEntryPage(false);

        AnalyticsHelper.track(this, "Entry Page");
    }

    public void toggleNightMode(View view) {
        _isNightMode = !_isNightMode;
        showNightMode(_isNightMode);

        AnalyticsHelper.track(this, _isNightMode ? "Enabled night mode" : "Disabled night mode");
    }

    private void showNightMode(boolean b) {
        _night_mode_container.setVisibility(b ? View.VISIBLE : View.GONE);

        boolean has_permit = true;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            has_permit = Settings.System.canWrite(getApplicationContext());
        }

        if (has_permit) {
            Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
            Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, (b ? 0 : 100));
        }
    }

    public void onNavItemClick(View view) {
        makeMenuItemActive(view, (view.getId() != R.id.itemHome && view.getId() != R.id.btn_home));

        Object action = view.getTag(R.string.tag_action);
        Object value = view.getTag(R.string.tag_value);

        Bundle bundle = new Bundle();
        MainFragment mainFragment = new MainFragment();
        mainFragment.setFragmentListener(fragmentListener);
        mainFragment.setParentListener(activityListener);

        if (action != null) {
            if (action.equals(R.string.tag_action_category) && value != null) {
                bundle.putInt(getString(R.string.param_main_category_id),
                        Integer.parseInt(value.toString()));

                MainFragment fragment = new MainFragment();
                fragment.setFragmentListener(fragmentListener);
                fragment.setParentListener(activityListener);
                fragment.setArguments(bundle);

                fragmentListener.onUpdateFragment(fragment);

            } else if (action.equals(R.string.tag_action_service) && value != null) {
                bundle.putInt(getString(R.string.param_service_id), Integer.parseInt(value.toString()));

                MainFragment fragment = new MainFragment();
                fragment.setArguments(bundle);
                fragment.setFragmentListener(fragmentListener);
                fragment.setParentListener(activityListener);

                fragmentListener.onUpdateFragment(fragment);
            } else if (action.equals(R.string.tag_action_map) && value != null) {
                if (value instanceof MapMarker) {
                    MapMarker mapMarker = (MapMarker) value;

                    bundle.putParcelable(getString(R.string.param_marker), mapMarker);

                    MainFragment fragment = new MainFragment();
                    fragment.setArguments(bundle);
                    fragment.setFragmentListener(fragmentListener);
                    fragment.setParentListener(activityListener);

                    fragmentListener.onUpdateFragment(fragment);
                }
            } else if (action.equals(R.string.tag_action_weather)) {
                bundle.putBoolean(getString(R.string.param_weather), true);

                MainFragment fragment = new MainFragment();
                fragment.setArguments(bundle);
                fragment.setFragmentListener(fragmentListener);
                fragment.setParentListener(activityListener);

                fragmentListener.onUpdateFragment(fragment);
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
        showEntryPage(false);

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

        AnalyticsHelper.track(this, "Moved to Guest Services");
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
        LinearLayout transport_options = findViewById(R.id.itemTransport);
        LinearLayout partner_offers = findViewById(R.id.itemPartner);

        LinearLayout[] all_items = new LinearLayout[]{ott_linear, wifi_linear, howTo_linear, Info_linear, map_linear, localReg_linear,
                weather_linear, news_linear, transport_options, partner_offers};

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
        item_icon_9.setImageResource(R.drawable.transport_icon);
        item_icon_10.setImageResource(R.drawable.partner_offer);

        String text = null;

        if (view != null) {
            switch (view.getId()) {
                case R.id.ott:
                    item_icon_1.setImageResource(R.drawable.operating_the_television_black);
                    text = getString(R.string.operating_the_television);
                    break;
                case R.id.itemWifi:
                    item_icon_2.setImageResource(R.drawable.connect_to_wifi_black);
                    text = getString(R.string.connect_to_wifi);
                    break;
                case R.id.itemHow:
                    item_icon_3.setImageResource(R.drawable.how_to_use_smart_tablet_black);
                    text = getString(R.string.how_to_use_smart_tablet);
                    break;
                case R.id.itemInfo:
                    item_icon_4.setImageResource(R.drawable.useful_info_black);
                    text = getString(R.string.useful_information);
                    break;
                case R.id.itemMap:
                    item_icon_5.setImageResource(R.drawable.local_map_black);
                    text = getString(R.string.local_map);
                    break;
                case R.id.itemLocalRegion:
                    item_icon_6.setImageResource(R.drawable.the_local_region_black);
                    text = getString(R.string.local_region);
                    break;
                case R.id.itemWeather:
                    item_icon_7.setImageResource(R.drawable.weather_black);
                    text = getString(R.string.weather);
                    break;
                case R.id.itemNews:
                    item_icon_8.setImageResource(R.drawable.news_black);
                    text = getString(R.string.news);
                    break;
                case R.id.itemTransport:
                    item_icon_9.setImageResource(R.drawable.transport_icon_black);
                    text = getString(R.string.transport_options);
                    break;
                case R.id.itemPartner:
                    item_icon_10.setImageResource(R.drawable.partner_offer_black);
                    text = getString(R.string.partner_offers);
                    break;
            }

            if (makeActive) {
                AnalyticsHelper.track(this, String.format("Tapped %s from side menu", text));
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
        AnalyticsHelper.track(this, "Tapped time");

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

        AnalyticsHelper.track(this, "Moved to Home");
    }

    public void setSignal(int wifi_signals_level) {

        if (wifi_signals_level == 4) {
            Bitmap btm = BitmapFactory.decodeResource(getResources(), R.drawable.wsignal);
            img_wifi_signals.setImageBitmap(btm);
            entry_page_img_wifi_signals.setImageBitmap(btm);
        } else if (wifi_signals_level == 3) {
            Bitmap btm = BitmapFactory.decodeResource(getResources(), R.drawable.wsignal3);
            img_wifi_signals.setImageBitmap(btm);
            entry_page_img_wifi_signals.setImageBitmap(btm);
        } else if (wifi_signals_level == 2) {
            Bitmap btm = BitmapFactory.decodeResource(getResources(), R.drawable.wsignal2);
            img_wifi_signals.setImageBitmap(btm);
            entry_page_img_wifi_signals.setImageBitmap(btm);
        } else if (wifi_signals_level == 1) {
            Bitmap btm = BitmapFactory.decodeResource(getResources(), R.drawable.wsignal1);
            img_wifi_signals.setImageBitmap(btm);
            entry_page_img_wifi_signals.setImageBitmap(btm);
        } else {
            Bitmap btm = BitmapFactory.decodeResource(getResources(), R.drawable.wifi_signals_in_active);
            img_wifi_signals.setImageBitmap(btm);
            entry_page_img_wifi_signals.setImageBitmap(btm);
        }
    }

    public void setBattery(float percentage) {
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
        } else if (percentage <= 100) {
            res = R.drawable.btfull1;
        }

        if (percentage < 5 && !_battery_low_popup) {
            _battery_low_popup = true;
            showPopupMessage("Battery Low", "Warning! Connect Power");
        } else {
            _battery_low_popup = false;
        }

        Bitmap battery_icon = BitmapFactory.decodeResource(getResources(), res);
        img_battery_level.setImageBitmap(battery_icon);
        entry_page_img_battery_level.setImageBitmap(battery_icon);
    }

    private void startCheckingActiveScreen() {
        _activeScreenHandler.postDelayed(_activeScreenRunnable, Constants.BACK_TO_HOME_WAIT);
    }

    private void stopCheckingActiveScreen() {
        _activeScreenHandler.removeCallbacks(_activeScreenRunnable);
    }

    private void startCheckingToShowEntryPage() {
        _isCheckingToShowEntryPage = true;
        _entryPageHandler.postDelayed(_entryPageRunnable, Constants.BACK_TO_ENTRY_PAGE);
    }

    private boolean isCheckingToShowEntryPage() {
        return _isCheckingToShowEntryPage;
    }

    private void stopCheckingToShowEntryPage() {
        _isCheckingToShowEntryPage = false;
        _entryPageHandler.removeCallbacks(_entryPageRunnable);
    }

    public void takeActions(ArrayList<ActivityAction> actions) {
        if (actions != null) {
            for (ActivityAction action :
                    actions) {
                activityListener.receive(action.getKey(), action.getData());
            }
        }
    }

    private void setKioskMode(boolean on) {
        try {
            if (on) {
                if (dpm.isLockTaskPermitted(this.getPackageName())) {

                    startLockTask();
                    setLauncher();

                    inKioskMode = true;

                } else {
                    showToast("Kiosk Mode not permitted");
                }
            } else {
                stopLockTask();
                restoreLauncher();

                inKioskMode = false;
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e);
        }
    }

    public void openDeviceInformationDialog(View view) {

        AnalyticsHelper.track(this, "Viewed Device Information");

        new RetrieveDevice(this)
                .onSuccess(new AsyncResultBag.Success() {
                    @Override
                    public void onSuccess(Object result) {
                        if (result != null) {
                            Device device = (Device) result;

                            String mac_address = device.getMac_address();
                            String din = device.getDevice_identity();
                            String room_number = "";

                            if (!device.getMeta().isEmpty()) {
                                JSONArray metas_arr = null;
                                try {
                                    metas_arr = new JSONArray(device.getMeta());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                if (metas_arr != null) {
                                    for (int i = 0; i < metas_arr.length(); i++) {
                                        try {
                                            JSONObject meta_obj = metas_arr.getJSONObject(i);
                                            String meta_key = meta_obj.getString("meta_key");
                                            String meta_value = meta_obj.getString("meta_value");

                                            switch (meta_key) {
                                                case "room_allocation":
                                                    room_number = meta_value;
                                                    break;
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }

                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setTitle("Device Information");
                            builder.setMessage("MAC Address: " + mac_address + "\nDIN: " + din + "\nRoom Number: " + room_number);

                            builder.show();
                        }
                    }
                })
                .execute();
    }

    public void toggleKioskMode(View view) {
        AnalyticsHelper.track(this, "Toggle Kiosk Mode");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Password");

        final EditText input = new EditText(this);

        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String text = input.getText().toString();

                if (text.equals(kioskPassword)) {
                    setKioskMode(false);
                } else {
                    showToast("Wrong Password");
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void restoreLauncher() {
        dpm.clearPackagePersistentPreferredActivities(deviceAdmin,
                this.getPackageName());
        showToast("Home activity: " + Common.getHomeActivity(this));
    }

    public void setLauncher() {
        Common.becomeHomeActivity(this);
    }

    class BatteryBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);

            txt_battery_percentage.setText(level + "%");
            entry_page_txt_battery_percentage.setText(level + "%");

            int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            final boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                    status == BatteryManager.BATTERY_STATUS_FULL;

            showElectricImage(isCharging);
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
