package com.smartapp.hztech.smarttebletapp.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.smartapp.hztech.smarttebletapp.R;
import com.smartapp.hztech.smarttebletapp.listeners.AsyncResultBag;
import com.smartapp.hztech.smarttebletapp.listeners.FragmentListener;
import com.smartapp.hztech.smarttebletapp.models.HotelModel;
import com.smartapp.hztech.smarttebletapp.tasks.RetrieveHotel;

public class WelcomeFragment extends Fragment {
    FragmentListener mCallback;
    TextView txtHotelName, txtDescription;

    public WelcomeFragment() {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (FragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentUpdate");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.services_home, container, false);

        txtHotelName = view.findViewById(R.id.txt_hotel_name);
        txtDescription = view.findViewById(R.id.txt_description);

        new RetrieveHotel(getContext())
                .onSuccess(new AsyncResultBag.Success() {
                    @Override
                    public void onSuccess(Object result) {
                        HotelModel hotel = (HotelModel) result;

                        String heading = getString(R.string.hotel_welcome_text, hotel.getName());

                        txtHotelName.setText(heading);
                        txtDescription.setText(heading);
                    }
                })
                .execute();

        return view;
    }
}
