package com.smartapp.hztech.smarttebletapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.smartapp.hztech.smarttebletapp.R;
import com.smartapp.hztech.smarttebletapp.entities.Category;

import java.util.List;

/**
 * Created by HNH on 4/21/2018.
 */

public class GalleryGridAdapter extends BaseAdapter {
    private int icons[];
    private Context context;
    private LayoutInflater inflater;

    public GalleryGridAdapter(Context context, int icons[]){
        this.icons = icons;
        this.context = context;
    }
    @Override
    public int getCount() {
        return icons.length;
    }

    @Override
    public Object getItem(int position) {
        return icons[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View gridView = convertView;
        if (convertView == null){
            inflater = (LayoutInflater)  context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            gridView =  inflater.inflate(R.layout.custome_gallery, null);
        }
        ImageView imgview = (ImageView) gridView.findViewById(R.id.garl1);
        imgview.setImageResource(icons[position]);
        return gridView;
    }
}
