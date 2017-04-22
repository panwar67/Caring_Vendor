package com.lions.caring_vendor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.lions.caring_vendor.R;
import com.lions.caring_vendor.structs.Completed_Bean;
import com.lions.caring_vendor.structs.Pending_Booking_Bean;

import java.util.ArrayList;

/**
 * Created by Panwar on 23/04/17.
 */
public class Completed_Adapter  extends BaseAdapter{


    Context context;
    private static LayoutInflater inflater=null;
    ArrayList<Completed_Bean> result = new ArrayList<Completed_Bean>();

    public Completed_Adapter(Context cont, ArrayList<Completed_Bean> data)
    {
        context = cont;
        result = data;

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


        final View root = inflater.inflate(R.layout.complete_item,null);

        TextView book_id = (TextView)root.findViewById(R.id.com_book_id);
        TextView price = (TextView)root.findViewById(R.id.com_book_price);

        book_id.setText("Booking Id # : "+result.get(i).getBook_id());
        price.setText("Price "+result.get(i).getPrice());
        return root;
    }



}
