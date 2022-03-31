package com.example.quirky;

import android.os.Parcel;

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

        this.numberCodesScanned = -1;
        this.pointsOfScannedCodes = -1;
        this.pointsOfLargestCodes = -1;
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
     * Adds a QRCode to the list of scanned QRCodes
     * @param qrId
     *      - The ID of the QRCode the player has scanned
     */
    public void addScanned(String qrId) {
        scanned.add(qrId);
    }

    /**
     * Removes a QRCode from the list of scanned QRCodes. Does nothing if the id is not in the array.
     * @param qrId
     *      - The ID of the QRCode the player wants to delete
     */
    public void removeScanned(String qrId) {
        if(scanned.contains(qrId))
            scanned.remove(qrId);
    }

    /**
     * Getter for numberCodesScanned
     * @return The number of codes the player has scanned
     */
    public int getNumberCodesScanned() {
        return numberCodesScanned;
    }

    /**
     * Setter for numberCodesScanned
     * @param numberCodesScanned The number of codes the player has scanned in the leadboard
     */
    public void setNumberCodesScanned(int numberCodesScanned) {
        this.numberCodesScanned = numberCodesScanned;
    }

    /**
     * Getter for pointsOfScannedCodes
     * @return The profile's sum of points
     */
    public int getPointsOfScannedCodes() {
        return pointsOfScannedCodes;
    }

    /**
     * Setter for pointsOfScannedCodes
     * @param pointsOfScannedCodes The profile's sum of points
     */
    public void setPointsOfScannedCodes(int pointsOfScannedCodes) {
        this.pointsOfScannedCodes = pointsOfScannedCodes;
    }

    /**
     * Getter for pointsOfLargestCodes
     * @return Point value of player's largest QRCode
     */
    public int getPointsOfLargestCodes() {
        return pointsOfLargestCodes;
    }

    /**
     * Setter for pointsOfLargestCodes
     * @param pointsOfLargestCodes Point value of player's largest QRCode
     */
    public void setPointsOfLargestCodes(int pointsOfLargestCodes) {
        this.pointsOfLargestCodes = pointsOfLargestCodes;
    }

    /**
     * Constructor from a Parcel. Typically used when passing between activities
     * @param in A parcel containing a profile
     */
    protected Profile(Parcel in) {
        uname = in.readString();
        email = in.readString();
        phone = in.readString();
        numberCodesScanned = in.readInt();
        pointsOfScannedCodes = in.readInt();
        pointsOfLargestCodes = in.readInt();
        scanned = in.createStringArrayList();
    }
}
