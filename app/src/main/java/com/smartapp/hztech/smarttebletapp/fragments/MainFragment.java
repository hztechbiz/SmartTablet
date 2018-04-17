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
import android.widget.TextView;

import com.smartapp.hztech.smarttebletapp.R;
import com.smartapp.hztech.smarttebletapp.listeners.AsyncResultBag;
import com.smartapp.hztech.smarttebletapp.listeners.FragmentListener;
import com.smartapp.hztech.smarttebletapp.tasks.RetrieveSetting;

import java.util.HashMap;

public class MainFragment extends Fragment implements FragmentListener {
    FrameLayout fragment_container;
    TextView menu_item_1, menu_item_2, menu_item_3, menu_item_4;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);

        fragment_container = view.findViewById(R.id.services_fragment_container);
        menu_item_1 = view.findViewById(R.id.menu_item_1);
        menu_item_2 = view.findViewById(R.id.menu_item_2);
        menu_item_3 = view.findViewById(R.id.menu_item_3);
        menu_item_4 = view.findViewById(R.id.menu_item_4);

        if (fragment_container != null) {

            WelcomeFragment welcomeFragment = new WelcomeFragment();

            getChildFragmentManager().beginTransaction()
                    .add(fragment_container.getId(), welcomeFragment).commit();
        }

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
                    updateFragment(fragment);
                } else {
                    Bundle bundle = new Bundle();
                    Object category_id = v.getTag(R.string.tag_value);

                    if (category_id != null) {
                        bundle.putInt(getString(R.string.param_category_id), Integer.parseInt(category_id.toString()));
                    }

                    CategoryFragment fragment = new CategoryFragment();
                    fragment.setFragmentListener(MainFragment.this);
                    fragment.setArguments(bundle);

                    updateFragment(fragment);
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

                            enable_item = values.containsKey("top_menu_item_3_show") && values.get("top_menu_item_3_show").equals("1");
                            item_text = values.containsKey("top_menu_item_3_text") ? values.get("top_menu_item_3_text") : "";
                            category_id = values.containsKey("top_menu_item_3_category") ? values.get("top_menu_item_3_category") : null;

                            menu_item_3.setVisibility(enable_item ? View.VISIBLE : View.INVISIBLE);
                            menu_item_3.setText(item_text);
                            menu_item_3.setTag(R.string.tag_action, "category");
                            menu_item_3.setTag(R.string.tag_value, category_id);

                            enable_item = values.containsKey("top_menu_item_4_show") && values.get("top_menu_item_4_show").equals("1");
                            item_text = values.containsKey("top_menu_item_4_text") ? values.get("top_menu_item_4_text") : "";
                            category_id = values.containsKey("top_menu_item_4_category") ? values.get("top_menu_item_4_category") : null;

                            menu_item_4.setVisibility(enable_item ? View.VISIBLE : View.INVISIBLE);
                            menu_item_4.setText(item_text);
                            menu_item_4.setTag(R.string.tag_action, "category");
                            menu_item_4.setTag(R.string.tag_value, category_id);

                            menu_item_1.setOnClickListener(menuItemClickListener);
                            menu_item_2.setOnClickListener(menuItemClickListener);
                            menu_item_3.setOnClickListener(menuItemClickListener);
                            menu_item_4.setOnClickListener(menuItemClickListener);
                        }
                    }
                })
                .execute();
    }

    public void updateFragment(Fragment newFragment) {
        Log.d("ServiceDetails", newFragment.getClass().getName());

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

        transaction.replace(fragment_container.getId(), newFragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }

    @Override
    public void onUpdateFragment(Fragment fragment) {
        updateFragment(fragment);
    }
}
