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
//TODO javadocs
public class CameraActivitiesController {
    private AppCompatActivity currentActivity;
    private Boolean login;

    public CameraActivitiesController(AppCompatActivity currentActivity, Boolean login) {
        this.currentActivity = currentActivity;
        this.login = login;
    }

    public void startCodeScannerActivity() {
        if (CameraController.hasCameraPermission(currentActivity)) {
            Intent intent = new Intent(currentActivity, CodeScannerActivity.class);
            intent.putExtra("login", login);
            currentActivity.startActivity(intent);
        } else {
            CameraController.requestCameraPermission(currentActivity);
        }
    }

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
