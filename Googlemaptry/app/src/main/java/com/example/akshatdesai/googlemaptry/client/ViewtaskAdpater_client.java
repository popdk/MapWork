package com.example.akshatdesai.googlemaptry.client;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.akshatdesai.googlemaptry.Admin.Assign_role;
import com.example.akshatdesai.googlemaptry.General.EnablePermission;
import com.example.akshatdesai.googlemaptry.R;
import com.example.akshatdesai.googlemaptry.WebServiceConstant;
import com.example.akshatdesai.googlemaptry.server.DefineRoute;

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
import java.net.URLEncoder;
import java.util.Date;

/**
 * Created by Admin on 28-Nov-16.
 */

public class ViewtaskAdpater_client extends RecyclerView.Adapter<ViewtaskAdpater_client.Viewholder1> {
    int id[],stat[];
    String title[], desc[], startdate[], enddate[], assignedby[],source[],destination[],stopage[];
    Context context;
    Intent callactivity;
    EnablePermission ep = new EnablePermission();
   // static  int flag = 0;

    public ViewtaskAdpater_client(int id[], String title[], String desc[], String startdate[], String enddate[], String assignedby[],String source[],String destination[], String stopage[] ,int stat[], Context context) {
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.startdate = startdate;
        this.enddate = enddate;
        this.assignedby = assignedby;
        this.source = source;
        this.destination = destination;
        this.stopage = stopage;
        this.stat = stat;
        this.context = context;

        Log.e("id length", "" +id.length +"  "  + this.id.length);
    }

    public static class Viewholder1 extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title, enddate, description, startdate, assignedby;
        Button ViewInMap;
        Button progress;
        LinearLayout display,linearLayout;


        public Viewholder1(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.title);
            enddate = (TextView) v.findViewById(R.id.projectedendtime);
            description = (TextView) v.findViewById(R.id.description);
            startdate = (TextView) v.findViewById(R.id.projectedstarttime);
            assignedby = (TextView) v.findViewById(R.id.assignedto);
            ViewInMap = (Button) v.findViewById(R.id.viewinmap);
            display = (LinearLayout) v.findViewById(R.id.activity_view_task_details);
            progress = (Button) v.findViewById(R.id.toggleButton);
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
        String stop = stopage[position];
         callactivity = new Intent(context, DefineRoute.class);

        if(!stop.equals(",,"))
        {

            String s[]= stop.split(",");
            for(int i =0;i<s.length;i++)
            {
                callactivity.putExtra("stopage"+i,s[i]);
            }

        }


        holder.ViewInMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                callactivity.putExtra("Source",source[position]);
                callactivity.putExtra("destination",destination[position]);
                context.startActivity(callactivity);
            }
        });


        if(stat[position] == 0)
        {
            holder.progress.setText("Running");
        }
        else if(stat[position] == 1)
        {
            holder.progress.setText("Completed");
        }
        else {
            holder.progress.setText("Scheduled");
        }

        holder.progress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             // String status = (String) holder.progress.getText();

                AlertDialog.Builder ab = new AlertDialog.Builder(context);
                ab.setTitle("Are you sure");
                ab.setMessage("Task Complete");
                ab.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        holder.progress.setText("Completed");
                        if(ep.isInternetConnected(context)) {
                            new TaskCompleated(context, id[position]).execute();
                        }else{
                            Toast.makeText(context,"No internrt connection",Toast.LENGTH_LONG);
                        }
                        holder.progress.setEnabled(false);

                        Intent intent = new Intent(context,LocationUpdateService.class);
                        context.stopService(intent);
                    }

                });
                        ab.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                               // holder.progress.setText("Running");
                            }
                        });

             AlertDialog dialog =   ab.create();
                dialog.show();
/*
                if(status.equalsIgnoreCase("completed"))
                {
                    new TaskCompleated(context,id[position]).execute();

                        holder.progress.setEnabled(false);

                    Intent intent = new Intent(context,LocationUpdateService.class);
                    context.stopService(intent);

                }*/
            }
        });

    }

    @Override
    public int getItemCount() {
        return id.length;
    }


    static class TaskCompleated extends AsyncTask {
        int taskid , status;
        Context context;
        String msg;
        JSONArray array;
        ProgressDialog pd;
        TaskCompleated(Context context,  int taskid)
        {
            this.taskid = taskid;
            this.context = context;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(context);
            pd.setMessage("Please wait..");
            pd.show();
            pd.setCancelable(false);

        }

        @Override
        protected Object doInBackground(Object[] objects) {


            try {

                String params = "id=" + URLEncoder.encode(String.valueOf(taskid), "UTF-8");
                URL url = new URL("http://"+ WebServiceConstant.ip+"/Tracking/TaskCompleted.php?" + params);

                Log.e("URL", "" + url);
                Object obj = null;

                URLConnection con = url.openConnection();
                HttpURLConnection httpURLConnection = (HttpURLConnection) con;
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestProperty("Accept", "application/json");
                httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                int rescode = httpURLConnection.getResponseCode();

                Log.e("responce", "" + rescode);
                if (rescode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    String inputLine;
                    StringBuffer responce = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        responce.append(inputLine);
                    }
                    Log.e("responce", "" + responce);
                    in.close();
                    msg = responce.toString();

                    array = new JSONArray(msg);
                    JSONObject temp = array.getJSONObject(0);
                    status = temp.getInt("status");

                    if(status == 1)
                    {
                        Log.e("SETALARM","Taskid"+ taskid + "COMPLETED");

                    }

                    Log.e("responce", "" + msg);
                }


            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
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
            super.onPostExecute(o);
            pd.cancel();

        }
    }

}
