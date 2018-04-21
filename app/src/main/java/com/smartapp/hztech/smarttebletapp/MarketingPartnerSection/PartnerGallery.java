package com.smartapp.hztech.smarttebletapp.MarketingPartnerSection;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.smartapp.hztech.smarttebletapp.R;
import com.smartapp.hztech.smarttebletapp.adapters.GalleryGridAdapter;

public class PartnerGallery extends Fragment {

    public PartnerGallery() {

    }

    GridView gridView;
    int gallerThumb[] = {R.drawable.gallery1,
            R.drawable.gallery2,
            R.drawable.gallery3,
            R.drawable.gallery4,
            R.drawable.gallery5,
            R.drawable.gallery6};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.market_partner_gallery, container, false);


        gridView = view.findViewById(R.id.list_gallery);

        GalleryGridAdapter adapter = new
                GalleryGridAdapter(getContext(), gallerThumb);
        gridView.setAdapter(adapter);
        return view;
    }
}
