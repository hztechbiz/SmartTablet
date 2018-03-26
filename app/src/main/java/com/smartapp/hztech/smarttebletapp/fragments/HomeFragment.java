package com.smartapp.hztech.smarttebletapp.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.smartapp.hztech.smarttebletapp.R;
import com.smartapp.hztech.smarttebletapp.ServicesSection.ServicesHome;

public class HomeFragment extends Fragment {
    OnFragmentUpdate mCallback;

    public HomeFragment() {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (OnFragmentUpdate) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentUpdate");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);

        LinearLayout serv = view.findViewById(R.id.services);
        serv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//
                ServicesHome fragmentt = new ServicesHome();
                mCallback.onUpdateFragment(fragmentt);
            }
        });

        return view;
    }

    public interface OnFragmentUpdate {
        void onUpdateFragment(Fragment fragment);
    }
}
