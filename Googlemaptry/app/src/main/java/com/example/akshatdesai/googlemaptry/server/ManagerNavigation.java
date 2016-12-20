package com.example.akshatdesai.googlemaptry.server;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.akshatdesai.googlemaptry.Fragment.AddEmployee;
import com.example.akshatdesai.googlemaptry.Fragment.AssignTask;
//import com.example.akshatdesai.googlemaptry.Fragment.EmployeeList;
import com.example.akshatdesai.googlemaptry.Fragment.ViewTask;
import com.example.akshatdesai.googlemaptry.General.EmployeeList;
import com.example.akshatdesai.googlemaptry.General.Sessionmanager;
import com.example.akshatdesai.googlemaptry.R;

public class ManagerNavigation extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Fragment fragment = null;
    String title;
    DrawerLayout drawer;
    public static Toolbar toolbar;
    android.support.v7.app.ActionBarDrawerToggle toggle;
    Sessionmanager sessionmanager;
   /* ProgressBar pb;
    Activity context;
    private RecyclerView taskView;
    Sessionmanager sessionManager;*/
    int UId, mid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_manager_navigation);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

      /*  pb = (ProgressBar) findViewById(R.id.progressBar_managerviewtask);
        taskView = (RecyclerView) findViewById(R.id.rv_manager);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(ManagerNavigation.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        taskView.setLayoutManager(layoutManager);*/
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

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
       // new Web().execute();
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
           finishAffinity();
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
            sessionmanager.LogOut1(ManagerNavigation.this);
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


   /* public class Web extends AsyncTask {

        private String msg, name[], sdate[], edate[], assignedto[], assignedby[], desc[];

        //int status, id[], length1;
        int status, id[], length1, cstatus[];
        JSONArray array;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pb.setVisibility(View.VISIBLE);
        }

        @Override
        protected Object doInBackground(Object[] params) {
            try {
                msg = "";

                Log.w("UID", "" + UId);
                Log.w("MID", "" + mid);

                UId = 4;
                mid = 1;
                String param = "id=" + URLEncoder.encode(UId + "", "UTF-8")+ "&" + "type="+ URLEncoder.encode(mid + "", "UTF-8");
                //String param = "id=" + URLEncoder.encode(String.valueOf(UId), "UTF-8") + "&" + "type=" + URLEncoder.encode(String.valueOf(mid), "UTF-8");
                HttpURLConnection httpURLConnection = null;
                URL url = new URL("http://" + WebServiceConstant.ip + "/Tracking/viewtask.php?" + param);

                httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestProperty("Accept", "application/json");
                httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

//                httpURLConnection.setInstanceFollowRedirects(true);
                int i = httpURLConnection.getResponseCode();
                Log.w("RCode", "" + i);
                if (i == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    String responce;

                    in.close();
                    responce = "responce" + response.toString();
                    String[] res = responce.split("\\[");
                    responce = "[" + res[1];


                    msg = responce.toString();
                    Log.e("responce", "" + msg);

                    array = new JSONArray(msg);
                    JSONObject temp = array.getJSONObject(0);
                    status = temp.getInt("status");
                    length1 = array.length();
                    id = new int[length1];
                    cstatus = new int[length1];
                    name = new String[length1];
                    desc = new String[length1];
                    sdate = new String[length1];
                    edate = new String[length1];
                    assignedby = new String[length1];
                    assignedto = new String[length1];


                    if (status == 1)

                    {
                        for (i = 0; i < length1; i++) {
                            JSONObject temp1 = array.getJSONObject(i);
                            id[i] = temp1.getInt("id");
                            name[i] = temp1.getString("name");
                            desc[i] = temp1.getString("desc");
                            sdate[i] = temp1.getString("sdate");
                            edate[i] = temp1.getString("edate");
                            assignedto[i] = temp1.getString("assignedto");
                            assignedby[i] = temp1.getString("assignedby");
                            cstatus[i] = temp1.getInt("cstatus");
                        }
                        Log.w("id", "" + id[1]);
                    } else {
                        Toast.makeText(ManagerNavigation.this, "No Data Found", Toast.LENGTH_LONG).show();
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(Object o) {

            try {
                pb.setVisibility(View.GONE);
                ViewTaskAdapter adapter = new ViewTaskAdapter(ManagerNavigation.this, id, name, desc, sdate, edate, assignedto, cstatus);
                taskView.setAdapter(adapter);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }*/
}

