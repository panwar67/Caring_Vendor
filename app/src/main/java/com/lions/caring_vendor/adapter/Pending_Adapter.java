package com.lions.caring_vendor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.lions.caring_vendor.R;
import com.lions.caring_vendor.options.Pending_Bookings;
import com.lions.caring_vendor.structs.Pending_Booking_Bean;

import java.util.ArrayList;

/**
 * Created by Panwar on 19/04/17.
 */
public class Pending_Adapter extends BaseAdapter {


    Context context;
    private static LayoutInflater inflater=null;
    String latitude, longitude;
    ArrayList<Pending_Booking_Bean> result = new ArrayList<Pending_Booking_Bean>();

    public  Pending_Adapter(Context cont, ArrayList<Pending_Booking_Bean> data, String lat, String longi)
    {
        context = cont;
        result = data;
        latitude = lat;
        longitude = longi;
        inflater = (LayoutInflater)context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);



    }


    @Override
    public int getCount() {
        return result.size();
    }

    @Override
    public Object getItem(int i) {
        return result.get(i);
    }

    @Override
    public long getItemId(int i) {
        return Long.parseLong(result.get(i).getBook_id());
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {


        final View root = inflater.inflate(R.layout.book_item,null);
        TextView book_id = (TextView)root.findViewById(R.id.book_id);

        final Button reject = (Button)root.findViewById(R.id.reject);
        Button accept =(Button)root.findViewById(R.id.accept);
        TextView advance = (TextView)root.findViewById(R.id.advance);
        TextView schedule = (TextView)root.findViewById(R.id.schedule);
        TextView distance = (TextView)root.findViewById(R.id.distance);
        TextView name =(TextView)root.findViewById(R.id.customer_name);
        name.setText("Customer Name : "+result.get(i).getCustomer_name());
        //distance.setText("Distance : "+distance(result.get(i).getLat(),result.get(i).getLongitude(),latitude,longitude));
        book_id.setText("Booking Id # : "+result.get(i).getBook_id());
        advance.setText("Booking Advance : "+result.get(i).getAdvance());
        schedule.setText("Schedule : "+result.get(i).getDate()+" Time "+result.get(i).getTime());
        distance.setText("Distance : "+distance);
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(context instanceof Pending_Bookings){
                    ((Pending_Bookings)context).Accept_Booking(result.get(i).getBook_id(),result.get(i).getCustomer_mob(),result.get(i).getOtp(),result.get(i).getCustomer_name());
                }
            }
        });
        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(context instanceof Pending_Bookings){
                    ((Pending_Bookings)context).Reject_Booking(result.get(i).getBook_id(),result.get(i).getCustomer_mob());
                }
            }
        });
        return root;
    }

    private double distance(String lati1, String loni1, String lati2, String loni2) {


       double lon1 = Double.parseDouble(loni1);

        double lon2 = Double.parseDouble(loni2);

        double lat1 = Double.parseDouble(lati1);

        double lat2 = Double.parseDouble(lati2);
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344;
        //Log.d("distance",""+dist);
        return dist;
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
}