package com.example.akshatdesai.googlemaptry.server;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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
import android.widget.Toast;

import com.example.akshatdesai.googlemaptry.General.EnablePermission;
import com.example.akshatdesai.googlemaptry.General.Login_new;
import com.example.akshatdesai.googlemaptry.R;
import com.example.akshatdesai.googlemaptry.WebServiceConstant;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

public class ViewTaskAdapter extends RecyclerView.Adapter<ViewTaskAdapter.Viewholder1> {

    int id[], cstatus[], flag[], task_type;
    String title[], desc[], startdate[], enddate[], assignedto[];
    static Context context;


    public ViewTaskAdapter(Context c, int id[], String title[], String desc[], String startdate[], String enddate[], String assignedto[], int cstatus[], int task_type) {
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.startdate = startdate;
        this.enddate = enddate;
        this.assignedto = assignedto;
        this.cstatus = cstatus;
        this.task_type = task_type;
        context = c;
        flag = new int[id.length];
        flag = new int[id.length];
        Log.w("id length", "" + this.id.length);
    }


    public void refresh() {
        notifyDataSetChanged();
    }


    public static class Viewholder1 extends RecyclerView.ViewHolder {
        TextView title, enddate, description, startdate, assignedto, status;
        Button ViewInMap, DeleteTask, cancleDeleteTask;
        LinearLayout linearLayout, display, display1, ll_delete_task;

