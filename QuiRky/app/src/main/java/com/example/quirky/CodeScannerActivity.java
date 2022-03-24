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

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.view.PreviewView;

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
    private CameraController cameraController;

    @Override
    @androidx.camera.core.ExperimentalGetImage
    protected void onCreate(Bundle savedInstanceState) {
        // TODO: Adjust the xml so that it matches the new fragment layout better (eg. there's currently two different back buttons)
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_scanner);
        previewView = findViewById(R.id.previewView);
        cameraController = new CameraController(this);
        cameraController.startCamera(previewView.createSurfaceProvider(), this);
        Button scan_button = findViewById(R.id.scan_button);
        scan_button.setOnClickListener(view -> {
            cameraController.captureQRCodes(this);
        });
    }
}
