package com.example.quirky;

import org.junit.*;

import static org.junit.Assert.*;

public class DatabaseTest {

    DatabaseController dm;
    String data;
    Comment c;
    Profile p;
    QRCode q;

    @Before
    public void setup() {
        dm = new DatabaseController();
        data = "";
        c = null; p = null; q = null;
    }

    @Test
    public void commentRead() {
        assertEquals(0, 1);
    }

    @Test
    public void commentWrite() {
        assertEquals(0, 1);
    }

    @Test
    public void profileRead() {
        assertEquals(0, 1);
    }

    @Test
    public void profileWrite() {
        assertEquals(0, 1);
    }

    @Test
    public void qrRead() {
        assertEquals(0, 1);
    }

    @Test
    public void qrWrite() {
        assertEquals(0, 1);
    }
}
