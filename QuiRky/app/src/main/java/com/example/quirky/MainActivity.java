package com.example.quirky;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.firebase.firestore.FirebaseFirestore;


import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;

/**
 * This is the activity that shows once the app is opened
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
        Button settings = findViewById(R.id.setting);
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
        settings.setOnClickListener(view -> startSettingsActivity());
        quit.setOnClickListener(view -> finishAffinity());
    }


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
     * This method is to let user to confirm the info after the user have wrote the info and starts the HubActivity
     * @param uname
     * User name which it stores
     */
    @Override
    public void OnClickConfirm(String uname) {
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

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                                                      @NonNull int[] grantResults) {
        cameraActivitiesController.getCameraPermissionRequestResult(requestCode, grantResults);
    }

    @Override
    public void LoginByQR() {
        cameraActivitiesController.startCodeScannerActivity();
    }

    private void displayOwnerButton() {
        Button ownerButton = findViewById(R.id.owner_button);
        ownerButton.setVisibility(View.VISIBLE);

        ownerButton.setOnClickListener(view -> {
            Intent i = new Intent(this, OwnerMenu.class);
            startActivity(i);
        });
    }

    private void startSettingsActivity() {
        Intent i = new Intent(this, SettingsActivity.class);
        startActivity(i);
    }
    
    private void startHubActivity() {
        Intent i = new Intent(this, StartingPageActivity.class);
        startActivity(i);
    }
}
