package com.smartapp.hztech.smarttebletapp.ServicesSection;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.smartapp.hztech.smarttebletapp.R;

public class NavigationPage extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.services_resort_facility);
        Button back = (Button)findViewById(R.id.bck);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent naviagte = new Intent(NavigationPage.this,ServicesHome.class);
                startActivity(naviagte);
            }
        });


    }
}
