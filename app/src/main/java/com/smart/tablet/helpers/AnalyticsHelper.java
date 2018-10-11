package com.smart.tablet.helpers;

import android.content.Context;

import com.smart.tablet.entities.Analytics;
import com.smart.tablet.tasks.StoreAnalytics;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AnalyticsHelper {
    private static AnalyticsHelper _instance;
    private Context _context;

    public static AnalyticsHelper getInstance(Context context) {
        if (_instance == null)
            _instance = new AnalyticsHelper();

        _instance._context = context;

        return _instance;
    }

    public static void track(Context context, String purpose) {
        Analytics analytics = new Analytics();

        JSONObject jsonObject = new JSONObject();
        DateFormat datetime_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String datetime = datetime_format.format(Calendar.getInstance().getTime());

        try {
            jsonObject.put("purpose", purpose);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        analytics.setData(jsonObject.toString());
        analytics.setCreated_at(datetime);
        analytics.setUpdated_at(datetime);

        new StoreAnalytics(context, analytics)
                .execute();
    }
}
