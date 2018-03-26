package com.smartapp.hztech.smarttebletapp.ReservationSection;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.smartapp.hztech.smarttebletapp.R;

public class ReservationAboutUs extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.reservation_about_us);


//        Back Button Event
        Button btnBack = findViewById(R.id.bck);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent naviagte = new Intent(ReservationAboutUs.this,MakeReservation.class);
                startActivity(naviagte);
            }
        });
    }
}
