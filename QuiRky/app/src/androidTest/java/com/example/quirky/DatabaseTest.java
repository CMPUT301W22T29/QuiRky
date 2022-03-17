package com.example.quirky;

import org.junit.*;
import org.osmdroid.util.GeoPoint;

import static org.junit.Assert.*;

import android.content.Context;

import com.google.firebase.firestore.FirebaseFirestore;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;

public class DatabaseTest {
    DatabaseController dc;
    Comment c;
    Profile p;
    QRCode qr;
    Context ct;

    @Before
    public void setup() {
        ct = ct.getApplicationContext();
        dc = new DatabaseController(FirebaseFirestore.getInstance());
        c = null; p = null; qr = null;
    }

    // Can not test that the write was successful without manually checking the database online.
    @Test
    public void writeComment() {
        Date d = new Date(823465789);
        c = new Comment("content1", "user1", d);
        dc.addComment(c, "sample1");
    }

    @Test
    public void writeProfile() {
        p = new Profile("player1", "palyer@ualberta.ac", "000-123-4321", new ArrayList<>());
        dc.writeProfile(p);
    }

    @Test
    public void writeQRCode() throws NoSuchAlgorithmException {
        GeoPoint gp = new GeoPoint(10.0, 10.0);
        qr = new QRCode("sample1", gp, null, new ArrayList<>());
        dc.writeQRCode(qr);
    }
}
