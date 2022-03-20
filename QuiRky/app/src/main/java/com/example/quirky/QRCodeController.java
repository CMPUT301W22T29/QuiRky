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

import android.content.Context;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * A controller class that computes data needed by the <code>QRCode</code> model.
 * <p>
 * Constructs <code>QRCode</code>s from <code>InputImage</code>s, computes a SHA256 hash of a string, and scores a string.
 *
 * @author Sean Meyers
 * @author Jonathen Adsit
 * @version 0.2.1
 * @see androidx.camera.core
 * @see CameraController
 * @see CodeScannerActivity
 * @see com.google.mlkit.vision
 * @see QRCode
 */
public class QRCodeController {
    private static final BarcodeScanner codeScanner = BarcodeScanning.getClient(
            new BarcodeScannerOptions.Builder().setBarcodeFormats(Barcode.FORMAT_QR_CODE).build());

    /**
     * Analyzes an image for qr codes, and constructs <code>QRCode</code>s from their data.
     *
     * @param inputImage
     *      - The image to analyze.
     * @param codes
     *      - The list in which the <code>QRCode</code>s will be stored once they are constructed.
     * @param context
     *      - The activity that the user is interacting with to capture QR code images.
     * @see CameraController
     */
    public static void scanQRCodes(InputImage inputImage, ArrayList<QRCode> codes, Context context) {
        Task<List<Barcode>> result = codeScanner.process(inputImage)
                .addOnSuccessListener(barcodes -> {
                    // Construct a QRCode with the scanned raw data
                    for (Barcode barcode: barcodes) {
                        codes.add(new QRCode(barcode.getRawValue()));
                    }
                    if (codes.size() == 0) {
                        String text
                                = "Could not find any QR codes. Move closer or further and try scanning again.";
                        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(context, "Scanned " + codes.size() + " code(s)!",
                                                                          Toast.LENGTH_LONG).show();
                    }
                });
    }
    
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
            byte[] temp = md.digest(content.getBytes(StandardCharsets.UTF_8));

            // return Arrays.toString(temp);
            // return new String(temp, StandardCharsets.US_ASCII);
            return new String(temp, StandardCharsets.UTF_8);    //See: https://utf8-chartable.de/unicode-utf8-table.pl, should have a character for negative byte values
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
        return -(sum % 100); // Actually, wouldn't returning just the sum w/out the modulo be better,
                          // that way our leaderboard isn't saturated with a bunch of 99s or whatever
    }
}
