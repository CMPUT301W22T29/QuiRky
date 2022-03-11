package com.example.quirky;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartingPageActivity extends AppCompatActivity {
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

    private void setQRlayout() {
        top.setText("Manage Codes");
        top.setOnClickListener(view -> {
            Intent i = new Intent(this, MainActivity.class);    // TODO: implement activity that views player's qr codes
            startActivity(i);
        });

        mid.setText("Scan Codes");
        mid.setOnClickListener(view -> {
            Intent i = new Intent(this, CodeScannerActivity.class);
            startActivity(i);
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
            Intent i = new Intent(this, MyProfile1.class);    // TODO: implement the activity this should direct to
            startActivity(i);
        });

        mid.setText("My Stats");
        mid.setOnClickListener(view -> {
            Intent i = new Intent(this, myStats.class);    // TODO: implement the activity this should direct to
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
            Intent i = new Intent(this, MainActivity.class);    // TODO: implement the activity this should direct to
            startActivity(i);
        });

        mid.setText("The Leaderboards");
        mid.setOnClickListener(view -> {
            Intent i = new Intent(this, MainActivity.class);    // TODO: implement the activity this should direct to
            startActivity(i);
        });

        bottom.setText("Nearby QR Codes");
        bottom.setOnClickListener(view -> {
            Intent i = new Intent(this, MainActivity.class);    // TODO: implement the activity this should direct to
            startActivity(i);
        });
    }
}