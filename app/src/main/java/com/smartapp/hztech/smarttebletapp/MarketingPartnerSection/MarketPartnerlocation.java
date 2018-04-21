package com.smartapp.hztech.smarttebletapp.MarketingPartnerSection;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smartapp.hztech.smarttebletapp.R;

public class MarketPartnerlocation extends Fragment {
    public MarketPartnerlocation() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.market_partner_location, container, false);
        return view;
    }
}
