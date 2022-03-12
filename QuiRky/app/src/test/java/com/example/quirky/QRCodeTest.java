package com.example.quirky;

import org.junit.*;

import static org.junit.Assert.*;

import android.location.Location;

import java.util.Date;

public class QRCodeTest {

    QRCode qr;
    Comment c;

    @Before
    public void setup() {
        qr = new QRCode("content", new Location("location"));
        c = new Comment("content", "user", new Date());
    }

    @Test
    public void TestCommentAdd() {
        qr.addComment(c);
        assertTrue(qr.getComments().contains(c));
    }

    @Test
    public void TestArraySize() {
        assertEquals(0, qr.getComments().size());
        qr.addComment(c);
        assertEquals(1, qr.getComments().size());
        qr.removeComment(c);
        assertEquals(0, qr.getComments().size());
    }
}
