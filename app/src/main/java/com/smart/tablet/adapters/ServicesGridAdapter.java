package com.smart.tablet.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.smart.tablet.R;
import com.smart.tablet.helpers.ImageHelper;
import com.smart.tablet.helpers.Util;
import com.smart.tablet.models.ServiceModel;

import java.util.List;

public class ServicesGridAdapter extends BaseAdapter {

    private final View.OnClickListener itemClickListener;
    private List<com.smart.tablet.models.ServiceModel> services;
    private Context context;
    private LayoutInflater inflater;

    public ServicesGridAdapter(Context context, List<com.smart.tablet.models.ServiceModel> services, View.OnClickListener itemClickListener) {
        this.context = context;
        this.services = services;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public int getCount() {
        return services.size();
    }

    @Override
    public com.smart.tablet.models.ServiceModel getItem(int position) {
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

        com.smart.tablet.models.ServiceModel service = getItem(position);
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

        txt_title.setTypeface(com.smart.tablet.helpers.Util.getTypeFace(context));
        txt_description.setTypeface(com.smart.tablet.helpers.Util.getTypeFace(context));

        button.setTag(service.getId());
        button.setOnClickListener(itemClickListener);

        if (service.getImage() != null)
            try {
                iv_image.setImageDrawable(com.smart.tablet.helpers.ImageHelper.getRoundedCornerBitmap(context, service.getImage(), 50.0f));
            } catch (Exception e) {
                e.printStackTrace();
            }

        return view;
    }
}
