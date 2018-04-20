package com.smartapp.hztech.smarttebletapp.fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.smartapp.hztech.smarttebletapp.R;
import com.smartapp.hztech.smarttebletapp.entities.Category;
import com.smartapp.hztech.smarttebletapp.listeners.AsyncResultBag;
import com.smartapp.hztech.smarttebletapp.listeners.FragmentListener;
import com.smartapp.hztech.smarttebletapp.tasks.RetrieveSetting;
import com.smartapp.hztech.smarttebletapp.tasks.RetrieveSingleCategory;

import java.util.HashMap;

public class MainFragment extends Fragment {
    private FrameLayout fragmentContainer;
    private Fragment _childFragment;
    private TextView menu_item_1, menu_item_2, menu_item_3, menu_item_4;
    private FragmentListener mainFragmentListener;
    private FragmentListener childFragmentListener = new FragmentListener() {
        @Override
        public void onUpdateFragment(Fragment newFragment) {
            Log.d("FragmentUpdated", "From: MainFragment, Fragment: " + newFragment.getClass().getName());
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

            transaction.replace(fragmentContainer.getId(), newFragment);
            transaction.addToBackStack(null);

            transaction.commit();
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);

        fragmentContainer = view.findViewById(R.id.listing_fragment_container);
        menu_item_1 = view.findViewById(R.id.menu_item_1);
        menu_item_2 = view.findViewById(R.id.menu_item_2);
        menu_item_3 = view.findViewById(R.id.menu_item_3);
        menu_item_4 = view.findViewById(R.id.menu_item_4);

        if (fragmentContainer != null) {

            if (_childFragment == null)
                _childFragment = new WelcomeFragment();

            getChildFragmentManager().beginTransaction()
                    .add(fragmentContainer.getId(), _childFragment).commit();
        }
        menu_item_1.setFilters(new InputFilter[]{new
                InputFilter.AllCaps()});
        menu_item_2.setFilters(new InputFilter[]{new
                InputFilter.AllCaps()});
        menu_item_3.setFilters(new InputFilter[]{new
                InputFilter.AllCaps()});
        menu_item_4.setFilters(new InputFilter[]{new
                InputFilter.AllCaps()});

        Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.lato_regular);
        menu_item_1.setTypeface(typeface);
        menu_item_2.setTypeface(typeface);
        menu_item_3.setTypeface(typeface);
        menu_item_4.setTypeface(typeface);

        bindMenuItems();

