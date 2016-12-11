package com.example.akshatdesai.googlemaptry.server;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.akshatdesai.googlemaptry.General.EnablePermission;
import com.example.akshatdesai.googlemaptry.R;
import com.example.akshatdesai.googlemaptry.WebServiceConstant;
import com.example.akshatdesai.googlemaptry.client.CurrentLocation;
import com.example.akshatdesai.googlemaptry.server.GetterSetter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.akshatdesai.googlemaptry.server.DefineRoute.connection;
import static com.example.akshatdesai.googlemaptry.server.DefineRoute.snackbar;

public class Tracking extends AppCompatActivity implements OnMapReadyCallback {


    private GoogleMap googleMap;
    static GetterSetter getterSetter;
    double latitude[],longitude[];
    String time[];
    private String TAG ="TRACKING";
   public Timer t;
    int id,dotrack;
    CoordinatorLayout coordinatorLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);

        Intent i = getIntent();
         id= i.getIntExtra("taskid",0);
         dotrack = i.getIntExtra("dotrack",0);

        Log.e("TaskId",""+id);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorlayout);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map1);
        mapFragment.getMapAsync(this);

        if(dotrack == 1) {
            t = new Timer();
//Set the schedule function and rate
            t.scheduleAtFixedRate(new TimerTask() {

                                      @Override
                                      public void run() {
                                          //Called each time when 1000 milliseconds (1 second) (the period parameter)
                                          new Track().execute();
                                      }

                                  },
//Set how long before to start calling the TimerTask (in milliseconds)
                    0,
//Set the amount of time between each execution (in milliseconds)
                    60000);
        }


    }


    @Override
    public void onResume() {
        //will be executed onResume
        super.onResume();
        connection = EnablePermission.isInternetConnected(Tracking.this);

        if(!connection)
        {
            snackbar = Snackbar
                    .make(coordinatorLayout, "Please Enable Internet", Snackbar.LENGTH_INDEFINITE);
            snackbar.show();
        }



      else {
           /* t = new Timer();
//Set the schedule function and rate
            t.scheduleAtFixedRate(new TimerTask() {

                                      @Override
                                      public void run() {
                                          //Called each time when 1000 milliseconds (1 second) (the period parameter)
                                          new Track().execute();
                                      }

                                  },
//Set how long before to start calling the TimerTask (in milliseconds)
                    0,
//Set the amount of time between each execution (in milliseconds)
                    60000);*/

        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(t != null)
        {
            t.cancel();
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.w(TAG,"Map Ready");
        //double lat[] ,lang[];
        //String tm[];
        this.googleMap = googleMap;



    }

        public class Track extends AsyncTask {

        JSONArray array;
        JSONObject object;
        int status;

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if(getterSetter != null) {
                    if (getterSetter.getLatitude().length != 0) {

                        double lat[] = getterSetter.getLatitude();
                        double lang[] = getterSetter.getLongitude();
                        String tm[] = getterSetter.getTime();
                        //lat = new double[la]

                        for (int j = 0; j < lang.length; j++) {


                            googleMap.addMarker(new MarkerOptions().position(new LatLng(lat[j], lang[j])).title(tm[j])
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.cir)));
                        }
                        LatLng latLng = new LatLng(lat[0], lang[0]);
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f));

                    }

                }

                else{
                    snackbar = Snackbar
                            .make(coordinatorLayout, "No Data Found", Snackbar.LENGTH_INDEFINITE);
                    snackbar.show();
                }
            }

            @Override
        protected Object doInBackground(Object[] objects) {
            try {
                String responce = "";
                //Log.e("YYY",""+responce);
                HttpURLConnection httpURLConnection = null;
                /*
                String param = "latitude=" + URLEncoder.encode(String.valueOf(location.getLatitude()), "UTF-8") +
                        "&longitude=" + URLEncoder.encode(String.valueOf(location.getLongitude()), "UTF-8") +
                        "&datetime=" + URLEncoder.encode(String.valueOf(currentDateandTime), "UTF-8");*/
                String param = "id="+ URLEncoder.encode(String.valueOf(id), "UTF-8");
                URL url = new URL("http://" + WebServiceConstant.ip + "/Tracking/fetch.php?"+param);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestProperty("Accept", "application/json");
                httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                int i = httpURLConnection.getResponseCode();
                Log.e(TAG, "" + i);
                if (i == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }

                    in.close();
                    responce = "responce"+response.toString();
                    String[] res = responce.split("\\[");
                    responce = "["+res[1];
                    Log.e(TAG, "" + responce);
                }


                array = new JSONArray(responce);
                object = array.getJSONObject(0);
                status = object.getInt("status");
                Log.e(TAG,""+array.length());


                if (status == 1) {
                    int size=array.length();
                    latitude = new double[size];
                    longitude = new double[size];
                    time = new String[size];


                    for (int j = 0; j < array.length(); j++) {
                        object = array.getJSONObject(j);
                        latitude[j] = object.getDouble("latitude");
                        longitude[j] = object.getDouble("longitude");
                        time[j] = object.getString("time");

                    }
                    getterSetter = new GetterSetter();
                    getterSetter.setLatitude(latitude);
                    getterSetter.setLongitude(longitude);
                    getterSetter.setTime(time);

                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}