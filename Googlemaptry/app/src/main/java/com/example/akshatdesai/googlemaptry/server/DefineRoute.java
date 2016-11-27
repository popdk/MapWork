package com.example.akshatdesai.googlemaptry.server;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.akshatdesai.googlemaptry.General.EnablePermission;
import com.example.akshatdesai.googlemaptry.Notification.RegistrationIntentService;
import com.example.akshatdesai.googlemaptry.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DefineRoute extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    Button btn;
    EditText source, destination;
    LatLng start, end;
    static String s1;
    static LatLng point;
    static int i = 0;
    HashMap<String, LatLng> hm;
    Polyline line;
    GoogleApiClient mGoogleApiClient;
    Marker marker;
    CoordinatorLayout coordinatorLayout;
    static Snackbar snackbar;
    static   boolean connection;
    private BroadcastReceiver mRegistrationBroadcastReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorlayout);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        btn = (Button) findViewById(R.id.btn_submit);
        source = (EditText) findViewById(R.id.et_source);
        destination = (EditText) findViewById(R.id.et_destination);
        if (savedInstanceState == null) {

            EnablePermission.checklocationservice(DefineRoute.this);

            /*Intent in = getIntent();
            source.setText(in.getStringExtra("Source"));
            destination.setText(in.getStringExtra("destination"));
            String s = source.getText().toString();
            s1 = destination.getText().toString();
            new fetchLatLongFromService(s).execute();


            new fetchLatLongFromService(s1).execute();*/

            Intent i = new Intent(this, RegistrationIntentService.class);
            startService(i);

           /* mRegistrationBroadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    // mRegistrationProgressBar.setVisibility(ProgressBar.GONE);
                    SharedPreferences sharedPreferences =
                            PreferenceManager.getDefaultSharedPreferences(context);
                    boolean sentToken = sharedPreferences
                            .getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
                    if (sentToken) {
                        // mInformationTextView.setText(getString(R.string.gcm_send_message));
                        Log.e("tokensent",""+sentToken);
                    } else {
                        //  mInformationTextView.setText(getString(R.string.token_error_message));
                        Log.e("Nottokensent",""+sentToken);
                    }
                }
            };*/


            // Toast.makeText(DefineRoute.this,"After First",Toast.LENGTH_SHORT).show();
            //start = point;


                /*end =point;
                Log.e("Slatitude",""+((double) start.latitude));
                Log.e("Slongitude",""+((double) start.longitude));
                Log.e("Elatitude",""+((double) end.latitude));
                Log.e("Elongitude",""+((double) end.longitude));
                String path = makeURL(((double) start.latitude),((double)start.longitude),((double)end.latitude),((double)end.longitude));*/
            // new connectAsyncTask(path,true).execute();


        }



        /*btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?saddr=%f,%f(%s)&daddr=%f,%f(%s)saddr=%f,%f(%s)&daddr=%f,%f (%s)",
                        23.0804, 72.5241, "Where the party is at", 32.2133397, -98.2194769,"Between station LJ",  32.2133397, -98.2194769,  "Between station Lj", 23.0124, 72.5968,"Home sweet home");
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.DefineRoute");
                try
                {
                    startActivity(intent);
                }
                catch(ActivityNotFoundException ex)
                {
                    try
                    {
                        Intent unrestrictedIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                        startActivity(unrestrictedIntent);
                    }
                    catch(ActivityNotFoundException innerEx)
                    {
                        Toast.makeText(DefineRoute.this, "Please install a maps application", Toast.LENGTH_LONG).show();
                    }
                }
               // startActivity(intent);

                //Intent i = new Intent(this,Mai)
            }
        });*/
    }

    public void showresult(View view) {


        if(EnablePermission.isInternetConnected(DefineRoute.this)) {


            hm = new HashMap<>();
            i = 0;
            String s = source.getText().toString();
            s1 = destination.getText().toString();


            if (s.equals("") || s1.equals("")) {
                Toast.makeText(DefineRoute.this, "Please enter Source and destination Address", Toast.LENGTH_SHORT).show();
            } else {


                if (s.equalsIgnoreCase("my location")) {
                    myLocation();
                } else {
                    new fetchLatLongFromService(s).execute();
                }
                if (s1.equalsIgnoreCase("my location")) {
                    myLocation();
                } else {
                    new fetchLatLongFromService(s1).execute();
                }


            }
        }
        else {

            Toast.makeText(DefineRoute.this,"Please Connect to the internet",Toast.LENGTH_LONG).show();
        }

    }







    private void myLocation()
    {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            return;
        }
        Location currentLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if(currentLocation != null)
        {
            LatLng currentlatlng = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
            hm.put("Start",currentlatlng);
            i++;
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        //int flag = 0;

         connection = EnablePermission.isInternetConnected(DefineRoute.this);
        snackbar = Snackbar
                .make(coordinatorLayout, "Please Enable Internet", Snackbar.LENGTH_INDEFINITE);
        if(!connection)
        {
            snackbar.show();
        }
      /*  new fetchLatLongFromService(s1).execute();
        Toast.makeText(DefineRoute.this,"After ",Toast.LENGTH_SHORT).show();*/

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        buildGoogleApiClient();
        mGoogleApiClient.connect();

    }

    protected synchronized void buildGoogleApiClient() {
        //Toast.makeText(this, "buildGoogleApiClient", Toast.LENGTH_SHORT).show();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener( this)
                .addApi(LocationServices.API)
                .build();
    }

    public LatLng getLocationFromAddress(String strAddress){
        LatLng latLng = null;
        Geocoder coder = new Geocoder(this);
        List<Address> address;

       // GeoPoint p1 = null;

        try {
            address = coder.getFromLocationName(strAddress,5);
            if (address==null) {
              //  return null;
            }
            Address location=address.get(0);
            location.getLatitude();
            location.getLongitude();
            double d = location.getLatitude();
            double d1 = location.getLongitude();
             latLng = new LatLng(d, d1);

            Toast.makeText(DefineRoute.this,"Latitude "+location.getLatitude()+" Longitude "+location.getLongitude()+"",Toast.LENGTH_LONG).show();

       /*     p1 = new GeoPoint((double) (location.getLatitude() * 1E6),
                    (double) (location.getLongitude() * 1E6));*/

           // return p1;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return latLng;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    public class fetchLatLongFromService extends
            AsyncTask<Void, Void, String> {
        String place;
        ProgressDialog pd;
        String msg;



        public fetchLatLongFromService(String place) {
            super();
            this.place = place;

        }

        @Override
        protected void onCancelled() {
            // TODO Auto-generated method stub
            super.onCancelled();
            this.cancel(false);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           // i++;
            pd = new ProgressDialog(DefineRoute.this);
            pd.setTitle("Please Wait");
            pd.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub
            try {
                String param = "address=" + URLEncoder.encode(String.valueOf(this.place), "UTF-8");
                String googleMapUrl = "https://maps.googleapis.com/maps/api/geocode/json?"
                        + param + "&sensor=false&key=AIzaSyCjvYgsqwRJCaySPonM8xAmdKohDwUYy5M";


                URL url = new URL(googleMapUrl);
                Log.e("URL",""+url);
                URLConnection con = url.openConnection();
                HttpURLConnection httpURLConnection = (HttpURLConnection) con;
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestProperty("Accept", "application/json");
                httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");




                int rescode = httpURLConnection.getResponseCode();
                Log.e("Responce",""+rescode);
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
                return msg;
            }  catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            pd.cancel();

            try {
                JSONObject jsonObj = new JSONObject(result.toString());
                String status = jsonObj.getString("status");


                if(status.equals("ZERO_RESULTS")) {
                    Toast.makeText(DefineRoute.this,"Can't Locate this Addtess",Toast.LENGTH_LONG).show();
                }
                else{
                    i++;
                    JSONArray resultJsonArray = jsonObj.getJSONArray("results");

                    // Extract the Place descriptions from the results
                    // resultList = new ArrayList<String>(resultJsonArray.length());

                    JSONObject before_geometry_jsonObj = resultJsonArray
                            .getJSONObject(0);

                    JSONObject geometry_jsonObj = before_geometry_jsonObj
                            .getJSONObject("geometry");

                    JSONObject location_jsonObj = geometry_jsonObj
                            .getJSONObject("location");

                    String lat_helper = location_jsonObj.getString("lat");
                    double lat = Double.valueOf(lat_helper);


                    String lng_helper = location_jsonObj.getString("lng");
                    double lng = Double.valueOf(lng_helper);

                    Toast.makeText(DefineRoute.this, "Lat:" + lat + " Long :" + lng, Toast.LENGTH_LONG).show();
                    point = new LatLng(lat, lng);


                    if (i == 1) {
                        hm.put("Start", point);
                    } else if (i == 2) {
                        hm.put("end", point);
                        double Lati1 = hm.get("Start").latitude;
                        double Lang1 = hm.get("Start").longitude;
                        double Lati2 = hm.get("end").latitude;
                        double Lang2 = hm.get("end").longitude;

                      /*  if (line != null) {
                            line.remove();

                        }
                        if(marker != null )
                        {
                            marker.remove();
                        }*/

                        mMap.clear();


                        String path = makeURL(Lati1, Lang1, Lati2, Lang2);
                        new connectAsyncTask(path, false).execute();
                        i = 0;
                    }

                }

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();

            }
        }
    }

    public String makeURL (double sourcelat, double sourcelog, double destlat, double destlog ){
        StringBuilder urlString = new StringBuilder();
        urlString.append("https://maps.googleapis.com/maps/api/directions/json");
        urlString.append("?origin=");// from
        urlString.append(Double.toString(sourcelat));
        urlString.append(",");
        urlString
                .append(Double.toString( sourcelog));
        urlString.append("&destination=");// to
        urlString
                .append(Double.toString( destlat));
        urlString.append(",");
        urlString.append(Double.toString( destlog));
        urlString.append("&sensor=false&mode=driving&alternatives=true");
        urlString.append("&key=AIzaSyCjvYgsqwRJCaySPonM8xAmdKohDwUYy5M");
        return urlString.toString();
    }






    private class connectAsyncTask extends AsyncTask<Void, Void, String> {
        private ProgressDialog progressDialog;
        String url;
        boolean steps;
        connectAsyncTask(String urlPass,boolean withSteps){
            url = urlPass;
            steps =withSteps;
        }
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog = new ProgressDialog(DefineRoute.this);
            progressDialog.setMessage("Fetching route, Please wait...");
            progressDialog.setIndeterminate(true);
            progressDialog.show();
        }
        @Override
        protected String doInBackground(Void... params) {
            JSONParser jParser = new JSONParser();
            String json = jParser.getJSONFromUrl(url);
            Log.w("Json",""+json);
            return json;

        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.hide();
            try {
                JSONObject path = new JSONObject(result);
                String s = path.getString("status");

                if(!s.equalsIgnoreCase("zero_results")){
                    drawPath(result,steps);
                }
                else
                {
                    Toast.makeText(DefineRoute.this,"By Driving and Walking Not Possible",Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


    public void drawPath(String  result,boolean withSteps) {

        try {
            //Tranform the string into a json object
            final JSONObject json = new JSONObject(result);
            JSONArray routeArray = json.getJSONArray("routes");
            JSONObject routes = routeArray.getJSONObject(0);
            JSONObject overviewPolylines = routes.getJSONObject("overview_polyline");
            String encodedString = overviewPolylines.getString("points");
            List<LatLng> list = decodePoly(encodedString);
            Log.e("New",""+list.get(0));
            line = mMap.addPolyline(new PolylineOptions()
                    .addAll(list)
                    .width(12)
                    .color(Color.parseColor("#05b1fb"))//Google maps blue color
                    .geodesic(true)
            );
             marker =  mMap.addMarker(new MarkerOptions().position(list.get(0)).title("Source Point"));
            marker = mMap.addMarker(new MarkerOptions().position(list.get(list.size()-1)).title("Destination Point") );
           /* mMap.addMarker(new MarkerOptions().position(list.get(0)).title("Source Point") );
            mMap.addMarker(new MarkerOptions().position(list.get(list.size()-1)).title("Destination Point") );*/
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(list.get(0),10.0f));

            if(withSteps)
            {
                JSONArray arrayLegs = routes.getJSONArray("legs");
                JSONObject legs = arrayLegs.getJSONObject(0);
                Log.w("legs",""+legs);
                JSONObject jsonObject = legs.getJSONObject("distance");
                Log.w("Distance",""+jsonObject);
                String text = jsonObject.getString("text");
                Log.w("Distance",""+text);
                JSONObject jsonObject1 = legs.getJSONObject("duration");
                String dtext = jsonObject1.getString("text");
                Log.w("Duration",""+dtext);
                JSONArray stepsArray = legs.getJSONArray("steps");
                //put initial point

                for(int i=0;i<stepsArray.length();i++)
                {
                    Step step = new Step(stepsArray.getJSONObject(i));
                    mMap.addMarker(new MarkerOptions()
                            .position(step.location)
                            .title(step.distance)
                            .snippet(step.instructions)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

                }
            }
           /*
           for(int z = 0; z<list.size()-1;z++){
                LatLng src= list.get(z);
                LatLng dest= list.get(z+1);
                Polyline line = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(src.latitude, src.longitude), new LatLng(dest.latitude,   dest.longitude))
                .width(2)
                .color(Color.BLUE).geodesic(true));
            }
           */
        }
        catch (JSONException e) {

        }
    }










    private class Step
    {
        public String distance;
        public LatLng location;
        public String instructions;

        Step(JSONObject stepJSON)
        {
            JSONObject startLocation;
            try {

                distance = stepJSON.getJSONObject("distance").getString("text");
                startLocation = stepJSON.getJSONObject("start_location");
                location = new LatLng(startLocation.getDouble("lat"),startLocation.getDouble("lng"));
                try {
                    instructions = URLDecoder.decode(Html.fromHtml(stepJSON.getString("html_instructions")).toString(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                };

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
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

