package com.example.androidproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;



public class AttractionAdapter extends ArrayAdapter<attraction> {
    public AttractionAdapter(Context context, ArrayList<attraction> notes) {
        super(context, 0, notes);

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        attraction user = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom, parent, false);

        }
        // Lookup view for data population
           TextView name = convertView.findViewById(R.id.tvName);
           ImageView img = convertView.findViewById(R.id.image);

        // Populate the data into the template view using the data object
        String atrName = user.getName();
        name.setText(atrName);
        String atrImG = user.getImgSrc();
        Picasso.get().load(atrImG).into(img);

        // Return the completed view to render on screen
        return convertView;
    }




}