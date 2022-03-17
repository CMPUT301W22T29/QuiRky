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

    public static ArrayList<QRCode> scanQRCodes(InputImage inputImage) throws NoSuchAlgorithmException {
        Log.d("scanQRCode", "enter method");    //TODO: get rid of.
        ArrayList<QRCode> codes = new ArrayList<>();
        Task<List<Barcode>> result = codeScanner.process(inputImage).addOnSuccessListener(barcodes -> {
            Log.d("scanQRCode", "onSuccess");   //TODO: get rid of.
            // Construct a QRCode with the scanned raw data
            for (Barcode barcode: barcodes) {
                try {
                    codes.add(new QRCode(barcode.getRawValue()));
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e.getMessage(), e.getCause());
                }
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
    public static byte[] SHA256(String content) throws NoSuchAlgorithmException {
        // MessageDigest code taken from
        // https://stackoverflow.com/a/5531479
        // By user:
        // https://stackoverflow.com/users/22656/jon-skeet
        // Published
        // April 3, 2011
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        return md.digest(content.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Calculate the score of a hash.
     * Algorithm works as follows: The ASCII value of each character in the string is summed together, and then modulo 100.
     * @param hash
     *      - The string to be scored
     * @return
     *      - The score of the hash
     */
    public static int score(byte[] hash) {
        int sum = 0;
        for (byte eachByteIn: hash) {
            sum += (int) eachByteIn;
        }
        return -(sum % 100); // Actually, wouldn't returning just the sum w/out the modulo be better,
                          // that way our leaderboard isn't saturated with a bunch of 99s or whatever
    }
}
