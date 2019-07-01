package com.smart.tablet.ServicesSection;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smart.tablet.R;

public class NavigationPage extends Fragment {

    //ServicesHome.OnFragmentUpdate mCallBack;
public NavigationPage(){

}
//public void onAttach(Activity activity){
//    super.onAttach(activity);
//    try {
//        mCallBack = (ServicesHome.OnFragmentUpdate) activity;
//    }
//    catch (ClassCastException e){
//        throw new ClassCastException(activity.toString() +
//        " must implement OnFragmentUpdate");
//    }
//}


    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container
    , Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.services_resort_facility, container, false);
//        Button back = view.findViewById(R.id.bck);
//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ServicesHome fragmentt = new ServicesHome();
//                mCallBack.onUpdateFragment(fragmentt);
//            }
//        });
    return view;
    }
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        setContentView(R.layout.services_resort_facility);
//        Button back = (Button)findViewById(R.id.bck);
//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent naviagte = new Intent(NavigationPage.this,ServicesHome.class);
//                startActivity(naviagte);
//            }
//        });
//
//
//    }
//public interface OnFragmentUpdate {
//    void onUpdateFragment(Fragment fragment);
//}
}
