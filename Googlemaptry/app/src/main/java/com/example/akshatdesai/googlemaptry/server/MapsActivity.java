package com.example.akshatdesai.googlemaptry.server;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.akshatdesai.googlemaptry.JSONParser;
import com.example.akshatdesai.googlemaptry.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.Barcode.GeoPoint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Button btn;
    EditText source,destination;
    LatLng start,end;
   static String s1;
   static LatLng point;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        btn = (Button) findViewById(R.id.btn_submit);
        source = (EditText) findViewById(R.id.et_source);
        destination = (EditText) findViewById(R.id.et_destination);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = source.getText().toString();
                 s1 = destination.getText().toString();
               new fetchLatLongFromService(s).execute();
                Toast.makeText(MapsActivity.this,"After First",Toast.LENGTH_SHORT).show();
                //start = point;


                /*end =point;
                Log.e("Slatitude",""+((double) start.latitude));
                Log.e("Slongitude",""+((double) start.longitude));
                Log.e("Elatitude",""+((double) end.latitude));
                Log.e("Elongitude",""+((double) end.longitude));
                String path = makeURL(((double) start.latitude),((double)start.longitude),((double)end.latitude),((double)end.longitude));*/
               // new connectAsyncTask(path,true).execute();

            }
        });
        /*btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?saddr=%f,%f(%s)&daddr=%f,%f(%s)saddr=%f,%f(%s)&daddr=%f,%f (%s)",
                        23.0804, 72.5241, "Where the party is at", 32.2133397, -98.2194769,"Between station LJ",  32.2133397, -98.2194769,  "Between station Lj", 23.0124, 72.5968,"Home sweet home");
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
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
                        Toast.makeText(MapsActivity.this, "Please install a maps application", Toast.LENGTH_LONG).show();
                    }
                }
               // startActivity(intent);

                //Intent i = new Intent(this,Mai)
            }
        });*/
    }


    @Override
    protected void onResume() {
        super.onResume();

      /*  new fetchLatLongFromService(s1).execute();
        Toast.makeText(MapsActivity.this,"After ",Toast.LENGTH_SHORT).show();*/

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     *
     *
     *
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera



        /*LatLng sydney = new LatLng(23.0804, 72.5241);
        LatLng sydney1 = new LatLng(23.0124, 72.5968);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.addMarker(new MarkerOptions().position(sydney1).title("Marker Not in Sydney").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.setMinZoomPreference(7.0f);*/



        //markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
       /* Polyline line  = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(23.0804, 72.5241),new LatLng(23.0124, 72.5968))
                .width(5)
                .color(Color.BLUE));
        line.setClickable(true);*/
        //GoogleMap.OnPolylineClickListener(new On())
        /*String s = makeURL(((double) start.latitude),((double)start.longitude),((double)end.latitude),((double)end.longitude));
        new connectAsyncTask(s,true).execute();*/
   /*    double d = distance(23.073426,72.626571,23.013413,72.562410);
        Toast.makeText(this,"Distance = "+d,Toast.LENGTH_LONG).show();*/

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

            Toast.makeText(MapsActivity.this,"Latitude "+location.getLatitude()+" Longitude "+location.getLongitude()+"",Toast.LENGTH_LONG).show();

       /*     p1 = new GeoPoint((double) (location.getLatitude() * 1E6),
                    (double) (location.getLongitude() * 1E6));*/

           // return p1;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return latLng;
    }


    public class fetchLatLongFromService extends
            AsyncTask<Void, Void, String> {
        String place;
        ProgressDialog pd;


        public fetchLatLongFromService(String place) {
            super();
            this.place = place;

        }

        @Override
        protected void onCancelled() {
            // TODO Auto-generated method stub
            super.onCancelled();
            this.cancel(true);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(MapsActivity.this);
            pd.setTitle("Please Wait");
            pd.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub
            try {
                HttpURLConnection conn = null;
                String msg;
                String googleMapUrl = "https://maps.googleapis.com/maps/api/geocode/json?address="
                        + this.place + "&sensor=false&key=AIzaSyCjvYgsqwRJCaySPonM8xAmdKohDwUYy5M";

                URL url = new URL(googleMapUrl);
                conn = (HttpURLConnection) url.openConnection();
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuffer responce = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    responce.append(inputLine);
                }
                in.close();
                msg = responce.toString();
                Log.e("Responce",msg);
                return msg;
            } catch (Exception e) {
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

                Toast.makeText(MapsActivity.this,"Lat:"+lat+ " Long :"+lng,Toast.LENGTH_LONG ).show();
                 point = new LatLng(lat, lng);


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


    public double distance(double lat1,double lon1,double lat2,double lon2) {
        double p = 0.017453292519943295;    // Math.PI / 180
        //  double c = Math.cos();
        double a = 0.5 - Math.cos((lat2 - lat1) * p)/2 +
                Math.cos(lat1 * p) * Math.cos(lat2 * p) *
                        (1 - Math.cos((lon2 - lon1) * p))/2;

        return 12742 * Math.asin(Math.sqrt(a)); // 2 * R; R = 6371 km
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
            Polyline line = mMap.addPolyline(new PolylineOptions()
                    .addAll(list)
                    .width(12)
                    .color(Color.parseColor("#05b1fb"))//Google maps blue color
                    .geodesic(true)
            );

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
            progressDialog = new ProgressDialog(MapsActivity.this);
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
            if(result!=null){
                drawPath(result,steps);
            }
        }
    }





}

