package com.example.quirky;

import android.location.Location;

import java.util.ArrayList;

/**
 * @author Jonathen Adsit
 * This model class represents a QR Code. It holds the QRCode's content, hash, score, location, and comments
 */
public class QRCode {
    private final String content, id; // id is hash of content.
    private final int score;
    private Location geolocation;
    private ArrayList<Comment> comments;

    // FIXME: Should QRCode have an instance of it's controller in itself? For 'content' and 'id' to be declared as final, they must be initialized in the constuctor, requiring QRCode to have a QRCodeController

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

    public QRCode(String content) {
        QRCodeController qrcc = new QRCodeController();
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
}
