package com.example.akshatdesai.googlemaptry.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.akshatdesai.googlemaptry.Admin.Assign_role;
import com.example.akshatdesai.googlemaptry.General.EnablePermission;
import com.example.akshatdesai.googlemaptry.General.Sessionmanager;
import com.example.akshatdesai.googlemaptry.R;
import com.example.akshatdesai.googlemaptry.WebServiceConstant;
import com.example.akshatdesai.googlemaptry.server.ManagerNavigation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.akshatdesai.googlemaptry.server.ManagerNavigation.toolbar;


public class AddEmployee extends Fragment {

    Button button;
    ProgressDialog pd;
    String msg="";
    String tosend="";
    JSONArray array;
    public int status, status1, UId;
    JSONObject temp;
    public String[] empNames;
    public Integer[] empIds;
    public String[] empString;
    Sessionmanager sessionManager;
    ProgressDialog pd5;

    public AddEmployee() {
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
        View view= inflater.inflate(R.layout.fragment_add_employee, container, false);
        button = (Button)view.findViewById(R.id.button2);
        sessionManager=new Sessionmanager(getContext());

        HashMap<String, String> user = sessionManager.getuserdetails();
        // name
        if (sessionManager.isLoggedIn())

        {
            UId = Integer.parseInt(user.get(Sessionmanager.KEY_ID));
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pd = new ProgressDialog(getContext());
                pd.setMessage("Please wait");
                pd.show();
                pd.setCancelable(true);
                if(EnablePermission.isInternetConnected(getActivity())) {
                    new employee().execute();
                }else{
                    Toast.makeText(getActivity(),"No internet connection",Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }

    public class employee extends AsyncTask {

        protected Object doInBackground(Object[] params) {
            try{
                String param = "Manager_Id=" + URLEncoder.encode(String.valueOf(UId), "UTF-8");
                URL url = new URL("http://" + WebServiceConstant.ip + "/Tracking/EmployeeList.php?" + param );
                URLConnection con = url.openConnection();
                HttpURLConnection httpURLConnection = (HttpURLConnection) con;
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestProperty("Accept", "application/json");
                httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                int rescode = httpURLConnection.getResponseCode();
                if (rescode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    String inputLine;
                    StringBuffer responce = new StringBuffer();
                    while ((inputLine = in.readLine()) != null) {
                        responce.append(inputLine);
                    }
                    in.close();
                    msg = responce.toString();
                }
                Log.e("ON ADD EmPLOYEECLICK",msg);
                array = new JSONArray(msg);
                temp = array.getJSONObject(0);
                status = temp.getInt("Status");
                status1 = array.length();
                empNames = new String[status1];
                empIds = new Integer[status1];
                empString=new String[status1];
                if (status == 1) {
                    for (int j = 0; j < status1; j++) {
                        temp = array.getJSONObject(j);
                        empNames[j] = temp.getString("Name");
                        empIds[j] = temp.getInt("Id");
                        empString[j]=temp.getString("eid");
                    }
                }
                Log.e("ENAme",empNames[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (JSONException e) {
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

            List<CharSequence> list = new ArrayList<CharSequence>();

            for (int i = 0; i < status1; i++) {

                list.add(empNames[i]);  // Add the item in the list

            }
            Log.e("Names"," "+list);
            final CharSequence[] dialogList = list.toArray(new CharSequence[list.size()]);
            final AlertDialog.Builder builderDialog = new AlertDialog.Builder(getContext());
            builderDialog.setCancelable(false);
            builderDialog.setTitle("Select an employee " );
            int count = dialogList.length;
            boolean[] is_checked = new boolean[count];
            /*builderDialog.setItems(empNames, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String selectedText = Arrays.asList(empNames).get(which);
                    Toast.makeText(getApplicationContext(),selectedText,Toast.LENGTH_SHORT);
                }
            });*/

            if (!empString[0].equals(" ")) {

                for (int i = 0, j = 0; i < status1; i++) {
                    if (empString[0].contains("E" + empIds[i] + "E")) {
                        is_checked[j++] = true;
                    } else {
                        j++;
                    }
                }
                    /*String[] empIdArray=empString[0].split("E");
                    Log.e("EmpIds"," " +empIdArray[0]);
                    Log.e("EmpIds"," " +empIdArray[1]);
                    Log.e("EmpIds"," " +empIdArray[2]);
                    for(int k=0;k<empIdArray.length;k++)
                    {
                        for (int i = 1; i < status1; i++) {
                        if(empIdArray[k].equals(empIds[i])){
                            is_checked[k]=true;
                        }
                    }*/

            }
                    /*if (empIdArray[j].contains("E" + empIds[i] + "E")) {
                        is_checked[j++] = true;
                    } else {
                        j++;
                    }*/

            // set is_checked boolean false;
            // Creating multiple selection by using setMutliChoiceItem method
            builderDialog.setMultiChoiceItems(dialogList, is_checked, new DialogInterface.OnMultiChoiceClickListener() {
                public void onClick(DialogInterface dialog, int whichButton, boolean isChecked) {
                }
            });

            builderDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ListView list = ((AlertDialog) dialog).getListView();// make selected item in the comma separated string
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i = 0; i < list.getCount(); i++) {
                        boolean checked = list.isItemChecked(i);
                        if (checked) {
                            if (stringBuilder.length() > 0)
                                stringBuilder.append(",");
                            for (int j = 0; j < status1; j++) {
                                if (empNames[j] == list.getItemAtPosition(i)) {
                                    stringBuilder.append(empIds[j]);
                                }
                            }
                        }
                    }
                    if (stringBuilder.toString().trim().equals("")) {
                        stringBuilder.setLength(0);
                    } else {
                        String[] temp = stringBuilder.toString().split(",");   //splitting all empids in EempidE form by coma(,)
                        for (int i = 0; i < temp.length; i++) {
                            tosend += "E" + temp[i] + "E";
                        }

                    }
                    /*Intent i=new Intent(getContext(),AssignTask.class);
                    startActivity(i);*/
                    new temp().execute();

                }
            });

            builderDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                }

            });
           AlertDialog alert = builderDialog.create();
            alert.show();
        }

        public class temp extends AsyncTask {

            String result="";
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pd5 = new ProgressDialog(getContext());
                pd5.setMessage("Please wait");
                pd5.setCancelable(false);
                pd5.show();

            }

            @Override
            protected Object doInBackground(Object[] params) {
                try {
                    String param ="Manager_Id=" + URLEncoder.encode(String.valueOf(UId), "UTF-8")+
                            "&Employees=" + URLEncoder.encode(String.valueOf(tosend), "UTF-8") ;
                    URL url = new URL("http://" + WebServiceConstant.ip + "/Tracking/AddEmployee.php?" + param);
                    Log.e("AFTER ADD EMPLOYEE",""+url);
                    URLConnection con = url.openConnection();
                    HttpURLConnection httpURLConnection = (HttpURLConnection) con;
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setRequestProperty("Accept", "application/json");
                    httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    int rescode = httpURLConnection.getResponseCode();
                    if (rescode == HttpURLConnection.HTTP_OK) {
                        BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                        String inputLine;
                        StringBuffer responce = new StringBuffer();
                        while ((inputLine = in.readLine()) != null) {
                            responce.append(inputLine);
                        }
                        in.close();
                        result = responce.toString();
                        result = result.trim();
                    }
                    Log.e("SELECTEDEMPLOYEERESULT",result);







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
                pd5.cancel();
                if(result.equals("1")) {
                    Toast.makeText(getContext(), "Employee List has been updated", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), ManagerNavigation.class);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(getContext(), "List has not been updated", Toast.LENGTH_SHORT).show();
                }



                /* Fragment fragment=new AssignTask();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_body, fragment);
                fragmentTransaction.commit();
                toolbar.setTitle("Assign Task");*/
            }
        }

    }

    /*@Override
    protected  void onBackPressed(){
        Intent i=new Intent(getContext(), ManagerNavigation.class);
        startActivity(i);
    }*/

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();


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
