package com.smartapp.hztech.smarttebletapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartapp.hztech.smarttebletapp.R;
import com.smartapp.hztech.smarttebletapp.helpers.ImageHelper;
import com.smartapp.hztech.smarttebletapp.helpers.Util;
import com.smartapp.hztech.smarttebletapp.models.ServiceModel;

import java.util.List;

public class ServicesGridAdapter extends BaseAdapter {

    private final View.OnClickListener itemClickListener;
    private List<ServiceModel> services;
    private Context context;
    private LayoutInflater inflater;

    public ServicesGridAdapter(Context context, List<ServiceModel> services, View.OnClickListener itemClickListener) {
        this.context = context;
        this.services = services;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public int getCount() {
        return services.size();
    }

    @Override
    public ServiceModel getItem(int position) {
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
            view = inflater.inflate(R.layout.service_item, null);
        }

        ServiceModel service = getItem(position);
        String description = service.getDescription();

        if (description.length() > 100) {
            description = service.getDescription().substring(0, 100);
        }

        Button button = view.findViewById(R.id.btn_more);
        final ImageView iv_image = view.findViewById(R.id.offer_img);
        TextView txt_title = view.findViewById(R.id.offer_title);
        TextView txt_description = view.findViewById(R.id.offer_descrip);

        txt_title.setText(service.getTitle().toUpperCase());
        txt_description.setText(description);

        txt_title.setTypeface(Util.getTypeFace(context));
        txt_description.setTypeface(Util.getTypeFace(context));

        button.setTag(service.getId());
        button.setOnClickListener(itemClickListener);

        if (service.getImage() != null)
            try {
                iv_image.setImageDrawable(ImageHelper.getRoundedCornerBitmap(context, service.getImage(), 50.0f));
            } catch (Exception e) {
                e.printStackTrace();
            }

        return view;
    }
}
