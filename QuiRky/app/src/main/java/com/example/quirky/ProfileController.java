/*
 * Copyright (c) 2022. CMPUT301W22T29
 * Subject to MIT License
 * See full terms at https://github.com/CMPUT301W22T29/QuiRky/blob/main/LICENSE
 */

package com.example.quirky;

import android.util.Log;

import java.util.ArrayList;

/**
 * Class to perform the calculations that a profile needs, as per MVC.
 */
public class ProfileController {

    private static String TAG = "Profile Controller Says ";

    /**
     * Calculate the total number of QRCodes a profile has scanned
     * @param p The profile in question
     * @return The number of QRCodes the player scanned
     */
    public static int calculateTotalScanned(Profile p) {
        ArrayList<String> codes = p.getScanned();
        return codes.size();
    }

    /**
     * Calculate the points a player has earned through scanning QRCodes
     * @param p The profile in question
     * @return The sum of points of all the player's scanned qrcodes
     */
    public static int calculateTotalPoints(Profile p) {
        ArrayList<String> codes = p.getScanned();
        int sum = 0;

        for(String id : codes) {
            sum += QRCodeController.score(id);
        }
        return sum;
    }

    /**
     * Find the greatest scoring QRCode the player has scanned
     * @param p The profile in question
     * @return The points of the highest scoring qrcode the player has scanned
     */
    public static int calculateGreatestScore(Profile p) {
        ArrayList<String> codes = p.getScanned();
        int largest = 0;

        for(String id : codes) {
            if(QRCodeController.score(id) > largest) {
                largest = QRCodeController.score(id);
            }
        }
        return largest;
    }
}
