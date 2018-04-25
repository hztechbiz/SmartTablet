package com.smartapp.hztech.smarttebletapp.MarketingPartnerSection;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;

import com.smartapp.hztech.smarttebletapp.R;
import com.smartapp.hztech.smarttebletapp.adapters.GalleryGridAdapter;

public class PartnerGallery extends FragmentActivity {

    public PartnerGallery() {

    }

    Button popUp;
    GridView gridView;
    int gallerThumb[] = {R.drawable.gallery1,
            R.drawable.gallery2,
            R.drawable.gallery3,
            R.drawable.gallery4,
            R.drawable.gallery5,
            R.drawable.gallery6};

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.market_partner_gallery);

        popUp = (Button) findViewById(R.id.popUpcheck);
        gridView = (GridView) findViewById(R.id.list_gallery);

        GalleryGridAdapter adapter = new GalleryGridAdapter (this, gallerThumb);
        gridView.setAdapter(adapter);


        popUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), PopUpActivity.class);
                startActivity(i);
            }
        });

    }
}
