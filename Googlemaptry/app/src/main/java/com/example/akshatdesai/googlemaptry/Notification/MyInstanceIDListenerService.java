package com.example.akshatdesai.googlemaptry.Notification;

import android.content.Intent;
import android.util.Log;

import com.google.android.gms.iid.InstanceIDListenerService;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyInstanceIDListenerService extends FirebaseInstanceIdService {

    private static final String TAG = "MyInstanceIDLS";
    public static String refreshedToken = "";
    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. This call is initiated by the
     * InstanceID provider.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Fetch updated Instance ID token and notify our app's server of any changes (if applicable).
       /* Log.e("Listener service","token refresh");
        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);*/

        refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        // TODO: Implement this method to send any registration to your app's servers.
         new RegistrationIntentService().sendRegistrationToServer(refreshedToken);
    }
    // [END refresh_token]
}