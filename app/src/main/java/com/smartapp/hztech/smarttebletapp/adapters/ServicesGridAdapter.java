package com.smartapp.hztech.smarttebletapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.smartapp.hztech.smarttebletapp.R;
import com.smartapp.hztech.smarttebletapp.entities.Service;

import java.util.List;

public class ServicesGridAdapter extends BaseAdapter {

    private final View.OnClickListener itemClickListener;
    private List<Service> services;
    private Context context;
    private LayoutInflater inflater;

    public ServicesGridAdapter(Context context, List<Service> services, View.OnClickListener itemClickListener) {
        this.context = context;
        this.services = services;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public int getCount() {
        return services.size();
    }

    @Override
    public Service getItem(int position) {
        return services.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (convertView == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.spofferlayout, null);
        }

        Service service = getItem(position);
        String description = service.getDescription();

        if (description.length() > 100) {
            description = service.getDescription().substring(0, 100);
        }

        Button button = view.findViewById(R.id.btn_more);

        ((TextView) view.findViewById(R.id.offer_title)).setText(service.getTitle());
        ((TextView) view.findViewById(R.id.offer_descrip)).setText(description);

        button.setTag(service.getId());
        button.setOnClickListener(itemClickListener);

        return view;
    }
}
