package com.smartapp.hztech.smarttebletapp.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.smartapp.hztech.smarttebletapp.R;
import com.smartapp.hztech.smarttebletapp.adapters.ServicesGridAdapter;
import com.smartapp.hztech.smarttebletapp.entities.Service;
import com.smartapp.hztech.smarttebletapp.listeners.AsyncResultBag;
import com.smartapp.hztech.smarttebletapp.listeners.FragmentListener;
import com.smartapp.hztech.smarttebletapp.tasks.RetrieveServices;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by HNH on 3/28/2018.
 */

public class ServiceSpecialOfferFragment extends Fragment {

    FragmentListener mCallback;
    private List<Service> _services;
    private GridView gridView;
    private ServicesGridAdapter gridAdapter;
    private int _parent_id;

    public ServiceSpecialOfferFragment() {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (FragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnFragmentUpdate");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.services_special_offer, container, false);
        _services = new ArrayList<>();
        getServices();
        gridView = view.findViewById(R.id.spcial_offer_list);
        //gridAdapter = new ServicesGridAdapter(getContext(), _services);
        //gridView.setAdapter(gridAdapter);

        return view;
    }

    public void getServices() {
        new RetrieveServices(getContext(), 17)
                .onSuccess(new AsyncResultBag.Success() {
                    @Override
                    public void onSuccess(Object result) {
                        if (result != null) {

                            Service[] services = (Service[]) result;
                            _services.clear();
                            _services.addAll(Arrays.asList(services));
                            Log.d("_serv", _services.size() + " ");
                            gridAdapter.notifyDataSetChanged();
                        }
                    }
                }).execute();
    }
}
