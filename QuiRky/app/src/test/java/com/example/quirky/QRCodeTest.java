package com.example.quirky;

import org.junit.*;

import static org.junit.Assert.*;

import com.example.quirky.controllers.QRCodeController;
import com.example.quirky.models.Comment;
import com.example.quirky.models.GeoLocation;
import com.example.quirky.models.QRCode;

import java.util.ArrayList;

public class QRCodeTest {
    QRCode qr;

    // Test that content is initialised correctly in constructors
    @Test
    public void ContentTests() {
        // Standard case
        String content = "AaZz019";

        qr = new QRCode(content);
        assertEquals("Content Constructor Failed", content, qr.getContent());

        qr = new QRCode(content, 0, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        assertEquals("Full Constructor Failed", content, qr.getContent());

        // Empty string edge case
        content = "";

        qr = new QRCode(content);
        assertEquals("Content Constructor Failed", content, qr.getContent());

        qr = new QRCode(content, 0, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        assertEquals("Full Constructor Failed", content, qr.getContent());

        // Special characters case
        content = "a~!{+/ \n,\t";

        qr = new QRCode(content);
        assertEquals("Content Constructor Failed", content, qr.getContent());

        qr = new QRCode(content, 0, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        assertEquals("Full Constructor Failed", content, qr.getContent());
    }

    @Test
    public void IdTests() {
        // Standard case
        String content = "AaZz019";
        String id = QRCodeController.SHA256(content);

        qr = new QRCode(content);
        assertEquals("Content Constructor Failed", id, qr.getId());

        qr = new QRCode(content, 0, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        assertEquals("Full Constructor Failed", id, qr.getId());

        // Empty string edge case
        content = "";
        id = QRCodeController.SHA256(content);

        qr = new QRCode(content);
        assertEquals("Content Constructor Failed", id, qr.getId());

        qr = new QRCode(content, 0, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        assertEquals("Full Constructor Failed", id, qr.getId());

        // Special characters case
        content = "a~!{+/ \n,\t";
        id = QRCodeController.SHA256(content);

        qr = new QRCode(content);
        assertEquals("Content Constructor Failed", id, qr.getId());

        qr = new QRCode(content, 0, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        assertEquals("Full Constructor Failed", id, qr.getId());
    }

    // Test that score is initialised correctly in constructors
    @Test
    public void ScoreTests() {
        String content = "x";
        qr = new QRCode(content);

        assertEquals("Incorrect score in content constructor", QRCodeController.score(qr.getId()), qr.getScore());

        int score = 0;
        qr = new QRCode(content, score, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        assertEquals("Incorrect score in full constructor", score, qr.getScore());

        score = Integer.MIN_VALUE;
        qr = new QRCode(content, score, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        assertEquals("Incorrect score in full constructor", score, qr.getScore());

        score = Integer.MAX_VALUE;
        qr = new QRCode(content, score, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        assertEquals("Incorrect score in full constructor", score, qr.getScore());
    }

    // Test that the comment list is initialised and manipulated correctly in all methods
    @Test
    public void CommentTests() {
        Comment comment;
        ArrayList<Comment> expected = new ArrayList<>();

        // Test the constructors
        qr = new QRCode("x");
        assertEquals("Content constructor failed!", expected, qr.getComments());

        qr = new QRCode("x", 0, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        assertEquals("Full constructor failed!", expected, qr.getComments());

        // Test the add comment method
        comment = new Comment("x", "y");
        expected.add(comment);
        qr.addComment(comment);

        assertEquals("Adding a comment failed!", expected, qr.getComments());

        // Test the remove comment method
        expected.clear();
        qr.removeComment(comment);
        assertEquals("Removing the comment failed!", expected, qr.getComments());

        // Test the direct setter
        expected.add(comment);
        comment = new Comment("abc", "123");
        expected.add(comment);
        qr.setComments(expected);
        assertEquals("Direct comment setter failed!", expected, qr.getComments());

        try {
            qr.addComment(null);
            throw new RuntimeException("Add Comment did not catch that a null comment was added!");
        } catch (AssertionError ignored) {
            // Do nothing, because the assertion error is expected.
        }
    }

    // Test that the scanners list is initialised and manipulated correctly in all methods
    @Test
    public void ScannersTest() {
        String name;
        ArrayList<String> expected = new ArrayList<>();

        // Test the constructors
        qr = new QRCode("x");
        assertEquals("Content constructor failed!", expected, qr.getScanners());

        qr = new QRCode("x", 0, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        assertEquals("Full constructor failed!", expected, qr.getScanners());

        // Test the add scanner method
        name = "bob";
        expected.add(name);
        qr.addScanner(name);

        assertEquals("Adding a comment failed!", expected, qr.getScanners());

        // Test the remove scanner method
        expected.clear();
        qr.removeScanner(name);
        assertEquals("Removing the comment failed!", expected, qr.getScanners());

        // Test the direct setter
        expected.add(name);
        name = "joe";
        expected.add(name);
        qr.setScanners(expected);
        assertEquals("Direct comment setter failed!", expected, qr.getScanners());

        // Below block is commented out because QRCode.addScanner() does not have functionality to check for null usernames
        // TODO: But we should consider adding that functionality & uncommenting this code
//        try {
//            qr.addScanner(null);
//            throw new RuntimeException("Add Comment did not catch that a null comment was added!");
//        } catch (AssertionError ignored) {
//            // Do nothing, because the assertion error is expected.
//        }
    }

    // Test that the titles list is initialised and manipulated correctly in all methods
    @Test
    public void TitlesTest() {
        String title;
        ArrayList<String> expected = new ArrayList<>();

        // Test the constructors
        qr = new QRCode("x");
        assertEquals("Content constructor failed!", expected, qr.getTitles());

        qr = new QRCode("x", 0, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        assertEquals("Full constructor failed!", expected, qr.getTitles());

        // Test the add title method
        title = "bob";
        expected.add(title);
        qr.addTitle(title);

        assertEquals("Adding a comment failed!", expected, qr.getTitles());

        // Test the remove title method
        expected.clear();
        qr.removeTitle(title);
        assertEquals("Removing the comment failed!", expected, qr.getTitles());

        // Test the direct setter
        expected.add(title);
        title = "joe";
        expected.add(title);
        qr.setTitles(expected);
        assertEquals("Direct comment setter failed!", expected, qr.getTitles());

        // Below block is commented out because QRCode.addTitle() does not have functionality to check for null usernames
        // TODO: But we should consider adding that functionality & uncommenting this code
//        try {
//            qr.addTitle(null);
//            throw new RuntimeException("Add Comment did not catch that a null comment was added!");
//        } catch (AssertionError ignored) {
//            // Do nothing, because the assertion error is expected.
//        }
    }

    @Test
    public void LocationsTests() {
        GeoLocation geo;
        ArrayList<GeoLocation> expected = new ArrayList<>();

        // Test constructors
        qr = new QRCode("x");
        assertEquals("Content constructor failed to initialise geolocations", expected, qr.getLocations());

        qr = new QRCode("x", 0, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        assertEquals("Full constructor failed to initialise geolocations!", expected, qr.getLocations());

        // Test add location method
        geo = new GeoLocation();
        expected.add(geo);
        qr.addLocation(geo);
        assertEquals("Adding geolocation failed!", expected, qr.getLocations());

        // Test remove method
        expected.clear();
        qr.removeLocation(geo);
        assertEquals("Removing geolocation failed!", expected, qr.getLocations());

        // Test direct setter
        expected.add(geo);
        expected.add(new GeoLocation(10.0, 10.0));
        qr.setLocations(expected);
        assertEquals("Direct set of locations failed!", expected, qr.getLocations());

        // Below block is commented out because QRCode.addTitle() does not have functionality to check for null usernames
        // TODO: But we should consider adding that functionality & uncommenting this code
//        try {
//            qr.addLocation(null);
//            throw new RuntimeException("Add Comment did not catch that a null comment was added!");
//        } catch (AssertionError ignored) {
//            // Do nothing, because the assertion error is expected.
//        }
    }
}
