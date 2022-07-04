/*
 * Copyright (c) 2022. CMPUT301W22T29
 * Subject to MIT License
 * See full terms at https://github.com/CMPUT301W22T29/QuiRky/blob/main/LICENSE
 */

package com.example.quirky.models;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * WIP class who's purpose is to separate stuff that should be unique and individual to the instance
 * of a qr code scanned by a user from stuff that should be shared between users at all times.
 * For now, this is mainly being used as a helper to get a single GeoLocation of a code for the
 * purpose of populating the map with nearby qr codes.
 * It can also be used later on so that each individual can name their qr code what they want to and
 * such. Also, it can be used to distinguish a user's photo's from other user's so that the photo's
 * most relevant to the user can be displayed first as the main thumbnail for their version of the
 * qr code.
 * TODO!
 */
public class UserOwnedQRCode {
    private String id; // id is hash of content, references its global counterpart.
    private GeoLocation location;
    private String scanner; // User who scanned this code.
    private String title; // User created title for the QRCode.
    private Bitmap photo; // user's photo, could make this a list if we want user's to be able to add several pics.

    public UserOwnedQRCode() {

    }

    public UserOwnedQRCode(String id, GeoLocation location) {
        this.id = id;
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public GeoLocation getLocation() {
        return location;
    }


}
