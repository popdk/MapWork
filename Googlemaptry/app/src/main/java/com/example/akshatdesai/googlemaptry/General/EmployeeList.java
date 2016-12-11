package com.example.akshatdesai.googlemaptry.General;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;


import com.example.akshatdesai.googlemaptry.R;
import com.example.akshatdesai.googlemaptry.WebServiceConstant;
import com.example.akshatdesai.googlemaptry.client.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;


public class EmployeeList extends Fragment {

    ListView employeeList;
    String[] empNames;
    String[] empIds;
    String empName;
    String msg;
    JSONArray array = null;
    JSONObject object = null;
    int status;
    String response;
    Sessionmanager sessionManager;
    //Context context;
    int UId;

    /*public EmployeeList(Context c) {
        context=c;
        // Required empty public constructor
    }*/

    public EmployeeList() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

     //   Toast.makeText(getContext(),)
        sessionManager = new Sessionmanager(getContext());

        HashMap<String, String> user = sessionManager.getuserdetails();
        // name
        if (sessionManager.isLoggedIn()) {
            UId = Integer.parseInt(user.get(Sessionmanager.KEY_ID));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_blank,
                container, false);
        employeeList = (ListView) view.findViewById(R.id.employeeList);

        return view;
    }
    public void onResume() {
                super.onResume();

                new Web().execute();
            }

            public class Web extends AsyncTask {

                @Override
                protected Object doInBackground(Object[] params) {
                    try {
                        msg = "";
                       // String param = "Manager_Id=" + URLEncoder.encode(String.valueOf(UId), "UTF-8");
                        URL url = new URL("http://" + WebServiceConstant.ip + "/Tracking/chattinglist.php?");
                        URLConnection con = url.openConnection();
                        HttpURLConnection httpURLConnection = (HttpURLConnection) con;
                        httpURLConnection.setRequestMethod("POST");
                        httpURLConnection.setDoOutput(true);
                        httpURLConnection.setDoInput(true);
                        httpURLConnection.setRequestProperty("Accept", "application/json");
                        httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                        int rescode = httpURLConnection.getResponseCode();
//                  Log.e("I", "" + rescode);

                        if (rescode == HttpURLConnection.HTTP_OK) {
                            BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                            String inputLine;
                            StringBuffer responce = new StringBuffer();
                            while ((inputLine = in.readLine()) != null) {
                                responce.append(inputLine);
                            }
                            in.close();
                            response= "responce" + responce.toString();
                            String[] res= response.split("\\[");
                            response="[" +res[1];

                            msg = response;


                    Log.e("response", "" + msg);
                        }

                        array = new JSONArray(msg);
                        object = array.getJSONObject(0);
                        status = object.getInt("Status");
                        int size = array.length();
                        empNames = new String[size];
                        empIds = new String[size];
                        if (status == 1) {
                            for (int j = 0; j < size; j++) {
                                object = array.getJSONObject(j);
                                empNames[j] = object.getString("Name");
                                empIds[j] = object.getString("Id");
                                //empIds[j] = object.getInt("Id");
                                //employeeList.add(empNames[j]);
                            }
                        }

                        /*if (status == 1) {
                            for (int j = 0; j < array.length(); j++) {
                                object = array.getJSONObject(j);
                                if (object.getInt("ManagerId") == MainActivity.UId) {
                                    size++;
                                } else {
                                    if (object.getString("ProjectTeamLeader").contains("T" + MainActivity.UId + "T")) {
                                        size++;
                                    } else if (object.getString("ProjectMember").contains("E" + MainActivity.UId + "E")) {
                                        size++;
                                    }
                                }
                            }
                            */

                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                protected void onPostExecute(Object o) {
                    super.onPostExecute(o);
                    try {
                        if (status == 1) {
                            // specify an adapter (see also next example)
                            EmployeeListAdapter listAdapter = new EmployeeListAdapter(getActivity(), empNames);
                            employeeList.setAdapter(listAdapter);
                            employeeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    empName = empNames[position];

                                    Intent i=new Intent(getActivity(), Chatting.class);
                                    Log.e("sendto",""+empIds[position]);
                                    i.putExtra("Key",empIds[position]);
                                    startActivity(i);
                                }
                            });

                        } else {
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
    // TODO: Rename method, update argument and hook method into UI event
   /* public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/

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

}
