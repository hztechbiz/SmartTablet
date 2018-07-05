package com.smartapp.hztech.smarttebletapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;

import com.smartapp.hztech.smarttebletapp.MainActivity;
import com.smartapp.hztech.smarttebletapp.R;
import com.smartapp.hztech.smarttebletapp.adapters.CategoryGridAdapter;
import com.smartapp.hztech.smarttebletapp.adapters.ServicesGridAdapter;
import com.smartapp.hztech.smarttebletapp.entities.Category;
import com.smartapp.hztech.smarttebletapp.entities.Service;
import com.smartapp.hztech.smarttebletapp.listeners.AsyncResultBag;
import com.smartapp.hztech.smarttebletapp.listeners.FragmentActivityListener;
import com.smartapp.hztech.smarttebletapp.listeners.FragmentListener;
import com.smartapp.hztech.smarttebletapp.models.ActivityAction;
import com.smartapp.hztech.smarttebletapp.models.MapMarker;
import com.smartapp.hztech.smarttebletapp.tasks.RetrieveCategories;
import com.smartapp.hztech.smarttebletapp.tasks.RetrieveSingleCategory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainFragment extends Fragment {

    private FragmentListener fragmentListener;
    private FragmentActivityListener parentListener;
    private Fragment childFragment;
    private List<Category> _categories;
    private List<Service> _services;
    private GridView gridView;
    private ImageView _logoImageView, _bgImageView;
    private CategoryGridAdapter categoryAdapter;
    private ServicesGridAdapter servicesAdapter;
    private int _category_id, _main_category_id, _service_id;
    private MapMarker _map_marker;
    private Boolean _has_children, _is_weather;
    private String _listing_type;
    private Bundle _bundle;
    private MainActivity _activity;

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
        _bundle = getArguments();
        _activity = (MainActivity) getActivity();

        gridView = view.findViewById(R.id.gridview);

        _category_id = _main_category_id = _service_id = 0;
        _has_children = false;
        _listing_type = null;
        _map_marker = null;
        _is_weather = false;

        if (_bundle != null) {
            if (_bundle.containsKey(getString(R.string.param_category_id))) {
                _category_id = _bundle.getInt(getString(R.string.param_category_id));
            }

            if (_bundle.containsKey(getString(R.string.param_main_category_id))) {
                _main_category_id = _bundle.getInt(getString(R.string.param_main_category_id));
            }

            if (_bundle.containsKey(getString(R.string.param_service_id))) {
                _service_id = _bundle.getInt(getString(R.string.param_service_id));
            }

            if (_bundle.containsKey(getString(R.string.param_has_children))) {
                _has_children = _bundle.getBoolean(getString(R.string.param_has_children));
            }

            if (_bundle.containsKey(getString(R.string.param_listing_type))) {
                _listing_type = _bundle.getString(getString(R.string.param_listing_type));
            }

            if (_bundle.containsKey(getString(R.string.param_marker))) {
                _map_marker = _bundle.getParcelable(getString(R.string.param_marker));
            }

            if (_bundle.containsKey(getString(R.string.param_weather))) {
                _is_weather = _bundle.getBoolean(getString(R.string.param_weather));
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
                Object embed_url = v.getTag(R.string.tag_embed_url);

                if (category_id != null) {
                    bundle.putInt(getString(R.string.param_category_id), Integer.parseInt(category_id.toString()));
                }

                if (has_children != null) {
                    bundle.putBoolean(getString(R.string.param_has_children), Boolean.valueOf(has_children.toString()));
                }

                if (embed_url != null) {
                    //bundle.putString(getString(R.string.param_embed_url), embed_url.toString());
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

        if (_is_weather) {
            moveToWeatherFragment();
        } else if (_map_marker != null) {
            moveToMapFragment();
        } else if (_service_id > 0) {
            moveToServiceFragment();
        } else if (_main_category_id > 0) {
            moveToCategoryFragment();
        } else {
            getCategories();
            gridView.setAdapter(categoryAdapter);
        }

        ArrayList<ActivityAction> actions = new ArrayList<>();

        actions.add(new ActivityAction((R.string.msg_show_sidebar), null));
        actions.add(new ActivityAction((R.string.msg_reset_menu), null));
        actions.add(new ActivityAction((R.string.msg_hide_home_button), null));
        actions.add(new ActivityAction((R.string.msg_hide_back_button), null));
        actions.add(new ActivityAction((R.string.msg_reset_background), null));
        actions.add(new ActivityAction((R.string.msg_hide_logo_button), null));
        actions.add(new ActivityAction((R.string.msg_show_main_logo), null));
        actions.add(new ActivityAction((R.string.msg_hide_welcome_button), null));
        actions.add(new ActivityAction((R.string.msg_show_guest_button), null));
        actions.add(new ActivityAction((R.string.msg_hide_app_heading), null));
        actions.add(new ActivityAction((R.string.msg_show_copyright), null));
        actions.add(new ActivityAction((R.string.msg_hide_top_guest_button), null));

        _activity.takeActions(actions);

        /*
        parentListener.receive(R.string.msg_show_sidebar, null);
        parentListener.receive(R.string.msg_reset_menu, null);
        parentListener.receive(R.string.msg_hide_home_button, null);
        parentListener.receive(R.string.msg_hide_back_button, null);
        parentListener.receive(R.string.msg_reset_background, null);
        parentListener.receive(R.string.msg_hide_logo_button, null);
        parentListener.receive(R.string.msg_show_main_logo, null);
        parentListener.receive(R.string.msg_hide_welcome_button, null);
        parentListener.receive(R.string.msg_show_guest_button, null);
        parentListener.receive(R.string.msg_hide_app_heading, null);
        parentListener.receive(R.string.msg_show_copyright, null);
        parentListener.receive(R.string.msg_hide_top_guest_button, null);
        */

        return view;
    }

    private void moveToWeatherFragment() {
        WeatherFragment weatherFragment = new WeatherFragment();
        weatherFragment.setFragmentListener(fragmentListener);
        weatherFragment.setParentListener(parentListener);
        weatherFragment.setArguments(_bundle);

        NavigationFragment fragment = new NavigationFragment();
        fragment.setFragmentListener(fragmentListener);
        fragment.setParentListener(parentListener);
        fragment.setChildFragment(weatherFragment);

        fragmentListener.onUpdateFragment(fragment);
    }

    private void moveToMapFragment() {
        MapFragment mapFragment = new MapFragment();
        mapFragment.setFragmentListener(fragmentListener);
        mapFragment.setParentListener(parentListener);
        mapFragment.setArguments(_bundle);

        NavigationFragment fragment = new NavigationFragment();
        fragment.setFragmentListener(fragmentListener);
        fragment.setParentListener(parentListener);
        fragment.setChildFragment(mapFragment);
        //fragment.setArguments(_bundle);

        fragmentListener.onUpdateFragment(fragment);
    }

    private void moveToServiceFragment() {
        Bundle bundle = new Bundle();
        bundle.putInt(getString(R.string.param_service_id), _service_id);
        bundle.putString(getString(R.string.param_listing_type), "gsd");

        ServiceFragment serviceFragment = new ServiceFragment();
        serviceFragment.setFragmentListener(fragmentListener);
        serviceFragment.setActivityListener(parentListener);
        serviceFragment.setArguments(bundle);

        NavigationFragment fragment = new NavigationFragment();
        fragment.setFragmentListener(fragmentListener);
        fragment.setParentListener(parentListener);
        fragment.setChildFragment(serviceFragment);
        fragment.setArguments(bundle);

        fragmentListener.onUpdateFragment(fragment);
    }

    private void moveToCategoryFragment() {
        new RetrieveSingleCategory(getContext(), _main_category_id)
                .onSuccess(new AsyncResultBag.Success() {
                    @Override
                    public void onSuccess(Object result) {
                        if (result != null) {
                            Category category = (Category) result;

                            Bundle bundle = new Bundle();

                            bundle.putInt(getString(R.string.param_category_id), _main_category_id);
                            bundle.putBoolean(getString(R.string.param_has_children), (category.getChildren_count() > 0));
                            bundle.putString(getString(R.string.param_listing_type), (category.isIs_marketing_partner() ? "mp" : "gsd"));
                            bundle.putString(getString(R.string.param_embed_url), category.getEmbed_url());

                            NavigationFragment fragment = new NavigationFragment();
                            fragment.setFragmentListener(fragmentListener);
                            fragment.setParentListener(parentListener);
                            fragment.setArguments(bundle);

                            fragmentListener.onUpdateFragment(fragment);
                        }
                    }
                })
                .execute();
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

    public void setChildFragment(Fragment childFragment) {
        this.childFragment = childFragment;
    }
}
