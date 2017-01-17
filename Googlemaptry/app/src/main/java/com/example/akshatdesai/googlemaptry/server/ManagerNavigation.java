package com.example.akshatdesai.googlemaptry.server;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.akshatdesai.googlemaptry.Fragment.AddEmployee;
import com.example.akshatdesai.googlemaptry.Fragment.AssignTask;
//import com.example.akshatdesai.googlemaptry.Fragment.EmployeeList;
import com.example.akshatdesai.googlemaptry.Fragment.ViewTask;
import com.example.akshatdesai.googlemaptry.General.EmployeeList;
import com.example.akshatdesai.googlemaptry.General.Registration;
import com.example.akshatdesai.googlemaptry.General.Sessionmanager;
import com.example.akshatdesai.googlemaptry.R;
import com.example.akshatdesai.googlemaptry.WebServiceConstant;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.akshatdesai.googlemaptry.Notification.MyInstanceIDListenerService.refreshedToken;
import static com.example.akshatdesai.googlemaptry.R.id.drawer_layout;
import static com.example.akshatdesai.googlemaptry.R.id.imageView;

public class ManagerNavigation extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Fragment fragment = null;
    String title,imagename;
    DrawerLayout drawer;
    public static Toolbar toolbar;
    android.support.v7.app.ActionBarDrawerToggle toggle;
    Sessionmanager sessionmanager;
    CircleImageView iv1;
    File imageFile = null;
    String imagepath,image,out,UId;
    int  mid;
    Sessionmanager sm;
    private Uri mCropImageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_manager_navigation);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView =  navigationView.getHeaderView(0);
        iv1 = (CircleImageView) hView.findViewById(R.id.profile_image);


       // iv1= (ImageView) findViewById(R.id.profile_image);

        sm = new Sessionmanager(this);
        HashMap hm =  sm.getuserdetails();
        imagename = (String) hm.get("imagename");
        UId = (String) hm.get("UserId");

