package com.lions.caring_vendor.sessions;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.lions.caring_vendor.options.Login;

import java.util.HashMap;

/**
 * Created by Panwar on 17/04/17.
 */
public class Session_Manager {

    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;


    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "Vendor";

    // All Shared Preferences Keys
    // Email address (make variable public to access from outside)
    public static final String KEY_NAME = "name";
    public  static final String KEY_UID = "uid";
    public static final String KEY_DP = "dp";
    public static final String KEY_MOB = "mobile";
    public static final String KEY_LAT = "lat";
    public static final String KEY_LONG = "long";
    // Constructor
    public Session_Manager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     * */
    public boolean createLoginSession( String name, String uid, String dp, String mob, String lat, String longitude){
        // Storing login value as TRUE

        editor.clear();
        editor.commit();


        // Storing email in pref
        editor.putString(KEY_NAME, name);

        editor.putString(KEY_UID,uid);

        editor.putString(KEY_DP, dp);
        editor.putString(KEY_MOB,mob);
        editor.putString(KEY_LAT,lat);
        editor.putString(KEY_LONG,longitude);

        // commit changes
        editor.commit();
        return  true;
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */

    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        // user name

        // user email id
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));

        user.put(KEY_UID, pref.getString(KEY_UID,null));

        user.put(KEY_DP,pref.getString(KEY_DP,null));
        user.put(KEY_MOB,pref.getString(KEY_MOB,null));
        user.put(KEY_LAT,pref.getString(KEY_LAT,null));

        user.put(KEY_LONG,pref.getString(KEY_LONG,null));


        // return user
        return user;

    }

    /**
     * Clear session details
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity


//        ActivityCompat.finishAffinity(null);
    }

    /**
     * Quick check for login
     * **/
    // Get Login State


}
