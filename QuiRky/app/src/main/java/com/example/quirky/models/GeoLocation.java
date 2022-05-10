/*
 * Copyright (c) 2022. CMPUT301W22T29
 * Subject to MIT License
 * See full terms at https://github.com/CMPUT301W22T29/QuiRky/blob/main/LICENSE
 */

package com.example.quirky.models;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import org.osmdroid.util.GeoPoint;

/**
 * Adapter class (OO Design Pattern) to allow Firebase API and OSMDroid API to work together easily
 * Represents a location on the earth. Does not account for height/altitude
 */
public class GeoLocation implements Parcelable {

    // These are the exact coordinates of the location
    private double exactLat;
    private double exactLong;

    // Coordinates above 180* are not valid
    private static final double limit = (double) 180.1;

    // These are approximate coordinates of the location, which are helpful for getting nearby points from FireStore using the Query class
    private short approxLat;
    private short approxLong;

    // An optional description of the location. ex. an address or area description
    private String description;

    /**
     * Empty constructor for FireStore
     */
    public GeoLocation() {
        exactLat = 0;
        exactLong = 0;

        approxLat = 0;
        approxLong = 0;

        description = "";
    }

    /**
     * Initialize GeoLocation with a latitude and longitude
     */
    public GeoLocation(double exactLat, double exactLong) {
        assert (exactLat < limit && exactLong < limit) : "Those are invalid coordinates";
        this.exactLat = exactLat;
        this.exactLong = exactLong;

        approxLat = (short) exactLat;
        approxLong = (short) exactLong;

        description = "";
    }

    /**
     * Initialize a GeoLocation with a latitude, longitude, and a description
     */
    public GeoLocation(double exactLat, double exactLong, String description) {
        assert (exactLat < limit && exactLong < limit) : "Those are invalid coordinates";
        this.exactLat = exactLat;
        this.exactLong = exactLong;

        approxLat = (short) exactLat;
        approxLong = (short) exactLong;

        this.description = description;
    }

    /**
     * Initialize a Geolocation from an android.util.location
     * @param location The location object to initialize from
     */
    public GeoLocation(Location location) {
        this.exactLat = location.getLatitude();
        this.exactLong = location.getLongitude();

        this.approxLat = (short) exactLat;
        this.approxLong = (short) exactLong;

        this.description = "";
    }

    /**
     * Getter for exact latitude
     */
    public double getExactLat() {
        return exactLat;
    }

    /**
     * Getter for exact longitude
     */
    public double getExactLong() {
        return exactLong;
    }

    /**
     * Setter for exact latitude
     */
    public void setExactLat(double exactLat) {
        assert exactLat < limit : "That is not a valid coordinate!";
        this.exactLat = exactLat;
        this.approxLat = (short) exactLat;
    }

    /**
     * Getter for exact longitude
     */
    public void setExactLong(double exactLong) {
        assert exactLat < limit : "That is not a valid coordinate!";
        this.exactLong = exactLong;
        this.approxLong = (short) exactLong;
    }

    /**
     * Getter for approximate latitude
     */
    public short getApproxLat() {
        return approxLat;
    }

    /**
     * Getter for approximate longitude
     */
    public short getApproxLong() {
        return approxLong;
    }

    /**
     * Getter for location description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Getter for exact longitude
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Typecast this GeoLocation into a GeoPoint for OSMDroid tools
     * @return A GeoPoint object representing the same point as this GeoLocation
     */
    public GeoPoint toGeoPoint() {
        return new GeoPoint(exactLat, exactLong, 0);
    }

    /* - - - - - - Parcelable implementation - - - - - - - */
    protected GeoLocation(Parcel in) {
        exactLat = in.readDouble();
        exactLong = in.readDouble();
        approxLat = (short) in.readInt();
        approxLong = (short) in.readInt();
        description = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(exactLat);
        dest.writeDouble(exactLong);
        dest.writeInt((int) approxLat);
        dest.writeInt((int) approxLong);
        dest.writeString(description);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GeoLocation> CREATOR = new Creator<GeoLocation>() {
        @Override
        public GeoLocation createFromParcel(Parcel in) {
            return new GeoLocation(in);
        }

        @Override
        public GeoLocation[] newArray(int size) {
            return new GeoLocation[size];
        }
    };
}
