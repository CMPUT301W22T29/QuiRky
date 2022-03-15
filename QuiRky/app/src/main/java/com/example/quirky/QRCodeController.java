/*
 * QRCodeController.java
 *
 * Version 0.2.0
 * Version History:
 *      Version 0.1.0 -- QRCodes can be constructed from input images
 *      Version 0.2.0 -- Can compute hashes and scores from strings
 *
 * Date (v0.2.0): March 14, 2022
 *
 * Copyright (c) 2022. CMPUT301W22T29
 * Subject to MIT License
 * See full terms at https://github.com/CMPUT301W22T29/QuiRky/blob/main/LICENSE
 */

package com.example.quirky;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

/**
 * A controller class that computes data needed by the <code>QRCode</code> model.
 * <p>
 * Constructs <code>QRCode</code>s from <code>InputImage</code>s, computes a SHA256 hash of a string, and scores a string.
 *
 * @author Sean Meyers
 * @author Jonathen Adsit
 * @version 0.2.0
 * @see androidx.camera.core
 * @see CameraController
 * @see CodeScannerActivity
 * @see com.google.mlkit.vision
 * @see QRCode
 */
public class QRCodeController {
    private static final BarcodeScanner codeScanner = BarcodeScanning.getClient(
            new BarcodeScannerOptions.Builder().setBarcodeFormats(Barcode.FORMAT_QR_CODE).build());

    public static ArrayList<QRCode> scanQRCodes(InputImage inputImage) {
        Log.d("scanQRCode", "enter method");    //TODO: get rid of.
        ArrayList<QRCode> codes = new ArrayList<>();
        Task<List<Barcode>> result = codeScanner.process(inputImage).addOnSuccessListener(barcodes -> {
            Log.d("scanQRCode", "onSuccess");   //TODO: get rid of.
            // Construct a QRCode with the scanned raw data
            for (Barcode barcode: barcodes) {
                codes.add(new QRCode(barcode.getRawValue()));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //TODO: Do something, possibly bring up a fragment saying to try retaking the picture.
                Log.d("scanQRCode", "onFailure");   //TODO: get rid of.
            }
        });
        Log.d("scanQRCode", "exit method"); //TODO: get rid of.
        return codes;
    }
    
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

    // FIXME: currently a ~50digit hash number is too large to represent as an integer, or even a long. How do we represent a hash as a number type?
    // TODO: In order to represent the hash as a number type, we need to use an array of bytes,
    //       probably the best way to do this is to use a fixed size byte[32] array. Could also use
    //       Byte[32] instead. I'm not sure which would be better, probably doesn't matter much.
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

    public int iScore(byte[] hash) {
        int sum = 0;
        for (byte eachByteIn: hash) {
            sum += (int) eachByteIn;
        }
        return sum % 100; // Actually, wouldn't returning just the sum w/out the modulo be better,
                          // that way our leaderboard isn't saturated with a bunch of 99s or whatever
    }
}
