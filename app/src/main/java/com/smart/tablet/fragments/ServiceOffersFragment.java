package com.smart.tablet.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.smart.tablet.R;
import com.smart.tablet.adapters.OffersGridAdapter;
import com.smart.tablet.entities.Offer;
import com.smart.tablet.entities.Service;
import com.smart.tablet.fragments.ServiceSingleOfferFragment;
import com.smart.tablet.helpers.AnalyticsHelper;
import com.smart.tablet.helpers.ImageHelper;
import com.smart.tablet.listeners.AsyncResultBag;
import com.smart.tablet.listeners.FragmentActivityListener;
import com.smart.tablet.listeners.FragmentListener;
import com.smart.tablet.models.OfferModel;
import com.smart.tablet.tasks.RetrieveOffers;
import com.smart.tablet.tasks.RetrieveSingleService;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class ServiceOffersFragment extends Fragment implements AsyncResultBag.Success {
    GridView gridView;
    List<OfferModel> _offers;
    int _service_id;
    Service _service;
    Bundle _bundle;
    OffersGridAdapter adapter;
    private FragmentActivityListener parentListener;
    private FragmentListener fragmentListener;

    public ServiceOffersFragment() {

    }

    public void setParentListener(FragmentActivityListener parentListener) {
        this.parentListener = parentListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mp_offers, container, false);
        _bundle = getArguments();
        _offers = new ArrayList<>();

        _service_id = 0;

        if (_bundle != null) {
            _service_id = _bundle.getInt(getString(R.string.param_service_id));
        }

        gridView = view.findViewById(R.id.gridview);

        adapter = new OffersGridAdapter(getContext(), _offers, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();

                if (v.getTag() != null) {
                    bundle.putInt(getString(R.string.param_offer_id), Integer.parseInt(v.getTag().toString()));
                }

                ServiceSingleOfferFragment fragment = new ServiceSingleOfferFragment();
                fragment.setFragmentListener(fragmentListener);
                fragment.setActivityListener(parentListener);
                fragment.setArguments(bundle);

                fragmentListener.onUpdateFragment(fragment);
            }
        });
        gridView.setAdapter(adapter);

        bind();

        return view;
    }

    private void bind() {
        new RetrieveOffers(getContext(), _service_id)
                .onSuccess(this)
                .execute();

        new RetrieveSingleService(getContext(), _service_id)
                .onSuccess(new AsyncResultBag.Success() {
                    @Override
                    public void onSuccess(Object result) {
                        if (result != null) {
                            Service service = (Service) result;

                            AnalyticsHelper.track(getContext(), String.format(Locale.US, "Viewed %s in #%d %s", "Offers", service.getId(), service.getTitle()), service.getId() + "", service.getCategory_id() + "");
                        }
                    }
                })
                .execute();
    }

    @Override
    public void onSuccess(Object result) {
        if (result != null) {
            Offer[] offers = (Offer[]) result;
            OfferModel[] offerModels = new OfferModel[offers.length];

            for (int i = 0; i < offers.length; i++) {
                Offer offer = offers[i];
                final OfferModel offerModel = new OfferModel();

                offerModels[i] = offerModel;

                offerModel.setId(offer.getId());
                offerModel.setDescription(offer.getDescription());
                offerModel.setMedia_id(offer.getMedia_id());
                offerModel.setService_id(offer.getService_id());
                offerModel.setTitle(offer.getTitle());

                if (offer.getMedia() != null) {
                    try {
                        String filePath = offer.getMedia().getPath();

                        if (filePath != null) {
                            File imgBG = new File(filePath);

                            if (imgBG.exists()) {
                                Bitmap bitmap = BitmapFactory.decodeFile(imgBG.getAbsolutePath());
                                bitmap = ImageHelper.getResizedBitmap(bitmap, 300);

                                offerModel.setImage(bitmap);
                            }
                        }
                    } catch (Exception | OutOfMemoryError e) {
                        e.printStackTrace();
                    }
                }
            }

            _offers.clear();
            _offers.addAll(Arrays.asList(offerModels));

            adapter.notifyDataSetChanged();
        }
    }

    public FragmentListener getFragmentListener() {
        return fragmentListener;
    }

    public void setFragmentListener(FragmentListener fragmentListener) {
        this.fragmentListener = fragmentListener;
    }
}
