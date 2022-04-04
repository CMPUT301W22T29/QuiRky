package com.example.quirky;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class MyStatsActivity extends AppCompatActivity {
    private Button backBt;
    TextView totalScore,totalScanned;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_stats);

        backBt = (Button)findViewById(R.id.mystatsBack);
        backBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnToPrev();
            }
        });
        totalScanned = findViewById(R.id.TotalScanned);


    }
    public void returnToPrev(){
        Intent intent1 = new Intent(this, MainActivity.class);
        startActivity(intent1);
    }

}