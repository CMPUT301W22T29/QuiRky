package com.example.quirky;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

/**
 * Hub-Style Activity that directs to all the other activites
 */
public class StartingPageActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private Button QRButton, ProfileButton, CommunityButton;
    private Button top, mid, bottom;
    private CameraActivitiesController cameraActivitiesController;
    MemoryController mc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting_page);

        cameraActivitiesController = new CameraActivitiesController(this, false);

        QRButton = findViewById(R.id.hub_qr_codes);
        ProfileButton = findViewById(R.id.hub_profile_button);
        CommunityButton = findViewById(R.id.hub_community_button);

        top = findViewById(R.id.hub_button1);
        mid = findViewById(R.id.hub_button2);
        bottom = findViewById(R.id.hub_button3);

        QRButton.setOnClickListener(view -> setQRlayout());
        ProfileButton.setOnClickListener(view -> setProfileLayout());
        CommunityButton.setOnClickListener(view -> setCommunityLayout());

        setQRlayout();
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                                                      @NonNull int[] grantResults) {
        cameraActivitiesController.getCameraPermissionRequestResult(requestCode, grantResults);
        if (MapController.requestingLocationPermissions(requestCode)) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission Granted!", Toast.LENGTH_SHORT).show();
                Intent in = new Intent(this, MapActivity.class);
                startActivity(in);
            } else{
                Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Set the buttons & their listeners to show the QRCode layout
     */
    private void setQRlayout() {
        top.setText("Manage Codes");
        top.setOnClickListener(view -> {
            mc = new MemoryController(this);
            Intent i = new Intent(this, ManageCodesActivity.class);
            i.putExtra("profile", mc.read());
            startActivity(i);
        });

        mid.setText("Scan Codes");
        mid.setOnClickListener(view -> {
            cameraActivitiesController.startCodeScannerActivity();
        });

        bottom.setText("Generate Codes");
        bottom.setOnClickListener(view -> {
            Intent i = new Intent(this, GenerateActivity.class);
            startActivity(i);
        });
    }

    /**
     * Set the buttons & their listeners to show the Profile layout
     */
    private void setProfileLayout() {
        top.setText("My Profile");
        top.setOnClickListener(view -> {

            mc = new MemoryController(this);
            Profile p = mc.read();

            Intent i = new Intent(this, ProfileViewerActivity.class);
            i.putExtra("profile", p);
            startActivity(i);
        });

        mid.setText("My Stats");
        mid.setOnClickListener(view -> {

            MemoryController mc = new MemoryController(this);
            Profile p = mc.read();

            Intent i = new Intent(this, MyStatsActivity.class);
            i.putExtra("profile", p);
            startActivity(i);
        });

        bottom.setText("My QR Codes");
        bottom.setOnClickListener(view -> {
            mc = new MemoryController(this);
            Intent i = new Intent(this, ManageCodesActivity.class);
            i.putExtra("profile", mc.read());
            startActivity(i);
        });
    }

    /**
     * Set the buttons & their listeners to show the Community layout
     */
    private void setCommunityLayout() {
        top.setText("Search Other Users");
        top.setOnClickListener(view -> {
            Intent i = new Intent(this, PlayerSearchActivity.class);
            startActivity(i);
        });

        mid.setText("The Leaderboards");
        mid.setOnClickListener(view -> {
            Intent i = new Intent(this, LeaderBoardActivity.class);
            startActivity(i);
        });

        bottom.setText("Nearby QR Codes");
        bottom.setOnClickListener(view -> {
            if (MapController.hasLocationPermissions(this)) {
                Intent intent = new Intent(this, MapActivity.class);
                startActivity(intent);
            } else {
                MapController.requestLocationPermission(this);
            }
        });
    }
}