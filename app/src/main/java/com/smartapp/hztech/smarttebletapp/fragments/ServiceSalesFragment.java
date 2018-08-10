package com.smartapp.hztech.smarttebletapp.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.smartapp.hztech.smarttebletapp.R;
import com.smartapp.hztech.smarttebletapp.adapters.OffersGridAdapter;
import com.smartapp.hztech.smarttebletapp.entities.Offer;
import com.smartapp.hztech.smarttebletapp.entities.Sale;
import com.smartapp.hztech.smarttebletapp.entities.Service;
import com.smartapp.hztech.smarttebletapp.helpers.ImageHelper;
import com.smartapp.hztech.smarttebletapp.listeners.AsyncResultBag;
import com.smartapp.hztech.smarttebletapp.listeners.FragmentActivityListener;
import com.smartapp.hztech.smarttebletapp.listeners.FragmentListener;
import com.smartapp.hztech.smarttebletapp.models.OfferModel;
import com.smartapp.hztech.smarttebletapp.tasks.RetrieveOffers;
import com.smartapp.hztech.smarttebletapp.tasks.RetrieveSales;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ServiceSalesFragment extends Fragment implements AsyncResultBag.Success {
    GridView gridView;
    List<OfferModel> _offers;
    int _service_id;
    Service _service;
    Bundle _bundle;
    OffersGridAdapter adapter;
    private FragmentActivityListener parentListener;
    private FragmentListener fragmentListener;

    public ServiceSalesFragment() {

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

                ServiceSingleSaleFragment fragment = new ServiceSingleSaleFragment();
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
        new RetrieveSales(getContext(), _service_id)
                .onSuccess(this)
                .execute();
    }

    @Override
    public void onSuccess(Object result) {
        if (result != null) {
            Sale[] sales = (Sale[]) result;
            OfferModel[] offerModels = new OfferModel[sales.length];

            for (int i = 0; i < sales.length; i++) {
                Sale offer = sales[i];
                final OfferModel offerModel = new OfferModel();

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
                            bitmap = ImageHelper.getResizedBitmap(bitmap, 300);

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

    public FragmentListener getFragmentListener() {
        return fragmentListener;
    }

    public void setFragmentListener(FragmentListener fragmentListener) {
        this.fragmentListener = fragmentListener;
    }
}
