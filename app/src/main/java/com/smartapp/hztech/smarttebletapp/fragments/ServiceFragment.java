package com.smartapp.hztech.smarttebletapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.smartapp.hztech.smarttebletapp.R;
import com.smartapp.hztech.smarttebletapp.entities.Service;
import com.smartapp.hztech.smarttebletapp.listeners.AsyncResultBag;
import com.smartapp.hztech.smarttebletapp.tasks.RetrieveSingleService;

public class ServiceFragment extends Fragment {
    TextView txt_title;
    int _service_id;

    public ServiceFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detailed_service_fragment, container, false);
        Bundle bundle = getArguments();

        _service_id = 0;

        if (bundle != null) {
            _service_id = bundle.getInt(getString(R.string.param_service_id));
        }

        txt_title = view.findViewById(R.id.txt_title);

        bind();

        return view;
    }

    private void bind() {
        new RetrieveSingleService(getContext(), _service_id)
                .onSuccess(new AsyncResultBag.Success() {
                    @Override
                    public void onSuccess(Object result) {
                        Service service = result != null ? (Service) result : null;

                        if (service != null) {
                            txt_title.setText(service.getTitle());
                        }
                    }
                })
                .execute();
    }

}
