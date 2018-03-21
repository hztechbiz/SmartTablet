package com.smartapp.hztech.smarttebletapp;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.GoogleMap;


public class MapTest extends AppCompatActivity{

    private static final String TAG = "MapTest";

    private  static final int ERROR_DIALOG_REQUEST = 9001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_test);
        if (isServicesOK()){
            init();
        }
    }
    private void init(){
        Button button = (Button) findViewById(R.id.btnMap);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapTest.this, MapActivity.class);
                startActivity(intent);
            }
        });
    }
    public boolean isServicesOK(){
        Log.d(TAG, "isServicesOK: checking google services version");
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MapTest.this);
        if (available == ConnectionResult.SUCCESS){
            // everything is fine and the user can make map request
            Log.d(TAG, "isServicesOK: Google Play Service is working");
            return true;
        }
        else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            // if error occures but we can resolve it
            Log.d(TAG, "isServicesOK: an error occures but we can resolve it ");
            Dialog dialog =GoogleApiAvailability.getInstance().getErrorDialog(MapTest.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }
        else {
            Toast.makeText(this, "You can't make map Requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }



}
