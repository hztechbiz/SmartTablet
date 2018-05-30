package com.smartapp.hztech.smarttebletapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import com.smartapp.hztech.smarttebletapp.R;
import com.smartapp.hztech.smarttebletapp.entities.Service;
import com.smartapp.hztech.smarttebletapp.listeners.AsyncResultBag;
import com.smartapp.hztech.smarttebletapp.listeners.FragmentActivityListener;
import com.smartapp.hztech.smarttebletapp.tasks.RetrieveMedia;
import com.smartapp.hztech.smarttebletapp.tasks.RetrieveSingleService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ServiceVideoFragment extends Fragment implements AsyncResultBag.Success {
    int _service_id;
    Service _service;
    Bundle _bundle;
    VideoView videoView;
    private FragmentActivityListener parentListener;

    public ServiceVideoFragment() {

    }

    public void setParentListener(FragmentActivityListener parentListener) {
        this.parentListener = parentListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.market_partner_vidoes, container, false);
        _bundle = getArguments();

        _service_id = 0;

        if (_bundle != null) {
            _service_id = _bundle.getInt(getString(R.string.param_service_id));
        }

        videoView = view.findViewById(R.id.video);

        MediaController mediaController = new MediaController(getContext());
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

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

                            if (meta_key.equals("video")) {
                                setupVideo(Integer.parseInt(meta_value));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    private void setupVideo(int media_id) {
        new RetrieveMedia(getContext(), media_id)
                .onSuccess(new AsyncResultBag.Success() {
                    @Override
                    public void onSuccess(Object result) {
                        if (result != null) {
                            videoView.setVideoPath(result.toString());
                        }
                    }
                })
                .execute();
    }
}
