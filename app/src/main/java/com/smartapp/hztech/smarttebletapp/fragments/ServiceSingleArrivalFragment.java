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
import com.smartapp.hztech.smarttebletapp.helpers.ImageHelper;
import com.smartapp.hztech.smarttebletapp.helpers.Util;
import com.smartapp.hztech.smarttebletapp.listeners.AsyncResultBag;
import com.smartapp.hztech.smarttebletapp.listeners.FragmentActivityListener;
import com.smartapp.hztech.smarttebletapp.listeners.FragmentListener;
import com.smartapp.hztech.smarttebletapp.tasks.RetrieveSingleArrival;

import java.io.File;

public class ServiceSingleArrivalFragment extends Fragment implements AsyncResultBag.Success {

    TextView txt_title, txt_description;
    RoundedImageView iv_image;
    LinearLayout mainContent;
    int _arrival_id;
    Arrival _arrival;
    Bundle _bundle;
    private FragmentActivityListener activityListener;
    private FragmentListener fragmentListener;

    public ServiceSingleArrivalFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detailed_offer_fragment, container, false);
        _bundle = getArguments();

        _arrival_id = 0;

        if (_bundle != null) {
            _arrival_id = _bundle.getInt(getString(R.string.param_offer_id));
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
        new RetrieveSingleArrival(getContext(), _arrival_id)
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
        Arrival arrival = result != null ? (Arrival) result : null;

        if (arrival != null) {
            _arrival = arrival;

            txt_title.setText(arrival.getTitle());
            txt_description.setText(arrival.getDescription());

            if (arrival.getMedia() != null)
                setupImage(arrival.getMedia().getPath());
        }
    }
}
