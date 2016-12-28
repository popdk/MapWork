package com.example.akshatdesai.googlemaptry.General;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.akshatdesai.googlemaptry.WebServiceConstant;
import com.example.akshatdesai.googlemaptry.client.ViewtaskAdpater_client;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by Admin on 21-Nov-16.
 */

public class Sessionmanager {


    public static final String KEY_NAME = "UserName";
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    static int UID=0;

    public static final String KEY_MAIL = "Email";
    public static final String KEY_ID = "UserId";
    public static final String Email = "Email";
    public static final String KEY_PASS = "Password";
    public static final String Wrong_Attempt = "WrongLoginAttempt";
    public static final String KEY_mid = "m_id";

    public static final String PREF_NAME = "FAM";
    public static final String IS_LOGIN = "IsLoggedIn";
    public static final String IMGNAME = "imagename";
    ProgressDialog pd1;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Context context,c;

    int PRIVATE_MODE =0;

    public Sessionmanager(Context context)
    {
        this.context = context;
        preferences = context.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        editor = preferences.edit();
    }

    public void CreateLoginSession(Integer userid,String name,String mail,String Password, String m_id,String imgname)
    {
        editor.putBoolean(IS_LOGIN,true);
        editor.putString(KEY_ID,userid.toString());
        editor.putString(KEY_NAME,name);
        editor.putString(KEY_MAIL,mail);
        editor.putString(KEY_PASS,Password);
        editor.putString(KEY_mid,m_id);
        editor.putString(IMGNAME,imgname);

        //editor.putString()
        // editor.putString(Wrong_Attempt,WrongAttempt);

        editor.commit();
    }



    public void setRegistrationToken(String token) {
        // Add custom implementation, as needed.
        Log.e("TOKEN","IN SET TOKEN");
        SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString("Token", token);
        //editor.putInt("idName", 12);
        editor.apply();

    }

    public String getRegistrationToken()
    {
        Log.e("TOKEN","IN GET TOKEN");
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        /* if (restoredText != null) {
            String name = prefs.getString("name", "No name defined");//"No name defined" is the default value.
            int idName = prefs.getInt("idName", 0); //0 is the default value.
        }*/
        return prefs.getString("Token", null);
    }












    public boolean checklogin()
    {

        if(this.isLoggedIn())
        {
            return true;
        }else{
            return  false;
        }

    }

    public HashMap<String,String> getuserdetails()
    {
        HashMap<String,String> user = new HashMap<>();
        user.put(KEY_ID,preferences.getString(KEY_ID,null));
        user.put(KEY_NAME,preferences.getString(KEY_NAME,null));
        user.put(KEY_PASS,preferences.getString(KEY_PASS,null));
        user.put(KEY_mid,preferences.getString(KEY_mid,null));
        user.put(IMGNAME,preferences.getString(IMGNAME,null));
        return user;

    }







    public void LogOut1(Context context,boolean b)
    {

        c = context;
        Log.e("Logout1","Clicked");
        new LogOut().execute();
        editor.clear();
        editor.commit();

        if(b) {
            Intent i = new Intent(c, Login_new.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }

    }

    public void settoken(String token)
    {
        editor.putString("TOKEN",token);
        editor.commit();
    }

    public String gettoken()
    {
        return preferences.getString("TOKEN",null);
    }

    public boolean isLoggedIn() {
        return preferences.getBoolean(IS_LOGIN, false);
    }

    public class LogOut extends AsyncTask {


        HashMap<String,String> hm;
        String result ="";
        String respond = "";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e("AsyncTask","Clicked");
            pd1 = new ProgressDialog(c);
            pd1.setMessage("Please wait");
            pd1.setCancelable(false);
            pd1.show();


          hm  =  getuserdetails();



        }

        @Override
        protected Object doInBackground(Object[] objects) {

            try {

                String param = "id=" + URLEncoder.encode(hm.get(KEY_ID) + "", "UTF-8");

                HttpURLConnection httpURLConnection;
                URL url = new URL("http://"+ WebServiceConstant.ip+"/Tracking/logout.php?" + param);

                httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setRequestMethod("POST");

                httpURLConnection.setDoOutput(true);

                httpURLConnection.setDoInput(true);

                httpURLConnection.setRequestProperty("Accept", "application/json");
                httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                int i = httpURLConnection.getResponseCode();
                Log.e("RCode", "" + i);
                if (i == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    String inputLine;
                    StringBuffer responce = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        responce.append(inputLine);
                    }
                    Log.e("responce", "" + responce);
                    in.close();
                     respond = responce.toString().trim();





                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {

            try {
                //pb.setVisibility(View.GONE);
                pd1.cancel();

                if(respond.equals("1"))
                {

                    Toast.makeText(context, "Logout Successfully", Toast.LENGTH_SHORT).show();

                }
                else {
                    Toast.makeText(context, "Please Logout Again", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

}
