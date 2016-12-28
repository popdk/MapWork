package com.example.akshatdesai.googlemaptry;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.akshatdesai.googlemaptry.client.LocationUpdateService;

/**
 * Created by Akshat Desai on 11/26/2016.
 */

public class AlarmRecevier extends BroadcastReceiver {

    String temp;
    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getExtras() != null) {
             temp = intent.getExtras().getString("taskid");
            Log.e("SO", temp);
        }
        if(isMyServiceRunning(LocationUpdateService.class,context))
        {
            Intent i =  new Intent(context,LocationUpdateService.class);
            context.stopService(i);
        }

        Intent i = new Intent(context,LocationUpdateService.class);
        i.putExtra("taskid", temp);
        context.startService(i);
    }

    private boolean isMyServiceRunning(Class<?> serviceClass,Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
