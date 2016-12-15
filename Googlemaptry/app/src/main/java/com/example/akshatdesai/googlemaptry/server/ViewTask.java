package com.example.akshatdesai.googlemaptry.server;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.akshatdesai.googlemaptry.General.EnablePermission;
import com.example.akshatdesai.googlemaptry.General.Sessionmanager;
import com.example.akshatdesai.googlemaptry.R;
import com.example.akshatdesai.googlemaptry.WebServiceConstant;

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
import java.util.Scanner;

public class ViewTask extends AppCompatActivity {

    Toolbar toolbar;
    ProgressBar pb;
    Activity context;
    private RecyclerView taskView;
    Sessionmanager sessionManager;
    int UId,mid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task);

            pb = (ProgressBar) findViewById(R.id.progressBar_managerviewtask);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        taskView = (RecyclerView) findViewById(R.id.rv_manager);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(ViewTask.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        taskView.setLayoutManager(layoutManager);




        sessionManager=new Sessionmanager(getApplicationContext());

        HashMap<String, String> user = sessionManager.getuserdetails();
        // name
        if (sessionManager.isLoggedIn())
        {
            UId = Integer.parseInt(user.get(Sessionmanager.KEY_ID));
            mid= Integer.parseInt(user.get(Sessionmanager.KEY_mid));
        }
        if(EnablePermission.isInternetConnected(ViewTask.this)) {
            new Web().execute();
        }else {
            Toast.makeText(ViewTask.this,"No Internet Connection",Toast.LENGTH_LONG);
        }
    }




    @Override
    public void onResume() {
        super.onResume();
        //new Web().execute();
    }

  public class Web extends AsyncTask {

        private String msg,name[],sdate[],edate[],assignedto[],assignedby[],desc[];
        private Scanner scanner;
        private ViewTask viewTask;
      int status,id[],length1,cstatus[];
      JSONArray array;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pb.setVisibility(View.VISIBLE);
        }

        @Override
        protected Object doInBackground(Object[] params) {
            try {
                msg = "";

                Log.w("UID",""+UId);
                Log.w("MID",""+mid);

                UId = 4;
                mid = 1;
                String param = "id=" + URLEncoder.encode(UId + "", "UTF-8")+ "&" + "type="+ URLEncoder.encode(mid + "", "UTF-8");
                HttpURLConnection httpURLConnection = null;
                URL url = new URL("http://" + WebServiceConstant.ip + "/Tracking/viewtask.php?" + param);

                httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestProperty("Accept", "application/json");
                httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

//                httpURLConnection.setInstanceFollowRedirects(true);
                int i = httpURLConnection.getResponseCode();
                Log.w("RCode",""+i);
                if (i == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    String responce;

                    in.close();
                    responce = "responce"+response.toString();
                    String[] res = responce.split("\\[");
                    responce = "["+res[1];


                    msg = responce.toString();
                    Log.e("responce", "" + msg);

                    array =  new JSONArray(msg);
                    JSONObject temp = array.getJSONObject(0);
                    status = temp.getInt("status");
                    length1 = array.length();
                    id = new int[length1];
                    cstatus = new int[length1];
                    name = new String[length1];
                    desc = new String[length1];
                    sdate = new String[length1];
                    edate = new String[length1];
                    assignedby= new String[length1];
                    assignedto = new String[length1];


                    if(status == 1)

                    {
                        for(i=0;i<length1;i++)
                        {
                            JSONObject temp1 = array.getJSONObject(i);
                            id[i] = temp1.getInt("id");
                            name[i] = temp1.getString("name");
                            desc[i] = temp1.getString("desc");
                            sdate[i] = temp1.getString("sdate");
                            edate[i] = temp1.getString("edate");
                            assignedto[i] = temp1.getString("assignedto");
                            assignedby[i] = temp1.getString("assignedby");
                            cstatus[i] = temp1.getInt("cstatus");
                        }
                        Log.w("id",""+id[1]);
                    }
                    else
                    {
                        Toast.makeText(ViewTask.this,"No Data Found",Toast.LENGTH_LONG).show();
                    }
                }
            } catch (MalformedURLException e) {
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
                pb.setVisibility(View.GONE);

                if(status == 1) {
                    ViewTaskAdapter adapter = new ViewTaskAdapter(ViewTask.this,id, name, desc, sdate, edate, assignedto, cstatus);
                    taskView.setAdapter(adapter);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    }


