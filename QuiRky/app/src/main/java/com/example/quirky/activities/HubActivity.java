/*
 * Copyright (c) 2022. CMPUT301W22T29
 * Subject to MIT License
 * See full terms at https://github.com/CMPUT301W22T29/QuiRky/blob/main/LICENSE
 */

package com.example.quirky.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.quirky.R;
import com.example.quirky.controllers.CameraActivitiesController;

/**
 * Hub-Style Activity that directs to all the other activities
 */
public class HubActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private CameraActivitiesController cameraActivitiesController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hub);

        cameraActivitiesController = new CameraActivitiesController(this, false);
    }
}