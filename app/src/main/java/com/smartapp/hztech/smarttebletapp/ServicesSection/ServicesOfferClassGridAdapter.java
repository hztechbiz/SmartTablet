package com.smartapp.hztech.smarttebletapp.ServicesSection;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.smartapp.hztech.smarttebletapp.R;
import com.smartapp.hztech.smarttebletapp.entities.Service;

import java.util.List;

/**
 * Created by HNH on 4/7/2018.
 */

public class ServicesOfferClassGridAdapter extends BaseAdapter {
    private List<Service> services;
    private Context context;
    private LayoutInflater inflater;


    public ServicesOfferClassGridAdapter(Context context, List<Service> services) {
        this.context = context;
        this.services = services;
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
        View OfferGridView = convertView;
        if (convertView == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            OfferGridView = inflater.inflate(R.layout.spofferlayout, null);
        }
        Service service = getItem(position);


        String shrt_description = service.getDescription();
        if (shrt_description.length() > 100) {
            shrt_description = service.getDescription().substring(0, 100);
        }
        ((TextView) OfferGridView.findViewById(R.id.offer_title)).setText(service.getTitle());
        ((TextView) OfferGridView.findViewById(R.id.offer_descrip)).setText(shrt_description);


        return OfferGridView;
    }
}
