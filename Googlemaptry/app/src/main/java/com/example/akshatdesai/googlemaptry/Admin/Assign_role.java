package com.example.akshatdesai.googlemaptry.Admin;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.akshatdesai.googlemaptry.General.Registration;
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
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class Assign_role extends AppCompatActivity {
    Button manager;
    JSONArray array;
    JSONObject temp;
    int status;
    int status1;
    Integer[] Ids;
    String msg, tosend;
    String[] Names;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_role);
        Toast.makeText(Assign_role.this, "in home activity", Toast.LENGTH_SHORT);
        manager = (Button) findViewById(R.id.btn_manager);
        manager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tosend = "";
                new manager().execute();

            }
        });
    }

    public class manager extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] params) {
            try {
//                Log.e("AP", "" + manager);
                //String param = "OId=" + URLEncoder.encode(MainActivity.id + "", "UTF-8");
                URL url = new URL("http://tracking.freevar.com/Tracking/assign.php?");
                URLConnection con = url.openConnection();
                HttpURLConnection httpURLConnection = (HttpURLConnection) con;
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestProperty("Accept", "application/json");
                httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                int rescode = httpURLConnection.getResponseCode();
//                Log.e("I", "" + rescode);

                if (rescode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    String inputLine;
                    StringBuffer responce = new StringBuffer();
                    while ((inputLine = in.readLine()) != null) {
                        responce.append(inputLine);
                    }
                    in.close();
                    msg = responce.toString();
//                    Log.e("responce", "" + msg);
                }

                array = new JSONArray(msg);
                temp = array.getJSONObject(0);

                Log.e("temp", "" + temp);
                status = temp.getInt("status");
                status1 = array.length() + 1;
                Names = new String[status1];

                Ids = new Integer[status1];
                Names[0] = "";
                Ids[0] = -1;
                if (status == 1) {
                    for (int j = 1; j < status1; j++) {
                        temp = array.getJSONObject(j - 1);

                        Names[j] = temp.getString("name");
                        Ids[j] = temp.getInt("id");
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

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            List<CharSequence> list = new ArrayList<CharSequence>();

            for (int i = 1; i < status1; i++) {

                list.add(Names[i]);  // Add the item in the list

            }

            final CharSequence[] dialogList = list.toArray(new CharSequence[list.size()]);
            final AlertDialog.Builder builderDialog = new AlertDialog.Builder(Assign_role.this);
            builderDialog.setTitle("Select Manager");
            int count = dialogList.length;
            boolean[] is_checked = new boolean[count];
//            Log.e("YYYY", "" + ProjectList.pn3);
           /* for (int i = 1, j = 0; i < status1; i++) {
                if (!empIds[i].equals(MainActivity.UId)) {
                    if (ProjectList.pn3.contains("E" + empIds[i] + "E")) {
                        is_checked[j++] = true;
                    } else {
                        j++;
                    }
                }
            }// set is_checked boolean false;
           */ // Creating multiple selection by using setMutliChoiceItem method
            builderDialog.setMultiChoiceItems(dialogList, is_checked, new DialogInterface.OnMultiChoiceClickListener() {
                public void onClick(DialogInterface dialog, int whichButton, boolean isChecked) {
                }
            });

            builderDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ListView list = ((AlertDialog) dialog).getListView();// make selected item in the comma seprated string
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i = 0; i < list.getCount(); i++) {
                        boolean checked = list.isItemChecked(i);
                        if (checked) {
                            if (stringBuilder.length() > 0)
                                stringBuilder.append(",");
                            for (int j = 1; j < status1; j++) {
                                if (Names[j] == list.getItemAtPosition(i)) {
                                    stringBuilder.append(Ids[j]);
                                }
                            }
                        }
                    }

                    if (stringBuilder.toString().trim().equals("")) {
                        stringBuilder.setLength(0);
                    } else {
                        String[] temp = stringBuilder.toString().split(",");
                        for (int i = 0; i < temp.length; i++) {
                            tosend += "e" + temp[i];
                            Log.e("to send", tosend);
                        }
                    }
                    new temp().execute();
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
    }

    public class temp extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] params) {
            try {
                String param = "tosend=" + URLEncoder.encode(String.valueOf(tosend), "UTF-8");
                Log.e("url",""+param);
                URL url = new URL("http://tracking.freevar.com/Tracking/position.php?" + param);
                Log.e("url",""+url);
                URLConnection con = url.openConnection();
                HttpURLConnection httpURLConnection = (HttpURLConnection) con;
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestProperty("Accept", "application/json");
                httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                int rescode = httpURLConnection.getResponseCode();

                Log.e("responce", "" +rescode);
                if (rescode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    String inputLine;
                    StringBuffer responce = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        responce.append(inputLine);
                    }
                    Log.e("responce", "" +responce);
                    in.close();
                    msg = responce.toString();
                    Log.e("responce", "" + msg);
                }
                array = new JSONArray(msg);
                temp = array.getJSONObject(0);

                Log.e("temp", "" + temp);
                status = temp.getInt("status");
                Log.e("temp", "" + status);
                //Log.e("responce", "" + msg);
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            } catch (ProtocolException e1) {
                e1.printStackTrace();
            } catch (MalformedURLException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            Log.e("responce", "" + msg);

            Toast.makeText(getApplicationContext(), "Record Inserted Successfully", Toast.LENGTH_LONG).show();
            //     Intent i = new Intent(getApplicationContext(), MainActivity.class);
            //   startActivity(i);
            // finish();

        }
    }
}




