package com.googlmaptesting.kun.demo_map;

/**
 * Created by User on 2017-02-16.
 */

public class AddressObject {
    private double latitude;
    private double longitude;
    //private Marker maker;
    private String Address;
    private String description;

    //default constructor
    public AddressObject(){}

    // alternate constructor
    public AddressObject(double latitude, double Longtidue, String Address, String description){
        this.latitude = latitude;
        this.longitude = Longtidue;
        this.Address = Address;
        this.description = description;
    }


    //set method to populate the location object
    public void setLatitude(double latitude){this.latitude = latitude;}

    public void setLongitude(double longitude){this.longitude = longitude;}

    public void setAddress(String Address){this.Address = Address;}

    public void setDescription(String description){this.description = description; }

    // get method to return each value
    public double getLatitude(){return latitude;}

    public double getLongitude90(){return longitude;}

    public String getAddress(){return Address;}

    public String getDescription(){return description;}

    //toString method in case need to print out the detail info of the object

    public String toString(){
        return getLatitude()+":"+getLongitude90()+":    " + getAddress() +":    "+getDescription();
    }
}
