package com.smartapp.hztech.smarttebletapp.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.smartapp.hztech.smarttebletapp.Constants;
import com.smartapp.hztech.smarttebletapp.R;
import com.smartapp.hztech.smarttebletapp.entities.Service;
import com.smartapp.hztech.smarttebletapp.listeners.AsyncResultBag;
import com.smartapp.hztech.smarttebletapp.listeners.FragmentActivityListener;
import com.smartapp.hztech.smarttebletapp.tasks.RetrieveSingleService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ServiceLocationFragment extends Fragment implements AsyncResultBag.Success {
    TextView txt_description, txt_title, txt_address, txt_email, txt_phone;
    int _service_id;
    Service _service;
    Bundle _bundle;
    private FragmentActivityListener parentListener;

    public ServiceLocationFragment() {

    }

    public void setParentListener(FragmentActivityListener parentListener) {
        this.parentListener = parentListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.market_partner_location, container, false);
        _bundle = getArguments();

        _service_id = 0;

        if (_bundle != null) {
            _service_id = _bundle.getInt(getString(R.string.param_service_id));
        }

        txt_description = view.findViewById(R.id.txt_description);
        txt_title = view.findViewById(R.id.txt_title);
        txt_address = view.findViewById(R.id.txt_address);
        txt_email = view.findViewById(R.id.txt_email);
        txt_phone = view.findViewById(R.id.txt_phone);

        Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.lato_regular);
        txt_description.setTypeface(typeface);

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

            txt_description.setText(service.getDescription());

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
                                case Constants.META_LOCATION_TITLE:
                                    txt_title.setText(meta_value);
                                    break;
                                case Constants.META_LOCATION_DESCRIPTION:
                                    txt_description.setText(meta_value);
                                    break;
                                case Constants.META_LOCATION_ADDRESS:
                                    txt_address.setText(meta_value);
                                    break;
                                case Constants.META_LOCATION_EMAIL:
                                    txt_email.setText(meta_value);
                                    break;
                                case Constants.META_LOCATION_PHONE:
                                    txt_phone.setText(meta_value);
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
}
