package com.smartapp.hztech.smarttebletapp.fragments;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartapp.hztech.smarttebletapp.Constants;
import com.smartapp.hztech.smarttebletapp.R;
import com.smartapp.hztech.smarttebletapp.entities.Service;
import com.smartapp.hztech.smarttebletapp.listeners.AsyncResultBag;
import com.smartapp.hztech.smarttebletapp.listeners.FragmentActivityListener;
import com.smartapp.hztech.smarttebletapp.listeners.FragmentListener;
import com.smartapp.hztech.smarttebletapp.tasks.RetrieveMedia;
import com.smartapp.hztech.smarttebletapp.tasks.RetrieveSingleService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

public class ServiceFragment extends Fragment implements AsyncResultBag.Success {

    TextView txt_title, txt_description;
    ImageView iv_image;
    int _service_id;
    Service _service;
    Bundle _bundle;
    private FragmentActivityListener activityListener;
    private FragmentListener fragmentListener;

    public ServiceFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detailed_service_fragment, container, false);
        _bundle = getArguments();

        _service_id = 0;

        if (_bundle != null) {
            _service_id = _bundle.getInt(getString(R.string.param_service_id));
        }

        txt_title = view.findViewById(R.id.txt_title);
        txt_description = view.findViewById(R.id.txt_description);
        iv_image = view.findViewById(R.id.imageView);

        activityListener.receive(R.string.msg_show_sidebar, null);
        activityListener.receive(R.string.msg_reset_menu, null);

        bind();

        return view;
    }

    private void bind() {
        new RetrieveSingleService(getContext(), _service_id)
                .onSuccess(this)
                .execute();
    }

    private void _setupImage(String image_id) {
        new RetrieveMedia(getContext(), Integer.parseInt(image_id))
                .onSuccess(new AsyncResultBag.Success() {
                    @Override
                    public void onSuccess(Object result) {
                        if (result != null) {
                            String filePath = result.toString();

                            if (filePath != null) {
                                File imgBG = new File(filePath);

                                if (imgBG.exists()) {
                                    Resources res = getResources();
                                    Bitmap bitmap = BitmapFactory.decodeFile(imgBG.getAbsolutePath());
                                    BitmapDrawable bd = new BitmapDrawable(res, bitmap);

                                    iv_image.setImageDrawable(bd);
                                }
                            }
                        }
                    }
                })
                .execute();
    }

    public void setActivityListener(FragmentActivityListener activityListener) {
        this.activityListener = activityListener;
    }

    public void setFragmentListener(FragmentListener fragmentListener) {
        this.fragmentListener = fragmentListener;
    }

    @Override
    public void onSuccess(Object result) {
        Service service = result != null ? (Service) result : null;
        ArrayList<NavigationFragment.MenuItem> menu_items_objects = new ArrayList<>();
        String[] menu_items = new String[]{Constants.TOP_MENU_SHOW_ABOUT, Constants.TOP_MENU_SHOW_LOCATION, Constants.TOP_MENU_SHOW_VIDEO, Constants.TOP_MENU_SHOW_GALLERY, Constants.TOP_MENU_SHOW_MENU, Constants.TOP_MENU_SHOW_BOOK};

        if (service != null) {
            _service = service;

            if (!service.isIs_marketing_partner()) {
                txt_title.setText(service.getTitle());
                txt_description.setText(service.getDescription());
            }

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

                            if (!service.isIs_marketing_partner() && meta_obj.getString("meta_key").equals("image")) {
                                _setupImage(meta_obj.getString("meta_value"));
                            }

                            for (int j = 0; j < menu_items.length; j++) {
                                if (meta_obj.getString("meta_key").equals(menu_items[j]) && meta_obj.getString("meta_value").equals("1")) {
                                    NavigationFragment.MenuItem item = new NavigationFragment.MenuItem();

                                    switch (menu_items[j]) {
                                        case Constants.TOP_MENU_SHOW_ABOUT:
                                            ServiceAboutFragment serviceAboutFragment = new ServiceAboutFragment();
                                            serviceAboutFragment.setArguments(_bundle);

                                            item.title = "ABOUT US";
                                            item.fragment = serviceAboutFragment;

                                            break;
                                        case Constants.TOP_MENU_SHOW_LOCATION:
                                            ServiceLocationFragment serviceLocationFragment = new ServiceLocationFragment();
                                            serviceLocationFragment.setArguments(_bundle);

                                            item.title = "LOCATION";
                                            item.fragment = serviceLocationFragment;

                                            break;
                                        case Constants.TOP_MENU_SHOW_VIDEO:
                                            ServiceVideoFragment serviceVideoFragment = new ServiceVideoFragment();
                                            serviceVideoFragment.setArguments(_bundle);

                                            item.title = "VIDEO";
                                            item.fragment = serviceVideoFragment;

                                            break;
                                        case Constants.TOP_MENU_SHOW_GALLERY:
                                            ServiceGalleryFragment serviceGalleryFragment = new ServiceGalleryFragment();
                                            serviceGalleryFragment.setArguments(_bundle);

                                            item.title = "GALLERY";
                                            item.fragment = serviceGalleryFragment;

                                            break;
                                        case Constants.TOP_MENU_SHOW_MENU:
                                            ServiceMenuFragment serviceMenuFragment = new ServiceMenuFragment();
                                            serviceMenuFragment.setArguments(_bundle);

                                            item.title = "MENUS";
                                            item.fragment = serviceMenuFragment;

                                            break;
                                    }

                                    if (item.title != null)
                                        menu_items_objects.add(item);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                if (service.isIs_marketing_partner() && !menu_items_objects.isEmpty()) {

                    if (fragmentListener != null)
                        fragmentListener.onUpdateFragment(menu_items_objects.get(0).fragment);

                    activityListener.receive(R.string.msg_hide_sidebar, null);
                    activityListener.receive(R.string.msg_update_menu, menu_items_objects);
                }
            }
        }
    }
}
