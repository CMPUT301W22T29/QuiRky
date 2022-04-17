/*
 * Copyright (c) 2022. CMPUT301W22T29
 * Subject to MIT License
 * See full terms at https://github.com/CMPUT301W22T29/QuiRky/blob/main/LICENSE
 */

package com.example.quirky.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quirky.ListeningList;
import com.example.quirky.OnAddListener;
import com.example.quirky.R;
import com.example.quirky.controllers.CameraActivitiesController;
import com.example.quirky.controllers.DatabaseController;
import com.example.quirky.controllers.MemoryController;

/**
 * Hub-Style Activity that directs to all the other activities
 */
public class HubActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private CameraActivitiesController cameraActivitiesController;
    private MemoryController mc;
    private DatabaseController dc;
    private boolean owner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hub);

        cameraActivitiesController = new CameraActivitiesController(this, false);
        mc = new MemoryController(this);
        dc = new DatabaseController();

        owner = false;
        ListeningList<Boolean> ownerResult = new ListeningList<>();
        ownerResult.setOnAddListener(new OnAddListener<Boolean>() {
            @Override
            public void onAdd(ListeningList<Boolean> listeningList) {
                owner = listeningList.get(0);
                if(owner)
                    addOwnerButtons();
            }
        });
        dc.isOwner( mc.readUser(), ownerResult);

        RecyclerView PhotoList = findViewById(R.id.hub_photo_list);

    }

    private void addOwnerButtons() {
    }
}