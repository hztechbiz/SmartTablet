package com.smartapp.hztech.smarttebletapp.fragments;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;

import com.smartapp.hztech.smarttebletapp.R;
import com.smartapp.hztech.smarttebletapp.adapters.ServicesGridAdapter;
import com.smartapp.hztech.smarttebletapp.adapters.CategoryGridAdapter;
import com.smartapp.hztech.smarttebletapp.entities.Category;
import com.smartapp.hztech.smarttebletapp.entities.Service;
import com.smartapp.hztech.smarttebletapp.listeners.AsyncResultBag;
import com.smartapp.hztech.smarttebletapp.listeners.FragmentListener;
import com.smartapp.hztech.smarttebletapp.tasks.RetrieveCategories;
import com.smartapp.hztech.smarttebletapp.tasks.RetrieveServices;
import com.smartapp.hztech.smarttebletapp.tasks.RetrieveSetting;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentListener fragmentListener;
    private List<Category> _categories;
    private List<Service> _services;
    private GridView gridView;
    private ImageView _logoImageView, _bgImageView;
    private CategoryGridAdapter categoryAdapter;
    private ServicesGridAdapter servicesAdapter;
    private int _category_id;
    private Boolean _has_children;
    private String FILE_PATH = "ST@FILE_PATH";

    public HomeFragment() {

    }

    public void setFragmentListener(FragmentListener fragmentListener) {
        this.fragmentListener = fragmentListener;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            fragmentListener = (FragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentUpdate");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.home_fragment, container, false);
        Bundle bundle = getArguments();

        gridView = view.findViewById(R.id.list_calllog);
        _bgImageView = view.findViewById(R.id.main_bg_img);
        _logoImageView = view.findViewById(R.id.MainLogo);

        _category_id = 0;
        _has_children = false;

        if (bundle != null) {
            _category_id = getArguments().getInt("category_id");
            _has_children = getArguments().getBoolean("has_children");
        }

        _categories = new ArrayList<>();
        _services = new ArrayList<>();

        categoryAdapter = new CategoryGridAdapter(getContext(), _categories, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();

                if (v.getTag() != null) {
                    bundle.putInt("category_id", Integer.parseInt(v.getTag().toString()));
                }

                if (v.getTag(R.string.tag_has_children) != null) {
                    bundle.putBoolean("has_children", Boolean.valueOf(v.getTag(R.string.tag_has_children).toString()));
                }

                HomeFragment fragment = new HomeFragment();
                fragment.setArguments(bundle);

                fragmentListener.onUpdateFragment(fragment);
            }
        });

        servicesAdapter = new ServicesGridAdapter(getContext(), _services, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();

                if (v.getTag() != null) {
                    bundle.putInt("service_id", Integer.parseInt(v.getTag().toString()));
                }

                ServicesFragment fragment = new ServicesFragment();
                fragment.setArguments(bundle);

                fragmentListener.onUpdateFragment(fragment);
            }
        });

        if (_category_id > 0 && !_has_children) {
            getServices();
            gridView.setAdapter(servicesAdapter);
        } else {
            getCategories();
            gridView.setAdapter(categoryAdapter);
        }

        setBranding();

        return view;
    }

    private void setBranding() {
        new RetrieveSetting(getContext(), FILE_PATH)
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
                                    Bitmap bitmap =  BitmapFactory.decodeFile(imgBG.getAbsolutePath());
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
        new RetrieveCategories(getContext(), _category_id)
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

                            _services.clear();
                            _services.addAll(Arrays.asList(services));

                            servicesAdapter.notifyDataSetChanged();
                        }
                    }
                }).execute();
    }
}
