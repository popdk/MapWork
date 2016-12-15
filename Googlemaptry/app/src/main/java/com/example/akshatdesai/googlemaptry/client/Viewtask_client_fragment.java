package com.example.akshatdesai.googlemaptry.client;

import android.app.Activity;
import android.app.ProgressDialog;
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

import com.example.akshatdesai.googlemaptry.Admin.Assign_role;
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
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.HashMap;

import static com.example.akshatdesai.googlemaptry.General.SetAlarm.SetAlarm1;

public class Viewtask_client_fragment extends Fragment {

    Toolbar toolbar;
    ProgressBar pb;
    Activity context;
    private RecyclerView taskView;

    String param;
    Sessionmanager sessionManager;
    int UId, mid;
    Toolbar mtoolbar;
    ProgressDialog pd1;
    String msg, name[], sdate[], edate[], assignedto[], assignedby[], desc[],source[],destination[],stopage[],specials_date[];
    Sessionmanager sessionmanager;
    static int status, id[], length1, cstatus[],specialt_id[],stat[];
    JSONArray array;
    EnablePermission ep = new EnablePermission();
  //  Context context = getContext();


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


//    private OnFragmentInteractionListener mListener;

    public Viewtask_client_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Viewtask_client_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Viewtask_client_fragment newInstance(String param1, String param2) {
        Viewtask_client_fragment fragment = new Viewtask_client_fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_viewtask_client);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view= inflater.inflate(R.layout.fragment_viewtask_client_fragment, container, false);

      //  mtoolbar = (Toolbar)view.findViewById(R.id.toolbar2);
       // setSupportActionBar(mtoolbar);
        sessionManager = new Sessionmanager(getContext());

        // toolbar = (Toolbar) findViewById(R.id.toolbar);
        taskView = (RecyclerView)view.findViewById(R.id.rv_manager);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        taskView.setLayoutManager(layoutManager);


        sessionManager = new Sessionmanager(getContext());
        HashMap<String, String> user = sessionManager.getuserdetails();
        // name
        UId = Integer.parseInt(user.get(Sessionmanager.KEY_ID));
        mid = Integer.parseInt(user.get(Sessionmanager.KEY_mid));
        if(ep.isInternetConnected(getActivity())) {
            new ViewTask_Web().execute();
        }else{
            Toast.makeText(getActivity(),"No internrt connection",Toast.LENGTH_LONG);
        }
        return  view;

    }

public class ViewTask_Web extends AsyncTask {

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd1 = new ProgressDialog(getContext());
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
            URL url = new URL("http://"+ WebServiceConstant.ip+"/Tracking/viewtask_client.php?" + param);
            Log.e("hiii...",""+url);
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
                specialt_id = new int[length1];
                specials_date = new String[length1];
                stat = new int[length1];
                int m =0;

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
                        stat[i] = temp1.getInt("stat");

                        Log.d("T_STATUS",""+temp1.getInt("t_status"));
                        if(temp1.getInt("t_status") == 0)
                        {
                            specialt_id[m] = temp1.getInt("id");
                            specials_date[m] = temp1.getString("sdate");
                            m++;
                        }


                    }
                    //  Log.w("id", "" + id[1]);
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
            //SetAlarm.SetAlarm1();



            if(status == 0)
            {
                Toast.makeText(getContext(), "No Data  Found", Toast.LENGTH_SHORT).show();
            }
            else {

                ViewtaskAdpater_client adapter = new ViewtaskAdpater_client(id, name, desc, sdate, edate, assignedby,source,destination,stopage,stat,getContext());
                taskView.setAdapter(adapter);

                Log.d("IDEA",""+specialt_id[0]);
                if(specialt_id[0] != 0) {
                    for (int k = 0; k < specialt_id.length; k++) {
                        setracking(specialt_id[k], specials_date[k]);
                    }
                }

            }




        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

    private void setracking(int id, String sdate) {

        //    2016-12-02 16:26:00

        String [] index = sdate.split("\\s");

        String [] date = index[0].split("-");
        String [] time = index[1].split(":");


        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, Integer.parseInt(date[1])-1);
        calendar.set(Calendar.YEAR, Integer.parseInt(date[0]));
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date[2]));


        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(time[1]));
        calendar.set(Calendar.SECOND,15);

        SetAlarm1(getActivity(),calendar,770,id);


    }


}
