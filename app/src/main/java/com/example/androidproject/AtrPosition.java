package com.example.androidproject;

public class AtrPosition {
    private String name;
    private String latitude;
    private String longitude;


    public AtrPosition(String name, String latitude, String longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }


    public AtrPosition() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

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

}
