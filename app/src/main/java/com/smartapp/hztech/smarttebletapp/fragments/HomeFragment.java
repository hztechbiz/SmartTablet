package com.smartapp.hztech.smarttebletapp.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ListView;

import com.smartapp.hztech.smarttebletapp.GridAdapter;
import com.smartapp.hztech.smarttebletapp.R;
import com.smartapp.hztech.smarttebletapp.entities.Category;
import com.smartapp.hztech.smarttebletapp.listeners.AsyncResultBag;
import com.smartapp.hztech.smarttebletapp.listeners.FragmentListener;
import com.smartapp.hztech.smarttebletapp.tasks.RetrieveCategories;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class HomeFragment extends Fragment {
    private View view;
    private ListView list_Category;
    private List<Category> _categories;
    private GridView gridView;
    HashMap<String, String> item;
    private GridAdapter gridAdapter;
    FrameLayout fragment_container;
    private int _parent_id;

    FragmentListener mCallback;

    public HomeFragment() {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (FragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentUpdate");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        fragment_container = view.findViewById(R.id.services_fragment_container);
        Bundle bundle = getArguments();

        if (bundle != null) {
            _parent_id = Integer.parseInt(getArguments().getString("Category_id"));
        }

        gridView = (GridView) view.findViewById(R.id.grdView);
        _categories = new ArrayList<>();

        getCategories();

        //CustomCategoryAdapter adapter = new CustomCategoryAdapter(getActivity(), R.layout.category_rows, _categories);
        //list_Category = (ListView) view.findViewById(R.id.list_calllog);
        //list_Category.setAdapter(adapter);
        //GridAdapter gridAdapter = new GridAdapter(getActivity(),R.layout.category_rows, item);
        gridView = view.findViewById(R.id.list_calllog);

        gridAdapter = new GridAdapter(getContext(), _categories);
        gridView.setAdapter(gridAdapter);
        Log.d("adapterListener", "checking");
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Bundle bundle = new Bundle();
                bundle.putString("Category_id", String.valueOf(17));

                HomeFragment hmFragment = new HomeFragment();
                hmFragment.setArguments(bundle);
                updateFragment(hmFragment);
            }
        });
        return view;
    }

    public void updateFragment(Fragment newFragment) {

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

        transaction.replace(fragment_container.getId(), newFragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }

    private void getCategories() {

        new RetrieveCategories(getContext(), _parent_id)
                .onSuccess(new AsyncResultBag.Success() {
                    @Override
                    public void onSuccess(Object result) {
                        if (result != null) {
                            Category[] categories = (Category[]) result;

                            _categories.clear();
                            _categories.addAll(Arrays.asList(categories));
                            gridAdapter.notifyDataSetChanged();
                        }
                    }
                }).execute();
    }
}
