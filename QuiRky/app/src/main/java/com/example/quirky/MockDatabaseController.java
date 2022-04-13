/*
 * Copyright (c) 2022. CMPUT301W22T29
 * Subject to MIT License
 * See full terms at https://github.com/CMPUT301W22T29/QuiRky/blob/main/LICENSE
 */

package com.example.quirky;

import org.osmdroid.util.GeoPoint;

/**
 * Mock class for developing without internet. Obviously can't access the real database with no wifi.
 */
public class MockDatabaseController extends DatabaseController {

    public void MockReadUser(String username, ListeningList<Profile> list) {
        Profile p = new Profile(username);
        p.addScanned("0xabcd1234");
        p.setPhone("123-321-1234");
        p.setEmail("abc@123.z9");

        list.add(p);
    }

    public void MockReadQRCode(String id, ListeningList<QRCode> list) {
        QRCode qr = new QRCode("content");
        qr.addScanner("bob");
        qr.addComment(new Comment("Bob said this", "bob"));
        qr.addLocation(new GeoPoint(10.0, 10.0, 10.0));

        list.add(qr);
    }

    public void MockIsOwner(String username, ListeningList<Boolean> list) {
        list.add(true);
    }
}
