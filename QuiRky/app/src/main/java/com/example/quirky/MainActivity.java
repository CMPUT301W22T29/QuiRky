package com.example.quirky;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * The activity that starts when the app is opened.
 * Provides an interface for the user to log into the app with.
 */
public class MainActivity extends AppCompatActivity implements
                                                 InputUnameLoginFragment.LoginFragListener,
                                                 ActivityCompat.OnRequestPermissionsResultCallback {

    DatabaseController dm;
    MemoryController mc;
    private CameraActivitiesController cameraActivitiesController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cameraActivitiesController = new CameraActivitiesController(this, true);

        dm = new DatabaseController(this);
        mc = new MemoryController(this);

        Button getStarted = findViewById(R.id.getStarted);
        Button quit = findViewById(R.id.quit);

        final boolean returningUser = mc.exists();
        if(returningUser) {
            String user = mc.readUser();

            dm.readProfile(user).addOnCompleteListener(task -> {
                if(dm.isOwner(task))
                    displayOwnerButton();
            });
        }

        getStarted.setOnClickListener(view -> login(returningUser));
        quit.setOnClickListener(view -> finishAffinity());
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
        dm.startCheckProfileExists(uname).addOnCompleteListener(task -> {
            if(dm.checkProfileExists(task)) {
                Profile p = new Profile(uname);

                mc.write(p);
                mc.writeUser(uname);
                dm.writeProfile(p);

                startHubActivity();

            } else {
                Toast.makeText(this, "This username already exists!", Toast.LENGTH_LONG).show();
                // Restart the process by calling login()
                login(false);
            }
        });
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
     * Makes the Owner Settings available. onCreate
     */
    private void displayOwnerButton() {
        Button ownerButton = findViewById(R.id.owner_button);
        ownerButton.setVisibility(View.VISIBLE);

        ownerButton.setOnClickListener(view -> {
            Intent i = new Intent(this, OwnerMenu.class);
            startActivity(i);
        });
    }

    /**
     * Launches StartingPageActivity
     */
    private void startHubActivity() {
        Intent i = new Intent(this, StartingPageActivity.class);
        startActivity(i);
    }
}
