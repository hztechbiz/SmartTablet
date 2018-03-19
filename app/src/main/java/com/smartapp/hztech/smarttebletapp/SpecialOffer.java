package com.smartapp.hztech.smarttebletapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;

public class SpecialOffer extends Activity {

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_special_offer);

        Button back = (Button)findViewById(R.id.bck);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent naviagte = new Intent(SpecialOffer.this,ServicesHome.class);
                startActivity(naviagte);
            }
        });
        lst = (ListView)findViewById(R.id.spcial_offer_list);
        CustomOfferClassListView customOfferClassListView = new CustomOfferClassListView(this, OfferName, OfferDescription, offerImg);
        lst.setAdapter(customOfferClassListView);
    }
}
