package com.example.quirky;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

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

        DatabaseController dc = new DatabaseController(this);
        dc.readAllProfiles().addOnCompleteListener(task -> {
            ArrayList<Profile> results = dc.getAllProfiles(task);
            doneReading(results);
        });
    }

    private void doneReading(ArrayList<Profile> players) {
        Log.d(TAG, "Reading from the database gave us these players:\n");
        for(Profile profile : players)
            Log.d(TAG, profile.getUname() + "\n");
        Log.d("", "The appholder is: " + p.getUname() + "\n");

        lbc = new LeaderBoardController(players);
        int position;

        ArrayList<Profile> temp = lbc.getRankingPoints();
        position = lbc.findRankPoints(p);

        totalScore.setText( String.valueOf( p.getPointsOfScannedCodes() ) );
        scoreRanking.setText( String.valueOf(position) );

        temp = lbc.getRankingNumScanned();
        position = lbc.findRankScanned(p);
        totalScanned.setText(String.valueOf(p.getNumberCodesScanned()));
        scannedRanking.setText(String.valueOf(position) );

        temp = lbc.getRankingLargestScanned();
        position = lbc.findRankLargest(p);
        largestScanned.setText(String.valueOf(p.getPointsOfLargestCodes()));
        lrgRanking.setText(String.valueOf(position));
    }
}