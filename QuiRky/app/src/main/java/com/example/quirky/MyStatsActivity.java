package com.example.quirky;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MyStatsActivity extends AppCompatActivity {
    private Button backBt;
    private Intent i;
    private Profile p;
    private MemoryController mc;
    private LeaderBoardController lbc;

    private TextView totScore, scoreRanking, totScanned, scannedRanking;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_stats);

        // Do I need to get the profile?
        i = getIntent();
        p = (Profile) i.getSerializableExtra("profile");
        lbc = new LeaderBoardController(this);
        lbc.readAllPlayers().addOnCompleteListener(task -> setTextRanking());

        totScore = findViewById(R.id.totScore2);
        scoreRanking = findViewById(R.id.scoreRanking2);
        totScanned = findViewById(R.id.totScanned2);
        scannedRanking = findViewById(R.id.scannedRanking2);

        totScore.setText(String.valueOf(p.getPointsOfScannedCodes()));
        totScanned.setText(String.valueOf(p.getNumberCodesScanned()));

        backBt = (Button)findViewById(R.id.mystatsBack);
        backBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnToPrev();
            }
        });

    }

    private void setTextRanking(){
        scoreRanking.setText(String.valueOf(lbc.getRankingPoints()));
        scannedRanking.setText(String.valueOf(lbc.getRankingNumScanned()));
    }
    public void returnToPrev(){
        Intent intent1 = new Intent(this, MainActivity.class);
        startActivity(intent1);
    }
}