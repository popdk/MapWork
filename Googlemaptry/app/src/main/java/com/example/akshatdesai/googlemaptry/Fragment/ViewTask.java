package com.example.akshatdesai.googlemaptry.Fragment;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

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


public class ViewTask extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    Toolbar toolbar;
    ProgressBar pb;
    Activity context;
    private RecyclerView taskView;
    Sessionmanager sessionManager;
    int UId,mid;


    public ViewTask() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_view_task, container, false);
        pb = (ProgressBar) view.findViewById(R.id.progressBar_managerviewtask);
        toolbar = (Toolbar)view.findViewById(R.id.toolbar);
        taskView = (RecyclerView) view.findViewById(R.id.rv_manager);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        taskView.setLayoutManager(layoutManager);




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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
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
                pb.setVisibility(View.GONE);

                if(status == 1) {
                    ViewTaskAdapter adapter = new ViewTaskAdapter(getContext(),id, name, desc, sdate, edate, assignedto, cstatus);
                    taskView.setAdapter(adapter);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
