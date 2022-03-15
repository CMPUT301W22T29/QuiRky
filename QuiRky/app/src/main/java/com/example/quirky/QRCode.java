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

import android.location.Location;

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
    private final String content, id; // id is hash of content.
    private final int score;
    private Location geolocation;
    private ArrayList<Comment> comments;

    // FIXME: Should QRCode have an instance of it's controller in itself? For 'content' and 'id' to be declared as final, they must be initialized in the constuctor, requiring QRCode to have a QRCodeController
    //TODO: Use static methods in QRCodeController so you don't need an instance, either that or use a singleton with a getInstance method.

    public QRCode(String content, Location geolocation, ArrayList<Comment> comments) {
        QRCodeController qrcc = new QRCodeController();
        this.content = content;
        this.id = qrcc.SHA256(content);
        this.score = qrcc.score(id);
        this.geolocation = geolocation;
        this.comments = comments;
    }

    public QRCode(String content, Location geolocation) {
        QRCodeController qrcc = new QRCodeController();
        this.content = content;
        this.id = qrcc.SHA256(content);
        this.score = qrcc.score(id);
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
        QRCodeController qrcc = new QRCodeController();     //TODO: prolly doesn't need this
        this.content = content;
        this.id = qrcc.SHA256(content);
        this.score = qrcc.score(id);
        this.comments = new ArrayList<>();
        this.geolocation = new Location("place");
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

    public void setGeolocation(Location geolocation) {
        this.geolocation = geolocation;
    }
}
