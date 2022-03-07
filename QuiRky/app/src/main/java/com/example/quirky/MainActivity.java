package com.example.quirky;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button getStarted = findViewById(R.id.getStarted);
        Button settings = findViewById(R.id.setting);
        Button quit = findViewById(R.id.quit);

        getStarted.setOnClickListener(view -> startHubActivity());
        settings.setOnClickListener(view -> startSettingsActivity());
        quit.setOnClickListener(view -> finish());
    }

    private void startHubActivity() {

        /*
        Code for getting unique device ID taken from:
        https://stackoverflow.com/a/2785493
        Written by user:
        https://stackoverflow.com/users/166712/anthony-forloney
        Published May 7 2010
        */ // FIXME: this method may sometimes return null I think? But also a rare case? may need to find another method to id a device
        String id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);

        Intent i = new Intent(this, StartingPageActivity.class);
        MemoryManager mm = new MemoryManager(this, id);

        // If the directory does not exist, this is the first time the user has opened the app
        //      We must write the user's id to local memory and the database
        if(!mm.exist()) {
            mm.make();
            mm.write("name", "");
            mm.write("email", "");
            mm.write("phone", "");

            DatabaseManager dm = new DatabaseManager("users");
            dm.write(new HashMap<>(), id);
        }
        startActivity(i);


    }

    private void startSettingsActivity() {
        Intent i = new Intent(this, SettingsActivity.class);
        startActivity(i);
    }
}