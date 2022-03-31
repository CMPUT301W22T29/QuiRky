package com.example.quirky;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
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

        /* Temporary code for debugging login functionality.
        QRCode qr = new QRCode("abc123");
        dm.writeLoginHash(qr.getId(), "JJ");
        */

        Button getStarted = findViewById(R.id.getStarted);
        Button settings = findViewById(R.id.setting);
        Button quit = findViewById(R.id.quit);

        getStarted.setOnClickListener(view -> login());
        settings.setOnClickListener(view -> startSettingsActivity());
        quit.setOnClickListener(view -> finishAffinity());
    }

    private void login() {
        // mc.exists() checks if the user has logged in with the app before
        if(!mc.exists()) {
            InputUnameLoginFragment frag = new InputUnameLoginFragment();
            frag.show(getSupportFragmentManager(), "GET_UNAME");
            // Once the fragment is closed, the method OnClickConfirm() is called.
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
        dm.readProfile(uname).addOnCompleteListener(task -> {
            if(!task.isSuccessful()) {
                task.getException().printStackTrace();
            } else {
                if(dm.getProfile(task) != null) {
                    Toast.makeText(this, "This username already exists!", Toast.LENGTH_LONG).show();
                    // Restart the process by calling login()
                    login();
                } else {
                    Profile p = new Profile(uname);

                    mc.write(p);
                    mc.writeUser(uname);
                    dm.writeProfile(p);

                    startHubActivity();
                }
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

    private void startSettingsActivity() {
        Intent i = new Intent(this, SettingsActivity.class);
        startActivity(i);
    }
    
    private void startHubActivity() {
        Intent i = new Intent(this, StartingPageActivity.class);
        startActivity(i);
    }
}
