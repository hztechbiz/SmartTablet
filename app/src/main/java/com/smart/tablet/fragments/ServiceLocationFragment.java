package com.smart.tablet.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.smart.tablet.Constants;
import com.smart.tablet.R;
import com.smart.tablet.helpers.AnalyticsHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class ServiceLocationFragment extends Fragment implements com.smart.tablet.listeners.AsyncResultBag.Success {
    TextView txt_description, txt_title, txt_address, txt_email, txt_phone;
    int _service_id;
    com.smart.tablet.entities.Service _service;
    Bundle _bundle;
    GoogleMap _googleMap;
    String _address;
    Double _latitude, _longitude;
    boolean _markerSetup;
    Button btn_booking;
    private com.smart.tablet.listeners.FragmentActivityListener parentListener;
    private com.smart.tablet.listeners.FragmentListener fragmentListener;

    public ServiceLocationFragment() {

    }

    public void setParentListener(com.smart.tablet.listeners.FragmentActivityListener parentListener) {
        this.parentListener = parentListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.market_partner_location, container, false);
        _bundle = getArguments();

        _service_id = 0;
        _markerSetup = false;

        if (_bundle != null) {
            _service_id = _bundle.getInt(getString(R.string.param_service_id));
        }

        txt_description = view.findViewById(R.id.txt_description);
        txt_title = view.findViewById(R.id.txt_title);
        txt_address = view.findViewById(R.id.txt_address);
        txt_email = view.findViewById(R.id.txt_email);
        txt_phone = view.findViewById(R.id.txt_phone);
        btn_booking = view.findViewById(R.id.btn_booking);

        btn_booking.setTypeface(com.smart.tablet.helpers.Util.getTypeFace(getContext()));
        btn_booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                com.smart.tablet.fragments.ServiceBookingFragment serviceBookingFragment = new com.smart.tablet.fragments.ServiceBookingFragment();
                serviceBookingFragment.setArguments(_bundle);

                if (fragmentListener != null)
                    fragmentListener.onUpdateFragment(serviceBookingFragment);
            }
        });

        Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.lato_regular);
        txt_description.setTypeface(typeface);

        bind();

        return view;
    }

    private void bind() {
        new com.smart.tablet.tasks.RetrieveSingleService(getContext(), _service_id)
                .onSuccess(this)
                .execute();
    }

    @Override
    public void onSuccess(Object result) {
        com.smart.tablet.entities.Service service = result != null ? (com.smart.tablet.entities.Service) result : null;

        if (service != null) {
            _service = service;

            AnalyticsHelper.track(getContext(), String.format(Locale.US, "Viewed %s in #%d %s", "Location", service.getId(), service.getTitle()), String.format(Locale.US, "Service #%d", service.getId()));

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
                                case com.smart.tablet.Constants.META_LOCATION_TITLE:
                                    txt_title.setText(meta_value);
                                    break;
                                case com.smart.tablet.Constants.META_LOCATION_DESCRIPTION:
                                    txt_description.setText(meta_value);
                                    break;
                                case com.smart.tablet.Constants.META_LOCATION_ADDRESS:
                                    _address = meta_value;
                                    txt_address.setText(meta_value);

                                    SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
                                    mapFragment.getMapAsync(new OnMapReadyCallback() {
                                        @Override
                                        public void onMapReady(GoogleMap googleMap) {
                                            _googleMap = googleMap;
                                            setupMarker();
                                        }
                                    });
                                    break;
                                case com.smart.tablet.Constants.META_LOCATION_EMAIL:
                                    txt_email.setText(meta_value);
                                    break;
                                case com.smart.tablet.Constants.META_LOCATION_PHONE:
                                    txt_phone.setText(meta_value);
                                    break;
                                case com.smart.tablet.Constants.META_LOCATION_LATITUDE:
                                    _latitude = Double.parseDouble(meta_value);
                                    setupMarker();
                                    break;
                                case com.smart.tablet.Constants.META_LOCATION_LONGITUDE:
                                    _longitude = Double.parseDouble(meta_value);
                                    setupMarker();
                                    break;
                                case Constants.TOP_MENU_SHOW_BOOK:
                                    if (meta_value.equals("1"))
                                        btn_booking.setVisibility(View.VISIBLE);
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    setupMarker();
                }
            }
        }
    }

    private void setupMarker() {
        if (!_markerSetup && _googleMap != null && _latitude != null && _longitude != null && _address != null) {
            LatLng latLng = new LatLng(_latitude, _longitude);
            MarkerOptions markerOptions = new MarkerOptions();

            markerOptions.position(latLng);
            markerOptions.title(_address);

            _googleMap.clear();
            _googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17.0f));
            _googleMap.addMarker(markerOptions);

            _markerSetup = true;
        }
    }

    public void setFragmentListener(com.smart.tablet.listeners.FragmentListener fragmentListener) {
        this.fragmentListener = fragmentListener;
    }
}
