package com.smartapp.hztech.smarttebletapp;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartapp.hztech.smarttebletapp.entities.Category;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by HNH on 4/5/2018.
 */

public class GridAdapter extends BaseAdapter {

    private List<Category> categories;
    private Context context;
    private LayoutInflater inflater;

    public GridAdapter(Context context, List<Category> categories) {
        this.context = context;
        this.categories = categories;
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public Category getItem(int position) {
        return categories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View gridView = convertView;

        if (convertView == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            gridView = inflater.inflate(R.layout.category_rows, null);
        }

        Category category = getItem(position);

      //  ((TextView) gridView.findViewById(R.id.text_calllog_number)).setText(category.getName());
         LinearLayout box_categories= (LinearLayout) gridView.findViewById(R.id.bx_category);
        ((TextView) gridView.findViewById(R.id.text_calllog_date)).setText(category.getName());
        ((TextView) gridView.findViewById(R.id.text_calllog_time)).setText(category.getDescription());
        box_categories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return gridView;


    }
}
