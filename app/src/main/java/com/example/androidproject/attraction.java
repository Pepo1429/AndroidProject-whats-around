package com.example.androidproject;

import android.os.Parcel;
import android.os.Parcelable;

public class attraction implements Parcelable {

    private String name;
    private String latitude;
    private String longitude;
    private String rate;
    private String totalVisiting;
    private String imgSrc;
    private String dateOfAdd;
    private String createdBy;


    public attraction() {
    }

    public attraction(String name, String latitude, String longitude, String rate, String totalVisitors, String imgSrc,String createdBy, String dateOfAdd)
     {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.rate = rate;
        this.totalVisiting = totalVisitors;
        this.imgSrc = imgSrc;
        this.createdBy = createdBy;
        this.dateOfAdd = dateOfAdd;
    }

    protected attraction(Parcel in) {
        name = in.readString();
        latitude = in.readString();
        longitude = in.readString();
        rate = in.readString();
        totalVisiting = in.readString();
        imgSrc = in.readString();
        dateOfAdd = in.readString();
        createdBy = in.readString();

    }

    public static final Creator<attraction> CREATOR = new Creator<attraction>() {
        @Override
        public attraction createFromParcel(Parcel in) {
            return new attraction(in);
        }

        @Override
        public attraction[] newArray(int size) {
            return new attraction[size];
        }
    };

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getLatitude(){
        return latitude;
    }
    public void setLatitude(String latitude){
        this.latitude = latitude;
    }
    public String getLongitude(){
        return longitude;
    }
    public void setLongitude(String longitude){
        this.longitude = longitude;
    }
    public String getRate(){
        return rate;
    }
    public void setRate(String rate){
        this.rate = rate;
    }
    public String getTotalVisiting(){
        return totalVisiting;
    }
    public void setTotalVisitors(String totalVisiting){
        this.totalVisiting = totalVisiting;
    }
    public String getImgSrc(){
        return imgSrc;
    }
    public void setImgSrc(String imgSrc){
        this.imgSrc = imgSrc;
    }
    public String getDateOfAdd(){
        return dateOfAdd;
    }
    public void setDateOfAdd(String dateOfAdd){
        this.dateOfAdd = dateOfAdd;
    }
    public String getCreatedBy(){
        return createdBy;
    }
    public void setCreatedBy(String createdBy){
        this.createdBy = createdBy;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(latitude);
        dest.writeString(longitude);
        dest.writeString(rate);
        dest.writeString(totalVisiting);
        dest.writeString(imgSrc);
        dest.writeString(dateOfAdd);
        dest.writeString(createdBy);

    }
}
