package com.example.akshatdesai.googlemaptry.client;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.akshatdesai.googlemaptry.General.Sessionmanager;
import com.example.akshatdesai.googlemaptry.R;
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
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;

import static android.R.attr.id;
import static android.R.attr.name;

/**
 * Created by Urvi on 12/02/2016.
 */

public class Chatting extends AppCompatActivity {
    Context context;
    RecyclerView recyclerView;
    /*RelativeLayout relativeLayout;
    RecyclerView.Adapter revAdap;
    RecyclerView.LayoutManager revLam;
  */ EditText type;
    Button send;
    int position;
    int sendto;
    String msgdata, msg;
    String[] messagearray, sendbyarray, sendtoarray, timearray;
    private JSONArray array;
    private JSONObject temp;
    private int status;
    //RecyclerViewAdapter recyclerViewAdapter;
    private ProgressDialog pd1;
    ListView lv;
    Sessionmanager sessionManager;
    int UId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);


        type = (EditText) findViewById(R.id.et_type);
        send = (Button) findViewById(R.id.button2);
        //recyclerView = (RecyclerView) findViewById(R.id.r1);
        lv = (ListView) findViewById(R.id.r1);
        new FetchMessageDetails().execute();
        /*final LinearLayoutManager layoutManager = new LinearLayoutManager(MainActiviy.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);*/

        sessionManager = new Sessionmanager(getApplicationContext());

        HashMap<String, String> user = sessionManager.getuserdetails();
        // name
        if (sessionManager.isLoggedIn()) {
            UId = Integer.parseInt(user.get(Sessionmanager.KEY_ID));
        }
        Bundle extras=getIntent().getExtras();
        if(extras!=null)
        {
            sendto =extras.getInt("sendto");

        }
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                msgdata = type.getText().toString();
                Log.e("msgdata", msgdata);
                new send_web().execute();
                type.setText("");
            }
        });
    }

    public class send_web extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            try {

                String param = "msg=" + URLEncoder.encode(String.valueOf(msgdata), "UTF-8") + "&" + "sendby=" + URLEncoder.encode(String.valueOf(UId), "UTF-8") + "&" + "sendto=" + URLEncoder.encode(String.valueOf(sendto), "UTF-8");
                Log.e("url", "" + param);
                URL url = new URL("http://"+ WebServiceConstant.ip+"/Tracking/send.php?" + param);
                Log.e("url", "" + url);
                URLConnection con = url.openConnection();
                HttpURLConnection httpURLConnection = (HttpURLConnection) con;
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestProperty("Accept", "application/json");
                httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                int rescode = httpURLConnection.getResponseCode();

                Log.e("responce", "" + rescode);

                if (rescode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    String inputLine;
                    StringBuffer responce = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        responce.append(inputLine);
                    }
                    Log.e("responce", "" + responce);
                    in.close();
               /* String response ="responce" +responce.toString();
                String[] res = response.split("\\[");
                response = "[" +res[1];
*/
                    msg = responce.toString();
                    Log.e("responce", "" + msg);
                }

                //Log.e("responce", "" + msg);
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            } catch (ProtocolException e1) {
                e1.printStackTrace();
            } catch (MalformedURLException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            Log.e("responce", "" + msg);

            Toast.makeText(getApplicationContext(), "message sent Successfully", Toast.LENGTH_SHORT).show();
            new FetchMessageDetails().execute();
        }

    }

    public class FetchMessageDetails extends AsyncTask {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd1 = new ProgressDialog(Chatting.this);
            Log.e("in pre1", "entered");
            pd1.setMessage("Please wait");
            Log.e("in pre2", "entered");
            pd1.show();
            Log.e("in pre3", "entered");
            pd1.setCancelable(true);
            Log.e("in pre4", "puru");
        }


        @Override
        protected Object doInBackground(Object[] objects) {
            try {

                String param = "sendBy=" + URLEncoder.encode(String.valueOf(UId), "UTF-8") + "&" + "sendTo=" + URLEncoder.encode(String.valueOf(sendto), "UTF-8");
                Log.e("url", "" + param);
                URL url = new URL("http://"+ WebServiceConstant.ip+ "/Tracking/MessageDetails.php?" + param);
                Log.e("url", "" + url);
                URLConnection con = url.openConnection();
                HttpURLConnection httpURLConnection = (HttpURLConnection) con;
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestProperty("Accept", "application/json");
                httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                int rescode = httpURLConnection.getResponseCode();

                Log.e("responce", "" + rescode);

                if (rescode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    String inputLine;
                    StringBuffer responce = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        responce.append(inputLine);
                    }
                    Log.e("responce1", "" + responce);
                    in.close();
                String response ="responce2" +responce.toString();
                String[] res = response.split("\\[");
                response = "[" +res[1];

                    msg = response.toString();
                    Log.e("responce3", "" + msg);



                    array = new JSONArray(msg);
                    JSONObject temp = array.getJSONObject(0);
                    status = temp.getInt("status");
                    int length1 = array.length();
                    messagearray = new String[length1];
                    sendbyarray = new String[length1];
                    sendtoarray = new String[length1];
                    timearray = new String[length1];

                    if (status == 1) {
                        for (int x = 0; x < length1; x++) {
                            JSONObject temp1 = array.getJSONObject(x);

                            messagearray[x] = temp1.getString("message");
                            sendbyarray[x] = temp1.getString("sendby");
                            sendtoarray[x] = temp1.getString("sendto");
                            timearray[x] = temp1.getString("time");

                        }


                        //Log.e("responce", "" + msg);
                    }
                }
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }




    @Override
    protected void onPostExecute(Object o) {

        try {
            //pb.setVisibility(View.GONE);
            pd1.cancel();

            if(status ==0)
            {
                Toast.makeText(getApplicationContext(),"No data found",Toast.LENGTH_LONG);
            }
            else {

                Log.e("activity", "" + getParent());
                // Log.e("length",""+desc.length);
                Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_LONG);
                SingleChat singleChat = new SingleChat(Chatting.this, messagearray, sendbyarray, sendtoarray, timearray);
                lv.setAdapter(singleChat);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}}
