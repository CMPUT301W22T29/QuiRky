package com.example.quirky;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

        i = getIntent();
        p = (Profile) i.getSerializableExtra("profile");
        lbc = new LeaderBoardController(this);

        totScore = findViewById(R.id.totScore2);
        scoreRanking = findViewById(R.id.scoreRanking2);
        totScanned = findViewById(R.id.totScanned2);
        scannedRanking = findViewById(R.id.scannedRanking2);

        int numTotScore = p.getPointsOfScannedCodes();
        int numTotScanned = p.getNumberCodesScanned();

        System.out.println(p.getNumberCodesScanned());
        Toast.makeText(this, String.valueOf(p.getNumberCodesScanned()), Toast.LENGTH_LONG).show();
        if (numTotScore < 1 && numTotScanned < 1) {
            defaultValues();
        }
        else {
            replaceValues();
        }

        backBt = (Button)findViewById(R.id.mystatsBack);
        backBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnToPrev();
            }
        });

    }
    private void defaultValues() {
        int zeroVal = 0;
        totScore.setText(String.valueOf(zeroVal));
        totScanned.setText(String.valueOf(zeroVal));
        scoreRanking.setText(String.valueOf(zeroVal));
        scannedRanking.setText(String.valueOf(zeroVal));
    }

    private void replaceValues() {
        totScore.setText(String.valueOf(p.getPointsOfScannedCodes()));
        totScanned.setText(String.valueOf(p.getNumberCodesScanned()));
        lbc.readAllPlayers().addOnCompleteListener(task -> setTextRanking());
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