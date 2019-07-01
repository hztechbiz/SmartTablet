package com.smart.tablet.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MarkerOptions;
import com.smart.tablet.R;
import com.smart.tablet.listeners.FragmentActivityListener;
import com.smart.tablet.listeners.FragmentListener;
import com.smart.tablet.models.MapMarker;

public class MapFragment extends Fragment {
    GoogleMap _googleMap;
    Bundle _bundle;
    MapMarker _marker;
    private FragmentActivityListener parentListener;
    private FragmentListener fragmentListener;

    public MapFragment() {

    }

    public void setParentListener(FragmentActivityListener parentListener) {
        this.parentListener = parentListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map_fragment, container, false);
        _bundle = getArguments();

        _marker = null;

        if (_bundle != null) {
            if (_bundle.containsKey(getString(R.string.param_marker))) {
                _marker = _bundle.getParcelable(getString(R.string.param_marker));
            }
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                _googleMap = googleMap;
                setupMarker();
            }
        });

        return view;
    }

    private void setupMarker() {
        if (_googleMap != null && _marker != null) {
            MarkerOptions markerOptions = new MarkerOptions();

            markerOptions.position(_marker.getLatLng());
            markerOptions.title(_marker.getAddress());

            _googleMap.clear();
            _googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(_marker.getLatLng(), 17.0f));
            _googleMap.addMarker(markerOptions);
        }
    }

    public void setFragmentListener(FragmentListener fragmentListener) {
        this.fragmentListener = fragmentListener;
    }
}
