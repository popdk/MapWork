package com.example.akshatdesai.googlemaptry.server;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.akshatdesai.googlemaptry.R;

public class ViewTaskAdapter extends RecyclerView.Adapter<ViewTaskAdapter.Viewholder1> {

    int id[];
    String title[],desc[],startdate[],enddate[],assignedby[];

    public ViewTaskAdapter(int id[],String title[],String desc[],String startdate[],String enddate[],String assignedby[]) {
        this.id=id;
        this.title = title;
        this.desc =desc;
        this.startdate=startdate;
        this.enddate=enddate;
        this.assignedby = assignedby;

        Log.w("id length","" +this.id.length);
    }

    public static class Viewholder1 extends RecyclerView.ViewHolder
    {
        TextView title,enddate,description,startdate,assignedby;
        public Viewholder1(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.title);
            enddate = (TextView) v.findViewById(R.id.enddate);
            description = (TextView) v.findViewById(R.id.description);
            startdate = (TextView) v.findViewById(R.id.startdate);
            assignedby = (TextView) v.findViewById(R.id.assignedby);
        }
    }




    @Override
    public Viewholder1 onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_view_task_details,parent,false);
        Viewholder1 vh = new Viewholder1(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(Viewholder1 holder, int position) {

        holder.title.setText(title[position]);
        holder.enddate.setText(enddate[position]);
        holder.description.setText(desc[position]);
        holder.startdate.setText(startdate[position]);
        holder.assignedby.setText(assignedby[position]);

    }





    @Override
    public int getItemCount() {
        return id.length;
    }
}
