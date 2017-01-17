package com.example.akshatdesai.googlemaptry.server;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.akshatdesai.googlemaptry.General.EnablePermission;
import com.example.akshatdesai.googlemaptry.General.Login_new;
import com.example.akshatdesai.googlemaptry.R;
import com.example.akshatdesai.googlemaptry.WebServiceConstant;
import com.example.akshatdesai.googlemaptry.server.GetterSetter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import static com.example.akshatdesai.googlemaptry.server.DefineRoute.connection;
import static com.example.akshatdesai.googlemaptry.server.DefineRoute.snackbar;

public class Tracking extends AppCompatActivity implements OnMapReadyCallback {


    private GoogleMap googleMap;
    static GetterSetter getterSetter;
    double latitude[], longitude[];
    String time[];
    private String TAG = "TRACKING";
    public Timer t;
    int id, dotrack;
    Polyline line;
    Marker marker;

    CoordinatorLayout coordinatorLayout;
    ProgressDialog pd1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);

        Intent i = getIntent();
        id = i.getIntExtra("taskid", 0);
        dotrack = i.getIntExtra("dotrack", 0);

        Log.e("TaskId", "" + id);
        Log.e("Flag","" +dotrack);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorlayout);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map1);
        mapFragment.getMapAsync(this);


    }



    @Override
    public void onResume() {
        //will be executed onResume
        super.onResume();
       // connection = EnablePermission.isInternetConnected(Tracking.this);
        if(EnablePermission.isInternetConnected(Tracking.this)) {
            if (dotrack == 1) {
                t = new Timer();

                t.scheduleAtFixedRate(new TimerTask() {

                    @Override
                    public void run() {

                        runOnUiThread(new Runnable(){

                            @Override
                            public void run() {
                                new Track().execute();
                            }});
                    }
                }, 0, 60000);

            } else {
                new Track().execute();
            }
        }else {
            Toast.makeText(Tracking.this,"No Internet Connection",Toast.LENGTH_LONG).show();
        }



    }

    @Override
    protected void onPause() {
        super.onPause();
        if (t != null) {
            t.cancel();
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.w(TAG, "Map Ready");
        //double lat[] ,lang[];
        //String tm[];
        this.googleMap = googleMap;


    }

    public class Track extends AsyncTask {

        JSONArray array;
        JSONObject object;
        int status;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd1 = new ProgressDialog(Tracking.this);
            pd1.setMessage("Please wait");
            pd1.setCancelable(false);
            pd1.show();

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
                String param = "id=" + URLEncoder.encode(String.valueOf(id), "UTF-8");
                URL url = new URL("http://" + WebServiceConstant.ip + "/Tracking/fetch.php?" + param);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestProperty("Accept", "application/json");
                httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                int i = httpURLConnection.getResponseCode();
                Log.e(TAG,""+ url);
                if (i == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }

                    in.close();
                    responce = "responce" + response.toString();
                    String[] res = responce.split("\\[");
                    responce = "[" + res[1];
                    Log.e(TAG, "" + responce);
                }


                array = new JSONArray(responce);
                object = array.getJSONObject(0);
                status = object.getInt("status");
                Log.e(TAG, "" + array.length());


                if (status == 1) {
                    int size = array.length();
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
                else
                {
                    Log.e("IN Else","");
                    getterSetter = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            pd1.cancel();
            if (getterSetter != null) {
                if (getterSetter.getLatitude().length != 0) {

                    double lat[] = getterSetter.getLatitude();
                    double lang[] = getterSetter.getLongitude();
                    String tm[] = getterSetter.getTime();
                    //lat = new double[la]
                    Bitmap drawableBitmap = getBitmap(R.drawable.black_dot);
                    for (int j = 0; j < lang.length; j++) {


                        googleMap.addMarker(new MarkerOptions().position(new LatLng(lat[j], lang[j])).title(tm[j])
                                .icon(BitmapDescriptorFactory.fromBitmap(drawableBitmap)));
                    }
                    LatLng latLng = new LatLng(lat[0], lang[0]);
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f));
                    int NoOfTimes = lat.length/10;
                    NoOfTimes += 1;
                    String path="";
                    List<String> urlList= new ArrayList<String>();
                    int flag= 0;
                    for(int i= 0;i<NoOfTimes;i++) {

                        if(i == NoOfTimes-1)
                        {
                            if(flag ==0) {
                                path = makeURL(lat, lang);
                                urlList.add(path);
                            }
                            else {
                                int k = lat.length;
                                k= k%10;
                                if(k != 0) {
                                    double lat1[] = new double[k];
                                    double lang1[] = new double[k];
                                    System.arraycopy(lat, Integer.parseInt(i + "0"), lat1, 0, k);
                                    System.arraycopy(lang, Integer.parseInt(i + "0"), lang1, 0, k);
                                    path = makeURL(lat1, lang1);
                                    Log.e("PATH", path);
                                    urlList.add(path);
                                }
                            }
                        }
                        else {
                            flag=1;
                            double lat1[] = new double[10];
                            double lang1[] = new double[10];
                             System.arraycopy(lat, Integer.parseInt(i + "0"), lat1, 0, 10);
                            System.arraycopy(lang, Integer.parseInt(i + "0"), lang1, 0, 10);
                            path = makeURL(lat1, lang1);
                            Log.e("PATH", path);
                            urlList.add(path);
                        }
                    }
                    if(EnablePermission.isInternetConnected(Tracking.this)) {

                      for(int m =0;m<urlList.size();m++)
                      {
                          try {
                              new connectAsyncTask(urlList.get(m)).execute().get();
                          } catch (InterruptedException e) {
                              e.printStackTrace();
                          } catch (ExecutionException e) {
                              e.printStackTrace();
                          }
                      }

                    }else {
                        Toast.makeText(Tracking.this,"No Internet Connection",Toast.LENGTH_LONG).show();
                    }

                }

            } else {
                snackbar = Snackbar
                        .make(coordinatorLayout, "No Data Found", Snackbar.LENGTH_INDEFINITE);
                snackbar.show();
            }
        }

        private Bitmap getBitmap(int drawableRes) {
            Drawable drawable = getResources().getDrawable(drawableRes);
            Canvas canvas = new Canvas();
            Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            canvas.setBitmap(bitmap);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            drawable.draw(canvas);

            return bitmap;
        }
    }

    public String makeURL(double lat[], double lang[]) {
        StringBuilder urlString = new StringBuilder();
        int length1 = lat.length;
        urlString.append("https://maps.googleapis.com/maps/api/directions/json");
        urlString.append("?origin=");// from
        urlString.append(Double.toString(lat[0]));
        urlString.append(",");
        urlString
                .append(Double.toString(lang[0]));
        urlString.append("&destination=");// to
        urlString
                .append(Double.toString(lat[length1 - 1]));
        urlString.append(",");
        urlString.append(Double.toString(lang[length1 - 1]));


            callmenow(lat, lang, urlString);

        //urlString.append("&waypoints="+URLEncoder.encode(String.valueOf(stp), "UTF-8")+"|"+ ) ; //Godhra|Halol);
        //  urlString.append("alternatives=true");
        urlString.append("&region=in");
        urlString.append("&sensor=false&mode=driving|walking&alternatives=true");
        urlString.append("&language=en|gu|hi");
        urlString.append("&key=AIzaSyCjvYgsqwRJCaySPonM8xAmdKohDwUYy5M");
        return urlString.toString();
    }


    private void callmenow(double lat[], double lang[], StringBuilder urlString) {


        urlString.append("&waypoints=");
        Log.e("TotalWayPoints",""+lat.length);

        String s = "";
        if(lat.length <11) {
            for (int i = 1; i < lat.length - 1; i++) {
                s = s + Double.toString(lat[i]) + "," + Double.toString(lang[i]);

                if (i != lat.length - 2) {
                    s = s + "|";
                }
            }
        }

        urlString.append(s);

        Log.e("TRACKINGLINE", "" + urlString);


    }


    public class connectAsyncTask extends AsyncTask<Void, Void, String> {
        private ProgressDialog progressDialog;
        String url;
        boolean steps;

        connectAsyncTask(String urlPass) {
            url = urlPass;
            // steps =withSteps;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog = new ProgressDialog(Tracking.this);
            progressDialog.setMessage("Fetching route, Please wait...");
            progressDialog.setIndeterminate(true);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            JSONParser jParser = new JSONParser();
            String json = jParser.getJSONFromUrl(url);
            Log.w("Json", "" + json);
            return json;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.hide();
            try {
                JSONObject path = new JSONObject(result);
                String s = path.getString("status");

                if (!s.equalsIgnoreCase("zero_results")) {
                    drawPath(result);
                } else {
                    Toast.makeText(Tracking.this, "By Driving and Walking Not Possible", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


    public void drawPath(String result) {

        try {
            //Tranform the string into a json object
            final JSONObject json = new JSONObject(result);
            JSONArray routeArray = json.getJSONArray("routes");
            JSONObject routes = routeArray.getJSONObject(0);
            JSONObject overviewPolylines = routes.getJSONObject("overview_polyline");
            String encodedString = overviewPolylines.getString("points");
            List<LatLng> list = decodePoly(encodedString);
            Log.e("New", "" + list.get(0));
            line = googleMap.addPolyline(new PolylineOptions()
                    .addAll(list)
                    .width(12)
                    .color(Color.parseColor("#05b1fb"))//Google maps blue color
                    .geodesic(true)
            );
            double lati[] = getterSetter.getLatitude();
            double longi[] = getterSetter.getLongitude();
              marker = googleMap.addMarker(new MarkerOptions().position(new LatLng(lati[0],longi[0])).title("Source Point"));
            marker = googleMap.addMarker(new MarkerOptions().position(new LatLng(lati[(lati.length -1)],longi[(longi.length-1)])).title("Destination Point"));
           /* mMap.addMarker(new MarkerOptions().position(list.get(0)).title("Source Point") );
            mMap.addMarker(new MarkerOptions().position(list.get(list.size()-1)).title("Destination Point") );*/
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(list.get(0), 10.0f));


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng( (((double) lat / 1E5)),
                    (((double) lng / 1E5) ));
            poly.add(p);
        }

        return poly;
    }
}
