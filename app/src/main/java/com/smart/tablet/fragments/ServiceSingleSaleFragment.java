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
import com.smart.tablet.entities.Sale;
import com.smart.tablet.entities.Service;
import com.smart.tablet.helpers.AnalyticsHelper;
import com.smart.tablet.helpers.ImageHelper;
import com.smart.tablet.helpers.Util;
import com.smart.tablet.listeners.AsyncResultBag;
import com.smart.tablet.listeners.FragmentActivityListener;
import com.smart.tablet.listeners.FragmentListener;
import com.smart.tablet.tasks.RetrieveSingleArrival;
import com.smart.tablet.tasks.RetrieveSingleSale;
import com.smart.tablet.tasks.RetrieveSingleService;

import java.io.File;
import java.util.Locale;

public class ServiceSingleSaleFragment extends Fragment implements com.smart.tablet.listeners.AsyncResultBag.Success {

    TextView txt_title, txt_description;
    RoundedImageView iv_image;
    LinearLayout mainContent;
    int _sale_id;
    com.smart.tablet.entities.Sale _sale;
    Bundle _bundle;
    private com.smart.tablet.listeners.FragmentActivityListener activityListener;
    private com.smart.tablet.listeners.FragmentListener fragmentListener;

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

        txt_description.setTypeface(com.smart.tablet.helpers.Util.getTypeFace(getContext()));
        txt_title.setTypeface(com.smart.tablet.helpers.Util.getTypeFace(getContext()));

        bind();

        return view;
    }

    private void bind() {
        new com.smart.tablet.tasks.RetrieveSingleSale(getContext(), _sale_id)
                .onSuccess(this)
                .execute();
    }

    private void setupImage(String filePath) {
        if (filePath != null) {
            try {
                File imgBG = new File(filePath);

                if (imgBG.exists()) {
                    Bitmap bitmap = BitmapFactory.decodeFile(imgBG.getAbsolutePath());
                    bitmap = com.smart.tablet.helpers.ImageHelper.getResizedBitmap(bitmap, 500);

                    iv_image.setImageBitmap(bitmap);
                }
            } catch (Exception | OutOfMemoryError e) {
                e.printStackTrace();
            }
        }
    }

    public void setActivityListener(com.smart.tablet.listeners.FragmentActivityListener activityListener) {
        this.activityListener = activityListener;
    }

    public void setFragmentListener(com.smart.tablet.listeners.FragmentListener fragmentListener) {
        this.fragmentListener = fragmentListener;
    }

    @Override
    public void onSuccess(Object result) {
        final com.smart.tablet.entities.Sale sale = result != null ? (com.smart.tablet.entities.Sale) result : null;

        if (sale != null) {
            _sale = sale;

            new RetrieveSingleService(getContext(), sale.getService_id())
                    .onSuccess(new AsyncResultBag.Success() {
                        @Override
                        public void onSuccess(Object result) {
                            if (result != null) {
                                Service service = (Service) result;

                                AnalyticsHelper.track(getContext(), String.format(Locale.US, "Viewed Sale(%s) in #%d %s", sale.getTitle(), service.getId(), service.getTitle()), service.getId() + "", service.getCategory_id() + "");
                            }
                        }
                    })
                    .execute();

            txt_title.setText(sale.getTitle());
            txt_description.setText(sale.getDescription());

            if (sale.getMedia() != null)
                setupImage(sale.getMedia().getPath());
        }
    }
}
