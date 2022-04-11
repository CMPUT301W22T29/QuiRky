/*
 * QRCode.java
 *
 * Version 0.2.0
 * Version History:
 *      Version 0.1.0 -- QRCode instances can be constructed
 *      Version 0.2.0 -- Methods that shouldn't go in model classes were moved to QRCodeController.java
 *
 * Date (v0.2.0): March 14, 2022
 *
 * Copyright (c) 2022. CMPUT301W22T29
 * Subject to MIT License
 * See full terms at https://github.com/CMPUT301W22T29/QuiRky/blob/main/LICENSE
 */

package com.example.quirky;

import android.os.Parcel;
import android.os.Parcelable;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;



// Praise this absolute mad lad who literally answered all of my questions at once.
// I didn't even take any code from him but he's getting a cite anyways.
// https://stackoverflow.com/a/54194734
//
/**
 * @author Jonathen Adsit
 * This model class represents a QR Code. It holds the <code>QRCode</code>'s content, hash, score, location, and comments
 *
 */
public class QRCode implements Parcelable {
    private String id; // id is hash of content.
    private int score;
    private ArrayList<Comment> comments;
    private ArrayList<GeoPoint> locations;
    private ArrayList<String> scanners;

    /**
     * Initialize this <code>QRCode</code> with only the content. To be used when creating a new QRCode.
     * @param content
     *          - The content of the <code>QRCode</code> retrieved from the barcode in the
     *            <code>QRCodeController.scanQRCodes()</code> method.
     */
    public QRCode(String content) {
        this.id = QRCodeController.SHA256(content);
        this.score = QRCodeController.score(id);

        this.comments = new ArrayList<>();
        this.locations = new ArrayList<>();
        this.scanners = new ArrayList<>();
    }

    /**
     * Initialize this QRCode with every field. To be used when reading from FireStore
     * @param id The id of the QRCode
     * @param score The score of the QRCode
     * @param comments The list of comments on the code
     * @param locations The list of locations the code has been scanned
     * @param scanners The list of users that has scanned the code
     */
    public QRCode(String id, int score, ArrayList<Comment> comments, ArrayList<GeoPoint> locations, ArrayList<String> scanners) {
        this.id = id;
        this.score = score;
        this.comments = comments;
        this.locations = locations;
        this.scanners = scanners;
    }

    /**
     * Empty constructor because FireStore tutorial told me to...
     */
    public QRCode() {}

    /**
     * Getter for ID
     * @return The ID of the qrcode
     */
    public String getId() {
        return id;
    }

    /**
     * Getter for score
     * @return The score of the qrcode
     */
    public int getScore() {
        return score;
    }

    /**
     * Getter for the comments on the QRCode
     * @return ArrayList containing each comment on the qrcode
     */
    public ArrayList<Comment> getComments() {
        return comments;
    }

    public ArrayList<String> getScanners() { return scanners; }

    public ArrayList<GeoPoint> getLocations() { return locations; }

    /**
     * Adds a comment to the array. Throws an assertion error if the parameter is null.
     * @param c
     *      - The comment to be added
     */
    public void addComment(Comment c) throws AssertionError {
        assert c != null : "Can not add a null object to the list!";
        comments.add(c);
    }

    /**
     * Removes a comment from the array. If the comment does not exist in the array, do nothing.
     * @param c
     *      - The comment to be removed
     */
    public void removeComment(Comment c) {
        if(comments.contains(c))
            comments.remove(c);
    }

    public void addLocation(GeoPoint gp) {
        locations.add(gp);
    }

    public void removeLocation(GeoPoint gp) {
        locations.remove(gp);
    }

    public void setLocations(ArrayList<GeoPoint> locations) {
        this.locations = locations;
    }

    /** FIXME: determine if FireStore needs setters for custom object reading. Prefer not to have a direct setter.
     * Set the comments on the QRCode
     * @param comments An arraylist of comments
     */
    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    public void addScanner(String username) {
        scanners.add(username);
    }

    public void removeScanner(String username) {
        scanners.remove(username);
    }

    public void setScanners(ArrayList<String> scanners) {
        this.scanners = scanners;
    }


    // Parcelable stuff below
    protected QRCode(Parcel in) {
        id = in.readString();
        score = in.readInt();
        comments = in.createTypedArrayList(Comment.CREATOR);
        locations = in.createTypedArrayList(GeoPoint.CREATOR);
        scanners = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeInt(score);
        dest.writeTypedList(comments);
        dest.writeTypedList(locations);
        dest.writeStringList(scanners);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<QRCode> CREATOR = new Creator<QRCode>() {
        @Override
        public QRCode createFromParcel(Parcel in) {
            return new QRCode(in);
        }

        @Override
        public QRCode[] newArray(int size) {
            return new QRCode[size];
        }
    };
}
