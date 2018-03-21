package com.smartapp.hztech.smarttebletapp;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class MakeReservation extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_make_reservation);

        //        About us screen
        TextView aboutUsScreen = (TextView) findViewById(R.id.aboutPage);
        aboutUsScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent about_us = new Intent(MakeReservation.this,ReservationAboutUs.class);
                startActivity(about_us);
            }
        });
//        back button navigation
        Button btnBack = findViewById(R.id.bck);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent naviagte = new Intent(MakeReservation.this,HomeActivity.class);
                startActivity(naviagte);
            }
        });
    }
}
