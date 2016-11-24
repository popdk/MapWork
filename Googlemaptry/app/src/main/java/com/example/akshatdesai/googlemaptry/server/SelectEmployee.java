package com.example.akshatdesai.googlemaptry.server;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.akshatdesai.googlemaptry.General.Login_new;
import com.example.akshatdesai.googlemaptry.General.Sessionmanager;
import com.example.akshatdesai.googlemaptry.R;
import com.example.akshatdesai.googlemaptry.WebServiceConstant;
import com.google.android.gms.cast.framework.SessionManager;

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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SelectEmployee extends AppCompatActivity {
    Button button;
    ProgressDialog pd;
    String msg="";
    String tosend="";
    JSONArray array;
    JSONObject temp;
    public int status, status1, UId;
    public String[] empNames;
    public Integer[] empIds;
    Sessionmanager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_employee);
        button = (Button)findViewById(R.id.button2);
        sessionManager=new Sessionmanager(getApplicationContext());

        HashMap<String, String> user = sessionManager.getuserdetails();
        // name
        if (sessionManager.isLoggedIn())

        {
            UId = Integer.parseInt(user.get(Sessionmanager.KEY_ID));
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pd = new ProgressDialog(SelectEmployee.this);
                pd.setMessage("Please wait");
                pd.show();
                pd.setCancelable(true);
                new employee().execute();



               /* alertdialogbuilder.setItems(value, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String selectedText = Arrays.asList(value).get(which);

                    }
                });*/

            }
        });
    }



    public class employee extends AsyncTask{

        protected Object doInBackground(Object[] params) {
            try {
                //String param = "OrganizationId=" + URLEncoder.encode(MainActivity.OrganizationId + "", "UTF-8");
                URL url = new URL("http://" + WebServiceConstant.ip + "/Tracking/EmployeeList.php" );
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
                Log.e("MSM",msg);
                array = new JSONArray(msg);
                temp = array.getJSONObject(0);
                status = temp.getInt("Status");
                status1 = array.length();
                empNames = new String[status1];
                empIds = new Integer[status1];
                if (status == 1) {
                    for (int j = 0; j < status1; j++) {
                        temp = array.getJSONObject(j);
                        empNames[j] = temp.getString("Name");
                        empIds[j] = temp.getInt("Id");
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

            for (int i = 1; i < status1; i++) {

                list.add(empNames[i]);  // Add the item in the list

            }
            final CharSequence[] dialogList = list.toArray(new CharSequence[list.size()]);
            final AlertDialog.Builder builderDialog = new AlertDialog.Builder(SelectEmployee.this);
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
                            for (int j = 1; j < status1; j++) {
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
                    new temp().execute();
                    Intent i=new Intent(SelectEmployee.this,AssignTask.class);
                    startActivity(i);
                }
            });
            builderDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            AlertDialog alert = builderDialog.create();
            alert.show();
        }

        public class temp extends AsyncTask {
            @Override
            protected Object doInBackground(Object[] params) {
                try {
                    String param ="Manager_id=" + URLEncoder.encode(String.valueOf(UId), "UTF-8")+
                            "&Employees=" + URLEncoder.encode(String.valueOf(tosend), "UTF-8") + "E";
                    URL url = new URL("http://" + WebServiceConstant.ip + "/hrms/Webservice/AssignEmployee.php?" + param);
                    URLConnection con = url.openConnection();
                    HttpURLConnection httpURLConnection = (HttpURLConnection) con;
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setRequestProperty("Accept", "application/json");
                    httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    int rescode = httpURLConnection.getResponseCode();
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
                Toast.makeText(getApplicationContext(), "Employees Added Successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        }

    }

}