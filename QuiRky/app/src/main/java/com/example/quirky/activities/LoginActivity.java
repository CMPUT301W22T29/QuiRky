/*
 * Copyright (c) 2022. CMPUT301W22T29
 * Subject to MIT License
 * See full terms at https://github.com/CMPUT301W22T29/QuiRky/blob/main/LICENSE
 */

package com.example.quirky.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.quirky.InputUnameLoginFragment;
import com.example.quirky.ListeningList;
import com.example.quirky.controllers.MemoryController;
import com.example.quirky.OnAddListener;
import com.example.quirky.models.Profile;
import com.example.quirky.controllers.ProfileController;
import com.example.quirky.R;
import com.example.quirky.controllers.CameraActivitiesController;
import com.example.quirky.controllers.DatabaseController;

/**
 * The activity that starts when the app is opened.
 * Provides an interface for the user to log into the app with.
 */
public class LoginActivity extends AppCompatActivity implements
        InputUnameLoginFragment.LoginFragListener,
                                                 ActivityCompat.OnRequestPermissionsResultCallback {

    DatabaseController dm;
    MemoryController mc;
    private CameraActivitiesController cameraActivitiesController;
    private boolean returningUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        cameraActivitiesController = new CameraActivitiesController(this, true);

        dm = new DatabaseController();
        mc = new MemoryController(this);

        returningUser = mc.exists();

        Button getStarted = findViewById(R.id.getStarted);
        Button quit = findViewById(R.id.quit);

        getStarted.setOnClickListener(view -> login(returningUser));
        quit.setOnClickListener(view -> {
            finishAffinity();
            System.exit(0);
        });
    }


    /**
     * Called when the user clicks the login button.
     * Will either enter the app or launch a fragment to prompt the user for login details
     * @param returningUser If the user has logged in with the app before.
     */
    private void login(boolean returningUser) {
        if(!returningUser) {
            // Display a fragment to get a username from the user.
            // Once the fragment closes, the method OnClickConfirm() is called.
            InputUnameLoginFragment frag = new InputUnameLoginFragment();
            frag.show(getSupportFragmentManager(), "GET_UNAME");
        } else {
            startHubActivity();
        }
    }

    /**
     * Called by the listener in the login dialogue fragment, when the user clicks confirm.
     * Checks if the entered username is taken in the database already, and calls controllers to write the new profile to local memory and the database
     * @param uname
     * User name which it stores
     */
    @Override
    public void OnClickConfirm(String uname) {

        // Check if the inputted username is valid
        if(!ProfileController.validUsername(uname)) {
            Toast.makeText(this, "This username is not valid!", Toast.LENGTH_SHORT).show();
            login(false);
            return;
        }

        // Read from the database to check if this username is already taken.
        ListeningList<Profile> doesExist = new ListeningList<>();
        doesExist.setOnAddListener(listeningList -> {
            if(listeningList.size() == 0) {
                Profile p = new Profile(uname);

                mc.writeUser(p);
                dm.writeProfile(p);

                startHubActivity();

            } else {
                Toast.makeText(getApplicationContext(), "This username already exists!", Toast.LENGTH_LONG).show();
                // Restart the process by calling login()
                login(false);
            }
        });

        dm.readProfile(uname, doesExist);
    }

    /**
     * Uses CameraActivitiesController to do permissions things to launch the CodeScannerActivity
     */
    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                                                      @NonNull int[] grantResults) {
        cameraActivitiesController.getCameraPermissionRequestResult(requestCode, grantResults);
    }

    /**
     * Uses CameraActivityController to launch the CodeScannerActivity, if the user chooses to log in by QRCode scan.
     */
    @Override
    public void LoginByQR() {
        cameraActivitiesController.startCodeScannerActivity();
    }

    /**
     * Launches HubActivity
     */
    private void startHubActivity() {
        Intent i = new Intent(this, HubActivity.class);
        startActivity(i);
    }
}
