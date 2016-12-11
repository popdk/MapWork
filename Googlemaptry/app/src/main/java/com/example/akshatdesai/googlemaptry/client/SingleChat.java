package com.example.akshatdesai.googlemaptry.client;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.akshatdesai.googlemaptry.General.Sessionmanager;
import com.example.akshatdesai.googlemaptry.R;

import java.util.HashMap;

/**
 * Created by Urvi on 12/02/2016.
 */

public class SingleChat extends ArrayAdapter {
    Context context;
    String[] messages,sendto,sendby,time;
    TextView l1,l2,r1,r2;
    Sessionmanager sessionmanager;
    int UId;
    public SingleChat(Context context, String[] messages, String[] sendto, String[] sendby, String[] time) {
        super(context, R.layout.chatting_recyclerview_layout,messages);

        this.context = context;
        this.messages=messages;
        this.sendto = sendto;
        this.sendby = sendby;
        this.time =time;
    }




    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View view = inflater.inflate(R.layout.chatting_recyclerview_layout,null,true);
        l1 = (TextView)view.findViewById(R.id.tv_l1);
        l2 = (TextView)view.findViewById(R.id.tv_l2);
        r1 = (TextView)view.findViewById(R.id.tv_r1);
        r2 = (TextView)view.findViewById(R.id.tv_r2);
        sessionmanager = new Sessionmanager(context);

        HashMap<String, String> user = sessionmanager.getuserdetails();
        // name
        if (sessionmanager.isLoggedIn()) {
            UId = Integer.parseInt(user.get(Sessionmanager.KEY_ID));
        }

        Long l = Long.valueOf(sendto[position]);
        Log.e("long",""+l);
//        int UId =3;
        if(l==UId) {
            r1.setText(messages[position]);
            r2.setText(time[position]);
            l1.setVisibility(View.GONE);
            l2.setVisibility(View.GONE);

            //}else{
        }else {

            l1.setText(messages[position]);
            l2.setText(time[position]);
            r1.setVisibility(View.GONE);
            r2.setVisibility(View.GONE);



        }
        return view;
    }


}
