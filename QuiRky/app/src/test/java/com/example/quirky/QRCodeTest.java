package com.example.quirky;

import org.junit.*;

import static org.junit.Assert.*;

import java.util.ArrayList;

public class QRCodeTest {

    QRCode qr;

    // Test that fields are initialised correctly in the various constructors. Also tests the getters
    @Test
    public void constructorTests() {
        String content = "womp womp";
        String id = QRCodeController.SHA256(content);

        ArrayList<Comment> comments = new ArrayList<>();
        comments.add( new Comment("content1", "user1" ) );
        comments.add( new Comment("content2", "user2" ) );

        int score = QRCodeController.score(id);

        // Test Constructor that only takes content
        qr = new QRCode(content);

        assertEquals("ID was not properly assigned", id, qr.getId());
        assertEquals("Score was not properly assigned", score, qr.getScore());

        // Test constructor with ID, Score, & comments
        qr = new QRCode("id", 5, comments, null, null);
        assertEquals("Comments was not properly assigned in the second constructor!",
                comments,
                qr.getComments());
    }

    // Test the QRCode constructor with edge case values
    @Test
    public void constructorEdgeCases() {
        // The below test is already covered in QRControllerTest
        // qr = new QRCode("");
        // assertEquals("Empty content failed", "", qr.getId());

        qr = new QRCode("x", 5, null, null, null);
        assertNull("Null arraylist failed!", qr.getComments());
    }

    // This test checks that setComment, addComment() and removeComment() work as intended. Also tests the getters
    @Test
    public void Comments() {

        Comment c = new Comment("I'm a new comment", "user1");

        ArrayList<Comment> comments = new ArrayList<>();

        comments.add(new Comment("content", "user"));

        qr = new QRCode("x", 5, comments, null, null);

        // Test removing comments that aren't actually in the QRCode
        qr.removeComment(new Comment("I'm not actually in the QRCode's comments", "user"));
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
        comments.add( new Comment("abc123", "user5") );
        comments.add( new Comment("abc124", "user6") );

        // Test setting the comments to something completely different.
        qr.setComments(comments);
        assertEquals("Setting comments did not work", comments, qr.getComments());
    }
}
