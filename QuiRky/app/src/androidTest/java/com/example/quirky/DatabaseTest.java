package com.example.quirky;

import org.junit.*;
import org.osmdroid.util.GeoPoint;

import static org.junit.Assert.*;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;

public class DatabaseTest {
    DatabaseController dc;
    Comment c;
    Profile p;
    QRCode qr;
    GeoPoint gp;
    Date date;

    @Before
    public void setup() {
        dc = new DatabaseController();
        c = null; p = null; qr = null;
        gp = new GeoPoint(10.0, 10.0);
        date = new Date(823465789);
    }

    // Can not test that the write was successful without manually checking the database online.
    @Test
    public void writeComment() {
        c = new Comment("content1", "user1", date);
        dc.writeComment(c, "sample1");
    }

    @Test
    public void writeProfile() {
        p = new Profile("player1", "palyer@ualberta.ac", "000-123-4321", new ArrayList<>());
        dc.writeProfile(p);
    }

    @Test
    public void writeQRCode() throws NoSuchAlgorithmException {
        qr = new QRCode("sample1", gp, null, new ArrayList<>());
        dc.writeQRCode(qr);
    }
}
