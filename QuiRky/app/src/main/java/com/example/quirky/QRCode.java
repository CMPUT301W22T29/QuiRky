/*
 * QRCode.java
 *
 * Version 0.1.1
 * Version History:
 *      Version 0.1.0 -- QRCode instances can be constructed
 *      Version 0.1.1 -- Geolocations may be set in retrospect.
 *
 * Date (v0.1.1): March 14, 2022
 *
 * Copyright (c) 2022. CMPUT301W22T29
 * Subject to MIT License
 * See full terms at https://github.com/CMPUT301W22T29/QuiRky/blob/main/LICENSE
 */

package com.example.quirky;

import android.location.Location;

import java.util.ArrayList;

/**
 * @author Jonathen Adsit
 * This model class represents a QR Code. It holds the <code>QRCode</code>'s content, hash, score, location, image(?), and comments
 *
 * Known Issues:
 *      - <code>SHA256()</code> does not actually generate an SHA hash yet (v0.1.1)
 *      - QR Codes contain the content of the code as a raw unencrypted string, which is probably
 *        bad for security and privacy, and contradicts US 08.0X.01. (v0.1.1)
 */
public class QRCode {
    private final String content, id; // id is hash of content.
    private final int score;
    private Location geolocation;
    private ArrayList<Comment> comments;

    public QRCode(String content, Location geolocation, ArrayList<Comment> comments) {
        this.content = content;
        this.id = SHA256(content);
        this.score = generateScore(id);
        this.geolocation = geolocation;
        this.comments = comments;
    }

    public QRCode(String content, Location geolocation) {
        this.content = content;
        this.id = SHA256(content);
        this.score = generateScore(id);
        this.geolocation = geolocation;
        this.comments = new ArrayList<Comment>();
    }

    /**
     * Initialize this <code>QRCode</code> with only the content.
     * @param content
     *          - The content of the <code>QRCode</code> retrieved from the barcode in the
     *            <code>QRCodeController.scanQRCodes()</code> method.
     */
    public QRCode(String content) {
        this.content = content;
        this.id = SHA256(content);
        this.score = generateScore(id);
        this.comments = new ArrayList<Comment>();
    }

    // TODO: implement the SHA-256 algorithm and the scoring of the hash
    private String SHA256(String content) {
        return content;
    }

    private int generateScore(String content) {
        return 0;
    }

    public String getContent() {
        return content;
    }

    public String getId() {
        return id;
    }

    public int getScore() {
        return score;
    }

    public Location getGeolocation() {
        return geolocation;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    /**
     * Adds a comment to the array
     * @param c
     *      - The comment to be added
     */
    public void addComment(Comment c) {
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

    public void setGeolocation(Location geolocation) {
        this.geolocation = geolocation;
    }
}
