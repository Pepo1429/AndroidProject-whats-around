package com.example.androidproject;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;

public class comments implements Parcelable {
    private String image;
    private String text;
    private String name;
    private String writedBy;
    private String rate;
    private String star;


    public comments() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public comments(String image,String text, String name, String writedBy, String rate, String star) {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
        this.image = image;
        this.text = text;
        this.name = name;
        this.writedBy = writedBy;
        this.rate = rate;
        this.star = star;


    }

    protected comments(Parcel in) {
        text = in.readString();
        image = in.readString();
        name = in.readString();
        writedBy = in.readString();
        rate = in.readString();
        star = in.readString();


    }

    public static final Creator<comments> CREATOR = new Creator<comments>() {
        @Override
        public comments createFromParcel(Parcel in) {
            return new comments(in);
        }

        @Override
        public comments[] newArray(int size) {
            return new comments[size];
        }
    };

    public String getImage(){
        return image;
    }
    public void setImage(String image){
        this.image = image;
    }
    public String getText(){
        return text;
    }
    public void setText(String text){ this.text = text;}

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getWritedBy(){
        return writedBy;
    }
    public void setWritedBy(String writedBy){
        this.writedBy = writedBy;
    }
    public  void setRate(String rate){
        this.rate = rate;
    }
    public String getRate(){
        return rate;
    }
    public  void setStar(String star){
        this.star = star;
    }
    public String getStar(){
        return star;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(text);
        dest.writeString(image);
        dest.writeString(name);
        dest.writeString(writedBy);
        dest.writeString(rate);
        dest.writeString(star);

    }
}


