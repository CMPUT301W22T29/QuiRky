package com.example.quirky;

import android.os.Parcel;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Jonathen Adsit
 * Model class that stores a user's name, contact information, and id's of the qrcodes they scanned
 */
public class Profile implements Serializable {
    private String uname, email, phone;

    // numberCodesScanned: the number of QRCodes the player has scanned
    // pointsOfScannedCodes: the sum of points of all QRCodes the player has scanned
    // pointsOfLargestCode: the points of the largest code the player has scanned
    private int numberCodesScanned, pointsOfScannedCodes, pointsOfLargestCodes;

    // Array list of QRCode id's to track which codes the user has scanned.
    private ArrayList<String> scanned;

    /**
     * Constructor to be used when all fields are known. Typically used when reading from the database, or getting the user's profile from memory
     * @param uname
     *      - The player's username
     * @param email
     *      - The player's email
     * @param phone
     *      - The player's phone number
     * @param scanned
     *      - The id's of QRCodes the player has scanned
     */
    public Profile(String uname, String email, String phone, ArrayList<String> scanned) {
        this.uname = uname;
        this.email = email;
        this.phone = phone;
        this.scanned = scanned;

        this.numberCodesScanned = ProfileController.calculateTotalScanned(this);
        this.pointsOfScannedCodes = ProfileController.calculateTotalPoints(this);
        this.pointsOfLargestCodes = ProfileController.calculateGreatestScore(this);
    }

    /**
     * Constructor to be used when only the username is known. Typically used when creating the user's profile upon logging in for the first time.
     * All other fields are set empty.
     * @param uname
     *      - The player's username.
     */
    public Profile(String uname) {
        this.uname = uname;
        this.scanned = new ArrayList<>();
        this.email = "";
        this.phone = "";

        this.numberCodesScanned = 0;
        this.pointsOfScannedCodes = 0;
        this.pointsOfLargestCodes = 0;
    }

    /**
     * Empty constructor because Firebase tutorial told me to...
     */
    public Profile() {}


    /**
     * Username getter
     * @return username
     */
    public String getUname() {
        return uname;
    }

    /**
     * Setter for username
     * @param uname New username
     */
    public void setUname(String uname) {
        this.uname = uname;
    }

    /**
     * Email getter
     * @return player's email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Email setter
     * @param email player's new email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Cell number getter
     * @return player's number
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Cell number setter
     * @param phone player's new phone number
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Getter for QRCodes scanned by player
     * @return ArrayList of QRCode id's scanned by the player
     */
    public ArrayList<String> getScanned() {
        return scanned;
    }

    /**
     * Adds a QRCode to the list of scanned QRCodes. Updates stats to reflect the added code.
     * Does nothing if the player already has this QRCode.
     * @param qrId The ID of the QRCode the player has scanned
     * @return Whether the QRCode was actually added or not.
     */
    public boolean addScanned(String qrId) {
        if(scanned.contains(qrId))
            return false;
        scanned.add(qrId);
        updateStats();
        return true;
    }

    /**
     * Removes a QRCode from the list of scanned QRCodes. Does nothing if the id is not in the array.
     * Also updates the profile's stats to reflect the added code.
     * @param qrId
     *      - The ID of the QRCode the player wants to delete
     */
    public void removeScanned(String qrId) {
        if(scanned.contains(qrId)) {
            scanned.remove(qrId);
            updateStats();
        }
    }

    /**
     * Getter for numberCodesScanned
     * @return The number of codes the player has scanned
     */
    public int getNumberCodesScanned() {
        return numberCodesScanned;
    }


    /**
     * Getter for pointsOfScannedCodes
     * @return The profile's sum of points
     */
    public int getPointsOfScannedCodes() {
        return pointsOfScannedCodes;
    }


    /**
     * Getter for pointsOfLargestCodes
     * @return Point value of player's largest QRCode
     */
    public int getPointsOfLargestCodes() {
        return pointsOfLargestCodes;
    }

    /**
     * Call the ProfileController to update the player's statistics.
     */
    private void updateStats() {
        this.numberCodesScanned = ProfileController.calculateTotalScanned(this);
        this.pointsOfScannedCodes = ProfileController.calculateTotalPoints(this);
        this.pointsOfLargestCodes = ProfileController.calculateGreatestScore(this);
    }
}
