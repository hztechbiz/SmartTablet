package com.smart.tablet.MarketingPartnerSection;

import android.graphics.Typeface;
import androidx.fragment.app.FragmentActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.smart.tablet.R;

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
