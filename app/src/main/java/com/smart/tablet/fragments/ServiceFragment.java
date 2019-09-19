package com.smart.tablet.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.StrictMode;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import com.makeramen.roundedimageview.RoundedImageView;
import com.smart.tablet.Constants;
import com.smart.tablet.R;
import com.smart.tablet.helpers.AnalyticsHelper;
import com.smart.tablet.helpers.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ServiceFragment extends Fragment implements com.smart.tablet.listeners.AsyncResultBag.Success {

    TextView txt_title, txt_description;
    WebView txt_description_html;
    RoundedImageView iv_image;
    LinearLayout mainContent, footerContent;
    int _service_id;
    com.smart.tablet.entities.Service _service;
    Bundle _bundle;
    Button btn_booking, btn_featured, btn_coupon, btn_transport;
    private com.smart.tablet.listeners.FragmentActivityListener activityListener;
    private com.smart.tablet.listeners.FragmentListener fragmentListener;
    private com.smart.tablet.MainActivity _activity;
    private com.smart.tablet.fragments.NavigationFragment _parentFragment;
    Translate translate;

    public ServiceFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detailed_service_fragment, container, false);
        _bundle = getArguments();
        _activity = (com.smart.tablet.MainActivity) getActivity();
        _parentFragment = (com.smart.tablet.fragments.NavigationFragment) getParentFragment();

        _service_id = 0;

        if (_bundle != null) {
            _service_id = _bundle.getInt(getString(R.string.param_service_id));
        }

        txt_title = view.findViewById(R.id.txt_title);
        txt_description = view.findViewById(R.id.txt_description);
        txt_description_html = view.findViewById(R.id.txt_description_html);
        iv_image = view.findViewById(R.id.imageView);
        mainContent = view.findViewById(R.id.mainContent);
        footerContent = view.findViewById(R.id.footerContent);
        btn_booking = view.findViewById(R.id.btn_booking);
        btn_coupon = view.findViewById(R.id.btn_coupon);
        btn_featured = view.findViewById(R.id.btn_featured);
        btn_transport = view.findViewById(R.id.btn_transport);

        btn_booking.setTypeface(com.smart.tablet.helpers.Util.getTypeFace(getContext()));
        btn_coupon.setTypeface(com.smart.tablet.helpers.Util.getTypeFace(getContext()));
        btn_featured.setTypeface(com.smart.tablet.helpers.Util.getTypeFace(getContext()));
        btn_transport.setTypeface(com.smart.tablet.helpers.Util.getTypeFace(getContext()));
        txt_description.setTypeface(com.smart.tablet.helpers.Util.getTypeFace(getContext()));
        txt_title.setTypeface(com.smart.tablet.helpers.Util.getTypeFace(getContext()));
        footerContent.setVisibility(View.GONE);
        btn_booking.setVisibility(View.GONE);
        btn_coupon.setVisibility(View.GONE);
        btn_featured.setVisibility(View.GONE);
        btn_transport.setVisibility(View.GONE);

        txt_description_html.setBackgroundColor(0);

        btn_featured.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();

                if (v.getTag() == null) {
                    return;
                }

                bundle.putInt(getString(R.string.param_service_id), Integer.parseInt(v.getTag().toString()));

                ServiceFragment serviceFragment = new ServiceFragment();
                serviceFragment.setArguments(bundle);
                serviceFragment.setFragmentListener(fragmentListener);
                serviceFragment.setActivityListener(activityListener);

                AnalyticsHelper.track(getContext(), String.format(Locale.US, "Viewed featured partner in #%d %s", _service.getId(), _service.getTitle()), _service.getId() + "", _service.getCategory_id() + "");

                if (fragmentListener != null)
                    fragmentListener.onUpdateFragment(serviceFragment);
            }
        });

        btn_booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServiceBookingFragment serviceBookingFragment = new ServiceBookingFragment();
                serviceBookingFragment.setParentListener(activityListener);
                serviceBookingFragment.setArguments(_bundle);

                AnalyticsHelper.track(getContext(), String.format(Locale.US, "Viewed booking section in #%d %s", _service.getId(), _service.getTitle()), _service.getId() + "", _service.getCategory_id() + "");

                if (fragmentListener != null)
                    fragmentListener.onUpdateFragment(serviceBookingFragment);
            }
        });

        btn_coupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServiceCouponFragment serviceCouponFragment = new ServiceCouponFragment();
                serviceCouponFragment.setParentListener(activityListener);
                serviceCouponFragment.setArguments(_bundle);

                AnalyticsHelper.track(getContext(), String.format(Locale.US, "Viewed coupon section in #%d %s", _service.getId(), _service.getTitle()), _service.getId() + "", _service.getCategory_id() + "");

                if (fragmentListener != null)
                    fragmentListener.onUpdateFragment(serviceCouponFragment);
            }
        });

        btn_transport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();

                if (v.getTag() == null) {
                    return;
                }

                bundle.putInt(getString(R.string.param_category_id), Integer.parseInt(v.getTag().toString()));

                CategoryFragment categoryFragment = new CategoryFragment();
                categoryFragment.setArguments(bundle);
                categoryFragment.setFragmentListener(fragmentListener);
                categoryFragment.setParentListener(activityListener);

                AnalyticsHelper.track(getContext(), String.format(Locale.US, "Viewed transport options in #%d %s", _service.getId(), _service.getTitle()), _service.getId() + "", _service.getCategory_id() + "");

                if (fragmentListener != null)
                    fragmentListener.onUpdateFragment(categoryFragment);
            }
        });

        ArrayList<com.smart.tablet.models.ActivityAction> actions = new ArrayList<>();

        actions.add(new com.smart.tablet.models.ActivityAction((R.string.msg_show_sidebar), null));
        actions.add(new com.smart.tablet.models.ActivityAction((R.string.msg_reset_menu), null));
        actions.add(new com.smart.tablet.models.ActivityAction((R.string.msg_hide_home_button), null));
        actions.add(new com.smart.tablet.models.ActivityAction((R.string.msg_hide_app_heading), null));
        actions.add(new com.smart.tablet.models.ActivityAction((R.string.msg_hide_night_mode_button), null));
        actions.add(new com.smart.tablet.models.ActivityAction((R.string.msg_hide_copyright), null));
        actions.add(new com.smart.tablet.models.ActivityAction((R.string.msg_viewing_smart_page), null));

        _activity.takeActions(actions);

        bind();
        sendReport();

        return view;
    }

    private void sendReport() {
        new com.smart.tablet.tasks.RetrieveSetting(getContext(), com.smart.tablet.Constants.TOKEN_KEY)
                .onSuccess(new com.smart.tablet.listeners.AsyncResultBag.Success() {
                    @Override
                    public void onSuccess(Object result) {
                        if (result != null) {
                            Log.d("SendReport", result + "");
                            final String token = result.toString();
                            String url = com.smart.tablet.Constants.GetApiUrl("analytics/report");

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

                                    params.put("AppKey", com.smart.tablet.Constants.APP_KEY);
                                    params.put("Authorization", token);

                                    return params;
                                }
                            };
                            com.smart.tablet.AppController.getInstance().addToRequestQueue(request);
                        }
                    }
                })
                .execute();
    }

    private void bind() {
        new com.smart.tablet.tasks.RetrieveSingleService(getContext(), _service_id)
                .onSuccess(this)
                .execute();
    }

    private void setupImage(String image_id) {
        new com.smart.tablet.tasks.RetrieveMedia(getContext(), Integer.parseInt(image_id))
                .onSuccess(new com.smart.tablet.listeners.AsyncResultBag.Success() {
                    @Override
                    public void onSuccess(Object result) {
                        if (result != null) {
                            String filePath = result.toString();

                            if (filePath != null) {
                                try {
                                    File imgBG = new File(filePath);

                                    if (imgBG.exists()) {
                                        Bitmap bitmap = BitmapFactory.decodeFile(imgBG.getAbsolutePath());
                                        bitmap = com.smart.tablet.helpers.ImageHelper.getResizedBitmap(bitmap, 500);

                                        iv_image.setImageBitmap(bitmap);
                                    }
                                } catch (Exception | OutOfMemoryError e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                })
                .execute();
    }

    public void setActivityListener(com.smart.tablet.listeners.FragmentActivityListener activityListener) {
        this.activityListener = activityListener;
    }

    public void setFragmentListener(com.smart.tablet.listeners.FragmentListener fragmentListener) {
        this.fragmentListener = fragmentListener;
    }

    @Override
    public void onSuccess(Object result) {
        com.smart.tablet.entities.Service service = result != null ? (com.smart.tablet.entities.Service) result : null;
        ArrayList<com.smart.tablet.fragments.NavigationFragment.MenuItem> menu_items_objects = new ArrayList<>();
        String[] menu_items = new String[]{com.smart.tablet.Constants.TOP_MENU_SHOW_ABOUT, com.smart.tablet.Constants.TOP_MENU_SHOW_LOCATION, com.smart.tablet.Constants.TOP_MENU_SHOW_VIDEO, com.smart.tablet.Constants.TOP_MENU_SHOW_GALLERY, com.smart.tablet.Constants.TOP_MENU_SHOW_MENU, com.smart.tablet.Constants.TOP_MENU_SHOW_BOOK, com.smart.tablet.Constants.TOP_MENU_SHOW_OFFERS, com.smart.tablet.Constants.TOP_MENU_SHOW_ARRIVALS, com.smart.tablet.Constants.TOP_MENU_SHOW_SALES, com.smart.tablet.Constants.TOP_MENU_SHOW_TESTIMONIALS, com.smart.tablet.Constants.TOP_MENU_SHOW_SERVICES, com.smart.tablet.Constants.TOP_MENU_SHOW_PRODUCTS, com.smart.tablet.Constants.TOP_MENU_SHOW_PRICE_LIST};

        if (service != null) {
            _service = service;

            AnalyticsHelper.track(getContext(), String.format(Locale.US, "Viewed Service #%d %s", service.getId(), service.getTitle()), service.getId() + "", service.getCategory_id() + "");
            String title = service.getTitle();

            if (!service.isIs_marketing_partner()) {
                String description = service.getDescription();

                txt_title.setText(title);
                txt_description.setText(Html.fromHtml(service.getDescription()), TextView.BufferType.SPANNABLE);
                txt_description_html.loadData(description, "text/html", "utf-8");
            } else {
                ArrayList<com.smart.tablet.models.ActivityAction> actions = new ArrayList<>();
                actions.add(new com.smart.tablet.models.ActivityAction((R.string.msg_show_app_heading), null));
                actions.add(new com.smart.tablet.models.ActivityAction((R.string.msg_set_app_heading), title.toUpperCase()));
                actions.add(new com.smart.tablet.models.ActivityAction((R.string.msg_hide_welcome_button), null));
                actions.add(new com.smart.tablet.models.ActivityAction((R.string.msg_hide_top_guest_button), null));

                _activity.takeActions(actions);
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
                            String meta_key = meta_obj.getString("meta_key");
                            String meta_value = meta_obj.getString("meta_value");

                            switch (meta_key) {
                                case "image":
                                    if (!service.isIs_marketing_partner())
                                        setupImage(meta_value);
                                    break;
                                case "background_image":
                                    setupBackgroundImage(meta_value);
                                    break;
                                case Constants.TOP_MENU_SHOW_BOOK:
                                    if (meta_value.equals("1")) {
                                        footerContent.setVisibility(View.VISIBLE);
                                        btn_booking.setVisibility(View.VISIBLE);
                                    }
                                    break;
                                case Constants.TOP_MENU_SHOW_COUPON:
                                    if (meta_value.equals("1")) {
                                        footerContent.setVisibility(View.VISIBLE);
                                        btn_coupon.setVisibility(View.VISIBLE);
                                    }
                                    break;
                                case Constants.TOP_MENU_SHOW_FEATURED:
                                    if (meta_value.equals("1")) {
                                        footerContent.setVisibility(View.VISIBLE);
                                        btn_featured.setVisibility(View.VISIBLE);
                                    }
                                    break;
                                case Constants.TOP_MENU_SHOW_TRANSPORT:
                                    if (meta_value.equals("1")) {
                                        footerContent.setVisibility(View.VISIBLE);
                                        btn_transport.setVisibility(View.VISIBLE);
                                    }
                                    break;
                                case "featured_service":
                                    btn_featured.setTag(meta_value);
                                    break;
                                case "transport_category":
                                    btn_transport.setTag(meta_value);
                                    break;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                if (service.isIs_marketing_partner()) {
                    mainContent.setVisibility(View.GONE);

                    ArrayList<com.smart.tablet.models.ActivityAction> actions = new ArrayList<>();
                    actions.add(new com.smart.tablet.models.ActivityAction((R.string.msg_hide_sidebar), null));
                    actions.add(new com.smart.tablet.models.ActivityAction((R.string.msg_show_home_button), null));
                    actions.add(new com.smart.tablet.models.ActivityAction((R.string.msg_update_menu), _service_id + ""));
                    actions.add(new com.smart.tablet.models.ActivityAction((R.string.msg_viewing_smart_page), "1"));

                    _parentFragment.takeActions(actions);
                }
            }
        }
    }

    private void setupBackgroundImage(String image_id) {
        new com.smart.tablet.tasks.RetrieveMedia(getContext(), Integer.parseInt(image_id))
                .onSuccess(new com.smart.tablet.listeners.AsyncResultBag.Success() {
                    @Override
                    public void onSuccess(Object result) {
                        if (result != null) {
                            String filePath = result.toString();

                            if (filePath != null) {
                                ArrayList<com.smart.tablet.models.ActivityAction> actions = new ArrayList<>();
                                actions.add(new com.smart.tablet.models.ActivityAction((R.string.msg_update_background), filePath));

                                Intent intent = new Intent(getString(R.string.param_activity_action));
                                intent.putParcelableArrayListExtra(getString(R.string.param_activity_actions), actions);

                                getContext().sendBroadcast(intent);
                            }
                        }
                    }
                })
                .execute();
    }
}
