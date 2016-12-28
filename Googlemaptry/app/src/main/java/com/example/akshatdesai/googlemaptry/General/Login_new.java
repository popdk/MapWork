package com.example.akshatdesai.googlemaptry.General;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.akshatdesai.googlemaptry.Admin.Assign_role;
import com.example.akshatdesai.googlemaptry.R;
import com.example.akshatdesai.googlemaptry.WebServiceConstant;
import com.example.akshatdesai.googlemaptry.client.Client_Home;
import com.example.akshatdesai.googlemaptry.client.CurrentLocation;

import com.example.akshatdesai.googlemaptry.server.ManagerNavigation;

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

import static com.example.akshatdesai.googlemaptry.General.Sessionmanager.KEY_mid;
import static com.example.akshatdesai.googlemaptry.Notification.MyInstanceIDListenerService.refreshedToken;

public class Login_new extends AppCompatActivity {
    TextView tv_mail, tv_pass, register;
    EditText et_mail, et_pass;
    String name, message, mail, pass, address, phone, gender, response, name1, pass1, mail1, m_id1,imgname;
    Button submit, cancel;
    HttpURLConnection httpURLConnection;
    JSONArray array;


    static int status=0, id1, i;
    ProgressDialog pd1;
    boolean res;
    Sessionmanager sessionManager;
    TextInputLayout inputLayoutName, inputLayoutPassword;


    // Sessionmanager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_new);
       /* toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setSubtitleTextColor(Color.WHITE);*/


        sessionManager = new Sessionmanager(getApplicationContext());
        res = sessionManager.checklogin();

        //Intent it = getIntent();
        if (getIntent().getStringExtra("Key") != null) {
            String sop = getIntent().getStringExtra("Key");
            Log.e("SOP", sop);
        }








        if (res) {
            HashMap<String, String> hm = sessionManager.getuserdetails();
            i = Integer.parseInt(hm.get(KEY_mid));

            if (i == 0) {
                Intent i = new Intent(Login_new.this, Client_Home.class);
                startActivity(i);
            } else if (i == 1) {
                Intent i = new Intent(Login_new.this, ManagerNavigation.class);
                startActivity(i);
            } else if (i == 2) {
                Intent i = new Intent(Login_new.this, Assign_role.class);
                startActivity(i);
            }
            finish();
        } else {



            inputLayoutName = (TextInputLayout) findViewById(R.id.input_layout_mail);
            inputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_password);
            // tv_mail = (TextView) findViewById(R.id.tv_mail);
            register = (TextView) findViewById(R.id.tv_nr);
            //tv_pass = (TextView) findViewById(R.id.tv_pass);
            et_mail = (EditText) findViewById(R.id.et_mail);
            et_pass = (EditText) findViewById(R.id.et_pass);
            submit = (Button) findViewById(R.id.btn_submit);
            cancel = (Button) findViewById(R.id.btn_cncl);
            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getApplicationContext(), Registration.class);
                    startActivity(i);
                }
            });
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                  /*  Intent i = new Intent(Login_new.this, RegistrationIntentService.class);
                    startService(i);*/

                    mail = et_mail.getText().toString();
                    pass = et_pass.getText().toString();
                    Log.e("mail id", mail);
                    Log.e("password", pass);
                    Log.e("NEW TOKEN",new Sessionmanager(Login_new.this).getRegistrationToken());
                    /*if (mail.equals("admin") && pass.equals("admin")) {
                        Intent i = new Intent(Login_new.this, Assign_role.class);
                        startActivity(i);
                    } else {*/
                    if (mail.equals("") || pass.equals("")) {
                        Toast.makeText(Login_new.this, "Enter Email and Password", Toast.LENGTH_LONG).show();
                    } else {
                        if (EnablePermission.isInternetConnected(Login_new.this)) {
                            new Check_web().execute();
                        } else {
                            Toast.makeText(Login_new.this, "No internet connection", Toast.LENGTH_LONG).show();
                        }

                    }
                }
            });


        }

    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void btnCancle(View view) {
//        Log.e("TOKEN",new DeleteTokenService().getTokenFromPrefs());

        finishAffinity();
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    public class Check_web extends AsyncTask {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd1 = new ProgressDialog(Login_new.this);
            pd1.setMessage("Please wait");
            pd1.setCancelable(false);
            pd1.show();


        }

        @Override
        protected Object doInBackground(Object[] params) {
            try {


                String param = "Email=" + URLEncoder.encode(String.valueOf(mail), "UTF-8") +
                        "&" + "Password=" + URLEncoder.encode(String.valueOf(pass), "UTF-8")+
                        "&" + "token=" + new Sessionmanager(Login_new.this).getRegistrationToken();

                Log.e("params", param);
                URL url = new URL("http://" + WebServiceConstant.ip + "/Tracking/login.php?" + param);
                Log.e("url", "" + url);

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
                Log.e("yash", "" + i);
                if (i == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    String inputLine;
                    StringBuffer responce = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        responce.append(inputLine);
                    }

                    in.close();
                    Log.e("in if", "" + responce);
                    response = responce.toString();
                    Log.e("in if", "" + response);
                    //    Log.e("i",""+response);

                }

                array = new JSONArray(response);
                JSONObject temp = array.getJSONObject(0);
                status = temp.getInt("status");
                //  message = temp.getString("message");
                Log.e("in if", "" + status);
                if (status == 1) {
                    id1 = temp.getInt("id");
                    name1 = temp.getString("name");
                    pass1 = temp.getString("password");
                    mail1 = temp.getString("mail");
                    m_id1 = temp.getString("m_id");
                    imgname = temp.getString("imgname");

                }
            } catch (MalformedURLException e) {

            } catch (IOException e) {
                // Log.e("yash",e.getMessage()+"");
                e.printStackTrace();
            } catch (JSONException e) {
                //Log.e("yash",e.getMessage()+"");
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            Log.e("status", "" + status);
            if (status == 0) {
                Toast.makeText(Login_new.this, "Username or Password may be wrong.", Toast.LENGTH_SHORT).show();
                pd1.cancel();
                et_mail.setText("");
                et_pass.setText("");
            } else if (status == 1) {
                pd1.cancel();
                Toast.makeText(Login_new.this, "Successful", Toast.LENGTH_SHORT).show();
                sessionManager.CreateLoginSession(id1, name1, mail1, pass1, m_id1,imgname);
                if (m_id1.equals("0")) {
                    Intent i = new Intent(Login_new.this, Client_Home.class);
                    startActivity(i);
                } else if (m_id1.equals("1")) {
                    Intent i = new Intent(Login_new.this, ManagerNavigation.class);
                    startActivity(i);
                } else if (m_id1.equals("2")) {
                    Intent i = new Intent(Login_new.this, Assign_role.class);
                    startActivity(i);

                }
                finish();


            }
            else {
                pd1.cancel();
                Toast.makeText(Login_new.this, "Already Logged In", Toast.LENGTH_SHORT).show();
            }
            
            
        }

    }


}
