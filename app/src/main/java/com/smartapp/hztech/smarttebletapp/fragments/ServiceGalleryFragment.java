package com.smartapp.hztech.smarttebletapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.smartapp.hztech.smarttebletapp.MarketingPartnerSection.PopUpActivity;
import com.smartapp.hztech.smarttebletapp.R;
import com.smartapp.hztech.smarttebletapp.adapters.GalleryGridAdapter;
import com.smartapp.hztech.smarttebletapp.entities.Service;
import com.smartapp.hztech.smarttebletapp.listeners.AsyncResultBag;
import com.smartapp.hztech.smarttebletapp.listeners.FragmentActivityListener;
import com.smartapp.hztech.smarttebletapp.tasks.RetrieveMedia;
import com.smartapp.hztech.smarttebletapp.tasks.RetrieveSingleService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ServiceGalleryFragment extends Fragment implements AsyncResultBag.Success {
    GridView gridView;
    List<String> _items;
    int _service_id;
    Service _service;
    Bundle _bundle;
    GalleryGridAdapter adapter;
    private FragmentActivityListener parentListener;

    public ServiceGalleryFragment() {

    }

    public void setParentListener(FragmentActivityListener parentListener) {
        this.parentListener = parentListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.market_partner_gallery, container, false);
        _bundle = getArguments();
        _items = new ArrayList<>();

        _service_id = 0;

        if (_bundle != null) {
            _service_id = _bundle.getInt(getString(R.string.param_service_id));
        }

        gridView = view.findViewById(R.id.list_gallery);
        adapter = new GalleryGridAdapter(getContext(), _items);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getContext(), PopUpActivity.class);
                i.putExtra("IMAGE", parent.getItemAtPosition(position).toString());

                startActivity(i);
            }
        });

        bind();

        return view;
    }

    private void bind() {
        new RetrieveSingleService(getContext(), _service_id)
                .onSuccess(this)
                .execute();
    }

    @Override
    public void onSuccess(Object result) {
        Service service = result != null ? (Service) result : null;

        if (service != null) {
            _service = service;

            if (!service.getMeta().isEmpty()) {
                JSONArray metas_arr = null;
                try {
                    metas_arr = new JSONArray(service.getMeta());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (metas_arr != null) {
                    for (int i = 0; i < metas_arr.length(); i++) {
                        try {
                            JSONObject meta_obj = metas_arr.getJSONObject(i);
                            String meta_key = meta_obj.getString("meta_key");
                            String meta_value = meta_obj.getString("meta_value");

                            switch (meta_key) {
                                case "gallery_images":
                                    if (meta_value != null && !meta_value.equals("[]")) {
                                        JSONArray images = new JSONArray(meta_value);
                                        int[] images_ids = new int[images.length()];

                                        for (int j = 0; j < images.length(); j++) {
                                            images_ids[j] = images.getInt(j);
                                        }
                                        setupImages(images_ids);
                                    }
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    private void setupImages(int[] images_ids) {
        new RetrieveMedia(getContext(), images_ids)
                .onSuccess(new AsyncResultBag.Success() {
                    @Override
                    public void onSuccess(Object result) {
                        HashMap<String, String> values = result != null ? (HashMap<String, String>) result : null;

                        if (values != null) {
                            _items.clear();

                            for (String path : values.values()) {
                                if (path != null) {
                                    _items.add(path);
                                }
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                })
                .execute();
    }
}
