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
import com.smart.tablet.entities.Offer;
import com.smart.tablet.entities.Service;
import com.smart.tablet.helpers.AnalyticsHelper;
import com.smart.tablet.helpers.ImageHelper;
import com.smart.tablet.helpers.Util;
import com.smart.tablet.listeners.AsyncResultBag;
import com.smart.tablet.listeners.FragmentActivityListener;
import com.smart.tablet.listeners.FragmentListener;
import com.smart.tablet.tasks.RetrieveSingleOffer;
import com.smart.tablet.tasks.RetrieveSingleService;

import java.io.File;
import java.util.Locale;

public class ServiceSingleOfferFragment extends Fragment implements AsyncResultBag.Success {

    TextView txt_title, txt_description;
    RoundedImageView iv_image;
    LinearLayout mainContent;
    int _offer_id;
    Offer _offer;
    Bundle _bundle;
    private FragmentActivityListener activityListener;
    private FragmentListener fragmentListener;

    public ServiceSingleOfferFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detailed_offer_fragment, container, false);
        _bundle = getArguments();

        _offer_id = 0;

        if (_bundle != null) {
            _offer_id = _bundle.getInt(getString(R.string.param_offer_id));
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
        new RetrieveSingleOffer(getContext(), _offer_id)
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
        final Offer offer = result != null ? (Offer) result : null;

        if (offer != null) {
            _offer = offer;

            new RetrieveSingleService(getContext(), offer.getService_id())
                    .onSuccess(new AsyncResultBag.Success() {
                        @Override
                        public void onSuccess(Object result) {
                            if (result != null) {
                                Service service = (Service) result;

                                AnalyticsHelper.track(getContext(), String.format(Locale.US, "Viewed Offer(%s) in #%d %s", offer.getTitle(), service.getId(), service.getTitle()), service.getId() + "", service.getCategory_id() + "");
                            }
                        }
                    })
                    .execute();

            txt_title.setText(offer.getTitle());
            txt_description.setText(offer.getDescription());

            if (offer.getMedia() != null)
                setupImage(offer.getMedia().getPath());
        }
    }
}
