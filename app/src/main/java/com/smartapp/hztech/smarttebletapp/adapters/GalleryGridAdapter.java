package com.smartapp.hztech.smarttebletapp.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.smartapp.hztech.smarttebletapp.R;

import java.io.File;
import java.util.List;


public class GalleryGridAdapter extends BaseAdapter {
    private View.OnClickListener itemClick;
    private List<Drawable> image;
    private int resource;
    private Context context;
    private LayoutInflater inflater;

    public GalleryGridAdapter(Context context, List<Drawable> image) {
        this.image = image;
        this.context = context;
    }

    @Override
    public int getCount() {
        return image.size();
    }

    @Override
    public Drawable getItem(int position) {
        return image.get(position);
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
            gridView = inflater.inflate(R.layout.gallery_item, null);
        }

        RoundedImageView imageView = gridView.findViewById(R.id.image);
        imageView.setImageDrawable(getItem(position));

        return gridView;
    }

}
