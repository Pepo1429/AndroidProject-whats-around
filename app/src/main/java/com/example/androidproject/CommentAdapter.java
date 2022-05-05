package com.example.androidproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
//}


public class CommentAdapter extends ArrayAdapter<comments> {
    public CommentAdapter(Context context, ArrayList<comments> notes) {
        super(context, 0, notes);

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        comments comment = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.comments, parent, false);

        }
        if(comment.getStar().equals("true")){
            ImageView imageview = convertView.findViewById(R.id.image2);
            imageview.setVisibility(View.VISIBLE);
        }
        RatingBar simpleRatingBar = convertView.findViewById(R.id.ratingBar2); // initiate a rating ba
        String sRate = comment.getRate();
        float rate = Float.parseFloat(sRate);
        simpleRatingBar.setRating(rate);
        ImageView img = convertView.findViewById(R.id.image1);

        // Lookup view for data population
        TextView name = convertView.findViewById(R.id.textView15);
        TextView text = convertView.findViewById(R.id.textView14);

        // Populate the data into the template view using the data object
        String writedBy = comment.getWritedBy();
        name.setText(writedBy);
        String textD = comment.getText();
        text.setText(textD);

        String imgCom = comment.getImage();
        Picasso.get().load(imgCom).into(img);


        // Return the completed view to render on screen
        return convertView;
    }




}
