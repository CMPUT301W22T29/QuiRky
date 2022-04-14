/*
 * Copyright (c) 2022. CMPUT301W22T29
 * Subject to MIT License
 * See full terms at https://github.com/CMPUT301W22T29/QuiRky/blob/main/LICENSE
 */

package com.example.quirky;

import org.junit.*;

import static org.junit.Assert.*;

import com.example.quirky.controllers.QRCodeController;
import com.example.quirky.models.Profile;

import java.util.ArrayList;

public class ProfileTest {

    // Test that the various constructors initialise variables correctly
    @Test
    public void constructor() {
        Profile p;
        String name, email, phone;
        name = "a"; email = "b"; phone = "c";
        ArrayList<String> ids = new ArrayList<>();
        ids.add("abc123");


        p = new Profile(name);
        assertEquals(name, p.getUname());
        assertEquals("", p.getEmail());
        assertEquals("", p.getPhone());
        assertEquals(new ArrayList<String>(), p.getScanned());

        assertEquals(0, p.getPointsOfScannedCodes());
        assertEquals(0, p.getNumberCodesScanned());
        assertEquals(0, p.getPointsOfLargestCodes());


        p = new Profile(name, email, phone, ids);
        assertEquals(name, p.getUname());
        assertEquals(email, p.getEmail());
        assertEquals(phone, p.getPhone());
        assertEquals(ids, p.getScanned());

        int score = QRCodeController.score(ids.get(0));
        assertEquals(1, p.getNumberCodesScanned());
        assertEquals(score, p.getPointsOfScannedCodes());
        assertEquals(score, p.getPointsOfLargestCodes());   // This test may fail if we are using a negative scoring system.
    }

    // Test that the setters work as expected
    @Test
    public void Setters() {
        Profile p = new Profile("name");

        p.setUname("x");
        assertEquals("x", p.getUname());

        p.setEmail("y");
        assertEquals("y", p.getEmail());

        p.setPhone("z");
        assertEquals("z", p.getPhone());
    }

    // Test that adding id's and removing id's works as intended.
    @Test
    public void idListTest() {
        ArrayList<String> ids = new ArrayList<>();
        Profile p = new Profile("name");

        // Check that adding works
        ids.add("x");
        p.addScanned("x");
        assertEquals(ids, p.getScanned());

        // Check that addScanned() returns the proper boolean
        assertTrue(p.addScanned("y"));
        assertFalse(p.addScanned("y"));

        // Check that removing works
        p.removeScanned("y");
        assertEquals(ids, p.getScanned());

        // Check that removing an ID the profile does not have does nothing.
        p.removeScanned("I am not in p.scanned");
        assertEquals(ids, p.getScanned());
    }

    // Test that the profiles stat's are correctly updated when it's scanned codes change
    @Test
    public void StatsTest() {
        String id1 = "a";
        String id2 = "abc123";

        int score1 = QRCodeController.score(id1);
        int score2 = QRCodeController.score(id2);

        Profile p = new Profile("name");

        // Test that stats are updated when an id is added
        p.addScanned(id1);
        assertEquals(1, p.getNumberCodesScanned());
        assertEquals(score1, p.getPointsOfScannedCodes());
        assertEquals(score1, p.getPointsOfLargestCodes());

        // Test scores do not change when attempting to remove an element from the array that does not exist.
        p.removeScanned("i am not in p's scanned codes");
        assertEquals(1, p.getNumberCodesScanned());
        assertEquals(score1, p.getPointsOfScannedCodes());
        assertEquals(score1, p.getPointsOfLargestCodes());

        // Test stats are updated when an id is removed
        p.removeScanned(id1);
        assertEquals(0, p.getNumberCodesScanned());
        assertEquals(0, p.getPointsOfScannedCodes());
        assertEquals(0, p.getPointsOfLargestCodes());

        // Test stats are updated when multiple codes are added.
        p.addScanned(id1);
        p.addScanned(id2);
        assertEquals(2, p.getNumberCodesScanned());
        assertEquals(score1+score2, p.getPointsOfScannedCodes());
        assertEquals(score2, p.getPointsOfLargestCodes());
    }
}
