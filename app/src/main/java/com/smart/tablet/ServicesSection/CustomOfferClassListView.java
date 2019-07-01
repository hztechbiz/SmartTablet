package com.smart.tablet.ServicesSection;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.smart.tablet.R;

/**
 * Created by HNH on 3/16/2018.
 */

public class CustomOfferClassListView extends ArrayAdapter<String> {
    private String[] OfferName;
    private String[] OfferDescription;
    private Integer[] offerImg;
    private Activity context;
    public CustomOfferClassListView(Activity context, String[] OfferName, String[] OfferDescription, Integer[] offerImg) {
        super(context, R.layout.service_item, OfferName);
        this.context=context;
        this.OfferName=OfferName;
        this.OfferDescription=OfferDescription;
        this.offerImg=offerImg;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View r= convertView;
        ViewHolder viewHolder =null;
        if (r==null){
            LayoutInflater layoutInflater = context.getLayoutInflater();
            r=layoutInflater.inflate(R.layout.service_item,null,true);
            viewHolder = new ViewHolder(r);
            r.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) r.getTag();
        }

        viewHolder.image.setImageResource(offerImg[position]);
        viewHolder.description.setText(OfferDescription[position]);
        viewHolder.heading.setText(OfferName[position]);
        return r;
    }
    class ViewHolder{
        TextView heading;
        TextView description;
        ImageView image;
        Button button;
        ViewHolder(View v){
            heading = v.findViewById(R.id.offer_title);
            description = v.findViewById(R.id.offer_descrip);
            image = v.findViewById(R.id.offer_img);
            button = v.findViewById(R.id.btn_more);
        }
    }
}
