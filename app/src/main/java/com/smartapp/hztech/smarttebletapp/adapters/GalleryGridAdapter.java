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

        //inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
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

//        ViewHolder holder = null;
//        if (convertView == null){
//            holder = new ViewHolder();
//            convertView = inflater.inflate(resource, null);
//            holder.imageView = (ImageView) convertView.findViewById(R.id.garl1);
//            convertView.setTag(holder);
//        }
//        else {
//            holder = (ViewHolder)convertView.getTag();
//        }
//        holder.imageView.setImageResource(image[position]);
//        holder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);


        Object object = getItem(position);

        View gridView = convertView;
        if (convertView == null) {
            inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            gridView = inflater.inflate(R.layout.custome_gallery, null);
        }
        final ImageView imgview = (ImageView) gridView.findViewById(R.id.garl1);
        imgview.setImageResource(image[position]);


        // imgview.setTag(R.string.tag_value, object.getClass());
        // imgview.setOnClickListener(itemClick);


//        imgview.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                v.getId();
//                Intent i = new Intent(context, PopUpActivity.class);
//                context.startActivity(i);
//            }
//        });
        return gridView;
    }

    class HolderView {

    }

}
