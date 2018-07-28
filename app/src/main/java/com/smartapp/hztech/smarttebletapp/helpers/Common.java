package com.smartapp.hztech.smarttebletapp.helpers;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.widget.Toast;

import com.smartapp.hztech.smarttebletapp.MainActivity;
import com.smartapp.hztech.smarttebletapp.SplashActivity;
import com.smartapp.hztech.smarttebletapp.receivers.AdminReceiver;

public class Common {

    public static void showToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }

    public static String getHomeActivity(Context c) {
        PackageManager pm = c.getPackageManager();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);

        ComponentName cn = intent.resolveActivity(pm);

        if (cn != null)
            return cn.flattenToShortString();
        else
            return "none";
    }

    public static void becomeHomeActivity(Context c) {
        ComponentName deviceAdmin = new ComponentName(c, AdminReceiver.class);
        DevicePolicyManager dpm = (DevicePolicyManager) c.getSystemService(Context.DEVICE_POLICY_SERVICE);

        if (!dpm.isAdminActive(deviceAdmin)) {
            showToast(c, "This app is not a device admin!");
            return;
        }
        if (!dpm.isDeviceOwnerApp(c.getPackageName())) {
            showToast(c, "This app is not the device owner!");
            return;
        }

        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_MAIN);

        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        intentFilter.addCategory(Intent.CATEGORY_HOME);

        ComponentName activity = new ComponentName(c, SplashActivity.class);
        dpm.addPersistentPreferredActivity(deviceAdmin, intentFilter, activity);

        showToast(c, "Home activity: " + getHomeActivity(c));
    }
}