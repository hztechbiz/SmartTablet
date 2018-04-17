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
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.smartapp.hztech.smarttebletapp.receivers.NetworkChangeReceiver;

import java.util.List;

public class topBarActivity extends Activity {
    private ImageView set_sginal_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_bar);
        set_sginal_img = (ImageView) findViewById(R.id.wifi_connect);
        final WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                WifiInfo info = wifiManager.getConnectionInfo();

                final String wifi_name = info.getSSID();
                final int wifi_signals_level = WifiManager.calculateSignalLevel(info.getRssi()
                        , 4);

               setSignal(wifi_signals_level);


            }
        }, 5000);

        class WifiScanReceiver extends BroadcastReceiver {
            public void onReceive(Context c, Intent intent) {
                List<ScanResult> wifiScanList = wifiManager.getScanResults();

                WifiInfo info = wifiManager.getConnectionInfo();

                final String wifi_name = info.getSSID();
                final int wifi_signals_level = WifiManager.calculateSignalLevel(info.getRssi()
                        , 4);
                setSignal(wifi_signals_level);
                Log.d("data_check", wifi_signals_level + "");
            }
        }
        WifiScanReceiver wifiReciever = new WifiScanReceiver();
        registerReceiver(wifiReciever, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

    }

    public void setSignal(int wifi_signals_level){

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
            Bitmap btm = BitmapFactory.decodeResource(getResources(), R.drawable.tvc);
            set_sginal_img.setImageBitmap(btm);
        }
    }

}
