/*
 * Copyright (c) 2022. CMPUT301W22T29
 * Subject to MIT License
 * See full terms at https://github.com/CMPUT301W22T29/QuiRky/blob/main/LICENSE
 */

package com.example.quirky.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quirky.AdapterButton;
import com.example.quirky.AdapterPhoto;
import com.example.quirky.ListeningList;
import com.example.quirky.R;
import com.example.quirky.RecyclerClickerListener;
import com.example.quirky.controllers.CameraActivitiesController;
import com.example.quirky.controllers.DatabaseController;
import com.example.quirky.controllers.MapController;
import com.example.quirky.controllers.MemoryController;
import com.example.quirky.models.Profile;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Hub-Style Activity that directs to all the other activities
 */
public class HubActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private CameraActivitiesController cac;
    private MemoryController mc;
    private DatabaseController dc;

    private static final String[] button_texts = {"Scan Codes!", "Make some QRs!", "My QRCodes", "My Profile", "The Leaderboards!", "Find Nearby QRCodes!", "Search Other Players", "Logout"};
    private ArrayList<String> features;
    private ListeningList<Bitmap> photos;
    private AdapterButton adapterButton;
    private boolean owner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hub);

        cac = new CameraActivitiesController(this, false);
        mc = new MemoryController(this);
        dc = new DatabaseController();
        features = new ArrayList<>( Arrays.asList(button_texts) );

        photos = new ListeningList<>();
        photos.setOnAddListener(listeningList -> doneRead());

        dc.getRecentPhotos(photos);
    }

    /**
     * Called once done reading from firebase. Finishes setting up the recycler views
     */
    private void doneRead() {
        if(photos.size() == 0)
            photos.addWithoutListener(BitmapFactory.decodeResource( getResources(), R.drawable.no_photos_backup) );

        RecyclerView PhotoList = findViewById(R.id.hub_photo_list);
        RecyclerView FeatureList = findViewById(R.id.hub_feature_list);

        AdapterPhoto adapterPhoto = new AdapterPhoto(photos, this);
        PhotoList.setAdapter(adapterPhoto);
        PhotoList.setLayoutManager( adapterPhoto.getLayoutManager() );

        RecyclerClickerListener listener = position -> StartActivity( features.get(position) );
        adapterButton = new AdapterButton(features, this, listener);
        FeatureList.setAdapter( adapterButton );
        FeatureList.setLayoutManager( adapterButton.getLayoutManager() );

        ListeningList<Boolean> ownerResult = new ListeningList<>();
        ownerResult.setOnAddListener(listeningList -> {
            owner = listeningList.get(0);
            if(owner)
                addOwnerButtons();

            ProgressBar loadbar = findViewById(R.id.hub_load_bar);
            loadbar.setVisibility(View.GONE);
        });
        dc.isOwner( mc.readUser(), ownerResult);
    }

    /**
     * Add Delete Players and Delete QRCode buttons to the list of scrollable buttons
     */
    private void addOwnerButtons() {
        features.add("Delete Players");
        features.add("Delete QRCodes");
        adapterButton.notifyItemInserted(8);
        adapterButton.notifyItemInserted(9);
    }

    /**
     * Start a new activity, or logout of the app. Called when one of the feature buttons is clicked on.
     * @param feature The text of the button that was clicked on.
     *                TODO: see CMPUT301 slides, the factory design pattern may simplify the code below. It deals with long switch statements or if statements.
     */
    private void StartActivity(String feature) {
        Intent i;
        Profile p = mc.read();
        switch (feature) {
            case "Scan Codes!":
                cac.startCodeScannerActivity();
                break;

            case "Make some QRs!" :
                i = new Intent(this, GenerateActivity.class);
                startActivity(i);
                break;

            case "My QRCodes" :
                i = new Intent(this, ManageCodesActivity.class);
                i.putExtra("profile", p);
                startActivity(i);
                break;

            case "My Profile" :
                i = new Intent(this, ProfileActivity.class);
                i.putExtra("profile", p);
                startActivity(i);
                break;

            case "The Leaderboards!":
                i = new Intent(this, LeaderBoardActivity.class);
                startActivity(i);
                break;

            case "Find Nearby QRCodes!":
                if (MapController.hasLocationPermissions(this)) {
                    i = new Intent(this, MapActivity.class);
                    startActivity(i);
                } else
                    MapController.requestLocationPermission(this);
                break;

            case "Search Other Players":
                i = new Intent(this, PlayerSearchActivity.class);
                startActivity(i);
                break;

            case "Delete Players" :
                i = new Intent(this, DeletePlayersActivity.class);
                startActivity(i);
                break;

            case "Delete QRCodes" :
                i = new Intent(this, DeleteCodesActivity.class);
                startActivity(i);
                break;

            case "Logout" :
                mc.deleteUser();
                i = new Intent(this, LoginActivity.class);
                finish();
                startActivity(i);
                break;
        }
    }
}