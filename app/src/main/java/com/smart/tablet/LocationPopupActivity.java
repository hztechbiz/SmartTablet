package com.smart.tablet;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
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
import com.smart.tablet.listeners.AsyncResultBag;
import com.smart.tablet.tasks.RetrieveSetting;

import java.util.HashMap;

public class LocationPopupActivity extends FragmentActivity implements com.smart.tablet.listeners.AsyncResultBag.Success {
    private LatLng _latLng;
    private String _address;
    private TextView _txt_address, _txt_email, _txt_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_popup);

        _txt_address = findViewById(R.id.txt_address);
        _txt_email = findViewById(R.id.txt_email);
        _txt_phone = findViewById(R.id.txt_phone);

        Button close = findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .9), (int) (height * .9));
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;

        getWindow().setAttributes(params);
        bind();
    }

    private void bind() {
        new com.smart.tablet.tasks.RetrieveSetting(this, com.smart.tablet.Constants.SETTING_LATITUDE, com.smart.tablet.Constants.SETTING_LONGITUDE, com.smart.tablet.Constants.SETTING_ADDRESS, com.smart.tablet.Constants.SETTING_EMAIL, com.smart.tablet.Constants.SETTING_PHONE)
                .onSuccess(this)
                .execute();
    }

    @Override
    public void onSuccess(Object result) {
        if (result != null) {
            HashMap<String, String> values = (HashMap<String, String>) result;

            String address = values.containsKey(com.smart.tablet.Constants.SETTING_ADDRESS) ? values.get(com.smart.tablet.Constants.SETTING_ADDRESS) : "";
            String email = values.containsKey(com.smart.tablet.Constants.SETTING_EMAIL) ? values.get(com.smart.tablet.Constants.SETTING_EMAIL) : "";
            String phone = values.containsKey(com.smart.tablet.Constants.SETTING_PHONE) ? values.get(com.smart.tablet.Constants.SETTING_PHONE) : "";
            double latitude = values.containsKey(com.smart.tablet.Constants.SETTING_LATITUDE) ? Double.parseDouble(values.get(com.smart.tablet.Constants.SETTING_LATITUDE)) : 0;
            double longitude = values.containsKey(com.smart.tablet.Constants.SETTING_LONGITUDE) ? Double.parseDouble(values.get(com.smart.tablet.Constants.SETTING_LONGITUDE)) : 0;

            _latLng = new LatLng(latitude, longitude);
            _address = address;

            _txt_address.setText(address);
            _txt_email.setText(email);
            _txt_phone.setText(phone);

            if (!address.isEmpty() && Math.abs(latitude) > 0 && Math.abs(longitude) > 0)
                setupMap();
        }
    }

    private void setupMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                MarkerOptions markerOptions = new MarkerOptions();

                markerOptions.position(_latLng);
                markerOptions.title(_address);

                googleMap.clear();
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(_latLng, 17.0f));
                googleMap.addMarker(markerOptions);
            }
        });
    }
}
