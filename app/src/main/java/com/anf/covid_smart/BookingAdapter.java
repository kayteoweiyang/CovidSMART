package com.anf.covid_smart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class BookingAdapter extends ArrayAdapter<Booking> {

    private Context mContext;
    private int mResource;

    public BookingAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Booking> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = LayoutInflater.from(mContext);

        convertView = layoutInflater.inflate(mResource,parent, false);

        TextView txtName = convertView.findViewById(R.id.b_name_txt);
        TextView txtNRIC = convertView.findViewById(R.id.b_nric_txt);
        TextView txtDate = convertView.findViewById(R.id.b_date_txt);
        TextView txtTime = convertView.findViewById(R.id.b_time_txt);

        txtName.setText(getItem(position).getB_name());
        txtNRIC.setText(getItem(position).getB_nric());
        txtDate.setText(getItem(position).getB_date());
        txtTime.setText(getItem(position).getB_time());


        return convertView;
    }
}
