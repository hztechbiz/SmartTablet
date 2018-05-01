package com.smartapp.hztech.smarttebletapp.MarketingPartnerSection;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.smartapp.hztech.smarttebletapp.R;

public class MarketPartnerAboutUs extends FragmentActivity {

    public MarketPartnerAboutUs() {

    }

    private TextView AboutParaText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.market_partner_about_us);
        AboutParaText = (TextView) findViewById(R.id.aboutParaTxt);
        Typeface AboutTextFont = ResourcesCompat.getFont(this, R.font.lato_regular);
        AboutParaText.setTypeface(AboutTextFont);

        // View view = inflater.inflate(R.layout.market_partner_about_us, container, false);
        // Bundle bundle = getArguments();


    }
}
