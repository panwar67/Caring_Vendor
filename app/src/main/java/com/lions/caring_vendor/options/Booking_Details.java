package com.lions.caring_vendor.options;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lions.caring_vendor.R;
import com.lions.caring_vendor.adapter.Service_Track_Adapter;
import com.lions.caring_vendor.structs.ExpandableHeightGridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.Key;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Booking_Details extends AppCompatActivity {

    ExpandableHeightGridView expandableHeightGridView;
    Button start, stop;
    EditText Otp, total;
    TextView cont;
    LinearLayout back;
    String book_id, mobile, otps;
    String status;
    String DOWN_URL1 = "http://www.car-ing.com/app/Get_Book_Services.php";
    String DOWN_URL2 = "http://www.car-ing.com/app/start_service.php";
    String DOWN_URL3 = "http://www.car-ing.com/app/stop_service.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking__details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        start = (Button)findViewById(R.id.startservice);
        stop = (Button)findViewById(R.id.stop_service);
        back = (LinearLayout)findViewById(R.id.back);
        back.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                onBackPressed();
                finish();
                return false;

            }
        });


        cont = (TextView)findViewById(R.id.customer_no);
        expandableHeightGridView = (ExpandableHeightGridView)findViewById(R.id.service_details);
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Stop_Services(book_id, total.getText().toString());
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Otp.getText().toString().equals(otps))
                {
                    Otp.setVisibility(View.GONE);
                    start.setVisibility(View.GONE);

                    Start_Services(book_id,Otp.getText().toString());

                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Incorrect OTP",Toast.LENGTH_SHORT).show();
                }
            }
        });
        setSupportActionBar(toolbar);
        Otp = (EditText)findViewById(R.id.getotp);
        total = (EditText)findViewById(R.id.grand_total);
        Intent intent = getIntent();
        book_id = intent.getStringExtra("book_id");
        mobile = intent.getStringExtra("mobile");
        cont.setText("Contact : "+mobile);
        status = intent.getStringExtra("status");
        otps = intent.getStringExtra("otp");
        if(status.equals("STARTED"))
        {
            Otp.setVisibility(View.GONE);
            total.setVisibility(View.VISIBLE);

        }
        SetUp_Services(book_id);


    }


    @Override
    protected void onResume() {
        super.onResume();
        if(status.equals("STARTED"))
        {
            Otp.setVisibility(View.GONE);
            total.setVisibility(View.VISIBLE);

        }
    }

    public void SetUp_Services(final String Book_id)
    {
        final ArrayList<HashMap<String,String>> master = new ArrayList<HashMap<String, String>>();
        final ProgressDialog progressDialog = new ProgressDialog(Booking_Details.this);
        progressDialog.setMessage("loading");
        progressDialog.show();
        final ArrayList<HashMap<String,String>> Ven_Data = new ArrayList<HashMap<String, String>>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DOWN_URL1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (s!=null)
                        {
                            Log.d("book_details",""+s);
                            try {

                                JSONObject jsonObject = new JSONObject(s);
                                JSONArray jsonArray = jsonObject.getJSONArray("Book_Details");
                                for(int i=0;i<jsonArray.length();i++)
                                {
                                    JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
                                    HashMap<String,String> map = new HashMap<String, String>();
                                    map.put("BOOK_SERVE_NAME",jsonObject1.getString("BOOK_SERVICE_NAME"));
                                    map.put("BOOK_SERVE_ID",jsonObject1.getString("BOOK_SERVICE_CODE"));
                                    map.put("BOOK_TRACK",jsonObject1.getString("BOOK_TRACK"));
                                    master.add(map);

                                }



                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            expandableHeightGridView.setAdapter(new Service_Track_Adapter(getApplicationContext(),master));
                            progressDialog.cancel();

                        }
                        else
                        {

                            progressDialog.cancel();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        progressDialog.cancel();
                        Toast.makeText(Booking_Details.this, "Error In Connectivity", Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String


                HashMap<String,String> Keyvalue = new HashMap<String,String>();
                Keyvalue.put("book_id",Book_id);

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


    public void Start_Services(final String Book_id, final String otp)
    {
        final ProgressDialog progressDialog = new ProgressDialog(Booking_Details.this);
        progressDialog.setMessage("loading");
        progressDialog.show();
        final ArrayList<HashMap<String,String>> Ven_Data = new ArrayList<HashMap<String, String>>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DOWN_URL2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.d("start_res",""+s);

                        progressDialog.cancel();
                        if (s!=null)
                        {

                            Toast.makeText(getApplicationContext(),"Service Started",Toast.LENGTH_SHORT).show();
                            Otp.setVisibility(View.GONE);
                            start.setVisibility(View.GONE);
                            startActivity(new Intent(Booking_Details.this,Accepted_Booking.class));
                            finish();


                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Error Occured",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Booking_Details.this,Accepted_Booking.class));
                            finish();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        progressDialog.cancel();
                        Toast.makeText(Booking_Details.this, "Error In Connectivity", Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String


                HashMap<String,String> Keyvalue = new HashMap<String,String>();
                Keyvalue.put("book_id",Book_id);
                Keyvalue.put("otp",otp);
                Keyvalue.put("user_mob",mobile);

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

    public void Stop_Services(final String Book_id,  final String price)
    {
        final ProgressDialog progressDialog = new ProgressDialog(Booking_Details.this);
        progressDialog.setMessage("loading");
        progressDialog.show();
        final ArrayList<HashMap<String,String>> Ven_Data = new ArrayList<HashMap<String, String>>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DOWN_URL3,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.d("stop_res",""+s);
                        progressDialog.cancel();
                        if (s!=null)
                        {
                            Toast.makeText(getApplicationContext(),"Service Ended",Toast.LENGTH_SHORT).show();
                            Otp.setVisibility(View.GONE);
                            start.setVisibility(View.GONE);
                            Intent intent = new Intent(Booking_Details.this,Home_Screen.class);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Error Occured",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Booking_Details.this,Accepted_Booking.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        progressDialog.cancel();
                        Toast.makeText(Booking_Details.this, "Error In Connectivity", Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String


                HashMap<String,String> Keyvalue = new HashMap<String,String>();
                Keyvalue.put("book_id",Book_id);
                Keyvalue.put("book_price",price);
                Keyvalue.put("user_mob",mobile);

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
