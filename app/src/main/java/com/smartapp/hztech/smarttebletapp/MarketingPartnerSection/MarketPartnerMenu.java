package com.smartapp.hztech.smarttebletapp.MarketingPartnerSection;

import android.graphics.Typeface;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.smartapp.hztech.smarttebletapp.R;

public class MarketPartnerMenu extends FragmentActivity {

    private TextView menu;
    private Typeface setLatoRegluar, setLatoBold;
    private Button btnBooking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.market_partner_menu);
        menu = (TextView) findViewById(R.id.menu);
        //btnBooking = (Button) findViewById(R.id.btnBooking);

        setLatoBold = ResourcesCompat.getFont(this, R.font.lato_bold);
        menu.setTypeface(setLatoBold);

        setLatoRegluar = ResourcesCompat.getFont(this, R.font.lato_regular);
        btnBooking.setTypeface(setLatoRegluar);


    }
}
