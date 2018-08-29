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
import com.smart.tablet.models.OfferModel;

import java.util.List;

public class OffersGridAdapter extends BaseAdapter {

    private final View.OnClickListener itemClickListener;
    private List<OfferModel> offers;
    private Context context;
    private LayoutInflater inflater;

    public OffersGridAdapter(Context context, List<OfferModel> offers, View.OnClickListener itemClickListener) {
        this.context = context;
        this.offers = offers;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public int getCount() {
        return offers.size();
    }

    @Override
    public OfferModel getItem(int position) {
        return offers.get(position);
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
            view = inflater.inflate(R.layout.offer_item, null);
        }

        OfferModel offer = getItem(position);
        String description = offer.getDescription();

        if (description.length() > 100) {
            description = offer.getDescription().substring(0, 100);
        }

        Button button = view.findViewById(R.id.btn_more);
        final ImageView iv_image = view.findViewById(R.id.offer_img);
        TextView txt_title = view.findViewById(R.id.offer_title);
        TextView txt_description = view.findViewById(R.id.offer_descrip);

        txt_title.setText(offer.getTitle());
        txt_description.setText(description);

        txt_title.setTypeface(Util.getTypeFace(context));
        txt_description.setTypeface(Util.getTypeFace(context));
        button.setTypeface(Util.getTypeFace(context));

        button.setTag(offer.getId());
        button.setOnClickListener(itemClickListener);

        if (offer.getImage() != null)
            try {
                iv_image.setImageDrawable(ImageHelper.getRoundedCornerBitmap(context, offer.getImage(), 50.0f));
            } catch (Exception e) {
                e.printStackTrace();
            }

        return view;
    }
}
