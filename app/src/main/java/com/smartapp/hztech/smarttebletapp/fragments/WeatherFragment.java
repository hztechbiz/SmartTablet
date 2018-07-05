package com.smartapp.hztech.smarttebletapp.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.smartapp.hztech.smarttebletapp.AppController;
import com.smartapp.hztech.smarttebletapp.Constants;
import com.smartapp.hztech.smarttebletapp.R;
import com.smartapp.hztech.smarttebletapp.helpers.Util;
import com.smartapp.hztech.smarttebletapp.listeners.FragmentActivityListener;
import com.smartapp.hztech.smarttebletapp.listeners.FragmentListener;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

public class WeatherFragment extends Fragment implements View.OnClickListener {
    private static final String ns = null;
    Bundle _bundle;
    private FragmentActivityListener parentListener;
    private FragmentListener fragmentListener;
    private TextView txt_main_title, txt_details, txt_temperature, btn_sydney, btn_melbourne, btn_canberra, btn_brisbane, btn_darwin, btn_hobart, btn_gold_coast, btn_perth;
    private ArrayList<Item> _weatherItems;
    private LinearLayout forecast_section;
    private LayoutInflater _inflater;
    private LinearLayout _lastForecastContainer;
    private TextView[] buttons;

    public WeatherFragment() {

    }

    public void setParentListener(FragmentActivityListener parentListener) {
        this.parentListener = parentListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        _inflater = inflater;
        View view = inflater.inflate(R.layout.weather_fragment, container, false);
        _bundle = getArguments();

        txt_main_title = view.findViewById(R.id.txt_main_title);
        txt_details = view.findViewById(R.id.txt_details);
        txt_temperature = view.findViewById(R.id.txt_temperature);
        btn_sydney = view.findViewById(R.id.btn_sydney);
        btn_melbourne = view.findViewById(R.id.btn_melbourne);
        btn_brisbane = view.findViewById(R.id.btn_brisbane);
        btn_canberra = view.findViewById(R.id.btn_canberra);
        btn_darwin = view.findViewById(R.id.btn_darwin);
        btn_hobart = view.findViewById(R.id.btn_hobart);
        btn_gold_coast = view.findViewById(R.id.btn_gold_coast);
        btn_perth = view.findViewById(R.id.btn_perth);
        forecast_section = view.findViewById(R.id.forecast_section);

        buttons = new TextView[]{btn_sydney, btn_melbourne, btn_brisbane, btn_canberra, btn_darwin, btn_hobart, btn_gold_coast, btn_perth};

        txt_details.setTypeface(Util.getTypeFace(getContext()));
        txt_temperature.setTypeface(Util.getTypeFace(getContext()));
        txt_main_title.setTypeface(Util.getTypeFace(getContext()));

        btn_gold_coast.setBackgroundResource(R.drawable.weather_button_active);
        txt_main_title.setText(getString(R.string.gold_coast));
        requestWeatherDetails(Constants.CITY_GOLD_COAST);

        for (View v : buttons) {
            v.setOnClickListener(this);
            ((TextView) v).setTypeface(Util.getTypeFace(getContext()));
        }

        return view;
    }

