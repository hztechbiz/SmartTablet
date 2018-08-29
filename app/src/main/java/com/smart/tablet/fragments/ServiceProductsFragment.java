package com.smart.tablet.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.github.barteksc.pdfviewer.PDFView;
import com.smart.tablet.R;
import com.smart.tablet.entities.Service;
import com.smart.tablet.fragments.ServiceBookingFragment;
import com.smart.tablet.helpers.Util;
import com.smart.tablet.listeners.AsyncResultBag;
import com.smart.tablet.listeners.FragmentActivityListener;
import com.smart.tablet.listeners.FragmentListener;
import com.smart.tablet.tasks.RetrieveMedia;
import com.smart.tablet.tasks.RetrieveSingleService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class ServiceProductsFragment extends Fragment implements AsyncResultBag.Success {
    PDFView pdfView;
    int _service_id;
    Service _service;
    Bundle _bundle;
    Button btn_booking;
    private FragmentActivityListener parentListener;
    private FragmentListener fragmentListener;

    public ServiceProductsFragment() {

    }

    public void setParentListener(FragmentActivityListener parentListener) {
        this.parentListener = parentListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.market_partner_menu, container, false);
        _bundle = getArguments();

        _service_id = 0;

        if (_bundle != null) {
            _service_id = _bundle.getInt(getString(R.string.param_service_id));
        }

        pdfView = view.findViewById(R.id.pdf_viewer);
        btn_booking = view.findViewById(R.id.btn_booking);

        btn_booking.setTypeface(Util.getTypeFace(getContext()));
        btn_booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServiceBookingFragment serviceBookingFragment = new ServiceBookingFragment();
                serviceBookingFragment.setArguments(_bundle);

                if (fragmentListener != null)
                    fragmentListener.onUpdateFragment(serviceBookingFragment);
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

                            if (meta_key.equals("products_pdf")) {
                                setupPdf(Integer.parseInt(meta_value));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    private void setupPdf(int media_id) {
        new RetrieveMedia(getContext(), media_id)
                .onSuccess(new AsyncResultBag.Success() {
                    @Override
                    public void onSuccess(Object result) {
                        String filePath = result.toString();

                        if (filePath != null) {
                            File file = new File(filePath);

                            if (file.exists()) {
                                pdfView.fromFile(file).load();
                                pdfView.zoomTo(1.5f);
                            }
                        }
                    }
                })
                .execute();
    }

    public void setFragmentListener(FragmentListener fragmentListener) {
        this.fragmentListener = fragmentListener;
    }
}
