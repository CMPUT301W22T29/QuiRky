package com.example.quirky;

import android.content.Context;

import org.junit.*;
import static org.junit.Assert.*;

import java.util.ArrayList;

// Code for getting context in a Unit Test taken from:
// https://stackoverflow.com/a/56514421
// Written by
// https://stackoverflow.com/users/7098269/ehsan
// Published June 9, 2019
public class MemoryTest {
    MemoryController mc;
    Context ct;

    @Before
    public void setup() {
        ct = ct.getApplicationContext();      // TODO: follow this https://stackoverflow.com/questions/2095695/android-unit-tests-requiring-context
        mc = new MemoryController(ct);
    }

    // This test checks that MC.readUser() and MC.writeUser() function properly.
    // This is a Happy Path test.
    @Test
    public void nameReadWrite() {
        // Test capital and non-capital letters
        String username = "PlaYer";
        String result;

        mc.writeUser(username);
        result = mc.readUser();

        assertEquals(username, result);

        // Test spaces
        username = "Bob Jame s";
        mc.writeUser(username);
        result = mc.readUser();

        assertEquals(username, result);

        // Test numbers and special characters
        username = "Hi 5, how art [] thou? ~}'%+";
        mc.writeUser(username);
        result = mc.readUser();

        assertEquals(username, result);
    }

    // Continue to test readUser() and writeUser() methods
    // This test is for edge cases, not the Happy path
    @Test
    public void nameReadWriteContinued() {
        // Case: empty string
        String username = "";
        mc.writeUser(username);
        String result = mc.readUser();

        assertEquals("Empty string case failed", username, result);

        // Case: control characters
        mc.writeUser("Hi\0 how are \' you? I am \t\\ good!");

        mc.writeUser(username);
        result = mc.readUser();

        assertEquals("Control characters case failed", username, result);
    }

    // This test checks that MC.read() and MC.write() function properly.
    // This is a Happy Path test.
    @Test
    public void ProfileReadWrite() {
        // Standard case
        Profile p = new Profile("Player");
        Profile result;

        mc.write(p);
        result = mc.read();

        assertEquals(p, result);

        p = new Profile("Second test", "abc", "123", new ArrayList<>());
        p.setPointsOfScannedCodes(5);

        mc.write(p);
        result = mc.read();

        assertEquals(p, result);
    }
}
