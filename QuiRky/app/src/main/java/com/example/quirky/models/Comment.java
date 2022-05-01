/*
 * Copyright (c) 2022. CMPUT301W22T29
 * Subject to MIT License
 * See full terms at https://github.com/CMPUT301W22T29/QuiRky/blob/main/LICENSE
 */

package com.example.quirky.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.quirky.controllers.QRCodeController;

/**
 * This is the comment that will be stored to a Database. The Comment will have
 * the content of the comment, and the user who wrote it
 *
 * @author Raymart Bless C. Datuin
 *
 */
public class Comment implements Parcelable {

    private String content, uname, id;

    /**
     * Empty constructor because Firestore tutorial told me to...
     */
    public Comment() {}

    /**
     * Constructor to initialise content and author
     * @param content The contents of the comment
     * @param uname The user who wrote it
     */
    public Comment(String content, String uname) {
        this.content = content;
        this.uname = uname;
        this.id = QRCodeController.SHA256(content+uname);
    }

    /**
     * Getter for content
     * @return The content of the QRCode
     */
    public String getContent() {
        return content;
    }

    /**
     * Getter for author
     * @return The username of the player who wrote it
     */
    public String getUname() {
        return uname;
    }

    /**
     * Setter for content
     * @param content The new content
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Getter for ID
     * @return The SHA256 hash of the content, for Firebase storage purposes.
     */
    public String getId() {
        return this.id;
    }

    protected Comment(Parcel in) {
        content = in.readString();
        uname = in.readString();
        id = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(content);
        dest.writeString(uname);
        dest.writeString(id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };
}
