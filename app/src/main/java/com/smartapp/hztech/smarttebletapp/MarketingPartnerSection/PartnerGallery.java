package com.smartapp.hztech.smarttebletapp.MarketingPartnerSection;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.smartapp.hztech.smarttebletapp.R;
import com.smartapp.hztech.smarttebletapp.adapters.GalleryGridAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PartnerGallery extends FragmentActivity {

    public PartnerGallery() {

    }

    Button popUp;
    GridView gridView;
    List<Integer> ItemsList;
    int selectedItem;
    int[] gallerThumb = {R.drawable.gallery1,
            R.drawable.gallery2,
            R.drawable.gallery3,
            R.drawable.gallery4,
            R.drawable.gallery5,
            R.drawable.gallery6};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.market_partner_gallery);
        final LinearLayout lnr = (LinearLayout) findViewById(R.id.mainBlur);
        popUp = (Button) findViewById(R.id.popUpcheck);
        gridView = (GridView) findViewById(R.id.list_gallery);

        // ItemsList = new ArrayList<Integer>(Arrays.asList(gallerThumb));

        GalleryGridAdapter adapter = new GalleryGridAdapter(this, gallerThumb);
        gridView.setAdapter(adapter);


//        Grid View Listener ID Get
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = (Integer) parent.getItemAtPosition(position);

                lnr.getBackground().setAlpha(100);
                Intent i = new Intent(getApplicationContext(), PopUpActivity.class);
                i.putExtra("IMAGE", selectedItem);
                startActivity(i);



            }
        });

//        Navigate on PopUp Activity On Button

//        popUp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(getApplicationContext(), PopUpActivity.class);
//                startActivity(i);
//            }
//        });

    }
}
