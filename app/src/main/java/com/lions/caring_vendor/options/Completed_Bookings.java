package com.lions.caring_vendor.options;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import com.lions.caring_vendor.adapter.Accepted_Adapter;
import com.lions.caring_vendor.adapter.Completed_Adapter;
import com.lions.caring_vendor.sessions.Session_Manager;
import com.lions.caring_vendor.structs.Completed_Bean;
import com.lions.caring_vendor.structs.ExpandableHeightGridView;
import com.lions.caring_vendor.structs.Pending_Booking_Bean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Completed_Bookings extends AppCompatActivity {

    ExpandableHeightGridView expandableHeightGridView;
    String DOWN_URL2 = "http://www.car-ing.com/app/GetComplete.php";
    Session_Manager session_manager;
    LinearLayout back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed__bookings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        back = (LinearLayout)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        session_manager = new Session_Manager(getApplicationContext());
        expandableHeightGridView = (ExpandableHeightGridView)findViewById(R.id.booking_list);
        Get_Accepted_Bookings(session_manager.getUserDetails().get("uid"));

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Get_Accepted_Bookings(session_manager.getUserDetails().get("uid"));
    }

    public void Get_Accepted_Bookings(final String vendor_id)
    {
        final ArrayList<Completed_Bean> data = new ArrayList<Completed_Bean>();
        final ProgressDialog progressDialog = new ProgressDialog(Completed_Bookings.this);
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
                                        Completed_Bean pending_booking_bean = new Completed_Bean();
                                        pending_booking_bean.setBook_id(book_details.getString("BOOK_ID"));
                                        pending_booking_bean.setPrice(book_details.getString("BOOK_PRICE"));
                                        data.add(pending_booking_bean);

                                        expandableHeightGridView.setExpanded(true);
                                        expandableHeightGridView.setNumColumns(1);
                                        expandableHeightGridView.setAdapter(new Completed_Adapter(Completed_Bookings.this,data));

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

                Log.d("completed_final_cut",Keyvalue.toString()+"");

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
