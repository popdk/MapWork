package com.example.akshatdesai.googlemaptry.server;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class AssignTask extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText at_taskname, at_taskdescription, at_startingdate, at_endingdate,at_speed,at_stoppage ,at_stoppage1, at_stoppage2,sourcelocation,destinationlocation;
    Button add, cancel, defineRoute;
    ImageView floatingPlus;
    Toolbar toolbar;
    Spinner employees;
    private DatePickerDialog datePickerDialog;
    private DatePickerDialog datePickerDialog2;
    private SimpleDateFormat dateFormatter;
    public int UId;
    public long employeeId;
    String tName = "";
    String tDesc = "";
    String sDate = "";
    String eDate = "";
    ProgressDialog pd;
    Context context;
    String got="";
    String msg="";
    JSONArray array;
    JSONObject temp;
    public int status, status1;
    static int m = 0;
    public String[] empNames;
    public Integer empIds[];
    Sessionmanager sessionManager;
    ArrayList<String> employeeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_task);
      toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Assign Task to Employee");
        //toolbar.setTitle("Assign Task to Employee");
        sourcelocation = (EditText) findViewById(R.id.source_location);
        destinationlocation = (EditText) findViewById(R.id.destination);
        at_taskname = (EditText) findViewById(R.id.task_name);
        at_taskdescription = (EditText) findViewById(R.id.task_desc);
        at_startingdate = (EditText) findViewById(R.id.task_starting_date);
        at_startingdate.setInputType(InputType.TYPE_NULL);
        at_endingdate = (EditText) findViewById(R.id.task_ending_date);
        at_endingdate.setInputType(InputType.TYPE_NULL);
        at_speed = (EditText) findViewById(R.id.travelling_speed);
        at_stoppage = (EditText) findViewById(R.id.stoppage);
        at_stoppage1 = (EditText) findViewById(R.id.stoppage_1);
        at_stoppage1.setVisibility(View.GONE);
        at_stoppage2 = (EditText) findViewById(R.id.stoppage_2);
        at_stoppage2.setVisibility(View.GONE);
        floatingPlus = (ImageView) findViewById(R.id.imageView);
        employees = (Spinner) findViewById(R.id.spinner);
        add = (Button) findViewById(R.id.assign_task_btn_submit);
        cancel = (Button) findViewById(R.id.assign_task_btn_cancel);
        defineRoute = (Button) findViewById(R.id.define_route_button);

        dateFormatter = new SimpleDateFormat();
        employees.setOnItemSelectedListener(this);
        new GetEmployees().execute();
        at_startingdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });
        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                at_startingdate.setText(dateFormatter.format(newDate.getTime()).toString());
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        at_endingdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog2.show();
            }
        });

        datePickerDialog2 = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                at_endingdate.setText(dateFormatter.format(newDate.getTime()).toString());
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        sessionManager = new Sessionmanager(getApplicationContext());

        HashMap<String, String> user = sessionManager.getuserdetails();
        // name
        if (sessionManager.isLoggedIn()) {
            UId = Integer.parseInt(user.get(Sessionmanager.KEY_ID));
        }

        add.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
            @Override
            public void onClick(View v) {

                try {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    sDate = at_startingdate.getText().toString();
                    Log.w("sdate", "" + sDate);
                        /*String[] y1 = sDate.split(" ");
                        String[] w1 = y1[0].split("/");
                        sDate = "20"+ w1[2] + "-" + w1[1] + "-" + w1[0];
                        Log.e("Sdate", ""+ sDate);*/
                    // eDate = format.format(Date.parse(at_endingdate.getText().toString()));
                    eDate = at_endingdate.getText().toString();
                    Log.w("edate", "" + eDate);
                        /*String[] y2 = sDate.split(" ");
                        String[] w2 = y2[0].split("/");
                        eDate = "20"+ w2[2] + "-" + w2[1] + "-" + w2[0];
                        Log.e("Edate", "" + eDate);*/
                    tName = at_taskname.getText().toString();
                    tDesc = at_taskdescription.getText().toString();
                    if (tName.equals("") || tDesc.equals("") || sDate.equals("") || eDate.equals("")) {
                        Toast.makeText(context, "Insufficient details..", Toast.LENGTH_SHORT).show();
                    } else if (at_startingdate.length() != at_endingdate.length()) {
                        Toast.makeText(context, "Select valid End date..", Toast.LENGTH_SHORT).show();
                        at_endingdate.setText("");
                        at_endingdate.callOnClick();
                    } else {
                        new AddTask().execute();
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    Toast.makeText(getApplicationContext(), "Select valid Starting date..", Toast.LENGTH_SHORT).show();
                    at_startingdate.setText("");
                    at_startingdate.callOnClick();
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        defineRoute.setOnClickListener(new View.OnClickListener() {

            String sl = sourcelocation.getText().toString();
            String dl = destinationlocation.getText().toString();

            public void onClick(View v) {
                if(sl == "" || dl == "" )
                {
                    Toast.makeText(AssignTask.this,"Please enter Source and destination",Toast.LENGTH_LONG).show();
                }
                else {
                    Intent callactivity = new Intent(AssignTask.this, DefineRoute.class);
                    callactivity.putExtra("Source",sl);
                    callactivity.putExtra("destination",dl);
                    startActivity(callactivity);
                }
            }
        });

        floatingPlus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                m++;
                if (m == 1) {
                    at_stoppage1.setVisibility(View.VISIBLE);
                } else if (m == 2) {
                    at_stoppage2.setVisibility(View.VISIBLE);
                }
            }

        });


           }


    class GetEmployees extends AsyncTask {
        protected void onPreExecute() {

        }

        protected Object doInBackground(Object[] params) {
            try {
                URL url = new URL("http://" + WebServiceConstant.ip + "/Tracking/EmployeeList.php");
                Log.e("UURL", "" + url);
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
                Log.e("MSG", msg);
                array = new JSONArray(msg);
                temp = array.getJSONObject(0);
                status = temp.getInt("Status");
                status1 = array.length();
                empNames = new String[status1];
                empIds = new Integer[status1];
                employeeList = new ArrayList<String>();
                if (status == 1) {
                    for (int j = 0; j < status1; j++) {
                        temp = array.getJSONObject(j);
                        empNames[j] = temp.getString("Name");
                        empIds[j] = temp.getInt("Id");
                        employeeList.add(empNames[j]);
                    }
                }
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

        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            populateSpinner();
        }

    }


    private void populateSpinner() {
        List<String> lables = new ArrayList<String>();

        for (int i = 0; i < employeeList.size(); i++) {
            lables.add(employeeList.get(i));
        }

        // Creating adapter for spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, lables);

        // Drop down layout style - list view with radio button
        spinnerAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        employees.setAdapter(spinnerAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        employeeId = empIds[i];
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public class AddTask extends AsyncTask {
        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(AssignTask.this);
            pd.setMessage("Adding Task.. please wait");
            pd.show();
            pd.setCancelable(true);
        }

        @Override
        protected Object doInBackground(Object[] params) {
            try {
                String param = "TaskName=" + URLEncoder.encode(String.valueOf(tName), "UTF-8") + "&"
                        + "Desc=" + URLEncoder.encode(String.valueOf(tDesc), "UTF-8") + "&"

                        + "sDate=" + URLEncoder.encode(String.valueOf(sDate), "UTF-8") + "&"
                        + "eDate=" + URLEncoder.encode(String.valueOf(eDate), "UTF-8") + "&"
                        + "AssignedBy=" + URLEncoder.encode(String.valueOf(UId), "UTF-8") + "&"
                        + "AssignedTo=" + URLEncoder.encode(String.valueOf(employeeId), "UTF-8") ;


                URL url = new URL("http://" + WebServiceConstant.ip + "/Tracking/AddTaskWebservice.php?" + param);
                URLConnection con = url.openConnection();
                HttpURLConnection httpURLConnection = (HttpURLConnection) con;

                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestProperty("Accept", "application/json");
                httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                int rescode = httpURLConnection.getResponseCode();
               Log.e("I", "" + url);
//
                if (rescode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    String inputLine;
                    StringBuffer responce = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        responce.append(inputLine);
                    }

                    in.close();
                    got = responce.toString();
                    got=got.trim();
                    Log.e("responce",got);
                }



//                Log.e("from", got);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
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
            if (got.equals("1")) {
                Toast.makeText(AssignTask.this, "Task is assigned Successfully", Toast.LENGTH_SHORT).show();

                at_taskname.setText("");
                at_taskdescription.setText("");
                at_startingdate.setText("");
                at_endingdate.setText("");
                Intent i=new Intent(AssignTask.this,ManagerNavigation.class);
                startActivity(i);

            } else {
                Toast.makeText(AssignTask.this, "Problem in assigning Task.", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
