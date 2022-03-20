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

    // rankingScanned: Leaderboard ranking of number of QRCodes scanned
    // rankingPoints: Leaderboard ranking of number of points accumulated
    // rankingBiggestCode: Leaderboard ranking of largest single QRCode scanned
    private int rankingScanned, rankingPoints, rankingBiggestCode;

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

        this.rankingScanned = -1;
        this.rankingPoints = -1;
        this.rankingBiggestCode = -1;
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

        this.rankingScanned = -1;
        this.rankingPoints = -1;
        this.rankingBiggestCode = -1;
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
     * Getter for getRankingScanned
     * @return The position of the player in the global leaderboards for number of QRCodes scanned
     */
    public int getRankingScanned() {
        return rankingScanned;
    }

    /**
     * Setter for rankingScanned
     * @param rankingScanned The new rank in the leadboard
     */
    public void setRankingScanned(int rankingScanned) {
        this.rankingScanned = rankingScanned;
    }

    /**
     * Getter for rankingPoints
     * @return The profile's rankingPoints
     */
    public int getRankingPoints() {
        return rankingPoints;
    }

    /**
     * Setter for rankingPoints
     * @param rankingPoints The profile's new rank
     */
    public void setRankingPoints(int rankingPoints) {
        this.rankingPoints = rankingPoints;
    }

    /**
     * Getter for ranking in largest scoring QRCode scanned
     * @return Player's rank
     */
    public int getRankingBiggestCode() {
        return rankingBiggestCode;
    }

    /**
     * Setter for ranking in largest scoring QRCode scanned
     * @param rankingBiggestCode Player's new rank
     */
    public void setRankingBiggestCode(int rankingBiggestCode) {
        this.rankingBiggestCode = rankingBiggestCode;
    }

    /**
     * Constructor from a Parcel. Typically used when passing between activities
     * @param in A parcel containing a profile
     */
    protected Profile(Parcel in) {
        uname = in.readString();
        email = in.readString();
        phone = in.readString();
        rankingScanned = in.readInt();
        rankingPoints = in.readInt();
        rankingBiggestCode = in.readInt();
        scanned = in.createStringArrayList();
    }
}
