/*
 * CodeScannerActivity.java
 *
 * Version 0.2.0
 * Version History:
 *      Version 0.1.0 -- Camera Previewing Works
 *      Version 0.2.0 -- QR Code Photo Capturing Works
 *
 * Date (v0.2.0): March 14, 2022
 *
 * Copyright (c) 2022. CMPUT301W22T29
 * Subject to MIT License
 * See full terms at https://github.com/CMPUT301W22T29/QuiRky/blob/main/LICENSE
 */

package com.example.quirky;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.view.PreviewView;

import com.google.firebase.firestore.FirebaseFirestore;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;

/**
 * Previews camera feed and allows scanning QR codes.
 * <p>
 * Known Issues:
 *      Captures QR codes, but does nothing with them yet. (v0.2.0)
 *
 * @author Sean Meyers
 * @version 0.2.0
 * @see androidx.camera.core
 * @see CameraController
 * @see QRCode
 * @see QRCodeController
 */
public class CodeScannerActivity extends AppCompatActivity {
    private PreviewView previewView;
    private Button scan_button;

    private CameraController cameraController;

    private final String TAG = "CodeScannerActivity says";

    @Override
    @androidx.camera.core.ExperimentalGetImage
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_scanner);

        previewView = findViewById(R.id.previewView);
        scan_button = findViewById(R.id.scan_button);

        cameraController = new CameraController(this);
        cameraController.startCamera(previewView.createSurfaceProvider(), this);
        scan_button.setOnClickListener(view -> {
            scan();
        });
    }

    @androidx.camera.core.ExperimentalGetImage
    public void scan() {
        ArrayList<QRCode> results = cameraController.captureQRCodes(this);
        if(results.size() == 0) {
            Log.d(TAG, "No QRCodes were scanned");
            return;
        } else if (results.size() > 1) {
            Log.d(TAG, "Multiple QRCodes were scanned?");
            return;
        }

        scan_button.setVisibility(View.INVISIBLE);

        // TODO: Find a way to get the Image taken from the CameraController. Static Public Image in Cameracontroler?
        // TODO: Get the current location of the phone
        save(results.get(0), null, null);

    }

    public void save(QRCode qr, GeoPoint gp, Bitmap image) {
        DatabaseController dc = new DatabaseController(FirebaseFirestore.getInstance(), this);
        dc.writeQRCode(qr);

        if(gp != null) {
            // dc.saveLocation(qrcode, location);
        }
        if(image != null) {
            // dc.saveImage(qrcode, image);
        }
    }
}