/*
        ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.displayImage("http://" + WebServiceConstant.ip + "/Tracking/images/8566.png",iv1);

*/

        new downloadimage().execute();

        iv1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (ContextCompat.checkSelfPermission(ManagerNavigation.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(ManagerNavigation.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ManagerNavigation.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 5);
                } else {
                    onSelectImageClick(iv1);
                }

                return false;
            }
        });




        fragment=new ViewTask();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_body, fragment);
        fragmentTransaction.commit();
        sessionmanager=new Sessionmanager(getApplicationContext());

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
       /* toggle = new android.support.v4.app.ActionBarDrawerToggle(this,drawer,R.drawable.ic_drawer,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();*/
        toggle = setupDrawerToggle();
        drawer.setDrawerListener(toggle);


        /*getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_drawer);*/

        //NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
       // new Web().execute();
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    }








    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 5: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, 5000);
                    }
                } else {
                    Toast.makeText(ManagerNavigation.this, "Need Camera permission", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }

        if (mCropImageUri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // required permissions granted, start crop image activity
            startCropImageActivity(mCropImageUri);
        } else {
            Toast.makeText(this, "Cancelling, required permissions are not granted", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(this, data);
            // For API >= 23 we need to check specifically that we have permissions to read external storage.
            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)) {
                // request permissions and handle the result in onRequestPermissionsResult()
                mCropImageUri = imageUri;
                ActivityCompat.requestPermissions(ManagerNavigation.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            } else {
                // no permissions required or already grunted, can start crop image activity
                startCropImageActivity(imageUri);
            }
        }

        // handle result of CropImageActivity
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                try {
                    imageFile = new File(result.getUri().getPath());
                } catch (NullPointerException e) {
                    Toast.makeText(this, "Image Path Error..", Toast.LENGTH_SHORT).show();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }
        if (imageFile != null) {
            Toast.makeText(getApplicationContext(), "Please wait.. while uploading image.", Toast.LENGTH_SHORT).show();
            imagepath = imageFile.getPath();
            iv1.setImageURI(Uri.fromFile(imageFile));
            Bitmap bitmap= BitmapFactory.decodeFile(imagepath);
            image = getStringImage(bitmap);
            new changeImage().execute();
            //new().execute();
        }
    }

    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(this);
    }

    public void onSelectImageClick(View view) {
        CropImage.startPickImageActivity(this);
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }



    class changeImage extends AsyncTask {

        ProgressDialog pd;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(ManagerNavigation.this);
            pd.setMessage("Please wait..");
            pd.setCancelable(false);
            pd.show();

        }

        @Override
        protected Object doInBackground(Object[] objects) {

            try {
                URL url = new URL("http://" + WebServiceConstant.ip + "/Tracking/changeprofilpic.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                HashMap<String, String> params = new HashMap<>();
               // params.put("id", UId);
                params.put("imgname", imagename);
                params.put("img", image);
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(params));
                writer.flush();
                writer.close();
                os.close();

                // conn.connect();

                int responseCode = conn.getResponseCode();
                Log.e("responceCode:", "" + responseCode);
                if (responseCode == HttpURLConnection.HTTP_OK) {

                } else {
                    Toast.makeText(ManagerNavigation.this, "Please Try Again", Toast.LENGTH_SHORT).show();

                }
              //  Log.e("response", "" + out);

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException{
            StringBuilder result = new StringBuilder();
            boolean first = true;
            for(Map.Entry<String, String> entry : params.entrySet()){
                if (first)
                    first = false;
                else
                    result.append("&");

                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            }

            return result.toString();
        }


        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            pd.cancel();

        }
    }














    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            final AlertDialog.Builder builderDialog = new AlertDialog.Builder(ManagerNavigation.this);
            builderDialog.setCancelable(false);
            builderDialog.setTitle("Alert !!");
            builderDialog.setMessage("Are you sure You want To Exit");
            builderDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finishAffinity();
                }
            });
            builderDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });

            AlertDialog alert = builderDialog.create();
            alert.show();

        }


    }


    class downloadimage extends AsyncTask {
        Bitmap bmImg;
        URL url = null;
        ProgressDialog pd;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(ManagerNavigation.this);
            pd.setMessage("Loading..");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Object  doInBackground(Object[] params) {

            try {
                url = new URL("http://" + WebServiceConstant.ip + "/Tracking/images/"+imagename);

               // URI uri = new URI("http://" + WebServiceConstant.ip + "/Tracking/images/"+imagename");

                Log.e("DownLoad Image Url",""+url);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true);
               // conn.setDoOutput(true);

                   conn.connect();
                int code = conn.getResponseCode();
                Log.e("Responce Code",""+code);

                if(code == HttpURLConnection.HTTP_OK) {
                    InputStream is = conn.getInputStream();
                    //InputStream is = new java.net.URL("http://" + WebServiceConstant.ip + "/Tracking/images/"+imagename).openStream();
                    bmImg = BitmapFactory.decodeStream(is);
                    Log.e("BitMap", "" + bmImg);
                }
                //iv.setImageBitmap(bmImg);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Bitmap bmImg;
            return null;
        }
        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            pd.cancel();
            if(bmImg != null) {
                iv1.setImageBitmap(bmImg);
            }
        }
    }

















    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        toggle.syncState();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        toggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.manager_navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
         if (id == R.id.action_logout) {
            Log.e("Button","Clicked");
            sessionmanager.LogOut1(ManagerNavigation.this,true);
           /*Intent in = new Intent(getApplicationContext(), Login_new.class);
            startActivity(in);*/
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.assign_task) {
            fragment = new AssignTask();
            title = "Assign Task to Employee";
            // Log.e("assign","assign");
            // Handle the camera action
        } else if (id == R.id.Add_Employee) {
            fragment = new AddEmployee();
            title = "Select Employee";
        } else if (id == R.id.view_task) {
            fragment = new ViewTask();
            title = "View Task";
        } else if (id == R.id.send_message) {
            fragment = new EmployeeList();
            title = "Send Message";
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();
            getSupportActionBar().setTitle(title);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }




}

