package com.example.akshatdesai.googlemaptry.Notification;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

//import com.example.akshatdesai.assetmanagment.R;
import com.example.akshatdesai.googlemaptry.R;
/*import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;*/

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

//import static com.example.android.assetmanagement.SessionManager.KEY_ID;

public class RegistrationIntentService extends IntentService {

    private static final String TAG = "RegIntentService";
    private static final String[] TOPICS = {"global"};
    public static  String token, id, response;
    int status;

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);


       // if (sm.gettoken() == null) {
            try {
                // [START register_for_gcm]
                // Initially this call goes out to the network to retrieve the token, subsequent calls
                // are local.
                // R.string.gcm_defaultSenderId (the Sender ID) is typically derived from google-services.json.
                // See https://developers.google.com/cloud-messaging/android/start for details on this file.
                // [START get_token]
               /* InstanceID instanceID = InstanceID.getInstance(this);
                token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                        GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
                Log.e("in service", "" + token);
                // [END get_token]
                Log.i(TAG, "GCM Registration Token: " + token);*/

                // TODO: Implement this method to send any registration to your app's servers.
             //   sendRegistrationToServer(token);

                // Subscribe to topic channels
             //   subscribeTopics(token);

             /*   sm.settoken(token);
                HashMap<String, String> hm = sm.getuserdetails();
                id = hm.get(KEY_ID);*/
                //new Token_update().execute();


                // You should store a boolean that indicates whether the generated token has been
                // sent to your server. If the boolean is false, send the token to your server,
                // otherwise your server should have already received the token.
               // sharedPreferences.edit().putBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, true).apply();
                // [END register_for_gcm]
            } catch (Exception e) {
                Log.d(TAG, "Failed to complete token refresh", e);
                // If an exception happens while fetching the new token or updating our registration data
                // on a third-party server, this ensures that we'll attempt the update at a later time.
              //  sharedPreferences.edit().putBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false).apply();
            }
            // Notify UI that registration has completed, so the progress indicator can be hidden.
         /*   Intent registrationComplete = new Intent(QuickstartPreferences.REGISTRATION_COMPLETE);
            LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
            Log.e("service", "registration complete");*/
        }
   // }


   /* public class Token_update extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {
            try {
                String param = "token=" + URLEncoder.encode(String.valueOf(token), "UTF-8") + "&uid=" + URLEncoder.encode(String.valueOf(id), "UTF-8");
                URL url = new URL("http://" + WebServiceConstant.ip + "/FAM/updatetoken.php?" + param);

                Object obj = null;
                HttpURLConnection httpURLConnection = null;
                httpURLConnection = (HttpURLConnection) url.openConnection();
                Log.e("URLInfo", url + "");

                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestProperty("Accept", "application/json");
                httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                Log.e("yash", "" + httpURLConnection.getResponseCode());
                int i = httpURLConnection.getResponseCode();
                // Log.e("yash",""+i);
                if (i == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    String inputLine;
                    StringBuffer responce = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        responce.append(inputLine);
                    }

                    in.close();
                    response = responce.toString();
                    //    Log.e("i",""+response);
                }


                JSONObject temp = new JSONObject(response);

                status = temp.getInt("status");
                //   message = temp.getString("message");

                //if (status == 1) {
                   *//* uid1 = temp.getInt("id");
                    user1 = temp.getString("name");
                    pass1 = temp.getString("Password");
                    email1 = temp.getString("email");*//*

                //}
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();

            }
            return null;
        }

    }

*/



    /**
     * Persist registration to third-party servers.
     *
     * Modify this method to associate the user's GCM registration token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    public void sendRegistrationToServer(String token) {
        // Add custom implementation, as needed.
    }

    /**
     * Subscribe to any GCM topics of interest, as defined by the TOPICS constant.
     *
     * @param token GCM token
     * @throws IOException if unable to reach the GCM PubSub service
     */
    // [START subscribe_topics]
    private void subscribeTopics(String token) throws IOException {
       // FirebaseMessaging.getInstance().subscribeToTopic("mytopic");
    }
    // [END subscribe_topics]

}