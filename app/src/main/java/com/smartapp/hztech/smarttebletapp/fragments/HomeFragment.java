package com.smartapp.hztech.smarttebletapp.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smartapp.hztech.smarttebletapp.R;
import com.smartapp.hztech.smarttebletapp.listeners.FragmentListener;

public class HomeFragment extends Fragment {
    FragmentListener mCallback;

    public HomeFragment() {

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
        View view = inflater.inflate(R.layout.home_fragment, container, false);

        return view;
    }
}
