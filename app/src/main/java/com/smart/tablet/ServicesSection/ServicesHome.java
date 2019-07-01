package com.smart.tablet.ServicesSection;

import android.app.Activity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smart.tablet.R;


public class ServicesHome extends Fragment {
   OnFragmentUpdate mCallback;

   public ServicesHome() {

    }

    public void onAttach(Activity activity){
        super.onAttach(activity);
        try {
            mCallback = (OnFragmentUpdate) activity;
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
