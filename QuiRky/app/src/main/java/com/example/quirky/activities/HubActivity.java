/*
 * Copyright (c) 2022. CMPUT301W22T29
 * Subject to MIT License
 * See full terms at https://github.com/CMPUT301W22T29/QuiRky/blob/main/LICENSE
 */

package com.example.quirky.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quirky.AdapterButton;
import com.example.quirky.AdapterPhoto;
import com.example.quirky.ListeningList;
import com.example.quirky.OnAddListener;
import com.example.quirky.R;
import com.example.quirky.RecyclerClickerListener;
import com.example.quirky.controllers.CameraActivitiesController;
import com.example.quirky.controllers.DatabaseController;
import com.example.quirky.controllers.MemoryController;
import com.example.quirky.models.Profile;

import java.util.ArrayList;

/**
 * Hub-Style Activity that directs to all the other activities
 */
public class HubActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private CameraActivitiesController cameraActivitiesController;
    private MemoryController mc;
    private DatabaseController dc;

    private ArrayList<String> features;
    private ListeningList<Drawable> photos;
    private boolean owner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hub);

        cameraActivitiesController = new CameraActivitiesController(this, false);
        mc = new MemoryController(this);
        dc = new DatabaseController();
        features = new ArrayList<>();

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

        photos = new ListeningList<>();
        photos.setOnAddListener(new OnAddListener<Drawable>() {
            @Override
            public void onAdd(ListeningList<Drawable> listeningList) {
                doneRead();
            }
        });

    }

    private void doneRead() {
        RecyclerView PhotoList = findViewById(R.id.hub_photo_list);
        RecyclerView FeatureList = findViewById(R.id.hub_feature_list);

        AdapterPhoto adapterPhoto = new AdapterPhoto(photos, this);
        PhotoList.setAdapter(adapterPhoto);
        PhotoList.setLayoutManager( adapterPhoto.getLayoutManager() );

        features.add("Scan Codes!");
        features.add("Make some QRs!");
        features.add("My QRCodes");
        features.add("My Profile");
        features.add("The Leaderboards!");
        features.add("Find Nearby QRCodes!");
        features.add("Search Other Players");

        RecyclerClickerListener listener = position -> StartActivity( features.get(position) );
        AdapterButton adapterButton = new AdapterButton(features, this, listener);
        FeatureList.setAdapter( adapterButton );
        FeatureList.setLayoutManager( adapterButton.getLayoutManager() );
    }

    private void addOwnerButtons() {
        features.add("Delete Players");
        features.add("Delete QRCodes");
    }

    private void StartActivity(String feature) {
        Intent i;
        Profile p = mc.read();
        switch (feature) {
            case "Scan Codes!" :
                i = new Intent(this, CodeScannerActivity.class);
                break;
            case "Make some QRs!" :
                i = new Intent(this, GenerateActivity.class);
                break;
            case "My QRCodes" :
                i = new Intent(this, ManageCodesActivity.class);
                i.putExtra("profile", p);
                break;
            case "My Profile" :
                i = new Intent(this, ProfileActivity.class);
                i.putExtra("profile", p);
                break;
            case "The Leaderboards!":
                i = new Intent(this, LeaderBoardActivity.class);
                break;
            case "Find Nearby QRCodes!":
                i = new Intent(this, MapActivity.class);
                break;
            case "Search Other Players":
                i = new Intent(this, PlayerSearchActivity.class);
                break;
            case "Delete Players" :
                i = new Intent(this, DeletePlayersActivity.class);
                break;
            case "Delete QRCodes" :
                i = new Intent(this, DeleteCodesActivity.class);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + feature);
        }

        startActivity(i);
    }
}