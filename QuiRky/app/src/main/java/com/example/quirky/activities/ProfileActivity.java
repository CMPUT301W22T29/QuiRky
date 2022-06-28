/*
 * Copyright (c) 2022. CMPUT301W22T29
 * Subject to MIT License
 * See full terms at https://github.com/CMPUT301W22T29/QuiRky/blob/main/LICENSE
 */

package com.example.quirky.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quirky.ListeningList;
import com.example.quirky.OnAddListener;
import com.example.quirky.R;
import com.example.quirky.activities.EditProfileActivity;
import com.example.quirky.activities.ManageCodesActivity;
import com.example.quirky.controllers.DatabaseController;
import com.example.quirky.controllers.LeaderBoardController;
import com.example.quirky.controllers.MemoryController;
import com.example.quirky.models.Profile;

/**
 * Activity to view a profile
 */
public class ProfileActivity extends AppCompatActivity {

    private Profile p;
    private MemoryController mc;
    private ListeningList<Profile> readResults;

    // This button will either allow the user to change their profile, or direct to the profile's QRCode
    // Depending on if the user is viewing their own profile or someone else's.
    private Button button;
    private TextView UsernameTitleText, EmailText, PhoneText;
    private TextView PointsText, NumScannedText, LargestScannedText;
    private TextView PointsRankText, NumScannedRank, LargestScannedRank;

    boolean view_self; // Is the player viewing themself?

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        Intent i = getIntent();
        p = (Profile) i.getSerializableExtra("profile");
        if(p == null)
            ExitWithError();

        UsernameTitleText = findViewById(R.id.profile_name);
        EmailText = findViewById(R.id.email_text);
        PhoneText = findViewById(R.id.phone_text);

        PointsText = findViewById(R.id.points_stat_text);
        PointsRankText = findViewById(R.id.points_rank_text);

        NumScannedText = findViewById(R.id.scanned_stat_text);
        NumScannedRank = findViewById(R.id.scanned_rank_text);

        LargestScannedText = findViewById(R.id.largest_stat_text);
        LargestScannedRank = findViewById(R.id.largest_rank_text);

        button = findViewById(R.id.profile_button);


        DatabaseController dc = new DatabaseController();
        readResults = new ListeningList<>();
        readResults.setOnAddListener(listeningList -> doneReading());

        dc.readAllUsers("", readResults);
        mc = new MemoryController(this);

        // If the username in local memory matches the username passed to this activity,
        // then the user is viewing themself.
        view_self = ( mc.readUser() ).equals(p.getUname());
        if( view_self ) {
            button.setOnClickListener(view -> changeProfile());
            button.setText("Change Profile");
        } else {
            button.setOnClickListener(view -> viewQRCodes());
            button.setText("View QRCodes");
        }
    }

    private void doneReading() {
        LeaderBoardController lbc = new LeaderBoardController(readResults);
        int position;
        String text_builder;

        position = lbc.findRankPoints(p);
        text_builder = p.getPointsOfScannedCodes() + " pts";
        PointsText.setText( text_builder );
        text_builder = "Rank: " + (position + 1);
        PointsRankText.setText( text_builder );

        position = lbc.findRankScanned(p);
        text_builder = p.getNumberCodesScanned() + " codes scanned";
        NumScannedText.setText( text_builder );
        text_builder = "Rank: " + (position+1);
        NumScannedRank.setText( text_builder );

        position = lbc.findRankLargest(p);
        text_builder = p.getPointsOfLargestCodes() + " pts";
        LargestScannedText.setText( text_builder );
        text_builder = "Rank: " + (position+1);
        LargestScannedRank.setText( text_builder );
    }

    private void viewQRCodes() {
        Intent i = new Intent(this, ManageCodesActivity.class);
        i.putExtra("profile", p);
        startActivity(i);
    }

    // Displayed information must be updated onResume in the event that the user is returning from EditProfileActivity
    @Override
    public void onResume() {
        super.onResume();

        if(view_self)
            p = mc.read();

        UsernameTitleText.setText(p.getUname());
        EmailText.setText(p.getEmail());
        PhoneText.setText(p.getPhone());
    }

    /**
     * Start up an activity to let the user edit their profile
     */
    public void changeProfile() {
        Intent intent = new Intent(this, EditProfileActivity.class);
        intent.putExtra("profile", p);
        startActivity(intent);
    }

    /**
     * Method called when data is passed to this activity incorrectly, or when there is an issue reading the data from FireStore.
     * Makes a toast and then finishes the activity.
     */
    private void ExitWithError() {
        Toast.makeText(this, "User was passed incorrectly!", Toast.LENGTH_SHORT).show();
        finish();
    }
}