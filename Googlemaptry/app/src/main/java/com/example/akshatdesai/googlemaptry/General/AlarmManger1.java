package com.example.akshatdesai.googlemaptry.General;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.akshatdesai.googlemaptry.AlarmRecevier;
import com.example.akshatdesai.googlemaptry.R;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class AlarmManger1 extends AppCompatActivity {


    PendingIntent pendingIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_manger);

        Intent intent = new Intent(AlarmManger1.this,AlarmRecevier.class);

        pendingIntent = PendingIntent.getBroadcast(AlarmManger1.this,0,intent,0);
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);

       // Long time  = new GregorianCalendar().getTimeInMillis()+2*1000;

       java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.set(java.util.Calendar.MONTH, Calendar.NOVEMBER);
        calendar.set(java.util.Calendar.YEAR,2016);
        calendar.set(java.util.Calendar.DAY_OF_MONTH,27);


        calendar.set(java.util.Calendar.HOUR_OF_DAY,1);
        calendar.set(java.util.Calendar.MINUTE,35);
        calendar.set(java.util.Calendar.SECOND,30);
        Long time = calendar.getTimeInMillis();
        Log.e("TIME",""+time);



        manager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);

    }
}
