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
public class QRCode implements Parcelable {
    private String id; // id is hash of content.
    private int score;
    private ArrayList<Comment> comments;

    /**
     * The constructor to be used when the QRCode has comments on it. This constructor likely be called when the QRCode already exists in the Database.
     * If the user does not want to save their geolocation or photo, constructor can be called with these values null.
     * If the user does not want to save the content of the code, the constructor can be called with Boolean saveContent == False.
     * @param content
     *      - The content of the QRCode. This is mandatory, as it is used to find the QRCode's hash and score
     * @param comments
     *      - The comments that have been posted on the QRCode, in an ArrayList<>
     */
    public QRCode(String content, ArrayList<Comment> comments) {
        this.id = QRCodeController.SHA256(content);
        this.score = QRCodeController.score(id);
        this.comments = comments;
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
    }

    /**
     * Initialize a QRCode with an assigned ID. To be used when reading from Firestore? A
     * @param id The ID of the QRCode
     */
    public QRCode(String id, int score) {
        this.id = id;
        this.score = score;
        this.comments = new ArrayList<>();
    }

    public QRCode(String id, int score, ArrayList<Comment> comments) {
        this.id = id;
        this.score = score;
        this.comments = comments;
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

    /**
     * Set the comments on the QRCode
     * @param comments An arraylist of comments
     */
    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    protected QRCode(Parcel in) {
        id = in.readString();
        score = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeInt(score);
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
