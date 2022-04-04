package com.example.quirky;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MyStatsActivity extends AppCompatActivity {
    private Button backBt;
    private Intent i;
    private Profile p;
    private MemoryController mc;
    private LeaderBoardController lbc;

    private TextView totScore, scoreRanking, totScanned, scannedRanking, largestScanned, lrgRanking;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_stats);

        i = getIntent();
        p = (Profile) i.getSerializableExtra("profile");
        lbc = new LeaderBoardController(this);

        totScore = findViewById(R.id.totScore2);
        scoreRanking = findViewById(R.id.scoreRanking2);
        totScanned = findViewById(R.id.totScanned2);
        scannedRanking = findViewById(R.id.scannedRanking2);
        largestScanned = findViewById(R.id.largestScanned2);
        lrgRanking = findViewById(R.id.largestScannedRanking2);

        replaceValues();
    }

    private void replaceValues() {
        totScore.setText(String.valueOf(p.getPointsOfScannedCodes()));
        totScanned.setText(String.valueOf(p.getNumberCodesScanned()));
        largestScanned.setText(String.valueOf(p.getPointsOfLargestCodes()));
        lbc.readAllPlayers().addOnCompleteListener(task -> setTextRanking());
    }

    private void setTextRanking(){
        ArrayList<Profile> leaderboard = lbc.getRankingPoints();
        scoreRanking.setText(String.valueOf(leaderboard.indexOf(p)));

        leaderboard = lbc.getRankingNumScanned();
        scannedRanking.setText(String.valueOf(leaderboard.indexOf(p)));

        leaderboard = lbc.getRankingLargestScanned();
        lrgRanking.setText(String.valueOf(leaderboard.indexOf(p)));
    }
}