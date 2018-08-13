package com.smartapp.hztech.smarttebletapp.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.makeramen.roundedimageview.RoundedImageView;
import com.smartapp.hztech.smarttebletapp.AppController;
import com.smartapp.hztech.smarttebletapp.Constants;
import com.smartapp.hztech.smarttebletapp.MainActivity;
import com.smartapp.hztech.smarttebletapp.R;
import com.smartapp.hztech.smarttebletapp.entities.Service;
import com.smartapp.hztech.smarttebletapp.helpers.ImageHelper;
import com.smartapp.hztech.smarttebletapp.helpers.Util;
import com.smartapp.hztech.smarttebletapp.listeners.AsyncResultBag;
import com.smartapp.hztech.smarttebletapp.listeners.FragmentActivityListener;
import com.smartapp.hztech.smarttebletapp.listeners.FragmentListener;
import com.smartapp.hztech.smarttebletapp.models.ActivityAction;
import com.smartapp.hztech.smarttebletapp.tasks.RetrieveMedia;
import com.smartapp.hztech.smarttebletapp.tasks.RetrieveSetting;
import com.smartapp.hztech.smarttebletapp.tasks.RetrieveSingleService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ServiceFragment extends Fragment implements AsyncResultBag.Success {

    TextView txt_title, txt_description;
    RoundedImageView iv_image;
    LinearLayout mainContent, footerContent;
    int _service_id;
    Service _service;
    Bundle _bundle;
    Button btn_booking;
    private FragmentActivityListener activityListener;
    private FragmentListener fragmentListener;
    private MainActivity _activity;
    private NavigationFragment _parentFragment;

    public ServiceFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detailed_service_fragment, container, false);
        _bundle = getArguments();
        _activity = (MainActivity) getActivity();
        _parentFragment = (NavigationFragment) getParentFragment();

        _service_id = 0;

        if (_bundle != null) {
            _service_id = _bundle.getInt(getString(R.string.param_service_id));
        }

        txt_title = view.findViewById(R.id.txt_title);
        txt_description = view.findViewById(R.id.txt_description);
        iv_image = view.findViewById(R.id.imageView);
        mainContent = view.findViewById(R.id.mainContent);
        footerContent = view.findViewById(R.id.footerContent);
        btn_booking = view.findViewById(R.id.btn_booking);

        btn_booking.setTypeface(Util.getTypeFace(getContext()));
        txt_description.setTypeface(Util.getTypeFace(getContext()));
        txt_title.setTypeface(Util.getTypeFace(getContext()));
        footerContent.setVisibility(View.GONE);

        btn_booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServiceBookingFragment serviceBookingFragment = new ServiceBookingFragment();
                serviceBookingFragment.setArguments(_bundle);

                if (fragmentListener != null)
                    fragmentListener.onUpdateFragment(serviceBookingFragment);
            }
        });

        ArrayList<ActivityAction> actions = new ArrayList<>();

        actions.add(new ActivityAction((R.string.msg_show_sidebar), null));
        actions.add(new ActivityAction((R.string.msg_reset_menu), null));
        actions.add(new ActivityAction((R.string.msg_hide_home_button), null));
        actions.add(new ActivityAction((R.string.msg_hide_app_heading), null));
        actions.add(new ActivityAction((R.string.msg_hide_night_mode_button), null));
        actions.add(new ActivityAction((R.string.msg_hide_copyright), null));

        _activity.takeActions(actions);

        /*
        activityListener.receive(R.string.msg_show_sidebar, null);
        activityListener.receive(R.string.msg_reset_menu, null);
        activityListener.receive(R.string.msg_hide_home_button, null);
        activityListener.receive(R.string.msg_hide_app_heading, null);
        activityListener.receive(R.string.msg_hide_copyright, null);
        */

        bind();
        sendReport();

        return view;
    }

    private void sendReport() {
        new RetrieveSetting(getContext(), Constants.TOKEN_KEY)
                .onSuccess(new AsyncResultBag.Success() {
                    @Override
                    public void onSuccess(Object result) {
                        if (result != null) {
                            Log.d("SendReport", result + "");
                            final String token = result.toString();
                            String url = Constants.GetApiUrl("analytics/report");

                            JSONObject jsonRequest = new JSONObject();
                            try {
                                jsonRequest.put("object_id", _service_id);
                                jsonRequest.put("object_type", "service");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonRequest, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.d("SendReport", response + "");
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e("SendReport", error + "");
                                }
                            }) {
                                @Override
                                public Map<String, String> getHeaders() {
                                    Map<String, String> params = new HashMap<String, String>();

                                    params.put("AppKey", Constants.APP_KEY);
                                    params.put("Authorization", token);

                                    return params;
                                }
                            };
                            AppController.getInstance().addToRequestQueue(request);
                        }
                    }
                })
                .execute();
    }

    private void bind() {
        new RetrieveSingleService(getContext(), _service_id)
                .onSuccess(this)
                .execute();
    }

    private void setupImage(String image_id) {
        new RetrieveMedia(getContext(), Integer.parseInt(image_id))
                .onSuccess(new AsyncResultBag.Success() {
                    @Override
                    public void onSuccess(Object result) {
                        if (result != null) {
                            String filePath = result.toString();

                            if (filePath != null) {
                                File imgBG = new File(filePath);

                                if (imgBG.exists()) {
                                    Bitmap bitmap = BitmapFactory.decodeFile(imgBG.getAbsolutePath());
                                    bitmap = ImageHelper.getResizedBitmap(bitmap, 500);

                                    iv_image.setImageBitmap(bitmap);
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
        String[] menu_items = new String[]{Constants.TOP_MENU_SHOW_ABOUT, Constants.TOP_MENU_SHOW_LOCATION, Constants.TOP_MENU_SHOW_VIDEO, Constants.TOP_MENU_SHOW_GALLERY, Constants.TOP_MENU_SHOW_MENU, Constants.TOP_MENU_SHOW_BOOK, Constants.TOP_MENU_SHOW_OFFERS, Constants.TOP_MENU_SHOW_ARRIVALS, Constants.TOP_MENU_SHOW_SALES, Constants.TOP_MENU_SHOW_TESTIMONIALS, Constants.TOP_MENU_SHOW_SERVICES, Constants.TOP_MENU_SHOW_PRODUCTS, Constants.TOP_MENU_SHOW_PRICE_LIST};

        if (service != null) {
            _service = service;

            if (!service.isIs_marketing_partner()) {
                txt_title.setText(service.getTitle());
                txt_description.setText(service.getDescription());
            } else {
                ArrayList<ActivityAction> actions = new ArrayList<>();
                actions.add(new ActivityAction((R.string.msg_show_app_heading), null));
                actions.add(new ActivityAction((R.string.msg_set_app_heading), service.getTitle().toUpperCase()));

                _activity.takeActions(actions);

                /*
                activityListener.receive(R.string.msg_show_app_heading, null);
                activityListener.receive(R.string.msg_set_app_heading, service.getTitle().toUpperCase());
                */
                footerContent.setVisibility(View.VISIBLE);
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
                                setupImage(meta_obj.getString("meta_value"));
                            }

                            if (meta_obj.getString("meta_key").equals("background_image")) {
                                setupBackgroundImage(meta_obj.getString("meta_value"));
                            }

                            /*
                            for (int j = 0; j < menu_items.length; j++) {
                                if (meta_obj.getString("meta_key").equals(menu_items[j]) && meta_obj.getString("meta_value").equals("1")) {
                                    NavigationFragment.MenuItem item = new NavigationFragment.MenuItem();

                                    switch (menu_items[j]) {
                                        case Constants.TOP_MENU_SHOW_ABOUT:
                                            ServiceAboutFragment serviceAboutFragment = new ServiceAboutFragment();
                                            serviceAboutFragment.setArguments(_bundle);
                                            serviceAboutFragment.setFragmentListener(fragmentListener);

                                            item.title = "ABOUT US";
                                            item.fragment = serviceAboutFragment;

                                            break;
                                        case Constants.TOP_MENU_SHOW_LOCATION:
                                            ServiceLocationFragment serviceLocationFragment = new ServiceLocationFragment();
                                            serviceLocationFragment.setArguments(_bundle);
                                            serviceLocationFragment.setFragmentListener(fragmentListener);

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
                                            serviceMenuFragment.setFragmentListener(fragmentListener);

                                            item.title = "MENUS";
                                            item.fragment = serviceMenuFragment;

                                            break;
                                        case Constants.TOP_MENU_SHOW_OFFERS:
                                            ServiceOffersFragment serviceOffersFragment = new ServiceOffersFragment();
                                            serviceOffersFragment.setArguments(_bundle);
                                            serviceOffersFragment.setFragmentListener(fragmentListener);

                                            item.title = "OFFERS";
                                            item.fragment = serviceOffersFragment;

                                            break;
                                        case Constants.TOP_MENU_SHOW_ARRIVALS:
                                            ServiceArrivalsFragment serviceArrivalsFragment = new ServiceArrivalsFragment();
                                            serviceArrivalsFragment.setArguments(_bundle);
                                            serviceArrivalsFragment.setFragmentListener(fragmentListener);

                                            item.title = "NEW ARRIVALS";
                                            item.fragment = serviceArrivalsFragment;

                                            break;
                                        case Constants.TOP_MENU_SHOW_SALES:
                                            ServiceSalesFragment serviceSalesFragment = new ServiceSalesFragment();
                                            serviceSalesFragment.setArguments(_bundle);
                                            serviceSalesFragment.setFragmentListener(fragmentListener);

                                            item.title = "SALES";
                                            item.fragment = serviceSalesFragment;

                                            break;
                                        case Constants.TOP_MENU_SHOW_SERVICES:
                                            ServiceServicesMenuFragment serviceServiceMenuFragment = new ServiceServicesMenuFragment();
                                            serviceServiceMenuFragment.setArguments(_bundle);
                                            serviceServiceMenuFragment.setFragmentListener(fragmentListener);

                                            item.title = "SERVICES";
                                            item.fragment = serviceServiceMenuFragment;

                                            break;
                                        case Constants.TOP_MENU_SHOW_PRODUCTS:
                                            ServiceProductsFragment serviceProductsFragment = new ServiceProductsFragment();
                                            serviceProductsFragment.setArguments(_bundle);
                                            serviceProductsFragment.setFragmentListener(fragmentListener);

                                            item.title = "PRODUCTS";
                                            item.fragment = serviceProductsFragment;

                                            break;
                                        case Constants.TOP_MENU_SHOW_PRICE_LIST:
                                            ServicePriceListFragment servicePriceListFragment = new ServicePriceListFragment();
                                            servicePriceListFragment.setArguments(_bundle);
                                            servicePriceListFragment.setFragmentListener(fragmentListener);

                                            item.title = "PRICE LIST";
                                            item.fragment = servicePriceListFragment;

                                            break;
                                        case Constants.TOP_MENU_SHOW_TESTIMONIALS:
                                            ServiceTestimonialsFragment serviceTestimonialsFragment = new ServiceTestimonialsFragment();
                                            serviceTestimonialsFragment.setArguments(_bundle);
                                            serviceTestimonialsFragment.setFragmentListener(fragmentListener);

                                            item.title = "TESTIMONIALS";
                                            item.fragment = serviceTestimonialsFragment;

                                            break;
                                    }

                                    if (item.title != null)
                                        menu_items_objects.add(item);
                                }
                            }
                            */
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                if (service.isIs_marketing_partner()) {
                    mainContent.setVisibility(View.GONE);
                    //if (fragmentListener != null)
                    //fragmentListener.onUpdateFragment(menu_items_objects.get(0).fragment);

                    ArrayList<ActivityAction> actions = new ArrayList<>();
                    actions.add(new ActivityAction((R.string.msg_hide_sidebar), null));
                    actions.add(new ActivityAction((R.string.msg_show_home_button), null));
                    actions.add(new ActivityAction((R.string.msg_update_menu), _service_id + ""));

                    _parentFragment.takeActions(actions);

                    //activityListener.receive(R.string.msg_update_menu, menu_items_objects);
                    /*
                    activityListener.receive(R.string.msg_hide_sidebar, null);
                    activityListener.receive(R.string.msg_update_menu, menu_items_objects);
                    activityListener.receive(R.string.msg_show_home_button, null);
                    */
                }
            }
        }
    }

    private void setupBackgroundImage(String image_id) {
        new RetrieveMedia(getContext(), Integer.parseInt(image_id))
                .onSuccess(new AsyncResultBag.Success() {
                    @Override
                    public void onSuccess(Object result) {
                        if (result != null) {
                            String filePath = result.toString();

                            if (filePath != null) {
                                ArrayList<ActivityAction> actions = new ArrayList<>();
                                actions.add(new ActivityAction((R.string.msg_update_background), filePath));

                                Intent intent = new Intent(getString(R.string.param_activity_action));
                                intent.putParcelableArrayListExtra(getString(R.string.param_activity_actions), actions);

                                getContext().sendBroadcast(intent);

                                //activityListener.receive(R.string.msg_update_background, filePath);
                            }
                        }
                    }
                })
                .execute();
    }
}
