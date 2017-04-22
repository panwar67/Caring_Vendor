package com.lions.caring_vendor.sessions;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.lions.caring_vendor.options.Login;

import java.util.HashMap;

/**
 * Created by Panwar on 19/04/17.
 */
public class Cloud_Session {

    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;


    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "Cloud_Vendor";

    // All Shared Preferences Keys

    public static final String KEY_REG = "reg_id";
    // Constructor
    public Cloud_Session(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     * */
    public boolean createSession(String token){
        // Storing login value as TRUE

        editor.clear();
        editor.commit();
        editor.putString(KEY_REG, token);

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

        user.put(KEY_REG,pref.getString(KEY_REG,null));
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
