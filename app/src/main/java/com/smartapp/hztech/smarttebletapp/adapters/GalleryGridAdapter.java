package com.smartapp.hztech.smarttebletapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.renderscript.Sampler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.smartapp.hztech.smarttebletapp.MarketingPartnerSection.PopUpActivity;
import com.smartapp.hztech.smarttebletapp.R;
import com.smartapp.hztech.smarttebletapp.entities.Category;

import java.util.List;


public class GalleryGridAdapter extends BaseAdapter {
    private View.OnClickListener itemClick;
    private int[] image;
    private int resource;
    private Context context;
    private LayoutInflater inflater;

    public GalleryGridAdapter(Context context, int[] image) {

        this.image = image;
        this.context = context;
    }

    @Override
    public int getCount() {
        return image.length;
    }

    @Override
    public Object getItem(int position) {
        return image[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View gridView = convertView;
        if (convertView == null) {
            inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            gridView = inflater.inflate(R.layout.custome_gallery, null);
        }
        final ImageView imgview = (ImageView) gridView.findViewById(R.id.garl1);
        imgview.setImageResource(image[position]);

        return gridView;
    }

}
