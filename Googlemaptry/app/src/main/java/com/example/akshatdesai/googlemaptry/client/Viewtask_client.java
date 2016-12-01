package com.example.akshatdesai.googlemaptry.client;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.akshatdesai.googlemaptry.General.Sessionmanager;
import com.example.akshatdesai.googlemaptry.R;

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
import java.net.URLEncoder;
import java.util.HashMap;

public class Viewtask_client extends AppCompatActivity {

    Toolbar toolbar;
    ProgressBar pb;
    Activity context;
    private RecyclerView taskView;

    String param;
    Sessionmanager sessionManager;
    int UId, mid;
    Toolbar mtoolbar;
    ProgressDialog pd1;
    String msg, name[], sdate[], edate[], assignedto[], assignedby[], desc[],source[],destination[],stopage[];
    Sessionmanager sessionmanager;
    int status, id[], length1, cstatus[];
    JSONArray array;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewtask_client);

        pb = (ProgressBar) findViewById(R.id.progressBar_managerviewtask);
        mtoolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(mtoolbar);
        sessionManager = new Sessionmanager(Viewtask_client.this);

        // toolbar = (Toolbar) findViewById(R.id.toolbar);
        taskView = (RecyclerView) findViewById(R.id.rv_manager);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(Viewtask_client.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        taskView.setLayoutManager(layoutManager);


        sessionManager = new Sessionmanager(getApplicationContext());
        HashMap<String, String> user = sessionManager.getuserdetails();
        // name
        UId = Integer.parseInt(user.get(Sessionmanager.KEY_ID));
        mid = Integer.parseInt(user.get(Sessionmanager.KEY_mid));


        new ViewTask_Web().execute();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.manager_navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if(id == R.id.action_logout){
            sessionManager.LogOut1();
           /* Intent in = new Intent(getApplicationContext(), Login_new.class);
            startActivity(in);*/
        }

        return super.onOptionsItemSelected(item);
    }


    public class ViewTask_Web extends AsyncTask {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd1 = new ProgressDialog(Viewtask_client.this);
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

                param = "id=" + URLEncoder.encode(UId + "", "UTF-8");

                HttpURLConnection httpURLConnection;
                URL url = new URL("http://tracking.freevar.com/Tracking/viewtask_client.php?" + param);
                 Log.e("hiii...",""+url);
                httpURLConnection = (HttpURLConnection) url.openConnection();

                Log.e("hiii...","abc");
                httpURLConnection.setRequestMethod("POST");
                Log.e("hiii...","abc2");
                httpURLConnection.setDoOutput(true);
                Log.e("hiii...","abc3");
                httpURLConnection.setDoInput(true);
                Log.e("hiii...","abc4");
                httpURLConnection.setRequestProperty("Accept", "application/json");
                httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                Log.e("hiii...","abc5");
                Log.e("hiii...",""+httpURLConnection.getResponseCode());
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
                    String response ="responce" +responce.toString();
                    String[] res = response.split("\\[");
                    response = "[" +res[1];
                    msg = response.toString();
                    Log.e("responce", "" + msg);

                    array = new JSONArray(msg);
                    JSONObject temp = array.getJSONObject(0);
                    status = temp.getInt("status");
                    length1 = array.length();
                    id = new int[length1];
                    // cstatus = new int[length1];
                    name = new String[length1];
                    desc = new String[length1];
                    sdate = new String[length1];
                    edate = new String[length1];
                    assignedby = new String[length1];
                    assignedto = new String[length1];
                    source = new String[length1];
                    destination = new String[length1];
                    stopage = new String[length1];


                    if (status == 1)
                       {
                        for (i = 0; i < length1; i++) {
                            JSONObject temp1 = array.getJSONObject(i);
                            id[i] = temp1.getInt("id");
                            name[i] = temp1.getString("name");
                            desc[i] = temp1.getString("desc");
                            sdate[i] = temp1.getString("sdate");
                            edate[i] = temp1.getString("edate");
                            assignedto[i] = temp1.getString("assignedto");
                            assignedby[i] = temp1.getString("assignedby");
                            source[i] = temp1.getString("source");
                            destination[i] = temp1.getString("destination");
                            stopage[i] = temp1.getString("stopage");
                            Log.e("desc" , ""+desc[i]);
                            Log.e("desc" , ""+sdate[i]);
                            Log.e("desc" , ""+edate[i]);
                        }
                        Log.w("id", "" + id[1]);
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
                //pb.setVisibility(View.GONE);
                pd1.cancel();

                Log.e("length",""+desc.length);
                int in =desc.length;
                for(int i=0;i<in;i++){
                    Log.e("name",""+name[i]);
                }

                ViewtaskAdpater_client adapter = new ViewtaskAdpater_client(id, name, desc, sdate, edate, assignedby,source,destination,stopage,Viewtask_client.this);
                taskView.setAdapter(adapter);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

}
