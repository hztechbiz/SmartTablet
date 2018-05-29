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
import com.smartapp.hztech.smarttebletapp.listeners.FragmentActivityListener;

public class ServiceMenuFragment extends Fragment {
    TextView txtDescription;
    private FragmentActivityListener parentListener;

    public ServiceMenuFragment() {

    }

    public void setParentListener(FragmentActivityListener parentListener) {
        this.parentListener = parentListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.market_partner_location, container, false);

        txtDescription = view.findViewById(R.id.txt_description);

        Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.lato_regular);
        txtDescription.setTypeface(typeface);

        return view;
    }
}
