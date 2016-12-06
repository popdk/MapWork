package com.example.akshatdesai.googlemaptry.Notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.akshatdesai.googlemaptry.R;
import com.example.akshatdesai.googlemaptry.server.DefineRoute;
//import com.example.android.assetmanagement.Activity.MainActivity;
import com.google.android.gms.gcm.GcmListenerService;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyGcmListenerService extends FirebaseMessagingService {

    private static final String TAG = "MyGcmListenerService";

    /**
     * Called when message is received.
     *
     * @param message SenderID of the sender.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage message){

        Log.e("ABC","aabbc");

        Map<String, String> data = message.getData();
        String myCustomKey = data.get("Key");
        Log.e("NOTIFICATION",myCustomKey);


        addNotification(message.getNotification().getBody());
        Log.d(TAG, "From: " + message.getFrom());
        Log.d(TAG, "Notification Message Body: " + message.getNotification().getBody());


        ;


        // [START_EXCLUDE]
        /**
         * Production applications would usually process the message here.
         * Eg: - Syncing with server.
         *     - Store message in local database.
         *     - Update UI.
         */

        /**
         * In some cases it may be useful to show a notification indicating to the user
         * that a message was received.
         */
       // sendNotification(data);
        // [END_EXCLUDE]
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param message GCM message received.
     */
  /*  private void sendNotification(String message) {
        Intent intent = new Intent(this, DefineRoute.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 *//* Request code *//*, intent,
                PendingIntent.FLAG_ONE_SHOT);
        Log.e("listener service","in send notification");
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)

                .setContentTitle("GCM Message")
                .setContentText(message)
                .setAutoCancel(false)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 *//* ID of notification *//*, notificationBuilder.build());
    }*/
    private void addNotification(String message) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_menu_camera)
                        .setContentTitle("Notifications Example")
                        .setContentText(message);

        Intent notificationIntent = new Intent(this, DefineRoute.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }
}