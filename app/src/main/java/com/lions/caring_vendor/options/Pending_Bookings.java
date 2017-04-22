package com.lions.caring_vendor.options;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lions.caring_vendor.R;
import com.lions.caring_vendor.adapter.Pending_Adapter;
import com.lions.caring_vendor.sessions.Session_Manager;
import com.lions.caring_vendor.structs.ExpandableHeightGridView;
import com.lions.caring_vendor.structs.Pending_Booking_Bean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Pending_Bookings extends AppCompatActivity {

    ExpandableHeightGridView expandableHeightGridView;
    Session_Manager session_manager;

    String DOWN_URL = "http://www.car-ing.com/app/AcceptBooking.php";

    LinearLayout back;
    String DOWN_URL1 = "http://www.car-ing.com/app/RejectBooking.php";
    String DOWN_URL2 = "http://www.car-ing.com/app/GetPendingBooking.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending__bookings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        back = (LinearLayout)findViewById(R.id.back);
        back.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                onBackPressed();
                finish();
                return false;

            }
        });
        session_manager = new Session_Manager(getApplicationContext());
        expandableHeightGridView = (ExpandableHeightGridView)findViewById(R.id.pending_bookings);
        expandableHeightGridView.setExpanded(true);
        expandableHeightGridView.setNumColumns(1);
        Get_Pending_Bookings(session_manager.getUserDetails().get("uid"),session_manager.getUserDetails().get("lat"),session_manager.getUserDetails().get("long"));

    }


    @Override
    protected void onResume() {
        super.onResume();
        Get_Pending_Bookings(session_manager.getUserDetails().get("uid"),session_manager.getUserDetails().get("lat"),session_manager.getUserDetails().get("long"));
    }

    public void Accept_Booking(final String book_id, final String user_mob, final String otp, final String user_name )
    {
        final ProgressDialog progressDialog = new ProgressDialog(Pending_Bookings.this);
        progressDialog.setMessage("Accepting....");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DOWN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        progressDialog.cancel();
                        if (s!=null)
                        {
                            Log.d("response_accept",s);
                            Get_Pending_Bookings(session_manager.getUserDetails().get("uid"),session_manager.getUserDetails().get("lat"),session_manager.getUserDetails().get("long"));
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
                Keyvalue.put("book_id",book_id);
                Keyvalue.put("user_mob",user_mob);
                Keyvalue.put("vendor_mob",session_manager.getUserDetails().get("mobile"));
                Keyvalue.put("user_name",user_name);
                Keyvalue.put("otp",otp);
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



    public void Reject_Booking(final String book_id,  final  String user_mob)
    {

        final ProgressDialog progressDialog = new ProgressDialog(Pending_Bookings.this);
        progressDialog.setMessage("Rejecting....");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DOWN_URL1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        progressDialog.cancel();
                        if (s!=null)
                        {
                            Log.d("response_reject",s);
                            Get_Pending_Bookings(session_manager.getUserDetails().get("uid"),session_manager.getUserDetails().get("lat"),session_manager.getUserDetails().get("long"));

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
                //Converting Bitmap to String


                HashMap<String,String> Keyvalue = new HashMap<String,String>();
                Keyvalue.put("book_id",book_id);

                Keyvalue.put("user_mob",user_mob);

                Log.d("reject_final_cut",Keyvalue.toString()+"");

                //returning parameters
                return Keyvalue;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        //Adding request to the queue
        requestQueue.add(stringRequest);

    }

    public void Get_Pending_Bookings(final String vendor_id, final String lat , final String longitude)
    {
        final ArrayList<Pending_Booking_Bean> data = new ArrayList<Pending_Booking_Bean>();
        final ProgressDialog progressDialog = new ProgressDialog(Pending_Bookings.this);
        progressDialog.setMessage("Checking....");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DOWN_URL2,
                new Response.Listener<String>() {
                    @Override
    public void onResponse(String s)
    {

                            progressDialog.cancel();
                        if (s!=null)
                         {
                                try {
                            JSONObject jsonObject = new JSONObject(s);
                            JSONArray jsonArray = jsonObject.getJSONArray("Book_Data");

                                if(jsonArray.length()>0)
                                {
                                    for(int i =0; i<jsonArray.length();i++)
                                    {

                                        JSONObject book_details = jsonArray.getJSONObject(i);
                                        Pending_Booking_Bean pending_booking_bean = new Pending_Booking_Bean();
                                        pending_booking_bean.setBook_id(book_details.getString("BOOK_ID"));
                                        pending_booking_bean.setCustomer_name(book_details.getString("BOOK_USER_NAME"));
                                        pending_booking_bean.setAdvance(book_details.getString("BOOK_ADVANCE"));
                                        pending_booking_bean.setDate(book_details.getString("BOOK_START_DATE"));
                                        pending_booking_bean.setTime(book_details.getString("BOOK_START_TIME"));
                                        pending_booking_bean.setCustomer_mob(book_details.getString("BOOK_USER_MOB"));
                                        pending_booking_bean.setLat(book_details.getString("BOOK_USER_LAT"));
                                        pending_booking_bean.setLongitude(book_details.getString("BOOK_USER_LONG"));
                                        pending_booking_bean.setOtp(book_details.getString("BOOK_OTP"));


                                        data.add(pending_booking_bean);

                                        expandableHeightGridView.setExpanded(true);
                                        expandableHeightGridView.setNumColumns(1);
                                        expandableHeightGridView.setAdapter(new Pending_Adapter(Pending_Bookings.this,data,lat,longitude));

                                    }



                                }
                                else
                                {

                                }

                                } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            progressDialog.cancel();


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
                //Converting Bitmap to String


                HashMap<String,String> Keyvalue = new HashMap<String,String>();
                Keyvalue.put("ven_id",vendor_id);

                Log.d("pending_final_cut",Keyvalue.toString()+"");

                //returning parameters
                return Keyvalue;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        //Adding request to the queue
        requestQueue.add(stringRequest);



    }

}
