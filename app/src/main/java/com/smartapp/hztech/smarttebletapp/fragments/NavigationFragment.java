package com.smartapp.hztech.smarttebletapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartapp.hztech.smarttebletapp.Constants;
import com.smartapp.hztech.smarttebletapp.R;
import com.smartapp.hztech.smarttebletapp.entities.Category;
import com.smartapp.hztech.smarttebletapp.listeners.AsyncResultBag;
import com.smartapp.hztech.smarttebletapp.listeners.FragmentActivityListener;
import com.smartapp.hztech.smarttebletapp.listeners.FragmentListener;
import com.smartapp.hztech.smarttebletapp.tasks.RetrieveCategories;
import com.smartapp.hztech.smarttebletapp.tasks.RetrieveSetting;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

public class NavigationFragment extends Fragment {
    private FrameLayout fragmentContainer;
    private Fragment _childFragment;
    private LinearLayout menu_item, menu_items;
    private TextView menu_item_1, menu_item_2, menu_item_3;
    private FragmentListener mainFragmentListener;
    private FragmentActivityListener parentListener;
    private ArrayList<MenuItem> menuItems;
    private LayoutInflater _inflater;
    private int _category_id;
    private Boolean _has_children;
    private String _listing_type;
    private FragmentListener childFragmentListener = new FragmentListener() {
        @Override
        public void onUpdateFragment(Fragment newFragment) {
            Log.d("FragmentUpdated", "From: NavigationFragment, Fragment: " + newFragment.getClass().getName());
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

            transaction.replace(fragmentContainer.getId(), newFragment);
            transaction.addToBackStack(null);

            transaction.commit();
        }
    };
    private FragmentActivityListener activityListener = new FragmentActivityListener() {
        @Override
        public void receive(int message, Object arguments) {
            switch (message) {
                case R.string.msg_update_menu:
                    if (arguments instanceof ArrayList && _inflater != null) {
                        menuItems = (ArrayList<MenuItem>) arguments;
                        bindMenuItems(_inflater);
                    }
                default:
                    if (parentListener != null)
                        parentListener.receive(message, null);
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        _inflater = inflater;

        View view = inflater.inflate(R.layout.main_fragment, container, false);
        Bundle bundle = getArguments();

        menuItems = new ArrayList<>();

        fragmentContainer = view.findViewById(R.id.listing_fragment_container);
        menu_item = view.findViewById(R.id.menu_item);
        menu_items = view.findViewById(R.id.menu_items);

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

        if (fragmentContainer != null) {

            if (_childFragment == null) {
                CategoryFragment categoryFragment = new CategoryFragment();
                categoryFragment.setFragmentListener(childFragmentListener);
                categoryFragment.setParentListener(activityListener);
                categoryFragment.setArguments(bundle);

                _childFragment = categoryFragment;
            }

            getChildFragmentManager().beginTransaction()
                    .add(fragmentContainer.getId(), _childFragment).commit();
        }

        bindMenuItems(inflater);

        activityListener.receive(R.string.msg_show_sidebar, null);
        activityListener.receive(R.string.msg_reset_menu, null);

        return view;
    }

    private void bindMenuItems(final LayoutInflater inflater) {
        if (menuItems.isEmpty()) {
            new RetrieveSetting(getContext(),
                    Constants.TOP_GUEST_CATEGORIES,
                    Constants.TOP_MENU_SHOW_WELCOME,
                    Constants.TOP_MENU_WELCOME_TEXT,
                    Constants.TOP_MP_CATEGORIES)
                    .onSuccess(new AsyncResultBag.Success() {
                        @Override
                        public void onSuccess(Object result) {
                            HashMap<String, String> values = result != null ? (HashMap<String, String>) result : null;

                            if (values != null) {
                                if (_listing_type.equals("gsd") && values.containsKey(Constants.TOP_MENU_SHOW_WELCOME) && values.get(Constants.TOP_MENU_SHOW_WELCOME).equals("1")) {
                                    WelcomeFragment fragment = new WelcomeFragment();
                                    fragment.setParentListener(parentListener);

                                    MenuItem item = new MenuItem();
                                    item.title = (values.containsKey(Constants.TOP_MENU_WELCOME_TEXT) && !values.get(Constants.TOP_MENU_WELCOME_TEXT).isEmpty()) ? values.get(Constants.TOP_MENU_WELCOME_TEXT) : "Welcome";
                                    item.fragment = fragment;

                                    menuItems.add(item);
                                }

                                JSONArray categories = new JSONArray();
                                int[] category_ids = null;

                                if (_listing_type.equals("mp") && values.containsKey(Constants.TOP_MP_CATEGORIES) && !values.get(Constants.TOP_MP_CATEGORIES).isEmpty()) {
                                    try {
                                        categories = new JSONArray(values.get(Constants.TOP_MP_CATEGORIES));
                                        category_ids = new int[categories.length()];
                                        for (int i = 0; i < categories.length(); i++) {
                                            category_ids[i] = Integer.parseInt(categories.getString(i));
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                if (_listing_type.equals("gsd") && values.containsKey(Constants.TOP_GUEST_CATEGORIES) && !values.get(Constants.TOP_GUEST_CATEGORIES).isEmpty()) {
                                    try {
                                        categories = new JSONArray(values.get(Constants.TOP_GUEST_CATEGORIES));
                                        category_ids = new int[categories.length()];
                                        for (int i = 0; i < categories.length(); i++) {
                                            category_ids[i] = Integer.parseInt(categories.getString(i));
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                if (categories.length() > 0) {
                                    new RetrieveCategories(getContext(), 0, null, category_ids)
                                            .onSuccess(new AsyncResultBag.Success() {
                                                @Override
                                                public void onSuccess(Object result) {
                                                    if (result != null) {
                                                        Category[] categories = (Category[]) result;

                                                        for (int i = 0; i < categories.length; i++) {
                                                            Bundle bundle = new Bundle();
                                                            bundle.putInt(getString(R.string.param_category_id), categories[i].getId());
                                                            bundle.putBoolean(getString(R.string.param_has_children), (categories[i].getChildren_count() > 0));

                                                            CategoryFragment categoryFragment = new CategoryFragment();
                                                            categoryFragment.setFragmentListener(childFragmentListener);
                                                            categoryFragment.setParentListener(activityListener);
                                                            categoryFragment.setArguments(bundle);

                                                            MenuItem item1 = new MenuItem();
                                                            item1.title = categories[i].getName();
                                                            item1.fragment = categoryFragment;

                                                            menuItems.add(item1);
                                                        }
                                                    }
                                                    finalizeNavigation(inflater);
                                                }
                                            })
                                            .execute();
                                } else {
                                    finalizeNavigation(inflater);
                                }
                            }
                        }
                    })
                    .execute();
        } else {
            finalizeNavigation(inflater);
        }
    }

    public void finalizeNavigation(LayoutInflater inflater) {
        menu_items.removeAllViews();

        for (int i = 0; i < menuItems.size(); i++) {
            final MenuItem menuItem = menuItems.get(i);
            LinearLayout view = (LinearLayout) inflater.inflate(R.layout.top_menu_item, null);

            ((TextView) view.findViewById(R.id.menu_item_text)).setText(menuItem.title);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    childFragmentListener.onUpdateFragment(menuItem.fragment);
                }
            });

            menu_items.addView(view);
        }
    }

    public void setFragmentListener(FragmentListener fragmentListener) {
        mainFragmentListener = fragmentListener;
    }

    public void setParentListener(FragmentActivityListener parentListener) {
        this.parentListener = parentListener;
    }

    public Fragment getChildFragment() {
        return _childFragment;
    }

    public void setChildFragment(Fragment fragment) {
        _childFragment = fragment;
    }

    public static class MenuItem {
        String title;
        Fragment fragment;
    }
}
