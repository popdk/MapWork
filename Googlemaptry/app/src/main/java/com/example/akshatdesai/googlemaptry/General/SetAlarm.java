package com.example.akshatdesai.googlemaptry.General;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.akshatdesai.googlemaptry.AlarmRecevier;
import com.example.akshatdesai.googlemaptry.WebServiceConstant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import static android.content.Context.ALARM_SERVICE;
import static com.example.akshatdesai.googlemaptry.Notification.MyInstanceIDListenerService.refreshedToken;

/**
 * Created by Akshat Desai on 12/5/2016.
 */

public  class SetAlarm {

   static String msg;
   static JSONArray array;
    static int status;
   static ProgressDialog pd;

    public static void  SetAlarm1(Context context, Calendar calendar, int reqCode, int taskid) {

        // Log.d("SetAlarm Texts", "Date : " + dateName + " Note: " + dateNote);



        Intent myIntent = new Intent(context, AlarmRecevier.class);
        myIntent.putExtra("taskid",""+taskid);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                reqCode, myIntent, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                pendingIntent);
        if(EnablePermission.isInternetConnected(context)) {
            try {
                new TaskStatus(context, taskid).execute().get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }else {
            Toast.makeText(context,"No Internet Connection",Toast.LENGTH_LONG).show();
        }
    }


    private static class TaskStatus extends AsyncTask {
        int taskid;
        Context context;
        TaskStatus(Context context,  int taskid)
        {
            this.taskid = taskid;
            this.context = context;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(context);
            pd.setMessage("Please wait.....................");
            pd.setCancelable(false);
            pd.show();


        }

        @Override
        protected Object doInBackground(Object[] objects) {


            try {

                String params = "id=" + URLEncoder.encode(String.valueOf(taskid), "UTF-8");
                URL url = new URL("http://"+ WebServiceConstant.ip+"/Tracking/TaskStatusUpdate.php?" + params);

                Log.e("URL", "" + url);
                Object obj = null;

                URLConnection con = url.openConnection();
                HttpURLConnection httpURLConnection = (HttpURLConnection) con;
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestProperty("Accept", "application/json");
                httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                int rescode = httpURLConnection.getResponseCode();


                if (rescode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    String inputLine;
                    StringBuffer responce = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        responce.append(inputLine);
                    }
                    Log.e("TaskStatusUpdaterespon", "" + responce);
                    in.close();
                    msg = responce.toString();

                    array = new JSONArray(msg);
                    JSONObject temp = array.getJSONObject(0);
                    status = temp.getInt("status");

                    if(status == 1)
                    {
                        Log.e("SETALARM","Taskid"+ taskid + "Updated");
                    }

                    pd.cancel();

                    Log.e("ALARMRESPONCE", "" + msg);
                }


            } catch ( JSONException | IOException e1) {
                e1.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            if (msg.equals("0")) {
                Toast.makeText(context, "Problem in Alarm Set", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(context, "Alarm Set", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
