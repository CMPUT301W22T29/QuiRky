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

public class StartingPageActivity extends AppCompatActivity
                                      implements ActivityCompat.OnRequestPermissionsResultCallback {
    private Button QRButton, ProfileButton, CommunityButton;
    private Button top, mid, bottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting_page);

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

    private void startCodeScannerActivity() {
        assert CameraController.hasCameraPermission(this);
        Intent intent = new Intent(this, CodeScannerActivity.class);
        startActivity(intent);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                                                      @NonNull int[] grantResults) {
        if (CameraController.requestingCameraPermissions(requestCode)) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCodeScannerActivity();
            } else {
                Toast.makeText(this,"Please allow camera permissions to scan QR codes.",
                                                                          Toast.LENGTH_LONG).show();
            }
        }
    }

    private void setQRlayout() {
        top.setText("Manage Codes");
        top.setOnClickListener(view -> {
            Intent i = new Intent(this, MainActivity.class);    // TODO: implement activity that views player's qr codes
            QRCode qr = new QRCode("this is my content");
            i.putExtra("code", qr);
            startActivity(i);
        });

        mid.setText("Scan Codes");
        mid.setOnClickListener(view -> {
            if (CameraController.hasCameraPermission(this)) {
                startCodeScannerActivity();
            } else {
                CameraController.requestCameraPermission(this);
            }
        });

        bottom.setText("Generate Codes");
        bottom.setOnClickListener(view -> {
            Intent i = new Intent(this, GenerateActivity.class);    // TODO: implement generate qrcodes activity
            startActivity(i);
        });
    }

    private void setProfileLayout() {
        top.setText("My Profile");
        top.setOnClickListener(view -> {

            MemoryController mc = new MemoryController(this);
            Profile p = mc.read();

            Intent i = new Intent(this, ProfileViewerActivity.class);
            i.putExtra("profile", p);
            startActivity(i);
        });

        mid.setText("My Stats");
        mid.setOnClickListener(view -> {
            Intent i = new Intent(this, MyStatsActivity.class);    // TODO: implement the activity this should direct to
            startActivity(i);
        });

        bottom.setText("My QR Codes");
        bottom.setOnClickListener(view -> {
            Intent i = new Intent(this, MainActivity.class);    // TODO: implement the activity this should direct to
            startActivity(i);
        });
    }

    private void setCommunityLayout() {
        top.setText("Search Other Users");
        top.setOnClickListener(view -> {
            Intent i = new Intent(this, PlayerSearchActivity.class);
            startActivity(i);
        });

        mid.setText("The Leaderboards");
        mid.setOnClickListener(view -> {
            Intent i = new Intent(this, MainActivity.class);    // TODO: implement the activity this should direct to
            startActivity(i);
        });

        bottom.setText("Nearby QR Codes");
        bottom.setOnClickListener(view -> {
            Intent i = new Intent(this, MapActivity.class);
            startActivity(i);
        });
    }
}