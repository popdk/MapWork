package com.example.akshatdesai.googlemaptry.General;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.akshatdesai.googlemaptry.R;
import com.example.akshatdesai.googlemaptry.server.Tracking;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreen extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
       // tv = (TextView) findViewById(R.id.textView);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent i = new Intent(SplashScreen.this,Login_new.class);
                startActivity(i);

                finish();
            }
        },3000);





    }
}
