package com.example.quirky;

import android.location.Location;

import java.util.ArrayList;

/**
 * @author Jonathen Adsit
 * This model class represents a QR Code. It holds the QRCode's content, hash, score, location, image(?), and comments
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
