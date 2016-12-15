package com.example.akshatdesai.googlemaptry.Notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.akshatdesai.googlemaptry.R;
import com.example.akshatdesai.googlemaptry.client.Chatting;
import com.example.akshatdesai.googlemaptry.client.Client_Home;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

//import com.example.android.assetmanagement.Activity.MainActivity;

public class MyGcmListenerService extends FirebaseMessagingService {

    private static final String TAG = "MyGcmListenerService";
    Intent notificationIntent;
    String name;
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
        if(!myCustomKey.equalsIgnoreCase("task")) {
            name = data.get("sendername");
            Log.e("NOTIFICATION",name);
        }
        Log.e("NOTIFICATION",myCustomKey);





        addNotification(myCustomKey,message.getNotification().getBody());
        Log.d(TAG, "From: " + message.getFrom());
        Log.d(TAG, "Notification Message Body: " + message.getNotification().getBody());


        if(!myCustomKey.equalsIgnoreCase("task")) {

            Intent intent = new Intent("ReceiveMessage");//put data if you want in putextras
            this.sendBroadcast(intent);
            Log.e("Broadcast","Broadcast done");
        }


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
    private void addNotification(String type,String message) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(name)
                        .setContentText(message);


        if(type.equalsIgnoreCase("task")) {

            notificationIntent  = new Intent(this, Client_Home.class);
        }
        else
        {
                             //   adapter.notifyDataSetChanged();

            Log.e("TYPE",type);
            notificationIntent  = new Intent(this, Chatting.class);
            notificationIntent.putExtra("Key",type);
        }

            PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
        builder.setAutoCancel(true);
    }
}