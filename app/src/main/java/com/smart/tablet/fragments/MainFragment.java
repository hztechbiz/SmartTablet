package com.smart.tablet.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridView;
import android.widget.ImageView;

import com.smart.tablet.MainActivity;
import com.smart.tablet.R;
import com.smart.tablet.adapters.CategoryGridAdapter;
import com.smart.tablet.adapters.ServicesGridAdapter;
import com.smart.tablet.entities.Category;
import com.smart.tablet.entities.Service;
import com.smart.tablet.fragments.MapFragment;
import com.smart.tablet.fragments.NavigationFragment;
import com.smart.tablet.fragments.ServiceFragment;
import com.smart.tablet.fragments.WeatherFragment;
import com.smart.tablet.listeners.AsyncResultBag;
import com.smart.tablet.listeners.FragmentActivityListener;
import com.smart.tablet.listeners.FragmentListener;
import com.smart.tablet.models.ActivityAction;
import com.smart.tablet.models.MapMarker;
import com.smart.tablet.tasks.RetrieveCategories;
import com.smart.tablet.tasks.RetrieveSingleCategory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainFragment extends Fragment {

    private com.smart.tablet.listeners.FragmentListener fragmentListener;
    private com.smart.tablet.listeners.FragmentActivityListener parentListener;
    private Fragment childFragment;
    private List<com.smart.tablet.entities.Category> _categories;
    private List<com.smart.tablet.entities.Service> _services;
    private GridView gridView;
    private ImageView _logoImageView, _bgImageView, _scrollIndicator;
    private com.smart.tablet.adapters.CategoryGridAdapter categoryAdapter;
    private com.smart.tablet.adapters.ServicesGridAdapter servicesAdapter;
    private int _category_id, _main_category_id, _service_id;
    private com.smart.tablet.models.MapMarker _map_marker;
    private Boolean _has_children, _is_weather;
    private String _listing_type;
    private Bundle _bundle;
    private com.smart.tablet.MainActivity _activity;

    public MainFragment() {

    }

    public void setFragmentListener(com.smart.tablet.listeners.FragmentListener fragmentListener) {
        this.fragmentListener = fragmentListener;
    }

    public void setParentListener(com.smart.tablet.listeners.FragmentActivityListener parentListener) {
        this.parentListener = parentListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.category_fragment, container, false);
        _bundle = getArguments();
        _activity = (com.smart.tablet.MainActivity) getActivity();

        gridView = view.findViewById(R.id.gridview);
        _scrollIndicator = view.findViewById(R.id.img_scroll_indicator);

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

        categoryAdapter = new com.smart.tablet.adapters.CategoryGridAdapter(getContext(), _categories, new View.OnClickListener() {
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

                com.smart.tablet.fragments.NavigationFragment fragment = new com.smart.tablet.fragments.NavigationFragment();
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

        ArrayList<com.smart.tablet.models.ActivityAction> actions = new ArrayList<>();

        actions.add(new com.smart.tablet.models.ActivityAction((R.string.msg_show_sidebar), null));
        actions.add(new com.smart.tablet.models.ActivityAction((R.string.msg_reset_menu), null));
        actions.add(new com.smart.tablet.models.ActivityAction((R.string.msg_hide_home_button), null));
        actions.add(new com.smart.tablet.models.ActivityAction((R.string.msg_hide_back_button), null));
        actions.add(new com.smart.tablet.models.ActivityAction((R.string.msg_reset_background), null));
        actions.add(new com.smart.tablet.models.ActivityAction((R.string.msg_hide_logo_button), null));
        actions.add(new com.smart.tablet.models.ActivityAction((R.string.msg_show_main_logo), null));
        actions.add(new com.smart.tablet.models.ActivityAction((R.string.msg_hide_welcome_button), null));
        actions.add(new com.smart.tablet.models.ActivityAction((R.string.msg_show_top_right_buttons), null));
        actions.add(new com.smart.tablet.models.ActivityAction((R.string.msg_hide_app_heading), null));
        actions.add(new com.smart.tablet.models.ActivityAction((R.string.msg_show_night_mode_button), null));
        actions.add(new com.smart.tablet.models.ActivityAction((R.string.msg_show_copyright), null));
        actions.add(new com.smart.tablet.models.ActivityAction((R.string.msg_hide_top_guest_button), null));

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
        com.smart.tablet.fragments.WeatherFragment weatherFragment = new com.smart.tablet.fragments.WeatherFragment();
        weatherFragment.setFragmentListener(fragmentListener);
        weatherFragment.setParentListener(parentListener);
        weatherFragment.setArguments(_bundle);

        com.smart.tablet.fragments.NavigationFragment fragment = new com.smart.tablet.fragments.NavigationFragment();
        fragment.setFragmentListener(fragmentListener);
        fragment.setParentListener(parentListener);
        fragment.setChildFragment(weatherFragment);

        fragmentListener.onUpdateFragment(fragment);
    }

    private void moveToMapFragment() {
        com.smart.tablet.fragments.MapFragment mapFragment = new com.smart.tablet.fragments.MapFragment();
        mapFragment.setFragmentListener(fragmentListener);
        mapFragment.setParentListener(parentListener);
        mapFragment.setArguments(_bundle);

        com.smart.tablet.fragments.NavigationFragment fragment = new com.smart.tablet.fragments.NavigationFragment();
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

        com.smart.tablet.fragments.ServiceFragment serviceFragment = new com.smart.tablet.fragments.ServiceFragment();
        serviceFragment.setFragmentListener(fragmentListener);
        serviceFragment.setActivityListener(parentListener);
        serviceFragment.setArguments(bundle);

        com.smart.tablet.fragments.NavigationFragment fragment = new com.smart.tablet.fragments.NavigationFragment();
        fragment.setFragmentListener(fragmentListener);
        fragment.setParentListener(parentListener);
        fragment.setChildFragment(serviceFragment);
        fragment.setArguments(bundle);

        fragmentListener.onUpdateFragment(fragment);
    }

    private void moveToCategoryFragment() {
        new com.smart.tablet.tasks.RetrieveSingleCategory(getContext(), _main_category_id)
                .onSuccess(new com.smart.tablet.listeners.AsyncResultBag.Success() {
                    @Override
                    public void onSuccess(Object result) {
                        if (result != null) {
                            com.smart.tablet.entities.Category category = (com.smart.tablet.entities.Category) result;

                            Bundle bundle = new Bundle();

                            bundle.putInt(getString(R.string.param_category_id), _main_category_id);
                            bundle.putBoolean(getString(R.string.param_has_children), (category.getChildren_count() > 0));
                            bundle.putString(getString(R.string.param_listing_type), (category.isIs_marketing_partner() ? "mp" : "gsd"));
                            bundle.putString(getString(R.string.param_embed_url), category.getEmbed_url());

                            com.smart.tablet.fragments.NavigationFragment fragment = new com.smart.tablet.fragments.NavigationFragment();
                            fragment.setFragmentListener(fragmentListener);
                            fragment.setParentListener(parentListener);
                            fragment.setArguments(bundle);

                            fragmentListener.onUpdateFragment(fragment);
                        }
                    }
                })
                .execute();
    }

    private void showScrollIndicator() {
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.bounce);

        _scrollIndicator.setVisibility(View.VISIBLE);
        _scrollIndicator.setAnimation(animation);
        _scrollIndicator.animate();
    }

    private void getCategories() {
        new com.smart.tablet.tasks.RetrieveCategories(getContext(), _category_id, null)
                .onSuccess(new com.smart.tablet.listeners.AsyncResultBag.Success() {
                    @Override
                    public void onSuccess(Object result) {
                        if (result != null) {
                            com.smart.tablet.entities.Category[] categories = (com.smart.tablet.entities.Category[]) result;

                            if (categories.length > 4) {
                                showScrollIndicator();
                            }

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
