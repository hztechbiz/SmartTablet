package com.smartapp.hztech.smarttebletapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;

import com.smartapp.hztech.smarttebletapp.R;
import com.smartapp.hztech.smarttebletapp.adapters.CategoryGridAdapter;
import com.smartapp.hztech.smarttebletapp.adapters.ServicesGridAdapter;
import com.smartapp.hztech.smarttebletapp.entities.Category;
import com.smartapp.hztech.smarttebletapp.entities.Service;
import com.smartapp.hztech.smarttebletapp.listeners.AsyncResultBag;
import com.smartapp.hztech.smarttebletapp.listeners.FragmentActivityListener;
import com.smartapp.hztech.smarttebletapp.listeners.FragmentListener;
import com.smartapp.hztech.smarttebletapp.tasks.RetrieveCategories;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainFragment extends Fragment {

    private FragmentListener fragmentListener;
    private FragmentActivityListener parentListener;
    private List<Category> _categories;
    private List<Service> _services;
    private GridView gridView;
    private ImageView _logoImageView, _bgImageView;
    private CategoryGridAdapter categoryAdapter;
    private ServicesGridAdapter servicesAdapter;
    private int _category_id;
    private Boolean _has_children;
    private String _listing_type;

    public MainFragment() {

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

        gridView = view.findViewById(R.id.gridview);

        _category_id = 0;
        _has_children = false;
        _listing_type = null;

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
        }

        _categories = new ArrayList<>();
        _services = new ArrayList<>();

        categoryAdapter = new CategoryGridAdapter(getContext(), _categories, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                Object category_id = v.getTag(R.string.tag_value);
                Object has_children = v.getTag(R.string.tag_has_children);
                Object is_mp = v.getTag(R.string.tag_is_mp);

                if (category_id != null) {
                    bundle.putInt(getString(R.string.param_category_id), Integer.parseInt(category_id.toString()));
                }

                if (has_children != null) {
                    bundle.putBoolean(getString(R.string.param_has_children), Boolean.valueOf(has_children.toString()));
                }

                if (is_mp != null) {
                    bundle.putString(getString(R.string.param_listing_type), (Boolean.valueOf(is_mp.toString()) ? "mp" : "gsd"));
                }

                NavigationFragment fragment = new NavigationFragment();
                fragment.setFragmentListener(fragmentListener);
                fragment.setParentListener(parentListener);
                fragment.setArguments(bundle);

                fragmentListener.onUpdateFragment(fragment);
            }
        });


        getCategories();
        gridView.setAdapter(categoryAdapter);

        parentListener.receive(R.string.msg_show_sidebar, null);
        parentListener.receive(R.string.msg_reset_menu, null);

        return view;
    }

    private void getCategories() {
        new RetrieveCategories(getContext(), _category_id, null)
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
}
