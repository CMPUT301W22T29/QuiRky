/*
 * Copyright (c) 2022. CMPUT301W22T29
 * Subject to MIT License
 * See full terms at https://github.com/CMPUT301W22T29/QuiRky/blob/main/LICENSE
 */

package com.example.quirky;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

// Provides GUI utilities for Activities that can start other, camera using, Activities.

/**
 * @author Sean
 * @see CameraController
 * Class that manages starting up the CodeScannerActivity class. This is necessary because CodeScannerActivity uses the Camera, and thus needs Camera Permissions
 */
public class CameraActivitiesController {
    private AppCompatActivity currentActivity;
    private Boolean login;

    /**
     * Constructor that takes in the current activity and if the user is starting CodeScannerActivity because they want to log in with a QRCode
     * @param currentActivity The current activity
     * @param login Does the player want to use CodeScannerActivity to log in
     */
    public CameraActivitiesController(AppCompatActivity currentActivity, Boolean login) {
        this.currentActivity = currentActivity;
        this.login = login;
    }

    /**
     * Start the CodeScannerActivity if the app has permissions to use the camera. Otherwise, call other methods to get the permissions
     */
    public void startCodeScannerActivity() {
        if (CameraController.hasCameraPermission(currentActivity)) {
            Intent intent = new Intent(currentActivity, CodeScannerActivity.class);
            intent.putExtra("login", login);
            currentActivity.startActivity(intent);
        } else {
            CameraController.requestCameraPermission(currentActivity);
        }
    }

    /**
     * Get the result of CameraController requesting permissions
     * @param requestCode The request code of the Camera Permission request
     * @param grantResults The result of the permission request
     */
    public void getCameraPermissionRequestResult(int requestCode, @NonNull int[] grantResults) {
        if (CameraController.requestingCameraPermissions(requestCode)) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCodeScannerActivity();
            } else {
                Toast.makeText(currentActivity,
                        R.string.deny_camera_permissions_toast, Toast.LENGTH_LONG).show();
            }
        }
    }
}
