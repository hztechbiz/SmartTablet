package com.smartapp.hztech.smarttebletapp.fragments;

import android.arch.lifecycle.HolderFragment;
import android.content.Context;
import android.provider.CallLog;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.smartapp.hztech.smarttebletapp.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by HNH on 4/5/2018.
 */

public class CustomCategoryAdapter extends ArrayAdapter<HashMap<String, String>> {
    private ArrayList<HashMap<String, String>> categoryData;
    private Context context;
    private int resource;
    private View view;
    private Holder holder;
    // private HolderFragment holder;
    private HashMap<String, String> hashMap;

    public CustomCategoryAdapter(Context context, int resource, ArrayList<HashMap<String, String>> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.categoryData = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(resource, parent, false);

        holder = new Holder();
        holder.text_number = (TextView) view.findViewById(R.id.text_calllog_number);
        holder.text_date = (TextView) view.findViewById(R.id.text_calllog_date);
        holder.text_time = (TextView) view.findViewById(R.id.text_calllog_time);

        hashMap = categoryData.get(position);

        holder.text_number.setText(hashMap.get("Logo"));
        holder.text_time.setText(hashMap.get("Description"));
        holder.text_date.setText(hashMap.get("Name"));

        return view;
    }

    public class Holder {
        TextView text_number;
        TextView text_date;
        TextView text_time;
    }
}
