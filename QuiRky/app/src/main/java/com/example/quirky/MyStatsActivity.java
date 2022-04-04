package com.example.quirky;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Activity to view the user's rankings in a text format, rather than in a list
 */
public class MyStatsActivity extends AppCompatActivity {
    private final String TAG = "\tMyStatsActivity says:";
    private Profile p;
    private LeaderBoardController lbc;

    private TextView totalScore, scoreRanking, totalScanned, scannedRanking, largestScanned, lrgRanking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_stats);

        totalScore = findViewById(R.id.totScore2);
        scoreRanking = findViewById(R.id.scoreRanking2);
        totalScanned = findViewById(R.id.totScanned2);
        scannedRanking = findViewById(R.id.scannedRanking2);
        largestScanned = findViewById(R.id.largestScanned2);
        lrgRanking = findViewById(R.id.largestScannedRanking2);

        Intent i = getIntent();
        p = (Profile) i.getSerializableExtra("profile");
        if(p == null)
            ExitWithError();

        DatabaseController dc = new DatabaseController();
        dc.readAllProfiles().addOnCompleteListener(task -> {
            ArrayList<Profile> results = dc.getAllProfiles(task);
            doneReading(results);
        });
    }

    /**
     * Called once DatabaseController is done reading the population of players from the database
     * Finishes setting up the text-boxes to display accurate information
     * @param players The population of players
     */
    private void doneReading(ArrayList<Profile> players) {
        Log.d(TAG, "Reading from the database gave us these players:\n");
        for(Profile profile : players)
            Log.d(TAG, profile.getUname() + "\n");
        Log.d("", "The appholder is: " + p.getUname() + "\n");

        lbc = new LeaderBoardController(players);
        int position;

        position = lbc.findRankPoints(p);

        totalScore.setText( String.valueOf( p.getPointsOfScannedCodes() ) );
        scoreRanking.setText( String.valueOf(position) );

        position = lbc.findRankScanned(p);
        totalScanned.setText(String.valueOf(p.getNumberCodesScanned()));
        scannedRanking.setText(String.valueOf(position) );

        position = lbc.findRankLargest(p);
        largestScanned.setText(String.valueOf(p.getPointsOfLargestCodes()));
        lrgRanking.setText(String.valueOf(position));
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