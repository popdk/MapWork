package com.example.akshatdesai.googlemaptry.client;

/**
 * Created by Akshat Desai on 11/16/2016.
 */

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.akshatdesai.googlemaptry.Admin.Assign_role;
import com.example.akshatdesai.googlemaptry.General.EnablePermission;
import com.example.akshatdesai.googlemaptry.WebServiceConstant;
import com.google.android.gms.location.LocationRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY;

public class LocationUpdateService extends Service {
    private static final String TAG = "GPS";
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 1000 * 30;
    private static final float LOCATION_DISTANCE = 10f;
    private static int taskid;

   // static int val =0;

    private class LocationListener implements android.location.LocationListener {
        Location mLastLocation;

        public LocationListener(String provider) {
            Log.e(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location) {
            Log.e(TAG, "onLocationChanged: " + location);
          //  Toast.makeText(LocationUpdateService.this,""+location,Toast.LENGTH_LONG).show();
            mLastLocation.set(location);
            if(EnablePermission.isInternetConnected(getApplicationContext())) {
                new Web(location, taskid).execute();
            }else{
                Toast.makeText(getApplicationContext(),"No internet connection",Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.e(TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.e(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
          //  Log.e(TAG, "onStatusChanged: " + provider);
        }
    }

    LocationListener[] mLocationListeners = new LocationListener[]{
            new LocationListener(LocationManager.GPS_PROVIDER),
           // new LocationListener(LocationManager.NETWORK_PROVIDER)
    };

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);

        if(intent.hasExtra("taskid")) {
            taskid = Integer.parseInt(intent.getExtras().getString("taskid"));
        }
            Log.e("TASKID",""+taskid);
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate");
        initializeLocationManager();
      /*  try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[1]);

        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }*/
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[0]);



            LocationRequest mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(10000);
            mLocationRequest.setFastestInterval(5000);
            mLocationRequest.setPriority(PRIORITY_HIGH_ACCURACY);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Toast.makeText(this, "gps provider does not exist", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    Log.i(TAG, "fail to remove location listners, ignore", ex);
                }
            }
        }
    }


        public  class Web extends AsyncTask
        {
            Location location;
            String currentDateandTime;
            int taskid;
            Web(Location location,int taskid)
            {
                this.location = location;
                this.taskid = taskid;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                 currentDateandTime = sdf.format(new Date());

            }

            @Override
            protected Object doInBackground(Object[] objects) {
                try {
                   String responce="";
                    //Log.e("YYY",""+responce);
                    HttpURLConnection httpURLConnection = null;
                    String param="latitude="+ URLEncoder.encode(String.valueOf(location.getLatitude()), "UTF-8")+
                            "&longitude="+ URLEncoder.encode(String.valueOf(location.getLongitude()), "UTF-8")+
                            "&datetime="+URLEncoder.encode(String.valueOf(currentDateandTime), "UTF-8")+
                            "&taskid="+URLEncoder.encode(String.valueOf(taskid),"UTF-8");
                    URL url=new URL("http://"+ WebServiceConstant.ip+"/Tracking/insert.php?"+param);
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setRequestProperty("Accept", "application/json");
                    httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                    Log.e(TAG,""+url);

                    int i = httpURLConnection.getResponseCode();
                    Log.e(TAG,""+i);
                    if (i == HttpURLConnection.HTTP_OK) {
                        BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                        String inputLine;
                        StringBuffer response = new StringBuffer();

                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }

                        in.close();
                        responce = response.toString();
                        Log.e(TAG,""+responce);
                    }
                }

                catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }













    }



    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
           // LocationRequest lr = (LocationRequest) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }
}
