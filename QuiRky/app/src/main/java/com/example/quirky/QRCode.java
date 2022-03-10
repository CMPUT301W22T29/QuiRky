package com.example.quirky;

import android.location.Location;

import java.util.ArrayList;

public class QRCode {
    private final String content, id; // id is hash of content.
    private int score;
    private Location geolocation;
    private ArrayList<Comment> comments;

    public QRCode(String content, String id, int score, Location geolocation, ArrayList<Comment> comments) {
        this.content = content;
        this.id = SHA256(content);
        this.score = generateScore(id);
        this.geolocation = geolocation;
        this.comments = comments;
    }

    public QRCode(String content, String id, int score, Location geolocation) {
        this.content = content;
        this.id = SHA256(content);
        this.score = generateScore(id);
        this.geolocation = geolocation;
        this.comments = new ArrayList<Comment>();
    }

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
}
