package com.example.akshatdesai.googlemaptry.General;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
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
import com.example.akshatdesai.googlemaptry.client.CurrentLocation;
import com.example.akshatdesai.googlemaptry.client.Viewtask_client;
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

public class Login_new extends AppCompatActivity {
    TextView tv_mail, tv_pass,register;
    EditText et_mail, et_pass;
    String name, message, mail, pass, address, phone, gender, response, name1, pass1, mail1,m_id1;
    Button submit, cancel;
    HttpURLConnection httpURLConnection;
    JSONArray array;

    int status, id1,i;
    ProgressDialog pd1;
boolean res;
    Sessionmanager sessionManager;


   // Sessionmanager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_new);
        sessionManager = new Sessionmanager(getApplicationContext());
        res = sessionManager.checklogin();



        if (res) {
            HashMap<String,String> hm = sessionManager.getuserdetails();
            i = Integer.parseInt(hm.get(KEY_mid));

            if(i==0){
                Intent i = new Intent(Login_new.this,Viewtask_client.class);
                startActivity(i);
            }else if(i == 1){
                Intent i = new Intent(Login_new.this,ManagerNavigation.class);
                startActivity(i);
            }else if(i == 2){
                Intent i = new Intent(Login_new.this,Assign_role.class);
                startActivity(i);

            }
            finish();
        } else {
            tv_mail = (TextView) findViewById(R.id.tv_mail);
            register = (TextView) findViewById(R.id.tv_nr);
            tv_pass = (TextView) findViewById(R.id.tv_pass);
            et_mail = (EditText) findViewById(R.id.et_mail);
            et_pass = (EditText) findViewById(R.id.et_pass);
            submit = (Button) findViewById(R.id.btn_submit);
            cancel = (Button) findViewById(R.id.btn_cncl);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),Registration.class);
                startActivity(i);
            }
        });
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mail = et_mail.getText().toString();
                    pass = et_pass.getText().toString();
                    Log.e("mail id", mail);
                    Log.e("password", pass);
                    /*if (mail.equals("admin") && pass.equals("admin")) {
                        Intent i = new Intent(Login_new.this, Assign_role.class);
                        startActivity(i);
                    } else {*/
                        new Check_web().execute();

                }
            });
        }
    }

    public class Check_web extends AsyncTask {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd1 = new ProgressDialog(Login_new.this);
            Log.e("in pre1", "entered");
            pd1.setMessage("Please wait");
            Log.e("in pre2", "entered");
            pd1.show();
            Log.e("in pre3", "entered");
            pd1.setCancelable(true);
            Log.e("in pre4", "puru");
        }
        @Override
        protected Object doInBackground(Object[] params) {
            try {


                String param = "Email=" + URLEncoder.encode(String.valueOf(mail), "UTF-8") + "&" + "Password=" + URLEncoder.encode(String.valueOf(pass), "UTF-8");

                Log.e("params", param);
                URL url = new URL("http://tracking.freevar.com/Tracking/login.php?" + param);
                Log.e("url", ""+url);

                Object obj = null;
                HttpURLConnection httpURLConnection = null;
                httpURLConnection = (HttpURLConnection) url.openConnection();
                Log.e("URLInfo",url+"");

                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestProperty("Accept", "application/json");
                httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                Log.e("yash",""+httpURLConnection.getResponseCode());
                int i = httpURLConnection.getResponseCode();
                Log.e("yash",""+i);
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

                array =  new JSONArray(response);
                JSONObject temp = array.getJSONObject(0);
                status = temp.getInt("status");
                //  message = temp.getString("message");
                Log.e("in if", ""+status);
                if (status == 1) {
                    id1 = temp.getInt("id");
                    name1 = temp.getString("name");
                    pass1 = temp.getString("password");
                    mail1 = temp.getString("mail");
                    m_id1 = temp.getString("m_id");
                    Log.e("in if", "" + id1);
                    Log.e("in if", "" + name1);
                    Log.e("in if", "" + pass1);
                    Log.e("in if", "" + mail1);
                    Log.e("in if", ""+m_id1);
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
            Log.e("status",""+status);
            if (status == 0) {
                Toast.makeText(Login_new.this, "Username or Password may be wrong.", Toast.LENGTH_SHORT).show();
                pd1.cancel();
                et_mail.setText("");
                et_pass.setText("");
            } else if (status == 1) {
                pd1.cancel();
                Toast.makeText(Login_new.this, "Successful", Toast.LENGTH_SHORT).show();
                sessionManager.CreateLoginSession(id1,mail1,pass1,m_id1);
                if(m_id1.equals("0")){
                    Intent i = new Intent(Login_new.this,Viewtask_client.class);
                    startActivity(i);
                }else if(m_id1.equals("1")){
                    Intent i = new Intent(Login_new.this,ManagerNavigation.class);
                    startActivity(i);
                }else if(m_id1.equals("2")){
                    Intent i = new Intent(Login_new.this,Assign_role.class);
                    startActivity(i);

                }
finish();


            }
        }

    }


}
