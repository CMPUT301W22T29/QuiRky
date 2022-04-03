package com.example.quirky;

import org.junit.*;
import org.osmdroid.util.GeoPoint;

import static org.junit.Assert.*;


import java.lang.reflect.Array;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;

public class QRCodeTest {

    QRCode qr;

    // Test that fields are initialised correctly in the various constructors. Also tests the getters
    @Test
    public void constructorTests() {
        String content = "womp womp";
        String id = QRCodeController.SHA256(content);

        ArrayList<Comment> comments = new ArrayList<>();
        comments.add( new Comment("content1", "user1", new Date() ) );
        comments.add( new Comment("content2", "user2", new Date() ) );

        int score = QRCodeController.score(id);

        // Test Constructor that only takes content
        qr = new QRCode(content);

        assertEquals("ID was not properly assigned", id, qr.getId());
        assertEquals("Score was not properly assigned", score, qr.getScore());

        // Test constructor with content & comments
        qr = new QRCode(content, comments);
        assertEquals("Comments was not properly assigned", comments, qr.getComments());


        // Test constructor that takes ID & Score
        qr = new QRCode("id", 5);
        assertEquals("ID was not properly assigned in the second constructor!",
                "id", qr.getId());
        assertEquals("Score was not properly assigned in the second constructor!",
                5, qr.getScore());

        // Test constructor with ID, Score, & comments
        qr = new QRCode("id", 5, comments);
        assertEquals("Comments was not properly assigned in the second constructor!",
                comments,
                qr.getComments());
    }

    @Test
    public void constructorEdgeCases() {
        qr = new QRCode("");
        assertEquals("Empty content failed", "", qr.getId());

        qr = new QRCode("x", null);
        assertNull("Null arraylist failed!", qr.getComments());
    }

    // This test checks that setComment, addComment() and removeComment() work as intended. Also tests the getters
    @Test
    public void Comments() {

        Comment c = new Comment("I'm a new comment", "user1", new Date());

        ArrayList<Comment> comments = new ArrayList<>();

        comments.add(new Comment("content", "user", new Date()));

        qr = new QRCode("x", comments);


        // Test removing comments that aren't actually in the QRCode
        qr.removeComment(new Comment("I'm not actually in the QRCode's comments", "user", new Date()));
        assertEquals("Removing a comment the QRCode didn't have somehow changed the ArrayList<>",
                comments,
                qr.getComments());


        // Test adding a comment
        comments.add(c);
        qr.addComment(c);
        assertEquals("Adding comments did not work", comments, qr.getComments());

        // Test removing a comment that IS in the QRCode
        comments.remove(c);
        qr.removeComment(c);
        assertEquals("Removing comments from QRCode did not work", comments, qr.getComments());


        comments.clear();
        comments.add( new Comment("abc123", "user5", new Date()) );
        comments.add( new Comment("abc124", "user6", new Date()) );

        // Test setting the comments to something completely different.
        qr.setComments(comments);
        assertEquals("Setting comments did not work", comments, qr.getComments());
    }
}
