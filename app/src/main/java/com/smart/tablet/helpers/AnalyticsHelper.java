package com.smart.tablet.helpers;

import android.content.Context;

import com.smart.tablet.Constants;
import com.smart.tablet.entities.Analytics;
import com.smart.tablet.listeners.AsyncResultBag;
import com.smart.tablet.models.HotelModel;
import com.smart.tablet.tasks.RetrieveHotel;
import com.smart.tablet.tasks.RetrieveSetting;
import com.smart.tablet.tasks.StoreAnalytics;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class AnalyticsHelper {
    private static AnalyticsHelper _instance;
    private Context _context;

    public static AnalyticsHelper getInstance(Context context) {
        if (_instance == null)
            _instance = new AnalyticsHelper();

        _instance._context = context;

        return _instance;
    }

    public static void track(final Context context, final String purpose, final String service_id, final String category_id) {
        new RetrieveHotel(context)
                .onSuccess(new AsyncResultBag.Success() {
                    @Override
                    public void onSuccess(Object result) {
                        String timezone = Constants.DEFAULT_TIMEZONE;

                        if (result != null) {
                            HotelModel hotel = (HotelModel) result;

                            if (hotel.getTimezone() != null && !hotel.getTimezone().equals(""))
                                timezone = hotel.getTimezone();
                        }

                        Analytics analytics = new Analytics();

                        JSONObject jsonObject = new JSONObject();
                        DateFormat datetime_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                        if (timezone != null && !timezone.equals("")) {
                            datetime_format.setTimeZone(TimeZone.getTimeZone(timezone));
                        }

                        String datetime = datetime_format.format(Calendar.getInstance().getTime());

                        try {
                            jsonObject.put("purpose", purpose);
                            jsonObject.put("service_id", service_id);
                            jsonObject.put("category_id", category_id);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        analytics.setData(jsonObject.toString());
                        analytics.setCreated_at(datetime);
                        analytics.setUpdated_at(datetime);

                        new StoreAnalytics(context, analytics)
                                .execute();
                    }
                })
                .execute();
    }
}
