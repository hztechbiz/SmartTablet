package com.smart.tablet.fragments;

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
import com.smart.tablet.R;
import com.smart.tablet.entities.Arrival;
import com.smart.tablet.entities.Service;
import com.smart.tablet.helpers.AnalyticsHelper;
import com.smart.tablet.helpers.ImageHelper;
import com.smart.tablet.helpers.Util;
import com.smart.tablet.listeners.AsyncResultBag;
import com.smart.tablet.listeners.FragmentActivityListener;
import com.smart.tablet.listeners.FragmentListener;
import com.smart.tablet.tasks.RetrieveSingleArrival;
import com.smart.tablet.tasks.RetrieveSingleService;

import java.io.File;
import java.util.Locale;

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
        final Arrival arrival = result != null ? (Arrival) result : null;

        if (arrival != null) {
            _arrival = arrival;

            new RetrieveSingleService(getContext(), arrival.getService_id())
                    .onSuccess(new AsyncResultBag.Success() {
                        @Override
                        public void onSuccess(Object result) {
                            if (result != null) {
                                Service service = (Service) result;

                                AnalyticsHelper.track(getContext(), String.format(Locale.US, "Viewed News Arrival(%s) in #%d %s", arrival.getTitle(), service.getId(), service.getTitle()), String.format(Locale.US, "Service #%d", service.getId()));
                            }
                        }
                    })
                    .execute();

            txt_title.setText(arrival.getTitle());
            txt_description.setText(arrival.getDescription());

            if (arrival.getMedia() != null)
                setupImage(arrival.getMedia().getPath());
        }
    }
}