        public Viewholder1(View v) {
            super(v);

            title = (TextView) v.findViewById(R.id.title);
            enddate = (TextView) v.findViewById(R.id.projectedendtime);
            description = (TextView) v.findViewById(R.id.description);
            startdate = (TextView) v.findViewById(R.id.projectedstarttime);
            assignedto = (TextView) v.findViewById(R.id.assignedto);
            ViewInMap = (Button) v.findViewById(R.id.viewinmap);
            status = (TextView) v.findViewById(R.id.status);
            display = (LinearLayout) v.findViewById(R.id.activity_view_task_details);
            display1 = (LinearLayout) v.findViewById(R.id.show_hide);
            DeleteTask = (Button) v.findViewById(R.id.delete_task);
            linearLayout = (LinearLayout) v.findViewById(R.id.wholelayout);
            ll_delete_task = (LinearLayout) v.findViewById(R.id.Ll_deleteTask);
            cancleDeleteTask = (Button) v.findViewById(R.id.cancle_deletetask);
            display1.setVisibility(View.GONE);

            display.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (display1.getVisibility() == view.VISIBLE) {
                        display1.setVisibility(View.GONE);
                    } else {
                        display1.setVisibility(View.VISIBLE);
                    }

                }
            });
            display.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    ll_delete_task.setVisibility(View.VISIBLE);
                    return false;
                }
            });


            cancleDeleteTask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ll_delete_task.setVisibility(View.GONE);
                }
            });


        }


    }


    @Override
    public Viewholder1 onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_view_task_details, parent, false);
        Viewholder1 vh = new Viewholder1(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final Viewholder1 holder, final int position) {

        holder.title.setText(title[position]);
        holder.enddate.setText(enddate[position]);
        holder.description.setText(desc[position]);
        holder.startdate.setText(startdate[position]);
        holder.assignedto.setText(assignedto[position]);

        holder.DeleteTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (EnablePermission.isInternetConnected(context)) {
                    new DeleteTask(id[position]).execute();
                    holder.display.setVisibility(View.GONE);
                    notifyItemRemoved(position);
                } else {
                    Toast.makeText(context, "Please Connect to internet", Toast.LENGTH_SHORT).show();
                }


            }
        });

        if (task_type == 0) {

            if (cstatus[position] == 2) {
                holder.status.setText("Not Started");
                holder.ViewInMap.setVisibility(View.GONE);
                flag[position] = 0;
                holder.title.setVisibility(View.GONE);
                holder.enddate.setVisibility(View.GONE);
                holder.description.setVisibility(View.GONE);
                holder.startdate.setVisibility(View.GONE);
                holder.assignedto.setVisibility(View.GONE);
                holder.status.setVisibility(View.GONE);
                holder.display.setVisibility(View.GONE);
                holder.display1.setVisibility(View.GONE);
                holder.DeleteTask.setVisibility(View.GONE);
                holder.ll_delete_task.setVisibility(View.GONE);
                holder.cancleDeleteTask.setVisibility(View.GONE);
                holder.linearLayout.setVisibility(View.GONE);
            } else if (cstatus[position] == 1) {
                holder.status.setText("Completed");
             //   holder.ViewInMap.setVisibility(View.VISIBLE);
                flag[position] = 0;
                holder.title.setVisibility(View.GONE);
                holder.enddate.setVisibility(View.GONE);
                holder.description.setVisibility(View.GONE);
                holder.startdate.setVisibility(View.GONE);
                holder.assignedto.setVisibility(View.GONE);
                holder.status.setVisibility(View.GONE);
                holder.display.setVisibility(View.GONE);
                holder.display.setVisibility(View.GONE);
                holder.display1.setVisibility(View.GONE);
                holder.DeleteTask.setVisibility(View.GONE);
                holder.ll_delete_task.setVisibility(View.GONE);
                holder.cancleDeleteTask.setVisibility(View.GONE);
                holder.linearLayout.setVisibility(View.GONE);
            } else if (cstatus[position] == 0) {
                holder.status.setText("Running");
                holder.ViewInMap.setVisibility(View.VISIBLE);
                flag[position] = 1;
            }
        } else if (task_type == 1) {
            if (cstatus[position] == 2) {
                holder.status.setText("Not Started");
                holder.ViewInMap.setVisibility(View.GONE);
                flag[position] = 0;
                holder.title.setVisibility(View.GONE);
                holder.enddate.setVisibility(View.GONE);
                holder.description.setVisibility(View.GONE);
                holder.startdate.setVisibility(View.GONE);
                holder.assignedto.setVisibility(View.GONE);
                holder.status.setVisibility(View.GONE);
                holder.display.setVisibility(View.GONE);
                holder.display.setVisibility(View.GONE);
                holder.display1.setVisibility(View.GONE);
                holder.DeleteTask.setVisibility(View.GONE);
                holder.ll_delete_task.setVisibility(View.GONE);
                holder.cancleDeleteTask.setVisibility(View.GONE);
                holder.linearLayout.setVisibility(View.GONE);
            } else if (cstatus[position] == 1) {
                holder.status.setText("Completed");
                holder.ViewInMap.setVisibility(View.VISIBLE);
                flag[position] = 0;
            } else if (cstatus[position] == 0) {
                holder.status.setText("Running");
               // holder.ViewInMap.setVisibility(View.VISIBLE);
                flag[position] = 1;
                holder.title.setVisibility(View.GONE);
                holder.enddate.setVisibility(View.GONE);
                holder.description.setVisibility(View.GONE);
                holder.startdate.setVisibility(View.GONE);
                holder.assignedto.setVisibility(View.GONE);
                holder.status.setVisibility(View.GONE);
                holder.display.setVisibility(View.GONE);
                holder.display.setVisibility(View.GONE);
                holder.display1.setVisibility(View.GONE);
                holder.DeleteTask.setVisibility(View.GONE);
                holder.ll_delete_task.setVisibility(View.GONE);
                holder.cancleDeleteTask.setVisibility(View.GONE);
                holder.linearLayout.setVisibility(View.GONE);
            }

        } else {
            if (cstatus[position] == 2) {
                holder.status.setText("Not Started");
                holder.ViewInMap.setVisibility(View.GONE);
                flag[position] = 0;
            } else if (cstatus[position] == 1) {
                holder.status.setText("Completed");
              //  holder.ViewInMap.setVisibility(View.VISIBLE);
                flag[position] = 0;
                holder.title.setVisibility(View.GONE);
                holder.enddate.setVisibility(View.GONE);
                holder.description.setVisibility(View.GONE);
                holder.startdate.setVisibility(View.GONE);
                holder.assignedto.setVisibility(View.GONE);
                holder.status.setVisibility(View.GONE);
                holder.display.setVisibility(View.GONE);
                holder.display.setVisibility(View.GONE);
                holder.display1.setVisibility(View.GONE);
                holder.DeleteTask.setVisibility(View.GONE);
                holder.ll_delete_task.setVisibility(View.GONE);
                holder.cancleDeleteTask.setVisibility(View.GONE);
                holder.linearLayout.setVisibility(View.GONE);
            } else if (cstatus[position] == 0) {
                holder.status.setText("Running");
               // holder.ViewInMap.setVisibility(View.VISIBLE);
                flag[position] = 1;
                holder.title.setVisibility(View.GONE);
                holder.enddate.setVisibility(View.GONE);
                holder.description.setVisibility(View.GONE);
                holder.startdate.setVisibility(View.GONE);
                holder.assignedto.setVisibility(View.GONE);
                holder.status.setVisibility(View.GONE);
                holder.display.setVisibility(View.GONE);
                holder.display1.setVisibility(View.GONE);
                holder.DeleteTask.setVisibility(View.GONE);
                holder.ll_delete_task.setVisibility(View.GONE);
                holder.cancleDeleteTask.setVisibility(View.GONE);
                holder.linearLayout.setVisibility(View.GONE);

            }

        }

        holder.ViewInMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = id[position];
                Intent intent = new Intent(context, Tracking.class);
                intent.putExtra("taskid", i);
                intent.putExtra("dotrack", flag[position]);
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return id.length;
    }


    private class DeleteTask extends AsyncTask {
        int id, result;
        ProgressDialog pd;

        public DeleteTask(int id) {
            this.id = id;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(context);
            pd.setMessage("Please wait");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Object doInBackground(Object[] objects) {

            String param = null;
            try {
                param = "id=" + URLEncoder.encode(String.valueOf(id), "UTF-8");

                HttpURLConnection httpURLConnection = null;
                URL url = new URL("http://" + WebServiceConstant.ip + "/Tracking/deletetask.php?" + param);

                httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestProperty("Accept", "application/json");
                httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                Log.e("DELETE TASK URL", "" + url);
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
                    result = Integer.parseInt(response.toString());

                }
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


        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            pd.cancel();
            if (result == 1) {
                Toast.makeText(context, "Task Deleted Successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Problem in task Delete", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
