package com.smartapp.hztech.smarttebletapp.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartapp.hztech.smarttebletapp.Constants;
import com.smartapp.hztech.smarttebletapp.MainActivity;
import com.smartapp.hztech.smarttebletapp.R;
import com.smartapp.hztech.smarttebletapp.entities.Category;
import com.smartapp.hztech.smarttebletapp.entities.Service;
import com.smartapp.hztech.smarttebletapp.helpers.Util;
import com.smartapp.hztech.smarttebletapp.listeners.AsyncResultBag;
import com.smartapp.hztech.smarttebletapp.listeners.FragmentActivityListener;
import com.smartapp.hztech.smarttebletapp.listeners.FragmentListener;
import com.smartapp.hztech.smarttebletapp.models.ActivityAction;
import com.smartapp.hztech.smarttebletapp.tasks.RetrieveCategories;
import com.smartapp.hztech.smarttebletapp.tasks.RetrieveSetting;
import com.smartapp.hztech.smarttebletapp.tasks.RetrieveSingleService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class NavigationFragment extends Fragment {
    private FrameLayout fragmentContainer;
    private Fragment _childFragment;
    private LinearLayout menu_item, menu_items;
    private TextView menu_item_1, menu_item_2, menu_item_3;
    private ImageButton btn_nav_left, btn_nav_right;
    private HorizontalScrollView scroll_view;
    private FragmentListener mainFragmentListener;
    private FragmentActivityListener parentListener;
    private ArrayList<MenuItem> menuItems;
    private LayoutInflater _inflater;
    private int _category_id;
    private Boolean _has_children, _show_first;
    private String _listing_type, _embed_url;
    private MainActivity _activity;
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
                        parentListener.receive(message, arguments);
            }
        }
    };

    private void getServiceMenu(final String service_id_str) {
        final int service_id = Integer.parseInt(service_id_str);

        new RetrieveSingleService(getContext(), service_id)
                .onSuccess(new AsyncResultBag.Success() {
                    @Override
                    public void onSuccess(Object result) {
                        Service service = result != null ? (Service) result : null;
                        ArrayList<NavigationFragment.MenuItem> menu_items_objects = new ArrayList<>();
                        String[] menu_items = new String[]{Constants.TOP_MENU_SHOW_ABOUT, Constants.TOP_MENU_SHOW_LOCATION, Constants.TOP_MENU_SHOW_VIDEO, Constants.TOP_MENU_SHOW_GALLERY, Constants.TOP_MENU_SHOW_MENU, Constants.TOP_MENU_SHOW_BOOK, Constants.TOP_MENU_SHOW_OFFERS, Constants.TOP_MENU_SHOW_TESTIMONIALS};

                        if (service != null) {
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

                                            Bundle bundle = new Bundle();
                                            bundle.putInt(getString(R.string.param_service_id), service_id);

                                            for (int j = 0; j < menu_items.length; j++) {
                                                if (meta_obj.getString("meta_key").equals(menu_items[j]) && meta_obj.getString("meta_value").equals("1")) {
                                                    NavigationFragment.MenuItem item = new NavigationFragment.MenuItem();

                                                    switch (menu_items[j]) {
                                                        case Constants.TOP_MENU_SHOW_ABOUT:
                                                            ServiceAboutFragment serviceAboutFragment = new ServiceAboutFragment();
                                                            serviceAboutFragment.setArguments(bundle);
                                                            serviceAboutFragment.setFragmentListener(childFragmentListener);

                                                            item.title = "ABOUT US";
                                                            item.fragment = serviceAboutFragment;

                                                            break;
                                                        case Constants.TOP_MENU_SHOW_LOCATION:
                                                            ServiceLocationFragment serviceLocationFragment = new ServiceLocationFragment();
                                                            serviceLocationFragment.setArguments(bundle);
                                                            serviceLocationFragment.setFragmentListener(childFragmentListener);

                                                            item.title = "LOCATION";
                                                            item.fragment = serviceLocationFragment;

                                                            break;
                                                        case Constants.TOP_MENU_SHOW_VIDEO:
                                                            ServiceVideoFragment serviceVideoFragment = new ServiceVideoFragment();
                                                            serviceVideoFragment.setArguments(bundle);

                                                            item.title = "VIDEO";
                                                            item.fragment = serviceVideoFragment;

                                                            break;
                                                        case Constants.TOP_MENU_SHOW_GALLERY:
                                                            ServiceGalleryFragment serviceGalleryFragment = new ServiceGalleryFragment();
                                                            serviceGalleryFragment.setArguments(bundle);

                                                            item.title = "GALLERY";
                                                            item.fragment = serviceGalleryFragment;

                                                            break;
                                                        case Constants.TOP_MENU_SHOW_MENU:
                                                            ServiceMenuFragment serviceMenuFragment = new ServiceMenuFragment();
                                                            serviceMenuFragment.setArguments(bundle);
                                                            serviceMenuFragment.setFragmentListener(childFragmentListener);

                                                            item.title = "MENUS";
                                                            item.fragment = serviceMenuFragment;

                                                            break;
                                                        case Constants.TOP_MENU_SHOW_OFFERS:
                                                            ServiceOffersFragment serviceOffersFragment = new ServiceOffersFragment();
                                                            serviceOffersFragment.setArguments(bundle);
                                                            serviceOffersFragment.setFragmentListener(childFragmentListener);

                                                            item.title = "OFFERS";
                                                            item.fragment = serviceOffersFragment;

                                                            break;
                                                        case Constants.TOP_MENU_SHOW_TESTIMONIALS:
                                                            ServiceTestimonialsFragment serviceTestimonialsFragment = new ServiceTestimonialsFragment();
                                                            serviceTestimonialsFragment.setArguments(bundle);
                                                            serviceTestimonialsFragment.setFragmentListener(childFragmentListener);

                                                            item.title = "TESTIMONIALS";
                                                            item.fragment = serviceTestimonialsFragment;

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
                                    menuItems = menu_items_objects;
                                    bindMenuItems(_inflater);
                                }
                            }
                        }
                    }
                })
                .execute();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        _inflater = inflater;

        View view = inflater.inflate(R.layout.main_fragment, container, false);

        _activity = (MainActivity) getActivity();

        Bundle bundle = getArguments();
        final FragmentManager fragmentManager = getChildFragmentManager();

        menuItems = new ArrayList<>();

        fragmentContainer = view.findViewById(R.id.listing_fragment_container);
        menu_item = view.findViewById(R.id.menu_item);
        menu_items = view.findViewById(R.id.menu_items);
        btn_nav_left = view.findViewById(R.id.btn_arrow_left);
        btn_nav_right = view.findViewById(R.id.btn_arrow_right);
        scroll_view = view.findViewById(R.id.scrollView);

        _category_id = 0;
        _has_children = false;
        _listing_type = "gsd";
        _embed_url = null;
        _show_first = false;

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

            if (bundle.containsKey(getString(R.string.param_show_first))) {
                _show_first = bundle.getBoolean(getString(R.string.param_show_first));
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

            fragmentManager.beginTransaction()
                    .add(fragmentContainer.getId(), _childFragment).commit();
        }

        btn_nav_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scroll_view.fullScroll(View.FOCUS_LEFT);
            }
        });

        btn_nav_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scroll_view.fullScroll(View.FOCUS_RIGHT);
            }
        });

        bindMenuItems(inflater);

        ArrayList<ActivityAction> actions = new ArrayList<>();

        actions.add(new ActivityAction((R.string.msg_show_sidebar), null));
        actions.add(new ActivityAction((R.string.msg_reset_menu), null));
        actions.add(new ActivityAction((R.string.msg_hide_home_button), null));
        actions.add(new ActivityAction((R.string.msg_show_back_button), null));
        actions.add(new ActivityAction((R.string.msg_reset_background), null));
        actions.add(new ActivityAction((R.string.msg_show_logo_button), null));
        actions.add(new ActivityAction((R.string.msg_hide_main_logo), null));
        actions.add(new ActivityAction((R.string.msg_hide_welcome_button), null));
        actions.add(new ActivityAction((R.string.msg_hide_guest_button), null));
        actions.add(new ActivityAction((R.string.msg_hide_app_heading), null));
        actions.add(new ActivityAction((R.string.msg_show_copyright), null));

        if (_listing_type != null && _listing_type.equals("mp")) {
            actions.add(new ActivityAction((R.string.msg_show_top_guest_button), null));
        } else {
            actions.add(new ActivityAction((R.string.msg_hide_top_guest_button), null));
        }

        _activity.takeActions(actions);

        /*
        activityListener.receive(R.string.msg_show_sidebar, null);
        activityListener.receive(R.string.msg_reset_menu, null);
        activityListener.receive(R.string.msg_hide_home_button, null);
        activityListener.receive(R.string.msg_show_back_button, null);
        activityListener.receive(R.string.msg_reset_background, null);
        activityListener.receive(R.string.msg_hide_main_logo, null);
        activityListener.receive(R.string.msg_show_logo_button, null);
        activityListener.receive(R.string.msg_hide_welcome_button, null);
        activityListener.receive(R.string.msg_hide_guest_button, null);
        activityListener.receive(R.string.msg_hide_app_heading, null);
        activityListener.receive(R.string.msg_show_copyright, null);
        activityListener.receive(R.string.msg_hide_top_guest_button, null);
        */

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
                                    item.title = ((values.containsKey(Constants.TOP_MENU_WELCOME_TEXT) && !values.get(Constants.TOP_MENU_WELCOME_TEXT).isEmpty()) ? values.get(Constants.TOP_MENU_WELCOME_TEXT) : "WELCOME").toUpperCase();
                                    item.fragment = fragment;

                                    //menuItems.add(item);
                                    ArrayList<ActivityAction> actions = new ArrayList<>();
                                    actions.add(new ActivityAction((R.string.msg_show_welcome_button), null));

                                    Intent intent = new Intent(getString(R.string.param_activity_action));
                                    intent.putParcelableArrayListExtra(getString(R.string.param_activity_actions), actions);

                                    getContext().sendBroadcast(intent);

                                    //activityListener.receive(R.string.msg_show_welcome_button, null);
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
                                                            item1.title = categories[i].getName().toUpperCase();
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
            TextView txt_item = view.findViewById(R.id.menu_item_text);

            txt_item.setTypeface(Util.getTypeFace(getContext()));
            txt_item.setText(menuItem.title);

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

    public void takeActions(ArrayList<ActivityAction> actions) {
        if (actions != null) {
            for (ActivityAction action :
                    actions) {
                if (action.getKey() == R.string.msg_update_menu) {
                    getServiceMenu(action.getData());
                } else {
                    activityListener.receive(action.getKey(), action.getData());
                }
            }
        }
    }

    public static class MenuItem {
        String title;
        Fragment fragment;
    }
}
