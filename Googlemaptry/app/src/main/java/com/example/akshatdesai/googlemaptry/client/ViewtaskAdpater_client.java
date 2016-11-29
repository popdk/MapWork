package com.example.akshatdesai.googlemaptry.client;

import android.content.Context;
import android.content.Intent;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.akshatdesai.googlemaptry.R;
import com.example.akshatdesai.googlemaptry.server.DefineRoute;

import java.util.Date;

/**
 * Created by Admin on 28-Nov-16.
 */

public class ViewtaskAdpater_client extends RecyclerView.Adapter<ViewtaskAdpater_client.Viewholder1> {
    int id[];
    String title[], desc[], startdate[], enddate[], assignedby[];
    Context context;

    public ViewtaskAdpater_client(int id[], String title[], String desc[], String startdate[], String enddate[], String assignedby[], Context context) {
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.startdate = startdate;
        this.enddate = enddate;
        this.assignedby = assignedby;
        this.context = context;

        Log.e("id length", "" +id.length +"  "  + this.id.length);
    }

    public static class Viewholder1 extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title, enddate, description, startdate, assignedby;
        Button ViewInMap;
        ToggleButton progress;
        LinearLayout display;


        public Viewholder1(View v) {
            super(v);

            title = (TextView) v.findViewById(R.id.title);
            enddate = (TextView) v.findViewById(R.id.projectedendtime);
            description = (TextView) v.findViewById(R.id.description);
            startdate = (TextView) v.findViewById(R.id.projectedstarttime);
            assignedby = (TextView) v.findViewById(R.id.assignedto);
            ViewInMap = (Button) v.findViewById(R.id.viewinmap);
            display = (LinearLayout) v.findViewById(R.id.activity_view_task_details);
            progress = (ToggleButton) v.findViewById(R.id.toggleButton);

           /* DateFormat d = new SimpleDateFormat("dd/mm/yyyy");
            Date date = new Date();
            d.format(date);
            Log.e("date", ""+d.format(date));
            if(startdate.equals(d.format(date))){
                progress.setVisibility(View.VISIBLE);
            }else{
                progress.setVisibility(View.GONE);
            }
           */
        }

        @Override
        public void onClick(View view) {

        }
    }


    @Override
    public Viewholder1 onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_viewtask_client_details, parent, false);
        Viewholder1 vh = new Viewholder1(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final Viewholder1 holder,final int position) {
        Log.e("position" ,""+position);
        Log.e("position" ,""+title[position]+"  "+title.length);
        holder.title.setText(title[position]);
        holder.enddate.setText(enddate[position]);
        holder.description.setText(desc[position]);
        holder.startdate.setText(startdate[position]);
        holder.assignedby.setText(assignedby[position]);

        holder.ViewInMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent callactivity = new Intent(context, DefineRoute.class);
                callactivity.putExtra("Source","Ahmedabad");
                callactivity.putExtra("destination","Surat");
                context.startActivity(callactivity);
            }
        });

    }

    @Override
    public int getItemCount() {
        return id.length;
    }

}
