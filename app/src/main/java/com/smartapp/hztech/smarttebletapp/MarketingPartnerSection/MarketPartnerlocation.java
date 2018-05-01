package com.smartapp.hztech.smarttebletapp.MarketingPartnerSection;

import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.smartapp.hztech.smarttebletapp.R;

public class MarketPartnerlocation extends FragmentActivity {
    public MarketPartnerlocation() {

    }

    private Typeface setLatoRegluar, setLatoBold;
    private TextView desTxt, descrip, addressTxt, address, emailTxt, email, phoneTxt, phoneNumbers;
    private Button btnReserv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.market_partner_location);

        desTxt = (TextView) findViewById(R.id.desTxt);
        descrip = (TextView) findViewById(R.id.descrip);
        addressTxt = (TextView) findViewById(R.id.addressTxt);
        address = (TextView) findViewById(R.id.address);
        emailTxt = (TextView) findViewById(R.id.emailTxt);
        email = (TextView) findViewById(R.id.email);
        phoneTxt = (TextView) findViewById(R.id.phoneTxt);
        phoneNumbers = (TextView) findViewById(R.id.phoneNumbers);

        btnReserv = (Button) findViewById(R.id.btnReserv);

        setLatoRegluar = ResourcesCompat.getFont(this, R.font.lato_regular);
        descrip.setTypeface(setLatoRegluar);
        address.setTypeface(setLatoRegluar);
        email.setTypeface(setLatoRegluar);
        phoneNumbers.setTypeface(setLatoRegluar);
        btnReserv.setTypeface(setLatoRegluar);

        setLatoBold = ResourcesCompat.getFont(this, R.font.lato_bold);
        desTxt.setTypeface(setLatoBold);
        addressTxt.setTypeface(setLatoBold);
        emailTxt.setTypeface(setLatoBold);
        phoneTxt.setTypeface(setLatoBold);


    }
}
