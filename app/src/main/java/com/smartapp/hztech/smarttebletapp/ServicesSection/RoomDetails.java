package com.smartapp.hztech.smarttebletapp.ServicesSection;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.smartapp.hztech.smarttebletapp.R;

public class RoomDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.services_room_details);
        Button back = (Button)findViewById(R.id.bck);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent naviagte = new Intent(RoomDetails.this,ServicesHome.class);
                startActivity(naviagte);
            }
        });
    }
}
