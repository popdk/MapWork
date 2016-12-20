package com.example.akshatdesai.googlemaptry;

import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class PreciseLocation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_precise_location);
        MyLocation.LocationResult locationResult = new MyLocation.LocationResult(){
            @Override
            public void gotLocation(Location location){
                //Got the location!
                Toast.makeText(PreciseLocation.this, ""+location, Toast.LENGTH_SHORT).show();
                Log.e("LOCATION",""+location);
            }
        };
        MyLocation myLocation = new MyLocation();
        myLocation.getLocation(this, locationResult);
    }
}
