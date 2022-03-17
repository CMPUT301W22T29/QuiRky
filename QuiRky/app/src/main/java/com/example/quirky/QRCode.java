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

import org.osmdroid.util.GeoPoint;
import android.media.Image;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

/**
 * @author Jonathen Adsit
 * This model class represents a QR Code. It holds the <code>QRCode</code>'s content, hash, score, location, and comments
 *
 * Known Issues:
 *      - QR Codes contain the content of the code as a raw unencrypted string, which is probably
 *        bad for security and privacy, and contradicts US 08.0X.01. (v0.1.1)
 *      - Each new <code>QRCode</code> instance instantiates a <code>QRCodeController</code> instance. bad. (v0.2.0)
 */
public class QRCode {
    private String id; // id is hash of content.
    private int score;
    private GeoPoint geolocation;
    private Image photo;                  // TODO: determine what datatype the photo will use. Not sure how to contain it in this class. Will also need to update constructor
    private ArrayList<Comment> comments;

    /**
     * The constructor to be used when the QRCode has comments on it. This constructor likely be called when the QRCode already exists in the Database.
     * If the user does not want to save their geolocation or photo, constructor can be called with these values null.
     * If the user does not want to save the content of the code, the constructor can be called with Boolean saveContent == False.
     * @param content
     *      - The content of the QRCode. This is mandatory, as it is used to find the QRCode's hash and score
     * @param geolocation
     *      - The location of the QRCode
     * @param photo
     *      - The photo of the QRCode. This is currently an Integer, and will need to be updated once we find how to store a Photo.
     * @param comments
     *      - The comments that have been posted on the QRCode, in an ArrayList<>
     */
    public QRCode(String content, GeoPoint geolocation, Image photo, ArrayList<Comment> comments) {
        this.id = QRCodeController.SHA256(content);
        this.score = QRCodeController.score(id);
        this.photo = photo;
        this.geolocation = geolocation;
        this.comments = comments;
    }

    /**
     * The constructor to be used when the QRCode has no comments yet. This constructor likely be called when this is the first time the code has been scanned by any user.
     * If the user does not want to save their geolocation or photo, constructor can be called with these values null.
     * If the user does not want to save the content of the code, the constructor can be called with Boolean saveContent == False.
     * The comments field will be initialised to an empty ArrayList.
     * @param content
     *      - The content of the QRCode. This is mandatory, as it is used to find the QRCode's hash and score
     * @param geolocation
     *      - The location of the QRCode
     * @param photo
     *      - The photo of the QRCode. This is currently an Integer, and will need to be updated once we find how to store a Photo.
     */
    public QRCode(String content, GeoPoint geolocation, Image photo) {
        this.id = QRCodeController.SHA256(content);
        this.score = QRCodeController.score(id);
        this.photo = photo;
        this.geolocation = geolocation;
        this.comments = new ArrayList<>();
    }

    /**
     * Initialize this <code>QRCode</code> with only the content.
     * @param content
     *          - The content of the <code>QRCode</code> retrieved from the barcode in the
     *            <code>QRCodeController.scanQRCodes()</code> method.
     */
    public QRCode(String content) {
        this.id = QRCodeController.SHA256(content);
        this.score = QRCodeController.score(id);
        this.comments = new ArrayList<>();
        this.photo = null;
        this.geolocation = null;
    }

    /**
     * Empty constructor because FireStore tutorial told me to...
     */
    public QRCode() {}

    public String getId() {
        return id;
    }

    public int getScore() {
        return score;
    }

    public GeoPoint getGeolocation() {
        return geolocation;
    }

    public ArrayList<Comment> getComments() {
        return comments;
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
        if(comments.contains(c))
            comments.remove(c);
    }

    public void setGeolocation(GeoPoint geolocation) {
        this.geolocation = geolocation;
    }
}
