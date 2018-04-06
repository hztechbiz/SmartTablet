package com.smartapp.hztech.smarttebletapp.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

        return view;
    }

    private void getCategories() {

        new RetrieveCategories(getContext(), 0)
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
