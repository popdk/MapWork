package com.example.akshatdesai.googlemaptry.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.akshatdesai.googlemaptry.General.Sessionmanager;
import com.example.akshatdesai.googlemaptry.R;
import com.example.akshatdesai.googlemaptry.WebServiceConstant;
import com.example.akshatdesai.googlemaptry.server.ViewTaskAdapter;

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

/**
 * Created by Akshat Desai on 1/7/2017.
 */

public class TaskScheduled extends Fragment {

    RecyclerView taskView;
    TextView noTask;
    Sessionmanager sessionManager;
    private static int UId,mid;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.task_scheduled,container,false);

        taskView = (RecyclerView) view.findViewById(R.id.rv_manager_scheduled_task);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        taskView.setLayoutManager(layoutManager);

        noTask = (TextView) view.findViewById(R.id.no_task_scheduled);

        sessionManager=new Sessionmanager(getContext());

        HashMap<String, String> user = sessionManager.getuserdetails();
        // name
        if (sessionManager.isLoggedIn())
        {
            UId = Integer.parseInt(user.get(Sessionmanager.KEY_ID));
            mid= Integer.parseInt(user.get(Sessionmanager.KEY_mid));
        }


        new Web().execute();




        return view;
    }

    public class Web extends AsyncTask {

        private String msg,name[],sdate[],edate[],assignedto[],assignedby[],desc[];
        private Scanner scanner;
        private ViewTask viewTask;
        int status,id[],length1,cstatus[];
        JSONArray array;
        ProgressDialog pd;
        ViewTaskAdapter adapter;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(getActivity());
            pd.setMessage("Please Wait");
            pd.setCancelable(false);
            pd.show();

        }

        @Override
        protected Object doInBackground(Object[] params) {
            try {
                msg = "";

                Log.w("UID",""+UId);
                Log.w("MID",""+mid);

                String param = "id=" + URLEncoder.encode(String.valueOf(UId), "UTF-8")+ "&" + "type="+ URLEncoder.encode(String.valueOf(mid), "UTF-8");
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
                        Log.w("id",""+id[0]);
                    }
                    else
                    {
                        //  Toast.makeText(getActivity(),"No Data Found",Toast.LENGTH_LONG).show();
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
        protected  void onProgressUpdate(){}
        @Override
        protected void onPostExecute(Object o) {

            try {
                pd.cancel();

                if(status == 1) {
                    adapter = new ViewTaskAdapter(getContext(),id, name, desc, sdate, edate, assignedto, cstatus,2);
                    taskView.setAdapter(adapter);
                }
                else {
                    noTask.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
}

