package com.smart.tablet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.smart.tablet.MarketingPartnerSection.MarketPartner;
import com.smart.tablet.R;

public class HomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.home_fragment);
        LinearLayout aaa = findViewById(R.id.send);
//        LinearLayout sercv = findViewById(R.id.services);
        aaa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(com.smart.tablet.HomeActivity.this, MarketPartner.class);
                startActivity(a);
            }
        });
//        sercv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent ser = new Intent(HomeActivity.this, ServicesHome.class);
//                startActivity(ser);
//            }
//        });

    }
}
