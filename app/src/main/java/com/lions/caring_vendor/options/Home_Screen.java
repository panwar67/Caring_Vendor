package com.lions.caring_vendor.options;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.lions.caring_vendor.R;
import com.lions.caring_vendor.sessions.Cloud_Session;
import com.lions.caring_vendor.sessions.Session_Manager;

import java.util.HashMap;
import java.util.Map;

public class Home_Screen extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private BroadcastReceiver mRegistrationBroadcastReceiver;
    TextView vendor_name, contact, reg_key;
    Cloud_Session cloud_session;
    String DOWN_URL = "http://www.car-ing.com/app/InserPush.php";
    Session_Manager session_manager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home__screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FirebaseApp.initializeApp(getApplicationContext());
        //FirebaseInstanceId.getInstance().getToken();


        cloud_session = new Cloud_Session(getApplicationContext());
        vendor_name = (TextView)findViewById(R.id.vendor_name);
        contact = (TextView)findViewById(R.id.venor_contact);
        reg_key = (TextView)findViewById(R.id.vendor_reg_cloud);
        session_manager = new Session_Manager(getApplicationContext());
        vendor_name.setText(session_manager.getUserDetails().get("name"));
        contact.setText(session_manager.getUserDetails().get("mobile"));
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Bookings.Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    //FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
                    SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
                    String regId = pref.getString("regId", null);
                    reg_key.setText(""+regId+"");
                    sendRegistrationToServer(regId,session_manager.getUserDetails().get("uid"));

                } else if (intent.getAction().equals(Bookings.Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");
                    Log.d("gcm_message",""+message);
                    New_Booking();
                }
            }
        };


    }

    private void sendRegistrationToServer(final String reg_id, final String ven_id) {
        // sending gcm token to server
       // Log.e(TAG, "sendRegistrationToServer: " + reg_id);
        final ProgressDialog progressDialog = new ProgressDialog(Home_Screen.this);
        progressDialog.setMessage("Accepting....");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DOWN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        progressDialog.cancel();
                        if (s!=null)
                        {
                            Log.d("response_registration",s);
                        }
                        else
                        {

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        progressDialog.cancel();
                        // Toast.makeText(MyFirebaseInstanceIDService.this, "Error In Connectivity", Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> Keyvalue = new HashMap<String,String>();
                Keyvalue.put("reg_id",reg_id);
                Keyvalue.put("ven_id",ven_id);
                Log.d("accept_final_cut",Keyvalue.toString()+"");
                return Keyvalue;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        //Adding request to the queue
        requestQueue.add(stringRequest);





    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home__screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

            // Handle the camera action
        } else if (id == R.id.nav_bookings) {

            startActivity(new Intent(Home_Screen.this,Pending_Bookings.class));
        } else if (id == R.id.nav_help) {

        } else if (id == R.id.nav_logout) {

        }
        else if(id == R.id.nav_accept)
        {
            startActivity(new Intent(Home_Screen.this,Accepted_Booking.class));
        }
        else if(id==R.id.nav_completed)
        {
            startActivity(new Intent(Home_Screen.this,Completed_Bookings.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void New_Booking()
    {
        AlertDialog alertDialog = new AlertDialog.Builder(
                Home_Screen.this).create();

        // Setting Dialog Title
        alertDialog.setTitle("New Booking Received");

        // Setting Dialog Message
        alertDialog.setMessage("Go to booking to Confirm / Reject the booking");

        // Setting Icon to Dialog


        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Home_Screen.this,Pending_Bookings.class));
                // Write your code here to execute after dialog closed

            }
        });

        // Showing Alert Message
        alertDialog.show();

    }


    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Bookings.Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Bookings.Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());

    }




}
