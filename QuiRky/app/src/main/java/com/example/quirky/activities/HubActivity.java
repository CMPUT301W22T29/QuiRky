/*
 * Copyright (c) 2022. CMPUT301W22T29
 * Subject to MIT License
 * See full terms at https://github.com/CMPUT301W22T29/QuiRky/blob/main/LICENSE
 */

package com.example.quirky.activities;

import android.content.DialogInterface;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.annotation.NonNull;
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

    private static final String[] button_texts = {"Scan Codes!", "Generate\nQRCode", "My QRCodes", "My Profile", "The\nLeaderboards!", "Map Activity", "Search\nPlayers", "Logout"};
    private static final String[] owner_texts = {"Delete\nPlayers", "Delete\nQRCodes"};
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

        RecyclerView FeatureList = findViewById(R.id.hub_feature_list);
        RecyclerClickerListener listener = position -> StartActivity( features.get(position) );
        adapterButton = new AdapterButton(features, this, listener);
        FeatureList.setAdapter( adapterButton );
        FeatureList.setLayoutManager( adapterButton.getLayoutManager() );

        dc.getRecentPhotos(photos);
    }

    /**
     * Called when a permissions dialogue started from the hub activity is resolved
     *
     * If permissions were granted, continues doing what the user was trying to do before the
     * permission request dialogue showed up.
     *
     * @param requestCode Internal number representing what is being requested
     * @param permissions
     * @param grantResults Array containing the results of one or more permission requests
     */
    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // Check the result of the permission request if camera permissions are being requested
        cac.getCameraPermissionRequestResult(requestCode, grantResults);

        // If location permissions are being requested
        if (MapController.requestingLocationPermissions(requestCode)) {

            // If location permissions were granted, start the map activity, else, don't.
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted!", Toast.LENGTH_SHORT).show();
                Intent in = new Intent(this, MapActivity.class);
                startActivity(in);
            } else {
                Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Called once done reading from firebase. Finishes setting up the recycler views
     */
    private void doneRead() {
        if(photos.size() == 0)
            photos.addWithoutListener(BitmapFactory.decodeResource( getResources(), R.drawable.no_recent_backup) );

        RecyclerView PhotoList = findViewById(R.id.hub_photo_list);

        AdapterPhoto adapterPhoto = new AdapterPhoto(photos, this);
        PhotoList.setAdapter(adapterPhoto);
        PhotoList.setLayoutManager( adapterPhoto.getLayoutManager() );

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
        features.add(owner_texts[0]);
        features.add(owner_texts[1]);
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
        
        if ( feature.equals(button_texts[0]) )
                cac.startCodeScannerActivity();

        else if ( feature.equals(button_texts[1]) )
                startGenerateActivity();

        else if ( feature.equals(button_texts[2]) )
        {
            i = new Intent(this, ManageCodesActivity.class);
            i.putExtra("profile", p);
            startActivity(i);
        }
        
        else if ( feature.equals(button_texts[3]) )
        {
            i = new Intent(this, ProfileActivity.class);
            i.putExtra("profile", p);
            startActivity(i);
        }

        else if ( feature.equals(button_texts[4]) )
        {
            i = new Intent(this, LeaderBoardActivity.class);
            startActivity(i);
        }

        else if ( feature.equals(button_texts[5]) )
        {
            if (MapController.hasLocationPermissions(this)) {
                i = new Intent(this, MapActivity.class);
                startActivity(i);
            } else
                MapController.requestLocationPermission(this);
        }
        
        else if ( feature.equals(button_texts[6]) )
        {
            i = new Intent(this, PlayerSearchActivity.class);
            startActivity(i);
        }
        
        else if ( feature.equals(button_texts[7]) )
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            DialogInterface.OnClickListener LogoutListener = (dialog, button) -> {
                if (button == DialogInterface.BUTTON_NEUTRAL)
                    startGenerateActivity();
                else if (button == DialogInterface.BUTTON_POSITIVE)
                    confirmLogout();
            };

            builder.setTitle("Logout?").setMessage("The only way to log back in is with a generated QRCode.\nAre you sure you would like to logout?");
            builder.setCancelable(true);
            builder.setNegativeButton("Cancel", LogoutListener);
            builder.setNeutralButton("Generate QRCode", LogoutListener);
            builder.setPositiveButton("Yes, Logout", LogoutListener);

            builder.create().show();
        }
        
        else if ( feature.equals(owner_texts[0]) )
        {
            i = new Intent(this, DeletePlayersActivity.class);
            startActivity(i);
        }

        else if ( feature.equals(owner_texts[1]) )
        {
            i = new Intent(this, DeleteCodesActivity.class);
            startActivity(i);
        }
    }

    private void startGenerateActivity() {
        Intent i = new Intent(this, GenerateActivity.class);
        startActivity(i);
    }


    public void confirmLogout() {
        mc.deleteUser();
        Intent i = new Intent(this, LoginActivity.class);
        finish();
        startActivity(i);
    }
}