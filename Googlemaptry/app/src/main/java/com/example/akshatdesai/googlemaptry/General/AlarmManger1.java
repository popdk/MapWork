package com.example.akshatdesai.googlemaptry.General;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.akshatdesai.googlemaptry.AlarmRecevier;
import com.example.akshatdesai.googlemaptry.R;
import com.example.akshatdesai.googlemaptry.client.LocationUpdateService;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class AlarmManger1 extends AppCompatActivity {


    PendingIntent pendingIntent;
    Button start1,start2,stop1,stop2;
    Intent i,in;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_manger);
        start1 = (Button) findViewById(R.id.btn_s1);
        start2 = (Button) findViewById(R.id.btn_s2);
        stop1 = (Button) findViewById(R.id.btn_st1);
        stop2 = (Button) findViewById(R.id.btn_st2);





        start1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i = new Intent(AlarmManger1.this,LocationUpdateService.class);
                startService(i);
            }
        });



        start2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 in = new Intent(AlarmManger1.this,LocationUpdateService.class);
                startService(in);
            }
        });

        stop1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  in = new Intent(AlarmManger1.this,LocationUpdateService.class);
                stopService(i);
            }
        });


        stop2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  in = new Intent(AlarmManger1.this,LocationUpdateService.class);
                stopService(in);
            }
        });

/*
        Intent intent = new Intent(AlarmManger1.this,AlarmRecevier.class);
        intent.putExtra("1","MyValue");

        pendingIntent = PendingIntent.getBroadcast(AlarmManger1.this,22,intent,0);
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);

       // Long time  = new GregorianCalendar().getTimeInMillis()+2*1000;

       java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.set(java.util.Calendar.MONTH, Calendar.DECEMBER);
        calendar.set(java.util.Calendar.YEAR,2016);
        calendar.set(java.util.Calendar.DAY_OF_MONTH,4);


        calendar.set(java.util.Calendar.HOUR_OF_DAY,12);
        calendar.set(java.util.Calendar.MINUTE,42);
        calendar.set(java.util.Calendar.SECOND,30);
        Long time = calendar.getTimeInMillis();
        Log.e("TIME",""+time);



        manager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);*/






    }


}
