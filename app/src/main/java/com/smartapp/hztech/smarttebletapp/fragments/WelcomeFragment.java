package com.smartapp.hztech.smarttebletapp.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.smartapp.hztech.smarttebletapp.R;
import com.smartapp.hztech.smarttebletapp.listeners.AsyncResultBag;
import com.smartapp.hztech.smarttebletapp.listeners.FragmentActivityListener;
import com.smartapp.hztech.smarttebletapp.models.HotelModel;
import com.smartapp.hztech.smarttebletapp.tasks.RetrieveHotel;
import com.smartapp.hztech.smarttebletapp.tasks.RetrieveSetting;

import java.util.HashMap;

public class WelcomeFragment extends Fragment {
    TextView txtHotelName, txtDescription;
    String _heading, _description, _new_heading, _new_description;
    private FragmentActivityListener parentListener;

    public WelcomeFragment() {

    }

    public void setParentListener(FragmentActivityListener parentListener) {
        this.parentListener = parentListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.services_home, container, false);

        txtHotelName = view.findViewById(R.id.txt_hotel_name);
        txtDescription = view.findViewById(R.id.txt_description);

        Typeface LatoBold = ResourcesCompat.getFont(getContext(), R.font.lato_bold);
        txtHotelName.setTypeface(LatoBold);
        Typeface LatoRegular = ResourcesCompat.getFont(getContext(), R.font.lato_regular);
        txtDescription.setTypeface(LatoRegular);

        new RetrieveHotel(getContext())
                .onSuccess(new AsyncResultBag.Success() {
                    @Override
                    public void onSuccess(Object result) {
                        HotelModel hotel = (HotelModel) result;

                        _heading = getString(R.string.hotel_welcome_text, hotel.getName());
                        _description = getString(R.string.hotel_welcome_text, hotel.getName());

                        setup();
                    }
                })
                .execute();

        new RetrieveSetting(getContext(), "welcome_heading", "welcome_description")
                .onSuccess(new AsyncResultBag.Success() {
                    @Override
                    public void onSuccess(Object result) {
                        HashMap<String, String> values = result != null ? (HashMap<String, String>) result : null;

                        if (values != null) {
                            _new_heading = values.containsKey("welcome_heading") ? values.get("welcome_heading") : null;
                            _new_description = values.containsKey("welcome_description") ? values.get("welcome_description") : null;
                        }

                        setup();
                    }
                })
                .execute();

        parentListener.receive(R.string.msg_show_sidebar, null);
        parentListener.receive(R.string.msg_reset_menu, null);
        parentListener.receive(R.string.msg_reset_background, null);

        return view;
    }

    private void setup() {
        if (_new_heading != null)
            txtHotelName.setText(_new_heading);
        else
            txtHotelName.setText(_heading);

        if (_new_description != null)
            txtDescription.setText(_new_description);
        else
            txtDescription.setText(_description);
    }
}
