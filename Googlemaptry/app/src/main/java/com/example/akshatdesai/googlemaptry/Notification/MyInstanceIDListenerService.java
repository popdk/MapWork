package com.example.akshatdesai.googlemaptry.Notification;

import android.content.Intent;
import android.util.Log;

import com.example.akshatdesai.googlemaptry.General.Sessionmanager;
import com.google.android.gms.iid.InstanceIDListenerService;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;

public class MyInstanceIDListenerService extends FirebaseInstanceIdService {

    private static final String TAG = "MyInstanceIDLS";
    public static String refreshedToken = "";
    private static final String DefaultTopic = "default";
    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. This call is initiated by the
     * InstanceID provider.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {


        refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        // TODO: Implement this method to send any registration to your app's servers.
        // new RegistrationIntentService().setRegistrationToken(refreshedToken);
        new Sessionmanager(this).setRegistrationToken(refreshedToken);
        FirebaseMessaging.getInstance().subscribeToTopic(DefaultTopic);
    }
    // [END refresh_token]
}