package com.smartapp.hztech.smarttebletapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.smartapp.hztech.smarttebletapp.R;
import com.smartapp.hztech.smarttebletapp.listeners.FragmentActivityListener;
import com.smartapp.hztech.smarttebletapp.listeners.FragmentListener;

public class WeatherFragment extends Fragment {
    Bundle _bundle;
    private FragmentActivityListener parentListener;
    private FragmentListener fragmentListener;
    private String textWeather;

    public WeatherFragment() {

    }

    public void setParentListener(FragmentActivityListener parentListener) {
        this.parentListener = parentListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.weather_fragment, container, false);
        _bundle = getArguments();

        TextView weather_details = view.findViewById(R.id.weather_detail);

        textWeather = "<b> Dew Point: </b> 8.7c, <b> Relative Humidity: </b> 65%," +
                " <b> Wind Speed: </b> 21.4 km/h, <b> Wind Gusts: </b> 27.8km/h," +
                " <b> Wind Direction: </b> SSW, <b> Pressure: </b> 1026.5hPa," +
                " <b> Rain Since 9AM: </b> 0.2mm," ;
        weather_details.setText(Html.fromHtml(textWeather));


        return view;
    }

    public void setFragmentListener(FragmentListener fragmentListener) {
        this.fragmentListener = fragmentListener;
    }
}
