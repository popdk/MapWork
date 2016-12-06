package com.example.akshatdesai.googlemaptry.General;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by Admin on 21-Nov-16.
 */

public class Sessionmanager {


    public static final String KEY_NAME = "UserName";
    public static final String KEY_ID = "UserId";
    public static final String Email = "Email";
    public static final String KEY_PASS = "Password";
    public static final String Wrong_Attempt = "WrongLoginAttempt";
    public static final String KEY_mid = "m_id";

    public static final String PREF_NAME = "FAM";
    public static final String IS_LOGIN = "IsLoggedIn";

    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Context context;

    int PRIVATE_MODE =0;

    public Sessionmanager(Context context)
    {
        this.context = context;
        preferences = context.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        editor = preferences.edit();
    }

    public void CreateLoginSession(Integer userid,String username,String Password, String m_id)
    {
        editor.putBoolean(IS_LOGIN,true);
        editor.putString(KEY_ID,userid.toString());
        editor.putString(KEY_NAME,username);
        editor.putString(KEY_PASS,Password);
        editor.putString(KEY_mid,m_id);
        //editor.putString()
        // editor.putString(Wrong_Attempt,WrongAttempt);

        editor.commit();
    }

    public boolean checklogin()
    {

        if(this.isLoggedIn())
        {
            return true;
        }else{
            return  false;
        }

    }

    public HashMap<String,String> getuserdetails()
    {
        HashMap<String,String> user = new HashMap<>();
        user.put(KEY_ID,preferences.getString(KEY_ID,null));
        user.put(KEY_NAME,preferences.getString(KEY_NAME,null));
        user.put(KEY_PASS,preferences.getString(KEY_PASS,null));
        user.put(KEY_mid,preferences.getString(KEY_mid,null));
        return user;

    }

    public void LogOut1()
    {
        editor.clear();
        editor.commit();
        Intent i = new Intent(context, Login_new.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);

    }

    public void settoken(String token)
    {
        editor.putString("TOKEN",token);
        editor.commit();
    }

    public String gettoken()
    {
        return preferences.getString("TOKEN",null);
    }

    public boolean isLoggedIn() {
        return preferences.getBoolean(IS_LOGIN, false);
    }


}
