package com.smartapp.hztech.smarttebletapp.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.smartapp.hztech.smarttebletapp.R;
import com.smartapp.hztech.smarttebletapp.listeners.FragmentListener;

public class ServicesFragment extends Fragment {
    FragmentListener mCallback;
    FrameLayout fragment_container;

    public ServicesFragment() {

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
        View view = inflater.inflate(R.layout.services_fragment, container, false);

        fragment_container = view.findViewById(R.id.services_fragment_container);

        if (fragment_container != null) {

            WelcomeFragment firstFragment = new WelcomeFragment();

            getChildFragmentManager().beginTransaction()
                    .add(fragment_container.getId(), firstFragment).commit();
        }

        view.findViewById(R.id.serv_home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WelcomeFragment firstFragment = new WelcomeFragment();
                updateFragment(firstFragment);
            }
        });

        view.findViewById(R.id.roomdet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServiceRoomFragment roomFragment = new ServiceRoomFragment();
                updateFragment(roomFragment);
            }
        });

        view.findViewById(R.id.naviga).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServiceResortFacilityFragment serviceResortFacilityFragment = new
                        ServiceResortFacilityFragment();
                updateFragment(serviceResortFacilityFragment);
            }
        });
        view.findViewById(R.id.sp_offer_page).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServiceSpecialOfferFragment serviceSpecialOfferFragment = new
                        ServiceSpecialOfferFragment();
                updateFragment(serviceSpecialOfferFragment);
            }
        });


        return view;
    }

    public void updateFragment(Fragment newFragment) {

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

        transaction.replace(fragment_container.getId(), newFragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }
}
