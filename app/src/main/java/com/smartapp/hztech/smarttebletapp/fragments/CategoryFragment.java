package com.smartapp.hztech.smarttebletapp.fragments;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.smartapp.hztech.smarttebletapp.Constants;
import com.smartapp.hztech.smarttebletapp.MainActivity;
import com.smartapp.hztech.smarttebletapp.R;
import com.smartapp.hztech.smarttebletapp.adapters.CategoryGridAdapter;
import com.smartapp.hztech.smarttebletapp.adapters.ServicesGridAdapter;
import com.smartapp.hztech.smarttebletapp.entities.Category;
import com.smartapp.hztech.smarttebletapp.entities.Service;
import com.smartapp.hztech.smarttebletapp.helpers.ImageHelper;
import com.smartapp.hztech.smarttebletapp.listeners.AsyncResultBag;
import com.smartapp.hztech.smarttebletapp.listeners.FragmentActivityListener;
import com.smartapp.hztech.smarttebletapp.listeners.FragmentListener;
import com.smartapp.hztech.smarttebletapp.models.ActivityAction;
import com.smartapp.hztech.smarttebletapp.models.ServiceModel;
import com.smartapp.hztech.smarttebletapp.tasks.RetrieveCategories;
import com.smartapp.hztech.smarttebletapp.tasks.RetrieveMedia;
import com.smartapp.hztech.smarttebletapp.tasks.RetrieveServices;
import com.smartapp.hztech.smarttebletapp.tasks.RetrieveSetting;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CategoryFragment extends Fragment {

    private FragmentListener fragmentListener;
    private FragmentActivityListener parentListener;
    private List<Category> _categories;
    private List<ServiceModel> _services;
    private GridView gridView;
    private WebView webView;
    private RelativeLayout webview_container;
    private ProgressBar progressBar;
    private ImageView _logoImageView, _bgImageView;
    private CategoryGridAdapter categoryAdapter;
    private ServicesGridAdapter servicesAdapter;
    private int _category_id;
    private Boolean _has_children;
    private String _listing_type, _embed_url;
    private MainActivity _activity;

    public CategoryFragment() {

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void setFragmentListener(FragmentListener fragmentListener) {
        this.fragmentListener = fragmentListener;
    }

    public void setParentListener(FragmentActivityListener parentListener) {
        this.parentListener = parentListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.category_fragment, container, false);
        Bundle bundle = getArguments();

        _activity = (MainActivity) getActivity();

        gridView = view.findViewById(R.id.gridview);
        webView = view.findViewById(R.id.webview);
        webview_container = view.findViewById(R.id.webview_container);
        progressBar = view.findViewById(R.id.progressBar);

        _category_id = 0;
        _has_children = false;
        _listing_type = null;
        _embed_url = null;

        if (bundle != null) {
            if (bundle.containsKey(getString(R.string.param_category_id))) {
                _category_id = bundle.getInt(getString(R.string.param_category_id));
            }

            if (bundle.containsKey(getString(R.string.param_has_children))) {
                _has_children = bundle.getBoolean(getString(R.string.param_has_children));
            }

            if (bundle.containsKey(getString(R.string.param_listing_type))) {
                _listing_type = bundle.getString(getString(R.string.param_listing_type));
            }

            if (bundle.containsKey(getString(R.string.param_embed_url))) {
                _embed_url = bundle.getString(getString(R.string.param_embed_url));
            }
        }

        _categories = new ArrayList<>();
        _services = new ArrayList<>();

        categoryAdapter = new CategoryGridAdapter(getContext(), _categories, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                Object category_id = v.getTag(R.string.tag_value);
                Object has_children = v.getTag(R.string.tag_has_children);
                Object embed_url = v.getTag(R.string.tag_embed_url);

                if (category_id != null) {
                    bundle.putInt(getString(R.string.param_category_id), Integer.parseInt(category_id.toString()));
                }

                if (has_children != null) {
                    bundle.putBoolean(getString(R.string.param_has_children), Boolean.valueOf(has_children.toString()));
                }

                if (embed_url != null) {
                    bundle.putString(getString(R.string.param_embed_url), embed_url.toString());
                }

                CategoryFragment fragment = new CategoryFragment();
                fragment.setFragmentListener(fragmentListener);
                fragment.setParentListener(parentListener);
                fragment.setArguments(bundle);

                fragmentListener.onUpdateFragment(fragment);
            }
        });

        servicesAdapter = new ServicesGridAdapter(getContext(), _services, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();

                if (v.getTag() != null) {
                    bundle.putInt(getString(R.string.param_service_id), Integer.parseInt(v.getTag().toString()));
                }

                ServiceFragment fragment = new ServiceFragment();
                fragment.setFragmentListener(fragmentListener);
                fragment.setActivityListener(parentListener);
                fragment.setArguments(bundle);

                fragmentListener.onUpdateFragment(fragment);
            }
        });

        webView.setWebViewClient(new WebViewClient() {
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

        ArrayList<ActivityAction> actions = new ArrayList<>();

        actions.add(new ActivityAction((R.string.msg_show_sidebar), null));
        actions.add(new ActivityAction((R.string.msg_reset_menu), null));
        actions.add(new ActivityAction((R.string.msg_hide_home_button), null));
        actions.add(new ActivityAction((R.string.msg_reset_background), null));
        actions.add(new ActivityAction((R.string.msg_show_logo_button), null));
        actions.add(new ActivityAction((R.string.msg_hide_main_logo), null));
        actions.add(new ActivityAction((R.string.msg_hide_guest_button), null));
        actions.add(new ActivityAction((R.string.msg_hide_app_heading), null));
        actions.add(new ActivityAction((R.string.msg_show_copyright), null));

        if (_listing_type != null && _listing_type.equals("mp")) {
            actions.add(new ActivityAction((R.string.msg_show_top_guest_button), null));
        } else {
            actions.add(new ActivityAction((R.string.msg_hide_top_guest_button), null));
        }

        _activity.takeActions(actions);

        if (_category_id > 0 && !_has_children) {
            getServices();
            gridView.setAdapter(servicesAdapter);
        } else {
            getCategories();
            gridView.setAdapter(categoryAdapter);
        }

        if (_embed_url != null) {
            showWebView();
        }

        /*
        parentListener.receive(R.string.msg_show_sidebar, null);
        parentListener.receive(R.string.msg_reset_menu, null);
        parentListener.receive(R.string.msg_hide_home_button, null);
        parentListener.receive(R.string.msg_reset_background, null);
        parentListener.receive(R.string.msg_hide_main_logo, null);
        parentListener.receive(R.string.msg_show_logo_button, null);
        parentListener.receive(R.string.msg_hide_guest_button, null);
        parentListener.receive(R.string.msg_hide_app_heading, null);
        parentListener.receive(R.string.msg_show_copyright, null);
        parentListener.receive(R.string.msg_hide_top_guest_button, null);
        */

        return view;
    }

    private void showWebView() {
        webView.loadUrl(_embed_url);

        gridView.setVisibility(View.GONE);
        webview_container.setVisibility(View.VISIBLE);
    }

    private void setBranding() {
        new RetrieveSetting(getContext(), Constants.FILE_PATH_KEY)
                .onSuccess(new AsyncResultBag.Success() {
                    @Override
                    public void onSuccess(Object result) {

                        if (result != null) {
                            String filePath = result.toString();

                            if (filePath != null) {
                                File imgBG = new File(filePath + "/Background.jpg");
                                File imglogo = new File(filePath + "/Logo.jpg");

                                if (imgBG.exists()) {
//                                    Bitmap myBitmap = BitmapFactory.decodeFile(imgBG.getAbsolutePath());
//                                    _bgImageView.setImageBitmap(myBitmap);

                                    Resources res = getResources();
                                    Bitmap bitmap = BitmapFactory.decodeFile(imgBG.getAbsolutePath());
                                    BitmapDrawable bd = new BitmapDrawable(res, bitmap);
                                    _bgImageView.setBackgroundDrawable(bd);
                                }

                                if (imglogo.exists()) {
                                    Bitmap LogoBitmap = BitmapFactory.decodeFile(imglogo.getAbsolutePath());
                                    _logoImageView.setImageBitmap(LogoBitmap);
                                }
                            }
                        }
                    }
                })
                .execute();
    }

    private void getCategories() {
        String type = null;

        if (_category_id == 0 && _listing_type != null)
            type = _listing_type;

        new RetrieveCategories(getContext(), _category_id, type)
                .onSuccess(new AsyncResultBag.Success() {
                    @Override
                    public void onSuccess(Object result) {
                        if (result != null) {
                            Category[] categories = (Category[]) result;

                            _categories.clear();
                            _categories.addAll(Arrays.asList(categories));

                            categoryAdapter.notifyDataSetChanged();
                        }
                    }
                }).execute();
    }

    private void getServices() {
        new RetrieveServices(getContext(), _category_id)
                .onSuccess(new AsyncResultBag.Success() {
                    @Override
                    public void onSuccess(Object result) {
                        if (result != null) {
                            Service[] services = (Service[]) result;
                            ServiceModel[] serviceModels = new ServiceModel[services.length];

                            for (int j = 0; j < services.length; j++) {
                                Service service = services[j];
                                final ServiceModel serviceModel = new ServiceModel();

                                serviceModels[j] = serviceModel;

                                serviceModel.setCategory_id(service.getCategory_id());
                                serviceModel.setDescription(service.getDescription());
                                serviceModel.setHotel_id(service.getHotel_id());
                                serviceModel.setId(service.getId());
                                serviceModel.setIs_marketing_partner(service.isIs_marketing_partner());
                                serviceModel.setMeta(service.getMeta());
                                serviceModel.setStatus(service.getStatus());
                                serviceModel.setTitle(service.getTitle());
                                serviceModel.setImage(null);

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

                                                if (meta_obj.getString("meta_key").equals("image")) {
                                                    String image_id = meta_obj.getString("meta_value");

                                                    new RetrieveMedia(getContext(), Integer.parseInt(image_id))
                                                            .onSuccess(new AsyncResultBag.Success() {
                                                                @Override
                                                                public void onSuccess(Object result) {
                                                                    if (result != null) {
                                                                        try {
                                                                            String filePath = result.toString();

                                                                            if (filePath != null) {
                                                                                File imgBG = new File(filePath);

                                                                                if (imgBG.exists()) {
                                                                                    Bitmap bitmap = BitmapFactory.decodeFile(imgBG.getAbsolutePath());
                                                                                    bitmap = ImageHelper.getResizedBitmap(bitmap, 300);

                                                                                    serviceModel.setImage(bitmap);
                                                                                    servicesAdapter.notifyDataSetChanged();
                                                                                }
                                                                            }
                                                                        } catch (Exception ex) {
                                                                            ex.printStackTrace();
                                                                        }
                                                                    }
                                                                }
                                                            })
                                                            .execute();
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }
                            }

                            _services.clear();
                            _services.addAll(Arrays.asList(serviceModels));

                            servicesAdapter.notifyDataSetChanged();
                        }
                    }
                }).execute();
    }
}
