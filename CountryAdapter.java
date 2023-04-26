package com.aysegulyilmaz.homework3_5;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CountryAdapter extends ArrayAdapter<Country> {

    public CountryAdapter(Context context, ArrayList<Country> countryArrayList){
        super(context,0,countryArrayList);
    }

    public View getView(int position, View convertView, ViewGroup parent){
        return initView(position,convertView,parent);
    }

    public View getDropDownView(int position,View convertView, ViewGroup parent){
        return initView(position,convertView,parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.spinner_item,parent,false
            );
        }

        ImageView imageView = convertView.findViewById(R.id.image_view);
        TextView textView = convertView.findViewById(R.id.txtView_name);

        Country current = getItem(position);

        if(current !=null) {
            imageView.setImageResource(current.getCountryFlag());
            textView.setText(current.getCountryName());

        }

        return convertView;



    }

}

