package com.smartapp.hztech.smarttebletapp.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.smartapp.hztech.smarttebletapp.R;
import com.smartapp.hztech.smarttebletapp.entities.Arrival;
import com.smartapp.hztech.smarttebletapp.entities.Sale;
import com.smartapp.hztech.smarttebletapp.helpers.ImageHelper;
import com.smartapp.hztech.smarttebletapp.helpers.Util;
import com.smartapp.hztech.smarttebletapp.listeners.AsyncResultBag;
import com.smartapp.hztech.smarttebletapp.listeners.FragmentActivityListener;
import com.smartapp.hztech.smarttebletapp.listeners.FragmentListener;
import com.smartapp.hztech.smarttebletapp.tasks.RetrieveSingleArrival;
import com.smartapp.hztech.smarttebletapp.tasks.RetrieveSingleSale;

import java.io.File;

public class ServiceSingleSaleFragment extends Fragment implements AsyncResultBag.Success {

    TextView txt_title, txt_description;
    RoundedImageView iv_image;
    LinearLayout mainContent;
    int _sale_id;
    Sale _sale;
    Bundle _bundle;
    private FragmentActivityListener activityListener;
    private FragmentListener fragmentListener;

    public ServiceSingleSaleFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detailed_offer_fragment, container, false);
        _bundle = getArguments();

        _sale_id = 0;

        if (_bundle != null) {
            _sale_id = _bundle.getInt(getString(R.string.param_offer_id));
        }

        txt_title = view.findViewById(R.id.txt_title);
        txt_description = view.findViewById(R.id.txt_description);
        iv_image = view.findViewById(R.id.imageView);
        mainContent = view.findViewById(R.id.mainContent);

        txt_description.setTypeface(Util.getTypeFace(getContext()));
        txt_title.setTypeface(Util.getTypeFace(getContext()));

        bind();

        return view;
    }

    private void bind() {
        new RetrieveSingleSale(getContext(), _sale_id)
                .onSuccess(this)
                .execute();
    }

    private void setupImage(String filePath) {
        if (filePath != null) {
            File imgBG = new File(filePath);

            if (imgBG.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(imgBG.getAbsolutePath());
                bitmap = ImageHelper.getResizedBitmap(bitmap, 500);

                iv_image.setImageBitmap(bitmap);
            }
        }
    }

    public void setActivityListener(FragmentActivityListener activityListener) {
        this.activityListener = activityListener;
    }

    public void setFragmentListener(FragmentListener fragmentListener) {
        this.fragmentListener = fragmentListener;
    }

    @Override
    public void onSuccess(Object result) {
        Sale sale = result != null ? (Sale) result : null;

        if (sale != null) {
            _sale = sale;

            txt_title.setText(sale.getTitle());
            txt_description.setText(sale.getDescription());

            if (sale.getMedia() != null)
                setupImage(sale.getMedia().getPath());
        }
    }
}
