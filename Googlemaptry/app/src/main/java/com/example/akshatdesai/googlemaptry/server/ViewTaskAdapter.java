package com.example.akshatdesai.googlemaptry.server;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.akshatdesai.googlemaptry.R;

public class ViewTaskAdapter extends RecyclerView.Adapter<ViewTaskAdapter.Viewholder1> {

    int id[],cstatus[],flag[];
    String title[],desc[],startdate[],enddate[],assignedto[];
    Context context;



    public ViewTaskAdapter(Context c, int id[], String title[], String desc[], String startdate[], String enddate[], String assignedto[], int cstatus[]) {
        this.id=id;
        this.title = title;
        this.desc =desc;
        this.startdate=startdate;
        this.enddate=enddate;
        this.assignedto = assignedto;
        this.cstatus = cstatus;
        context = c;
        flag = new int[id.length];
        flag = new int[id.length];
        Log.w("id length","" +this.id.length);
    }

    public static class Viewholder1 extends RecyclerView.ViewHolder
    {
        TextView title,enddate,description,startdate,assignedto,status;
        Button ViewInMap;
        LinearLayout linearLayout,display;
        public Viewholder1(View v) {
            super(v);

            title = (TextView) v.findViewById(R.id.title);
            enddate = (TextView) v.findViewById(R.id.projectedendtime);
            description = (TextView) v.findViewById(R.id.description);
            startdate = (TextView) v.findViewById(R.id.projectedstarttime);
            assignedto = (TextView) v.findViewById(R.id.assignedto);
            ViewInMap = (Button) v.findViewById(R.id.viewinmap);
            status = (TextView) v.findViewById(R.id.status);
            linearLayout = (LinearLayout) v.findViewById(R.id.activity_view_task_details);
            display = (LinearLayout) v.findViewById(R.id.show_hide);
            display.setVisibility(View.GONE);

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(display.getVisibility() == view.VISIBLE)
                    {
                        display.setVisibility(View.GONE);
                    }
                    else
                    {
                        display.setVisibility(View.VISIBLE);
                    }

                }
            });

           /* description.setVisibility(View.GONE);
            startdate.setVisibility(View.GONE);
            enddate.setVisibility(View.GONE);
            ViewInMap.setVisibility(View.GONE);*/

        }


    }




    @Override
    public Viewholder1 onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_view_task_details,parent,false);
        Viewholder1 vh = new Viewholder1(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(Viewholder1 holder, final int position) {

        holder.title.setText(title[position]);
        holder.enddate.setText(enddate[position]);
        holder.description.setText(desc[position]);
        holder.startdate.setText(startdate[position]);
        holder.assignedto.setText(assignedto[position]);

        if(cstatus[position] == 2)
        {
            holder.status.setText("Not Started");
            holder.ViewInMap.setVisibility(View.GONE);
            flag[position] = 0;
        }
        else if(cstatus[position] == 1)
        {
            holder.status.setText("Completed");
            holder.ViewInMap.setVisibility(View.VISIBLE);
            flag[position] =0;
        }
        else if(cstatus[position] == 0)
        {
            holder.status.setText("Running");
            holder.ViewInMap.setVisibility(View.VISIBLE);
            flag[position] =1;
        }

        holder.ViewInMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = id[position];
                Intent intent = new Intent(context,Tracking.class);
                intent.putExtra("taskid",i);
                intent.putExtra("dotrack",flag[position]);
                context.startActivity(intent);
            }
        });
    }





    @Override
    public int getItemCount() {
        return id.length;
    }
}
