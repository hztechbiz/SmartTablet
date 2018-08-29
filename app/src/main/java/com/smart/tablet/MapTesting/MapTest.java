package com.smart.tablet.MapTesting;

import android.app.Dialog;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.smart.tablet.R;


public class MapTest extends Fragment{

    public MapTest(){

    }

    private static final String TAG = "MapTest";

    private  static final int ERROR_DIALOG_REQUEST = 9001;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.map_test, container, false);
//        setContentView(R.layout.map_test);
//        if (isServicesOK()){
//            init();
//        }
    }
//    private void init(){
//        Button button = (Button) findViewById(R.id.btnMap);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MapTest.this, MapActivity.class);
//                startActivity(intent);
//            }
//        });
//    }
//    public boolean isServicesOK(){
//        Log.d(TAG, "isServicesOK: checking google services version");
//        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MapTest.this);
//        if (available == ConnectionResult.SUCCESS){
//            // everything is fine and the user can make map request
//            Log.d(TAG, "isServicesOK: Google Play Service is working");
//            return true;
//        }
//        else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)){
//            // if error occures but we can resolve it
//            Log.d(TAG, "isServicesOK: an error occures but we can resolve it ");
//            Dialog dialog =GoogleApiAvailability.getInstance().getErrorDialog(MapTest.this, available, ERROR_DIALOG_REQUEST);
//            dialog.show();
//        }
//        else {
//            Toast.makeText(this, "You can't make map Requests", Toast.LENGTH_SHORT).show();
//        }
//        return false;
//    }



}
