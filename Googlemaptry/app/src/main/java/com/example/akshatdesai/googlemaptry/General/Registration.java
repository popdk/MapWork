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

import com.example.akshatdesai.googlemaptry.R;

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

public class Registration extends AppCompatActivity {
    TextView tv_name, tv_mail, tv_pass, tv_phone, tv_address;
    EditText et_name, et_mail, et_pass, et_phone, et_address;
    Button submit, cancel;
    String name, message, mail, pass, address, phone, gender, response,msg;
    RadioButton male, female;
    HttpURLConnection httpURLConnection;
    JSONArray array;
    int status, i,abc;
    ProgressDialog pd;
    RadioGroup rg;
    JSONObject temp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_mail = (TextView) findViewById(R.id.tv_mail);
        tv_pass = (TextView) findViewById(R.id.tv_password);
        tv_phone = (TextView) findViewById(R.id.tv_phone);
        tv_address = (TextView) findViewById(R.id.tv_address);
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

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = et_name.getText().toString();
                mail = et_mail.getText().toString();
                pass = et_pass.getText().toString();
                address = et_address.getText().toString();
                phone = et_phone.getText().toString();
                abc = rg.getCheckedRadioButtonId();
                if(abc == male.getId())
                {
                    gender = "male";
                }else{
                    gender = "female";
                }
                /*if (male.isSelected()) {
                    gender = "male";
                    Log.e("temp", gender);
                } else if (female.isSelected()) {
                    gender = "female";
                    Log.e("temp", gender);
                }*/

                Log.e("temp", name);
                Log.e("temp", mail);
                Log.e("temp", pass);
                Log.e("temp", address);
                Log.e("temp", phone);
                Log.e("temp", gender);

                new Register_Web().execute();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),Login_new.class);
                startActivity(i);
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
            Log.e("temp", name);
            Log.e("temp", mail);
            Log.e("temp", pass);
            Log.e("temp", address);
            Log.e("temp", phone);
            Log.e("temp", gender);


            try {

                String params = "Name=" + URLEncoder.encode(String.valueOf(name), "UTF-8") + "&" + "Email=" + URLEncoder.encode(String.valueOf(mail), "UTF-8") + "&" + "Password=" + URLEncoder.encode(String.valueOf(pass), "UTF-8") + "&" + "Phone=" + URLEncoder.encode(String.valueOf(phone) + "&" + "Address=" + URLEncoder.encode(String.valueOf(address), "UTF-8") + "&" + "Gender=" + URLEncoder.encode(String.valueOf(gender), "UTF-8"));
                URL url = new URL("http://tracking.freevar.com/Tracking/registration.php?" + params);


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
                e1.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
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