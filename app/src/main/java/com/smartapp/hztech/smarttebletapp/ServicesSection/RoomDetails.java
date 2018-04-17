package com.smartapp.hztech.smarttebletapp.ServicesSection;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smartapp.hztech.smarttebletapp.R;

public class RoomDetails extends Fragment {
  //  ServicesHome.OnFragmentUpdate mCallback;
    public RoomDetails(){

    }

//    public void onAttach(Activity activity){
//        super.onAttach(activity);
//        try {
//            mCallback = (ServicesHome.OnFragmentUpdate) activity;
//        }
//        catch (ClassCastException e){
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnFragmentUpdate");
//        }
//    }
    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup container, Bundle savedInstanceState){
               View view = inflater.inflate(R.layout.services_room_details, container, false);

//        Button back = view.findViewById(R.id.bck);
//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//           public void onClick(View v) {
//                ServicesHome fragmentt = new ServicesHome();
//                mCallback.onUpdateFragment(fragmentt);
//           }
//       });

                return view;
    }
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        setContentView(R.layout.services_room_details);
//        Button back = (Button)findViewById(R.id.bck);
//
//    }
//    public interface OnFragmentUpdate {
//    void onUpdateFragment(Fragment fragment);
//    }
}
