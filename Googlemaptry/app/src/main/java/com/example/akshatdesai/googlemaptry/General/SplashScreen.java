package com.example.akshatdesai.googlemaptry.General;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.akshatdesai.googlemaptry.R;
import com.example.akshatdesai.googlemaptry.server.Tracking;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

public class SplashScreen extends AppCompatActivity {
    Handler h;
    Runnable myRunnable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
       // tv = (TextView) findViewById(R.id.textView);

        if(getIntent().getStringArrayExtra("android.intent.extra.PACKAGES")!=null ) {

            String[] packageNames = getIntent().getStringArrayExtra("android.intent.extra.PACKAGES");
            Toast.makeText(SplashScreen.this, "Hope", Toast.LENGTH_SHORT).show();
            Log.e("PACKAGES NAME23", "" + packageNames[0]);
            Toast.makeText(SplashScreen.this, "Hope", Toast.LENGTH_SHORT).show();
        }


        h = new Handler();

        myRunnable = new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashScreen.this,Login_new.class);
                startActivity(i);

                finish();

            }
        };
        h.postDelayed(myRunnable,3000);





    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {
        h.removeCallbacks(myRunnable);
        finishAffinity();

    }
}
