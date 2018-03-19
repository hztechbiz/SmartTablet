package com.smartapp.hztech.smarttebletapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class ServicesHome extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_services_home);
        Button btn = findViewById(R.id.bck);
        TextView special_offer = (TextView) findViewById(R.id.sp_offer_page);
        TextView Room_Detail = (TextView) findViewById(R.id.roomdet);
        TextView navi = (TextView) findViewById(R.id.naviga);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent naviagte = new Intent(ServicesHome.this,HomeActivity.class);
                startActivity(naviagte);
            }
        });

        special_offer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent naviagte_sp_offer_screen = new Intent(ServicesHome.this,SpecialOffer.class);
                startActivity(naviagte_sp_offer_screen);
            }
        });
        Room_Detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent naviagte_room_details= new Intent(ServicesHome.this,RoomDetails.class);
                startActivity(naviagte_room_details);
            }
        });
        navi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent naviagtionPage_room_details= new Intent(ServicesHome.this,NavigationPage.class);
                startActivity(naviagtionPage_room_details);
            }
        });
    }
}
