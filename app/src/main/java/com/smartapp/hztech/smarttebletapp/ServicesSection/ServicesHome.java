package com.smartapp.hztech.smarttebletapp.ServicesSection;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smartapp.hztech.smarttebletapp.R;

public class ServicesHome extends Fragment {

    public ServicesHome() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.services_home, container, false);
    }
    //   @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        setContentView(R.layout.services_home);
//        Button btn = findViewById(R.id.bck);
//        TextView special_offer = (TextView) findViewById(R.id.sp_offer_page);
//        TextView Room_Detail = (TextView) findViewById(R.id.roomdet);
//        TextView navi = (TextView) findViewById(R.id.naviga);
//
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent naviagte = new Intent(ServicesHome.this,HomeActivity.class);
//                startActivity(naviagte);
//            }
//        });
//
//        special_offer.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent naviagte_sp_offer_screen = new Intent(ServicesHome.this,SpecialOffer.class);
//                startActivity(naviagte_sp_offer_screen);
//            }
//        });
//        Room_Detail.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent naviagte_room_details= new Intent(ServicesHome.this,RoomDetails.class);
//                startActivity(naviagte_room_details);
//            }
//        });
//        navi.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent naviagtionPage_room_details= new Intent(ServicesHome.this,NavigationPage.class);
//                startActivity(naviagtionPage_room_details);
//            }
//        });
//    }
}
