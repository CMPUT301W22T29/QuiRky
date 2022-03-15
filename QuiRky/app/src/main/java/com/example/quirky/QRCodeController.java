package com.example.quirky;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * @author Jonathen Adsit
 * A controller class to compute the data and do the work that QRCode needs. Currently computes a SHA256 hash of a string, and scores a string.
 * TODO: Sean put your @author name in this javadoc once you write the functions you were planning on putting here
 *
 */
public class QRCodeController {
    // TODO: Everything.

    // TODO: get qr code stuff from input image ();

    /**
     * Returns the SHA-256 Hash of a string as a string
     * @param content
     *      - The string to be hashed
     * @return
     *      - The hash, stored as a string.
     */
    public static String SHA256(String content) {
        try {
            // MessageDigest code taken from
            // https://stackoverflow.com/a/5531479
            // By user:
            // https://stackoverflow.com/users/22656/jon-skeet
            // Published
            // April 3, 2011
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(content.getBytes(StandardCharsets.UTF_8));

            return Arrays.toString(hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return ""; // This return statement should never be used, but android studio is complaining so here it is anyways.
    }

    /**
     * Returns the SHA-256 Hash of a string as an integer
     * @param content
     *      - The string to be hashed
     * @return
     *      - The hash, stored as a integer.
     * @deprecated
     *      - Decided that we wanted to store the hash as either a byte[] or a String, not a int or long.
     */
    public static int iSHA256(String content) {
        try {
            // MessageDigest code taken from
            // https://stackoverflow.com/a/5531479
            // By user:
            // https://stackoverflow.com/users/22656/jon-skeet
            // Published
            // April 3, 2011
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(content.getBytes(StandardCharsets.UTF_8));

            return Integer.valueOf(Arrays.toString(hash), 16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return 0; // This return statement should never be used, but android studio is complaining so here it is anyways.
    }


    /**
     * Calculate the score of a hash.
     * Algorithm works as follows: The ASCII value of each character in the string is summed together, and then modulo 100.
     * @param hash
     *      - The string to be scored
     * @return
     *      - The score of the hash
     */
    public static int score(String hash) {
        int sum = 0;
        for(int i = 0; i < hash.length(); i++)
            sum += hash.charAt(i);
        return sum%100;
    }
}
