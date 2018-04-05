package com.smartapp.hztech.smarttebletapp.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.arch.lifecycle.HolderFragment;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;

import com.smartapp.hztech.smarttebletapp.GridAdapter;
import com.smartapp.hztech.smarttebletapp.R;
import com.smartapp.hztech.smarttebletapp.listeners.FragmentListener;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeFragment extends Fragment {
    private View view;
    private ListView list_Category;
    private ArrayList<HashMap<String, String>> categories;
    private GridView gridView;
    HashMap<String, String> item;

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

        gridView =(GridView) view.findViewById(R.id.grdView);
        /*
        if (view == null) {

        } else {
            ViewGroup parent = (ViewGroup) view.getParent();
            parent.removeView(view);
        }
        */

        categories = getCategory();
        CustomCategoryAdapter adapter = new CustomCategoryAdapter(getActivity(),
                R.layout.category_rows, categories);
        list_Category = (ListView) view.findViewById(R.id.list_calllog);
        list_Category.setAdapter(adapter);

//        GridAdapter gridAdapter = new
//                GridAdapter(getActivity(),R.layout.category_rows, item);
//        gridView.setAdapter(gridAdapter);
        return view;

    }

    @SuppressLint("NewApi")
    public ArrayList<HashMap<String, String>> getCategory() {
        ArrayList<HashMap<String, String>> cateyList = new ArrayList<HashMap<String, String>>();

        item = new HashMap<>();
        item.put("Logo", "Logo ");
        item.put("Name", "Resort");
        item.put("Description", "Welcome to the Resorts. Everything you need to know about your room and the Resort facilities");


        cateyList.add(item);
        cateyList.add(item);
        cateyList.add(item);
        cateyList.add(item);
        cateyList.add(item);
        cateyList.add(item);
        cateyList.add(item);



        return cateyList;
    }
}
