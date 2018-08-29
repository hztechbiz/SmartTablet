package com.smart.tablet.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.smart.tablet.LocationPopupActivity;
import com.smart.tablet.MainActivity;
import com.smart.tablet.R;
import com.smart.tablet.helpers.Util;
import com.smart.tablet.listeners.AsyncResultBag;
import com.smart.tablet.listeners.FragmentActivityListener;
import com.smart.tablet.models.ActivityAction;
import com.smart.tablet.models.HotelModel;
import com.smart.tablet.tasks.RetrieveHotel;
import com.smart.tablet.tasks.RetrieveSetting;

import java.util.ArrayList;
import java.util.HashMap;

public class WelcomeFragment extends Fragment {
    TextView txtHotelName, txtDescription;
    String _heading, _description, _new_heading, _new_description;
    Button _btn_location;
    private FragmentActivityListener parentListener;
    private MainActivity _activity;

    public WelcomeFragment() {

    }

    public void setParentListener(FragmentActivityListener parentListener) {
        this.parentListener = parentListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.services_home, container, false);
        _activity = (MainActivity) getActivity();

        txtHotelName = view.findViewById(R.id.txt_hotel_name);
        txtDescription = view.findViewById(R.id.txt_description);
        _btn_location = view.findViewById(R.id.btn_location);

        txtHotelName.setTypeface(Util.getBoldTypeFace(getContext()));
        txtDescription.setTypeface(Util.getTypeFace(getContext()));

        bind();

        _btn_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), LocationPopupActivity.class);
                startActivity(intent);
            }
        });

        ArrayList<ActivityAction> actions = new ArrayList<>();

        actions.add(new ActivityAction((R.string.msg_show_sidebar), null));
        actions.add(new ActivityAction((R.string.msg_reset_menu), null));
        actions.add(new ActivityAction((R.string.msg_reset_background), null));
        actions.add(new ActivityAction((R.string.msg_show_top_guest_button), null));
        actions.add(new ActivityAction((R.string.msg_hide_welcome_button), null));

        _activity.takeActions(actions);

        /*
        parentListener.receive(R.string.msg_show_sidebar, null);
        parentListener.receive(R.string.msg_reset_menu, null);
        parentListener.receive(R.string.msg_reset_background, null);
        parentListener.receive(R.string.msg_show_top_guest_button, null);
        */

        return view;
    }

    private void bind() {
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
