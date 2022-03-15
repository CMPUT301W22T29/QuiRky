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

        assertTrue("Add comment did not properly add!", qr.getComments().contains(c));
        assertEquals("Comment did not contain the right message!", "content", qr.getComments().get(0).getContent());
        assertEquals("Comment did not contain the right user!", "user", qr.getComments().get(0).getUname());
    }

    @Test
    public void TestArraySize() {
        assertEquals(0, qr.getComments().size());
        qr.addComment(c);
        assertEquals(1, qr.getComments().size());
        qr.removeComment(c);
        assertEquals(0, qr.getComments().size());
    }

    @Test(expected=AssertionError.class)
    public void AddNull() {
        c = null;
        qr.addComment(c);
    }

    @Test
    public void RemoveNull() {
        c = null;
        qr.removeComment(c);    // TODO: consider what the result is when we do ArrayList.exists(null)
    }
}