        return view;
    }

    private void bindMenuItems() {
        final View.OnClickListener menuItemClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object action = v.getTag(R.string.tag_action);

                if (action.equals("welcome")) {
                    WelcomeFragment fragment = new WelcomeFragment();
                    childFragmentListener.onUpdateFragment(fragment);
                } else {
                    Bundle bundle = new Bundle();
                    Object category_id = v.getTag(R.string.tag_value);
                    Object has_children = v.getTag(R.string.tag_has_children);

                    if (category_id != null) {
                        bundle.putInt(getString(R.string.param_category_id), Integer.parseInt(category_id.toString()));
                    }

                    if (has_children != null) {
                        bundle.putBoolean(getString(R.string.param_has_children), Boolean.valueOf(has_children.toString()));
                    }

                    CategoryFragment fragment = new CategoryFragment();
                    fragment.setFragmentListener(childFragmentListener);
                    fragment.setArguments(bundle);

                    childFragmentListener.onUpdateFragment(fragment);
                }
            }
        };

        new RetrieveSetting(getContext(),
                "top_menu_item_1_show",
                "top_menu_item_1_text",
                "top_menu_item_2_show",
                "top_menu_item_2_text",
                "top_menu_item_2_category",
                "top_menu_item_3_show",
                "top_menu_item_3_text",
                "top_menu_item_3_category",
                "top_menu_item_4_show",
                "top_menu_item_4_text",
                "top_menu_item_4_category")
                .onSuccess(new AsyncResultBag.Success() {
                    @Override
                    public void onSuccess(Object result) {
                        HashMap<String, String> values = result != null ? (HashMap<String, String>) result : null;

                        if (values != null) {
                            boolean enable_item = values.containsKey("top_menu_item_1_show") && values.get("top_menu_item_1_show").equals("1");
                            String item_text = values.containsKey("top_menu_item_1_text") ? values.get("top_menu_item_1_text") : "";
                            String category_id;

                            menu_item_1.setVisibility(enable_item ? View.VISIBLE : View.INVISIBLE);
                            menu_item_1.setText(item_text);
                            menu_item_1.setTag(R.string.tag_action, "welcome");

                            enable_item = values.containsKey("top_menu_item_2_show") && values.get("top_menu_item_2_show").equals("1");
                            item_text = values.containsKey("top_menu_item_2_text") ? values.get("top_menu_item_2_text") : "";
                            category_id = values.containsKey("top_menu_item_2_category") ? values.get("top_menu_item_2_category") : null;

                            menu_item_2.setVisibility(enable_item ? View.VISIBLE : View.INVISIBLE);
                            menu_item_2.setText(item_text);
                            menu_item_2.setTag(R.string.tag_action, "category");
                            menu_item_2.setTag(R.string.tag_value, category_id);

                            if (category_id != null) {
                                new RetrieveSingleCategory(getContext(), Integer.parseInt(category_id))
                                        .onSuccess(new AsyncResultBag.Success() {
                                            @Override
                                            public void onSuccess(Object result) {
                                                Category category = result != null ? (Category) result : null;

                                                if (category != null) {
                                                    menu_item_2.setTag(R.string.tag_has_children, (category.getChildren_count() > 0));
                                                }
                                            }
                                        })
                                        .execute();
                            }

                            enable_item = values.containsKey("top_menu_item_3_show") && values.get("top_menu_item_3_show").equals("1");
                            item_text = values.containsKey("top_menu_item_3_text") ? values.get("top_menu_item_3_text") : "";
                            category_id = values.containsKey("top_menu_item_3_category") ? values.get("top_menu_item_3_category") : null;

                            menu_item_3.setVisibility(enable_item ? View.VISIBLE : View.INVISIBLE);
                            menu_item_3.setText(item_text);
                            menu_item_3.setTag(R.string.tag_action, "category");
                            menu_item_3.setTag(R.string.tag_value, category_id);

                            if (category_id != null) {
                                new RetrieveSingleCategory(getContext(), Integer.parseInt(category_id))
                                        .onSuccess(new AsyncResultBag.Success() {
                                            @Override
                                            public void onSuccess(Object result) {
                                                Category category = result != null ? (Category) result : null;

                                                if (category != null) {
                                                    menu_item_3.setTag(R.string.tag_has_children, (category.getChildren_count() > 0));
                                                }
                                            }
                                        })
                                        .execute();
                            }

                            enable_item = values.containsKey("top_menu_item_4_show") && values.get("top_menu_item_4_show").equals("1");
                            item_text = values.containsKey("top_menu_item_4_text") ? values.get("top_menu_item_4_text") : "";
                            category_id = values.containsKey("top_menu_item_4_category") ? values.get("top_menu_item_4_category") : null;

                            menu_item_4.setVisibility(enable_item ? View.VISIBLE : View.INVISIBLE);
                            menu_item_4.setText(item_text);
                            menu_item_4.setTag(R.string.tag_action, "category");
                            menu_item_4.setTag(R.string.tag_value, category_id);

                            if (category_id != null) {
                                new RetrieveSingleCategory(getContext(), Integer.parseInt(category_id))
                                        .onSuccess(new AsyncResultBag.Success() {
                                            @Override
                                            public void onSuccess(Object result) {
                                                Category category = result != null ? (Category) result : null;

                                                if (category != null) {
                                                    menu_item_4.setTag(R.string.tag_has_children, (category.getChildren_count() > 0));
                                                }
                                            }
                                        })
                                        .execute();
                            }

                            menu_item_1.setOnClickListener(menuItemClickListener);
                            menu_item_2.setOnClickListener(menuItemClickListener);
                            menu_item_3.setOnClickListener(menuItemClickListener);
                            menu_item_4.setOnClickListener(menuItemClickListener);
                        }
                    }
                })
                .execute();
    }

    public void setFragmentListener(FragmentListener fragmentListener) {
        mainFragmentListener = fragmentListener;
    }

    public void setChildFragment(Fragment fragment) {
        _childFragment = fragment;
    }

    public Fragment getChildFragment() {
        return _childFragment;
    }
}
