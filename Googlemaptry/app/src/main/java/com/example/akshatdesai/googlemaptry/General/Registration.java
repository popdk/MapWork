package com.example.akshatdesai.googlemaptry.General;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.akshatdesai.googlemaptry.Notification.RegistrationIntentService;
import com.example.akshatdesai.googlemaptry.R;
import com.example.akshatdesai.googlemaptry.WebServiceConstant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

import static android.R.id.message;
import static com.example.akshatdesai.googlemaptry.Notification.MyInstanceIDListenerService.refreshedToken;
import static com.example.akshatdesai.googlemaptry.Notification.RegistrationIntentService.token;

public class Registration extends AppCompatActivity {
    TextView tv_name, tv_mail, tv_pass, tv_phone, tv_address;
    EditText et_name, et_mail, et_pass, et_phone, et_address;
    Button submit, cancel;
    String name, message, mail, pass, address, phone, gender, response,msg="";
    RadioButton male, female;
    HttpURLConnection httpURLConnection;
    JSONArray array;
    int status, i,abc;
    ProgressDialog pd;
    RadioGroup rg;
    JSONObject temp;
    EnablePermission eb;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);


        et_name = (EditText) findViewById(R.id.et_name);
        et_mail = (EditText) findViewById(R.id.et_mail);
        et_pass = (EditText) findViewById(R.id.et_pass);
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_address = (EditText) findViewById(R.id.et_address);
        submit = (Button) findViewById(R.id.btn_submit);
        cancel = (Button) findViewById(R.id.btn_cncl);
        rg = (RadioGroup)findViewById(R.id.rg_mf);
        male = (RadioButton) findViewById(R.id.rb_male);
        female = (RadioButton) findViewById(R.id.rb_female);
        eb = new EnablePermission();
        if(savedInstanceState == null)
        {
            Intent i = new Intent(this, RegistrationIntentService.class);
            startService(i);


        }



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = et_name.getText().toString().trim();
                mail = et_mail.getText().toString().trim();
                pass = et_pass.getText().toString().trim();
                address = et_address.getText().toString().trim();
                phone = et_phone.getText().toString().trim();
                abc = rg.getCheckedRadioButtonId();

                Log.e("abc",""+abc);

                if(abc == male.getId())
                {
                    gender = "male";
                }else{
                    gender = "female";
                }
                        if(EnablePermission.isInternetConnected(Registration.this)) {
                            if (name.equals("") || mail.equals("") ||abc == -1 ||pass.equals("") || address.equals("") || phone.equals("") || gender.equals("")) {
                                Toast.makeText(Registration.this, "Enter Sufficient Details", Toast.LENGTH_LONG).show();
                            } else {

                                new Register_Web().execute();
                            }
                        }else {
                            Toast.makeText(Registration.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                        }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    class Register_Web extends AsyncTask {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(Registration.this);
            pd.setMessage("Please wait..");
            pd.show();
            pd.setCancelable(true);
        }

        @Override
        protected Object doInBackground(Object[] objects) {



            try {

                String params = "Name=" + URLEncoder.encode(String.valueOf(name), "UTF-8") + "&" + "Email=" + URLEncoder.encode(String.valueOf(mail), "UTF-8") + "&" + "Password=" + URLEncoder.encode(String.valueOf(pass), "UTF-8") + "&" + "Phone=" + URLEncoder.encode(String.valueOf(phone),"UTF-8") + "&" + "Address=" + URLEncoder.encode(String.valueOf(address), "UTF-8") + "&" + "Gender=" + URLEncoder.encode(String.valueOf(gender), "UTF-8") + "&" + "token=" + URLEncoder.encode(String.valueOf(refreshedToken), "UTF-8");
                URL url = new URL("http://"+ WebServiceConstant.ip+"/Tracking/registration.php?" + params);

                Log.e("URL",""+url);
                Object obj = null;

                URLConnection con = url.openConnection();
                HttpURLConnection httpURLConnection = (HttpURLConnection) con;
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestProperty("Accept", "application/json");
                httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                int rescode = httpURLConnection.getResponseCode();

                Log.e("responce", "" +rescode);
                if (rescode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    String inputLine;
                    StringBuffer responce = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        responce.append(inputLine);
                    }
                    Log.e("responce", "" +responce);
                    in.close();
                    msg = responce.toString();
                    Log.e("responce", "" + msg);
                }


            } catch (UnsupportedEncodingException e1) {
              //  e1.printStackTrace();
                Log.e("Unsupported",e1.getMessage().toString());
            } catch (MalformedURLException e) {
                Log.e("MalformedURLException",e.getMessage().toString());
              //  e.printStackTrace();
            } catch (IOException e) {
                Log.e("IOException",e.getMessage().toString());
               // e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            pd.cancel();
            if (msg.equals("0")) {
                Toast.makeText(Registration.this, "Problem in registration.", Toast.LENGTH_SHORT).show();
                //pd.cancel();

                //  etEmail.setText("");
                //etPassword.setText("");
            /*} else if (status == 1) {
                Toast.makeText(Login.this, message, Toast.LENGTH_SHORT).show();
                sessionManager.createLoginSession(UId, UName, UEmail, UType, OrganizationId);
                Intent i = new Intent(getApplicationContext(), CurrentLocation.class);
                startActivity(i);
                finish();
            }*/
            }else{
                Toast.makeText(Registration.this, "Successfully registered.", Toast.LENGTH_SHORT).show();


                Intent i = new Intent(getApplicationContext(), Login_new.class);
                startActivity(i);
            }

        }
    }




}