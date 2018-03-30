package com.smartapp.hztech.smarttebletapp.HomeSection;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.smartapp.hztech.smarttebletapp.R;
import com.smartapp.hztech.smarttebletapp.fragments.CategoryFragment;
import com.smartapp.hztech.smarttebletapp.fragments.HomeFragment;
import com.smartapp.hztech.smarttebletapp.fragments.ServicesFragment;
import com.smartapp.hztech.smarttebletapp.listeners.FragmentListener;

public class Main2Activity extends FragmentActivity implements FragmentListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main2);

        if (findViewById(R.id.fragment_container) != null) {

            if (savedInstanceState != null) {
                return;
            }

            HomeFragment firstFragment = new HomeFragment();

            firstFragment.setArguments(getIntent().getExtras());

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, firstFragment).commit();
        }
    }

    public void onNavItemClick(View view) {
        switch (view.getId()) {
            case R.id.itemHome:
                ServicesFragment fragment = new ServicesFragment();
                updateFragment(fragment);
                break;
            case R.id.itemHow:
                CategoryFragment categoryFragment = new CategoryFragment();
                updateFragment(categoryFragment);
                break;
        }
    }

    public void updateFragment(Fragment newFragment) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }

    @Override
    public void onUpdateFragment(Fragment fragment) {
        updateFragment(fragment);
    }
}
