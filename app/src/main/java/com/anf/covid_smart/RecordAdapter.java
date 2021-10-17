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

public class RecordAdapter extends ArrayAdapter<Records> {

    private Context mContext;
    private int mResource;

    public RecordAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Records> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = LayoutInflater.from(mContext);

        convertView = layoutInflater.inflate(mResource,parent, false);

        TextView txtCountry = convertView.findViewById(R.id.r_addr);
        TextView txtActiveNum = convertView.findViewById(R.id.r_date);
        TextView txtNewNum = convertView.findViewById(R.id.r_time);

        txtCountry.setText(getItem(position).getR_addr());
        txtActiveNum.setText(getItem(position).getR_date());
        txtNewNum.setText(getItem(position).getR_time());


        return convertView;
    }
}
