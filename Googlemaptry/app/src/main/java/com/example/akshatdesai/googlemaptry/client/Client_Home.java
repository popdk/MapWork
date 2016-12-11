package com.example.akshatdesai.googlemaptry.client;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.akshatdesai.googlemaptry.General.*;
import com.example.akshatdesai.googlemaptry.General.EmployeeList;
import com.example.akshatdesai.googlemaptry.R;

import java.util.ArrayList;
import java.util.List;

public class Client_Home extends AppCompatActivity {
    Toolbar toolbar;
    ViewPager pager;
    TabLayout slidingTabLayout;
    Sessionmanager sessionmanager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client__home);
         toolbar= (Toolbar) findViewById(R.id.toolbar);
        //toolbar.setTitle("Slidin");
        setSupportActionBar(toolbar);
        EnablePermission.checklocationservice(Client_Home.this);
        showPermissionDialog(this);

        pager = (ViewPager) findViewById(R.id.pager);
        setupViewPager(pager);
        slidingTabLayout = (TabLayout) findViewById(R.id.tabs);
        slidingTabLayout.setupWithViewPager(pager);
        sessionmanager = new Sessionmanager(getApplicationContext());

    }

    private void showPermissionDialog(Context context) {
        if (!checkPermission(context)) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION},
                    2);
        }
    }
    public static boolean checkPermission(final Context context) {
        return ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        // Fragment fm = new Fragment();
        adapter.addFragment(new Viewtask_client_fragment(), "View Task");
        adapter.addFragment(new EmployeeList(), "Chat");
        // adapter.addFragment(new ThreeFragment(), "THREE");
        viewPager.setAdapter(adapter);
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if(id == R.id.action_logout){
            sessionmanager.LogOut1();
           /* Intent in = new Intent(getApplicationContext(), Login_new.class);
            startActivity(in);*/
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!EnablePermission.isInternetConnected(Client_Home.this))
        {
            Toast.makeText(Client_Home.this,"Please Connect to internet",Toast.LENGTH_LONG).show();
        }
    }
}


class ViewPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }
}

