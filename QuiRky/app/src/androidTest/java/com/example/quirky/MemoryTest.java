package com.example.quirky;

import android.content.Context;

import org.junit.*;
import static org.junit.Assert.*;

import com.example.quirky.controllers.MemoryController;
import com.example.quirky.models.Profile;

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

    // This test checks that MC.read() and MC.write() function properly.
    // This is a Happy Path test.
    @Test
    public void ProfileReadWrite() {
        // Standard case
        Profile p = new Profile("Player");
        Profile result;

        mc.writeUser(p);
        result = mc.read();

        assertEquals(p, result);

        p = new Profile("Second test", "abc", "123", new ArrayList<>());

        mc.writeUser(p);
        result = mc.read();

        assertEquals(p, result);
    }
}
