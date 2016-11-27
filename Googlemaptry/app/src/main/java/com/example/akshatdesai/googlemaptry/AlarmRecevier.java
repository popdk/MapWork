package com.example.akshatdesai.googlemaptry;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Akshat Desai on 11/26/2016.
 */

public class AlarmRecevier extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,"Alarm is Set",Toast.LENGTH_LONG).show();

        Toast.makeText(context,"Alarm is Set1",Toast.LENGTH_LONG).show();

        Toast.makeText(context,"Alarm is Set2",Toast.LENGTH_LONG).show();

        Toast.makeText(context,"Alarm is Set3",Toast.LENGTH_LONG).show();


        Log.e("ALARM:", "HAAAAA");
    }
}
