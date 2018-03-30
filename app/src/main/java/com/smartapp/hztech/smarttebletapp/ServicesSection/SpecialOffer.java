package com.smartapp.hztech.smarttebletapp.ServicesSection;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;

import com.smartapp.hztech.smarttebletapp.R;
import com.smartapp.hztech.smarttebletapp.fragments.HomeFragment;

public class SpecialOffer extends Fragment {

//ServicesHome.OnFragmentUpdate mCallBack;
    public SpecialOffer(){

    }
//    public void onAttach(Activity activity){
//        super.onAttach(activity);
//        try {
//            mCallBack = (ServicesHome.OnFragmentUpdate) activity;
//        }
//        catch (ClassCastException e){
//            throw new ClassCastException(activity.toString() + "must implement OnFragmentUpdate");
//        }
//    }
    ListView lst;
    String[] OfferName = {
            "Special Offers",
            "Special Offers",
            "Special Offers",
            "Special Offers",
            "Special Offers",
            "Special Offers"};
    String[] OfferDescription = {
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi dapibus tincidunt.",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi dapibus tincidunt.",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi dapibus tincidunt.",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi dapibus tincidunt.",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi dapibus tincidunt.",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi dapibus tincidunt."};
    Integer[] offerImg ={
            R.drawable.offerimg1,
            R.drawable.offerimg2,
            R.drawable.offerimg3,
            R.drawable.offerimg4,
            R.drawable.offerimg2,
            R.drawable.offerimg1};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.services_special_offer, container, false);

//
        lst = view.findViewById(R.id.spcial_offer_list);
//        CustomOfferClassListView customOfferClassListView = new
//                CustomOfferClassListView();
//
//        lst.setAdapter(customOfferClassListView);
        return view;
    }


//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//
//        lst = (ListView)findViewById(R.id.spcial_offer_list);
//        CustomOfferClassListView customOfferClassListView = new CustomOfferClassListView(this, OfferName, OfferDescription, offerImg);
//        lst.setAdapter(customOfferClassListView);
//    }
}
