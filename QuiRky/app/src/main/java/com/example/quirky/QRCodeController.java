/*
 * QRCodeController.java
 *
 * Version 0.2.0
 * Version History:
 *      Version 0.1.0 -- QRCodes can be constructed from input images
 *      Version 0.2.0 -- Can compute hashes and scores from strings
 *      Version 0.2.1 -- Scanning qr codes now pops toast with info about the scan results.
 *
 * Date (v0.2.1): March 19, 2022
 *
 * Copyright (c) 2022. CMPUT301W22T29
 * Subject to MIT License
 * See full terms at https://github.com/CMPUT301W22T29/QuiRky/blob/main/LICENSE
 */

package com.example.quirky;

import android.graphics.Bitmap;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * A controller class that computes data needed by the <code>QRCode</code> model.
 * <p>
 * Constructs images of QRCodes, hashes Strings, and scores Strings
 *
 * @author Sean Meyers
 * @author Jonathen Adsit
 * @version 0.2.1
 * @see QRCode
 */
public class QRCodeController {
    /**
     * Returns the SHA-256 Hash of a string as a string
     * @param content
     *      - The string to be hashed
     * @return
     *      - The hash, stored as a string.
     */
    public static String SHA256(String content) {
        // MessageDigest code taken from
        // https://stackoverflow.com/a/5531479
        // By user:
        // https://stackoverflow.com/users/22656/jon-skeet
        // Published
        // April 3, 2011
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(content.getBytes(StandardCharsets.UTF_8));

            // The byte[] is converted into a String using the UTF-8 character set.
            // In Firestore, it is illegal for Documents and Collections to have '/' or '.' in their ID
            // So the byte[] is parsed for these characters before it is turned to a string.
            // This reduces the number of unique ID's, but only by a small amount.
            for(int i = 0; i < hash.length; i++) {
                if(hash[i] == 0x2f)   // 0x2f -> '/'
                    hash[i] = 0x30;
                if(hash[i] == 0x2e)   // 0x2e -> '.'
                    hash[i] = 0x30;
            }

            // byte[] -> String using UTF_8 because that's the character set FireStore document names can use
            return new String(hash, StandardCharsets.UTF_8);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage(), e.getCause());
        }
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
        for (int i = 0; i < hash.length(); i++) {
            sum += hash.charAt(i);
        }
        return sum;
    }


    /**
     * Create a random string of specified length
     * @param length
     *      - The length of the string to generate
     * @return A random string
     */
    public static String getRandomString(int length){

        StringBuilder val = new StringBuilder();
        Random random = new Random();
        String finalString;
        for (int i = 0; i<length;i++){
            int chatTypa = random.nextInt(3);
            switch (chatTypa){
                case 0:
                    val.append(random.nextInt(10));
                    break;
                case 1:
                    val.append((char) (random.nextInt(26)+97));
                    break;
                //capital
                case 2:
                    val.append((char)(random.nextInt(26)+65));
            }
        }
        finalString = val.toString();
        return  finalString;
    }

    /**
     * Generate a Bitmap of a QRCode based off a string
     * @param text
     *      - The generated qr code based on this string
     */
    public static Bitmap generateQR(String text){


        Bitmap generatedQRCode = null;
        MultiFormatWriter writer = new MultiFormatWriter();
        try {
            BitMatrix matrix = writer.encode(text, BarcodeFormat.QR_CODE, 400, 400);
            BarcodeEncoder encoder = new BarcodeEncoder();
            Bitmap bitmap = encoder.createBitmap(matrix);
            generatedQRCode = bitmap;
        }catch (WriterException e)
        {
            e.printStackTrace();
        }
        return generatedQRCode;
    }
}