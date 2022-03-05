package com.example.quirky;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button getStarted;
    private Button settings;
    private Button quit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getStarted = findViewById(R.id.getStarted);
        settings = findViewById(R.id.setting);
        quit = findViewById(R.id.quit);
        Intent viewOfSetting = new Intent(this, SettingsActivity.class);
        Intent viewOfMainPage = new Intent(this, StartingPageActivity.class);
        getStarted.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                startActivity(viewOfMainPage);
            }
        });

        settings.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                startActivity(viewOfSetting);
            }
        });

        quit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
            }
        });
    }
}