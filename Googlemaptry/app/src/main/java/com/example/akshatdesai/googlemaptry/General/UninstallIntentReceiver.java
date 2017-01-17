package com.example.akshatdesai.googlemaptry.General;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Arrays;

/**
 * Created by Akshat Desai on 12/28/2016.
 */
public class UninstallIntentReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // fetching package names from extras
        String[] packageNames = intent.getStringArrayExtra("android.intent.extra.PACKAGES");

        Log.e("PACKAGES NAME1",""+ Arrays.toString(packageNames));

        if(packageNames!=null){
            for(String packageName: packageNames){
                if(packageName!=null && packageName.equals("com.example.akshatdesai.googlemaptry")){
                    // User has selected our application under the Manage Apps settings
                    // now initiating background thread to watch for activity
                    Log.e("DELETING PACKAGE","IN DELETE");
                    new ListenActivities(context).start();

                }
            }
        }
    }

}
