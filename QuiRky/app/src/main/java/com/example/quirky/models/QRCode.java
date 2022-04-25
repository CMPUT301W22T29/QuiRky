/*
 * Copyright (c) 2022. CMPUT301W22T29
 * Subject to MIT License
 * See full terms at https://github.com/CMPUT301W22T29/QuiRky/blob/main/LICENSE
 */

package com.example.quirky.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.quirky.controllers.QRCodeController;

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
    private String content, id; // id is hash of content.
    private int score;
    private ArrayList<Comment> comments;
    private ArrayList<GeoPoint> locations;
    private ArrayList<String> scanners; // Users who have scanned this code.
    private ArrayList<String> titles; // User created titles for the QRCode.

    /**
     * Initialize this <code>QRCode</code> with only the content. To be used when creating a new QRCode.
     * @param content
     *          - The content of the <code>QRCode</code> retrieved from the barcode in the
     *            <code>QRCodeController.scanQRCodes()</code> method.
     */
    public QRCode(String content) {
        this.content = content;
        this.id = QRCodeController.SHA256(content);
        this.score = QRCodeController.score(id);

        this.comments = new ArrayList<>();
        this.locations = new ArrayList<>();
        this.scanners = new ArrayList<>();
        this.titles = new ArrayList<>();
    }

    /**
     * Initialize this QRCode with every field. To be used when reading from FireStore
     * @param content The content of the QRCode
     * @param score The score of the QRCode
     * @param comments The list of comments on the code
     * @param locations The list of locations the code has been scanned
     * @param scanners The list of users that has scanned the code
     */
    public QRCode(String content, int score, ArrayList<Comment> comments, ArrayList<GeoPoint> locations, ArrayList<String> scanners, ArrayList<String> titles) {
        this.content = content;
        this.id = QRCodeController.SHA256(content);
        this.score = score;
        this.comments = comments;
        this.locations = locations;
        this.scanners = scanners;
        this.titles = titles;
    }

    /**
     * Empty constructor because FireStore tutorial told me to...
     */
    public QRCode() {}

    /**
     * Getter for QRCode content
     */
    public String getContent() { return content; }

    /**
     * Getter for QRCode's ID
     */
    public String getId() {
        return id;
    }

    /**
     * Getter for score
     */
    public int getScore() {
        return score;
    }

    /**
     * Getter for the comments on the QRCode
     */
    public ArrayList<Comment> getComments() {
        return comments;
    }

    /**
     * Get the list of players that have scanned this QRCode
     */
    public ArrayList<String> getScanners() { return scanners; }

    /**
     * Get the list of locations this QRCode has been scanned at
     */
    public ArrayList<GeoPoint> getLocations() { return locations; }

    public ArrayList<String> getTitles() {
        if(titles == null)
            titles = new ArrayList<>();
        return this.titles;
    }

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
        comments.remove(c);
    }

    /** FIXME: determine if FireStore needs setters for custom object reading. Prefer not to have a direct setter.
     * Set the comments on the QRCode
     * @param comments An arraylist of comments
     */
    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    /**
     * Add a location to the list of places the QRCode has been scanned at
     * @param gp The GeoPoint to add
     */
    public void addLocation(GeoPoint gp) {
        locations.add(gp);
    }

    /**
     * Remove a location from the list of places the QRCode has been scanned at
     * @param gp The GeoPoint to remove
     *           TODO: Consider if we need this method. Under what circumstance will it be called?
     */
    public void removeLocation(GeoPoint gp) {
        locations.remove(gp);
    }

    public void setLocations(ArrayList<GeoPoint> locations) {
        this.locations = locations;
    }


    /**
     * Add a scanner to the list of players that have scanned the code
     * @param username The username of the player that has scanned it
     */
    public void addScanner(String username) {
        scanners.add(username);
    }

    /**
     * Remove a scanner from the list of players that have scanned the code
     * @param username The username of the player to remove
     */
    public void removeScanner(String username) {
        scanners.remove(username);
    }

    public void setScanners(ArrayList<String> scanners) {
        this.scanners = scanners;
    }

    public void addTitle(String title) { titles.add(title); }

    public void removeTitle(String title) { titles.remove(title); }

    public void setTitle(ArrayList<String> titles) { this.titles = titles; }


    public void setContent(String content) {
        this.content = content;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setTitles(ArrayList<String> titles) {
        this.titles = titles;
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
