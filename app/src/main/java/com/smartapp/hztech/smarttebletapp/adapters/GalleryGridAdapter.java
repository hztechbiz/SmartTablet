package com.smartapp.hztech.smarttebletapp.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.smartapp.hztech.smarttebletapp.R;

import java.io.File;
import java.util.List;


public class GalleryGridAdapter extends BaseAdapter {
    private View.OnClickListener itemClick;
    private List<String> image;
    private int resource;
    private Context context;
    private LayoutInflater inflater;

    public GalleryGridAdapter(Context context, List<String> image) {
        this.image = image;
        this.context = context;
    }

    @Override
    public int getCount() {
        return image.size();
    }

    @Override
    public Object getItem(int position) {
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
            gridView = inflater.inflate(R.layout.custome_gallery, null);
        }

        File imgBG = new File(image.get(position));

        if (imgBG.exists()) {
            Resources res = context.getResources();
            Bitmap bitmap = BitmapFactory.decodeFile(imgBG.getAbsolutePath());
            BitmapDrawable bd = new BitmapDrawable(res, bitmap);

            final ImageView imgview = gridView.findViewById(R.id.garl1);
            imgview.setImageDrawable(bd);
        }

        return gridView;
    }

}
