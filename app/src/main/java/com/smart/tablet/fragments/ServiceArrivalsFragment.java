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
import com.smart.tablet.entities.Arrival;
import com.smart.tablet.entities.Offer;
import com.smart.tablet.entities.Service;
import com.smart.tablet.fragments.ServiceSingleArrivalFragment;
import com.smart.tablet.helpers.ImageHelper;
import com.smart.tablet.listeners.AsyncResultBag;
import com.smart.tablet.listeners.FragmentActivityListener;
import com.smart.tablet.listeners.FragmentListener;
import com.smart.tablet.models.OfferModel;
import com.smart.tablet.tasks.RetrieveArrivals;
import com.smart.tablet.tasks.RetrieveOffers;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ServiceArrivalsFragment extends Fragment implements com.smart.tablet.listeners.AsyncResultBag.Success {
    GridView gridView;
    List<com.smart.tablet.models.OfferModel> _offers;
    int _service_id;
    com.smart.tablet.entities.Service _service;
    Bundle _bundle;
    com.smart.tablet.adapters.OffersGridAdapter adapter;
    private com.smart.tablet.listeners.FragmentActivityListener parentListener;
    private com.smart.tablet.listeners.FragmentListener fragmentListener;

    public ServiceArrivalsFragment() {

    }

    public void setParentListener(com.smart.tablet.listeners.FragmentActivityListener parentListener) {
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

        adapter = new com.smart.tablet.adapters.OffersGridAdapter(getContext(), _offers, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();

                if (v.getTag() != null) {
                    bundle.putInt(getString(R.string.param_offer_id), Integer.parseInt(v.getTag().toString()));
                }

                com.smart.tablet.fragments.ServiceSingleArrivalFragment fragment = new com.smart.tablet.fragments.ServiceSingleArrivalFragment();
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
        new com.smart.tablet.tasks.RetrieveArrivals(getContext(), _service_id)
                .onSuccess(this)
                .execute();
    }

    @Override
    public void onSuccess(Object result) {
        if (result != null) {
            com.smart.tablet.entities.Arrival[] arrivals = (com.smart.tablet.entities.Arrival[]) result;
            com.smart.tablet.models.OfferModel[] offerModels = new com.smart.tablet.models.OfferModel[arrivals.length];

            for (int i = 0; i < arrivals.length; i++) {
                com.smart.tablet.entities.Arrival offer = arrivals[i];
                final com.smart.tablet.models.OfferModel offerModel = new com.smart.tablet.models.OfferModel();

                offerModels[i] = offerModel;

                offerModel.setId(offer.getId());
                offerModel.setDescription(offer.getDescription());
                offerModel.setMedia_id(offer.getMedia_id());
                offerModel.setService_id(offer.getService_id());
                offerModel.setTitle(offer.getTitle());

                if (offer.getMedia() != null) {
                    String filePath = offer.getMedia().getPath();

                    if (filePath != null) {
                        File imgBG = new File(filePath);

                        if (imgBG.exists()) {
                            Bitmap bitmap = BitmapFactory.decodeFile(imgBG.getAbsolutePath());
                            bitmap = com.smart.tablet.helpers.ImageHelper.getResizedBitmap(bitmap, 300);

                            offerModel.setImage(bitmap);
                        }
                    }
                }
            }

            _offers.clear();
            _offers.addAll(Arrays.asList(offerModels));

            adapter.notifyDataSetChanged();
        }
    }

    public com.smart.tablet.listeners.FragmentListener getFragmentListener() {
        return fragmentListener;
    }

    public void setFragmentListener(com.smart.tablet.listeners.FragmentListener fragmentListener) {
        this.fragmentListener = fragmentListener;
    }
}
