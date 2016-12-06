package com.example.akshatdesai.googlemaptry.client;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.akshatdesai.googlemaptry.R;


/**
 * Created by Parita Shah on 02-12-2016.
 */

public class EmployeeListAdapter extends ArrayAdapter {

    Activity activity;
    String empName[];
    int empId[];

    public EmployeeListAdapter( Activity activity,String empName[]) {
        super(activity, R.layout.fragment_employee_list,empName);
        this.activity = activity;
        this.empName = empName;
        //this.empId = empId;

    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = activity.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.fragment_employee_list, null, true);
        TextView name = (TextView) view.findViewById(R.id.username);
        name.setText(empName[position]);
        return view;
    }
}

