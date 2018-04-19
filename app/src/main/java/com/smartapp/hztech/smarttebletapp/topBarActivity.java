package com.smartapp.hztech.smarttebletapp;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartapp.hztech.smarttebletapp.receivers.NetworkChangeReceiver;

import java.util.List;

public class topBarActivity extends Activity {
    private ImageView set_sginal_img, setBatteryStatus;
    private TextView percentage_set;
   // private BatteryBroadcastReceiver batteryBroadcastReceiver;
   // private WifiScanReceiver wifiReciever;
    private WifiManager wifiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_top_bar);

//        set_sginal_img = (ImageView) findViewById(R.id.wifi_connect);
//        setBatteryStatus = (ImageView) findViewById(R.id.bettryStatus);
//        percentage_set = (TextView) findViewById(R.id.percentage_set);
//
//        batteryBroadcastReceiver = new BatteryBroadcastReceiver();
//        wifiReciever = new WifiScanReceiver();
//        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                WifiInfo info = wifiManager.getConnectionInfo();
//
//                final String wifi_name = info.getSSID();
//                final int wifi_signals_level = WifiManager.calculateSignalLevel(info.getRssi()
//                        , 4);
//
//                setSignal(wifi_signals_level);
//            }
//        }, 5000);
    }

//    @Override
//    protected void onStart() {
//        registerReceiver(wifiReciever, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
//        registerReceiver(batteryBroadcastReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
//
//        super.onStart();
//    }
//
//    @Override
//    protected void onStop() {
//        unregisterReceiver(batteryBroadcastReceiver);
//        unregisterReceiver(wifiReciever);
//
//        super.onStop();
//    }
//
//    public void setSignal(int wifi_signals_level) {
//
//        if (wifi_signals_level == 4) {
//            Bitmap btm = BitmapFactory.decodeResource(getResources(), R.drawable.wsignal);
//            set_sginal_img.setImageBitmap(btm);
//        } else if (wifi_signals_level == 3) {
//            Bitmap btm = BitmapFactory.decodeResource(getResources(), R.drawable.wsignal3);
//            set_sginal_img.setImageBitmap(btm);
//        } else if (wifi_signals_level == 2) {
//            Bitmap btm = BitmapFactory.decodeResource(getResources(), R.drawable.wsignal2);
//            set_sginal_img.setImageBitmap(btm);
//        } else if (wifi_signals_level == 1) {
//            Bitmap btm = BitmapFactory.decodeResource(getResources(), R.drawable.wsignal1);
//            set_sginal_img.setImageBitmap(btm);
//        } else {
//            Bitmap btm = BitmapFactory.decodeResource(getResources(), R.drawable.operatingthetelevision);
//            set_sginal_img.setImageBitmap(btm);
//        }
//    }
//
//    public void setBattery(float percentage) {
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
//        }
//
//        Bitmap bettryMap = BitmapFactory.decodeResource(getResources(), res);
//        setBatteryStatus.setImageBitmap(bettryMap);
//    }
//
//    class BatteryBroadcastReceiver extends BroadcastReceiver {
//
//        private final static String BATTERY_LEVEL = "level";
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//
//            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
//
//            Log.d("checkPercent", level + " ");
//            percentage_set.setText(level + "%");
//
//            setBattery(level);
//        }
//    }
//
//    class WifiScanReceiver extends BroadcastReceiver {
//        public void onReceive(Context c, Intent intent) {
//            List<ScanResult> wifiScanList = wifiManager.getScanResults();
//
//            WifiInfo info = wifiManager.getConnectionInfo();
//
//            final String wifi_name = info.getSSID();
//            final int wifi_signals_level = WifiManager.calculateSignalLevel(info.getRssi()
//                    , 4);
//            setSignal(wifi_signals_level);
//            Log.d("data_check", wifi_signals_level + "");
//        }
//    }
}
