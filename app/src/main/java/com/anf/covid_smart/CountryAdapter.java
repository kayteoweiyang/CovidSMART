package com.anf.covid_smart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.LayoutInflaterCompat;

import java.util.ArrayList;

public class CountryAdapter extends ArrayAdapter<Country> {

    private Context mContext;
    private int mResource;

    public CountryAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Country> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = LayoutInflater.from(mContext);

        convertView = layoutInflater.inflate(mResource,parent, false);

        TextView txtCountry = convertView.findViewById(R.id.c_name);
        TextView txtActiveNum = convertView.findViewById(R.id.c_active_num);
        TextView txtNewNum = convertView.findViewById(R.id.c_new_num);

        txtCountry.setText(getItem(position).getC_name());
        txtActiveNum.setText(getItem(position).getC_active());
        txtNewNum.setText(getItem(position).getC_new());


        return convertView;
    }
}
