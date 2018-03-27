package com.smartapp.hztech.smarttebletapp.ServicesSection;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.smartapp.hztech.smarttebletapp.R;
import com.smartapp.hztech.smarttebletapp.fragments.HomeFragment;

public class ServicesHome extends Fragment {
   HomeFragment.OnFragmentUpdate mCallback;
    public ServicesHome() {

    }
    public void onAttach(Activity activity){
        super.onAttach(activity);
        try {
            mCallback = (HomeFragment.OnFragmentUpdate) activity;
        }
        catch (ClassCastException e){
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentUpdate");
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.services_home, container, false);

        TextView room_detail = view.findViewById(R.id.roomdet);
        TextView special_offer = view.findViewById(R.id.sp_offer_page);
        TextView resort_facility = view.findViewById(R.id.naviga);

        room_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RoomDetails navigateToRoom = new RoomDetails();
                mCallback.onUpdateFragment(navigateToRoom);
            }
        });

        special_offer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpecialOffer navigateSpecial_offer = new SpecialOffer();
                mCallback.onUpdateFragment(navigateSpecial_offer);
            }
        });
        resort_facility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationPage navigationResort_facility = new NavigationPage();
                mCallback.onUpdateFragment(navigationResort_facility);
            }
        });


        return view;

    }
    public interface OnFragmentUpdate {
        void onUpdateFragment(Fragment fragment);
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