    private void requestWeatherDetails(String city) {
        txt_details.setText("...");
        txt_temperature.setText("...");

        String url = String.format("http://rss.weather.com.au/%s", new Object[]{city});
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    readResponse(response);
                } catch (IOException | XmlPullParserException e) {
                    showMessage("failed");
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showMessage("Failed to retrieve weather details");
            }
        });
        AppController.getInstance().addToRequestQueue(request);
    }

    private void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void readResponse(String xml) throws IOException, XmlPullParserException {
        InputStream in = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
        XmlPullParser parser = Xml.newPullParser();

        try {
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();

            readAndFindTag(parser, "rss");
            bind();

        } finally {
            in.close();
        }
    }

    private void bind() {
        forecast_section.removeAllViews();

        for (Item item : _weatherItems) {
            if (item.temperature != null) {
                Object[] args = new Object[]{
                        item.temperature.get("dewPoint"),
                        item.temperature.get("humidity"),
                        item.temperature.get("windSpeed"),
                        item.temperature.get("windGusts"),
                        item.temperature.get("windDirection"),
                        item.temperature.get("pressure"),
                        item.temperature.get("rain"),
                };
                String weather_details = String.format("<b> Dew Point: </b> %sc, <b> Relative Humidity: </b> %s%%," +
                        " <b> Wind Speed: </b> %s km/h, <b> Wind Gusts: </b> %skm/h," +
                        " <b> Wind Direction: </b> %s, <b> Pressure: </b> %shPa," +
                        " <b> Rain Since 9AM: </b> %smm,", args);

                txt_details.setText(Html.fromHtml(weather_details));
                txt_temperature.setText(String.format("%s\u2103", item.temperature.get("temperature")));

            } else if (item.forecast != null && item.forecast.size() > 0) {
                _lastForecastContainer = null;

                for (int i = 0; i < item.forecast.size(); i++) {

                    HashMap<String, String> forecast = item.forecast.get(i);

                    LinearLayout item_container = null;
                    TextView txt_title = null;
                    TextView txt_description = null;
                    TextView txt_min_max = null;
                    ImageView icon = null;

                    if (_lastForecastContainer == null) {
                        _lastForecastContainer = (LinearLayout) _inflater.inflate(R.layout.forecast_item, null, false);

                        item_container = _lastForecastContainer.findViewById(R.id.item_1);
                        txt_title = _lastForecastContainer.findViewById(R.id.txt_title);
                        txt_description = _lastForecastContainer.findViewById(R.id.txt_description);
                        txt_min_max = _lastForecastContainer.findViewById(R.id.txt_min_max);
                        icon = _lastForecastContainer.findViewById(R.id.icon);

                        forecast_section.addView(_lastForecastContainer);

                    } else if (_lastForecastContainer != null) {
                        item_container = _lastForecastContainer.findViewById(R.id.item_2);
                        txt_title = _lastForecastContainer.findViewById(R.id.txt_title_2);
                        txt_description = _lastForecastContainer.findViewById(R.id.txt_description_2);
                        txt_min_max = _lastForecastContainer.findViewById(R.id.txt_min_max_2);
                        icon = _lastForecastContainer.findViewById(R.id.icon_2);

                        _lastForecastContainer = null;
                    }

                    if (item_container != null)
                        item_container.setVisibility(View.VISIBLE);
                    if (txt_title != null)
                        txt_title.setText(forecast.get("day"));
                    if (txt_description != null)
                        txt_description.setText(forecast.get("description"));
                    if (txt_min_max != null)
                        txt_min_max.setText(String.format("%s - %s", new Object[]{forecast.get("min"), forecast.get("max")}));
                    if (icon != null) {
                        Drawable drawable = getContext().getDrawable(getContext().getResources().getIdentifier("weather_" + forecast.get("icon"), "drawable",
                                getContext().getPackageName()));
                        icon.setImageDrawable(drawable);
                    }
                }
            }
        }
    }

    private void readAndFindTag(XmlPullParser parser, String type) throws IOException, XmlPullParserException {
        _weatherItems = new ArrayList<>();

        parser.require(XmlPullParser.START_TAG, ns, type);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();

            // Starts by looking for the item tag
            if (name.equals("item")) {
                Item item = readItem(parser);
                _weatherItems.add(item);
            } else if (name.equals("channel")) {
                readAndFindTag(parser, "channel");
            } else {
                skip(parser);
            }
        }
    }

    private Item readItem(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "item");
        String title = null;
        String description = null;
        String link = null;
        HashMap<String, String> temperature = null;
        ArrayList<HashMap<String, String>> forecast = new ArrayList<>();

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("title")) {
                title = readTitle(parser);
            } else if (name.equals("description")) {
                description = readDescription(parser);
            } else if (name.equals("link")) {
                link = readLink(parser);
            } else if (name.equals("w:current")) {
                temperature = readTemperature(parser);
            } else if (name.equals("w:forecast")) {
                forecast.add(readForecast(parser));
            } else {
                skip(parser);
            }
        }
        return new Item(title, temperature, forecast, link, description);
    }

    private String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "title");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "title");
        return title;
    }

    private String readLink(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "link");
        String link = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "link");
        return link;
    }

    private HashMap<String, String> readTemperature(XmlPullParser parser) throws IOException, XmlPullParserException {
        HashMap<String, String> temperature = new HashMap<>();
        parser.require(XmlPullParser.START_TAG, ns, "w:current");

        String temp = parser.getAttributeValue(null, "temperature");
        String dewPoint = parser.getAttributeValue(null, "dewPoint");
        String humidity = parser.getAttributeValue(null, "humidity");
        String windSpeed = parser.getAttributeValue(null, "windSpeed");
        String windGusts = parser.getAttributeValue(null, "windGusts");
        String windDirection = parser.getAttributeValue(null, "windDirection");
        String pressure = parser.getAttributeValue(null, "pressure");
        String rain = parser.getAttributeValue(null, "rain");

        temperature.put("temperature", temp);
        temperature.put("dewPoint", dewPoint);
        temperature.put("humidity", humidity);
        temperature.put("windSpeed", windSpeed);
        temperature.put("windGusts", windGusts);
        temperature.put("windDirection", windDirection);
        temperature.put("pressure", pressure);
        temperature.put("rain", rain);

        parser.nextTag();

        parser.require(XmlPullParser.END_TAG, ns, "w:current");
        return temperature;
    }

    private HashMap<String, String> readForecast(XmlPullParser parser) throws IOException, XmlPullParserException {
        HashMap<String, String> temperature = new HashMap<>();
        parser.require(XmlPullParser.START_TAG, ns, "w:forecast");

        String day = parser.getAttributeValue(null, "day");
        String description = parser.getAttributeValue(null, "description");
        String min = parser.getAttributeValue(null, "min");
        String max = parser.getAttributeValue(null, "max");
        String icon = parser.getAttributeValue(null, "icon");
        String iconUri = parser.getAttributeValue(null, "iconUri");
        String iconAlt = parser.getAttributeValue(null, "iconAlt");

        temperature.put("day", day);
        temperature.put("description", description);
        temperature.put("min", min);
        temperature.put("max", max);
        temperature.put("icon", icon);
        temperature.put("iconUri", iconUri);
        temperature.put("iconAlt", iconAlt);

        parser.nextTag();

        parser.require(XmlPullParser.END_TAG, ns, "w:forecast");
        return temperature;
    }

    private String readDescription(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "description");
        String summary = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "description");
        return summary;
    }

    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

    public void setFragmentListener(FragmentListener fragmentListener) {
        this.fragmentListener = fragmentListener;
    }

    @Override
    public void onClick(View v) {
        if (v.getTag() != null) {
            int inactive = R.drawable.weather_button;
            int active = R.drawable.weather_button_active;

            for (View v1 : buttons) {
                v1.setBackgroundResource(inactive);
            }

            txt_main_title.setText(((TextView) v).getText());

            v.setBackgroundResource(active);
            requestWeatherDetails(v.getTag().toString());
        }
    }

    private class Item {
        String title;
        HashMap<String, String> temperature;
        ArrayList<HashMap<String, String>> forecast;
        String link;
        String description;

        public Item(String title, HashMap<String, String> temperature, ArrayList<HashMap<String, String>> forecast, String link, String description) {
            this.title = title;
            this.temperature = temperature;
            this.forecast = forecast;
            this.link = link;
            this.description = description;
        }

        @Override
        public String toString() {
            return "Item{" +
                    "title='" + title + '\'' +
                    ", temperature=" + temperature +
                    ", forecast=" + forecast +
                    ", link='" + link + '\'' +
                    ", description='" + description + '\'' +
                    '}';
        }
    }
}
