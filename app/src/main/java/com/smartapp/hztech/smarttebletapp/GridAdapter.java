package com.smartapp.hztech.smarttebletapp;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

/**
 * Created by HNH on 4/5/2018.
 */

public class GridAdapter extends BaseAdapter {

    private int icons[];
    private String img[];
    private Layout lnr[];
    private String nam[];
    private String des[];
    private Context context;
    private LayoutInflater inflater;

    public GridAdapter(Context context,Layout lnr[] ){
        this.context= context;
        this.lnr = lnr;

    }
    @Override
    public int getCount() {
        return lnr.length;
    }

    @Override
    public Object getItem(int position) {
        return lnr[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View gridView = convertView;
        if (convertView == null){
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            gridView = inflater.inflate(R.layout.category_rows, null);
        }
        LinearLayout linre = (LinearLayout) gridView.findViewById(R.id.send);
      //  linre.setLayoutMode(lnr[position]);
        return gridView;
    }
}
