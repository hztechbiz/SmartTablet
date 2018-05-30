package com.smartapp.hztech.smarttebletapp.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.smartapp.hztech.smarttebletapp.R;
import com.smartapp.hztech.smarttebletapp.entities.Service;
import com.smartapp.hztech.smarttebletapp.listeners.AsyncResultBag;
import com.smartapp.hztech.smarttebletapp.listeners.FragmentActivityListener;
import com.smartapp.hztech.smarttebletapp.tasks.RetrieveSingleService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ServiceBookingFragment extends Fragment implements AsyncResultBag.Success {
    WebView webview;
    ProgressBar progressBar;
    int _service_id;
    Service _service;
    Bundle _bundle;
    private FragmentActivityListener parentListener;

    public ServiceBookingFragment() {

    }

    public void setParentListener(FragmentActivityListener parentListener) {
        this.parentListener = parentListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.market_partner_booking, container, false);
        _bundle = getArguments();

        _service_id = 0;

        if (_bundle != null) {
            _service_id = _bundle.getInt(getString(R.string.param_service_id));
        }

        webview = view.findViewById(R.id.webview);
        progressBar = view.findViewById(R.id.progressBar);

        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                progressBar.setVisibility(View.VISIBLE);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                progressBar.setVisibility(View.GONE);
                super.onPageFinished(view, url);
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

                            if (meta_key.equals("booking_url")) {
                                webview.loadUrl(meta_value);
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
