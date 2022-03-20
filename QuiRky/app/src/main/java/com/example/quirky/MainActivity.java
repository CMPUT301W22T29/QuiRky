package com.example.quirky;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;


import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;
public class MainActivity extends AppCompatActivity implements InputUnameLoginFragment.LoginFragListener {
    public static final String EXTRA_MESSAGE = "com.example.QuiRky.MESSAGE";

    DatabaseController dm;
    MemoryController mc;

    /*
    Code for getting unique device ID taken from:
    https://stackoverflow.com/a/2785493
    Written by user:
    https://stackoverflow.com/users/166712/anthony-forloney
    Published May 7 2010
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dm = new DatabaseController(FirebaseFirestore.getInstance(), this);

        Button getStarted = findViewById(R.id.getStarted);
        Button settings = findViewById(R.id.setting);
        Button quit = findViewById(R.id.quit);

        getStarted.setOnClickListener(view -> login());
        settings.setOnClickListener(view -> startSettingsActivity());
        quit.setOnClickListener(view -> finishAffinity());
    }


    @Override
    public void confirm(String uname) {
        dm.readProfile(uname).addOnCompleteListener(task -> {
            if(!task.isSuccessful()) {
                task.getException().printStackTrace();
            } else {
                // dm.getProfile(task) will return null if the profile does not exist yet.
                if(dm.getProfile(task) != null) {
                    Toast.makeText(this, "This username already exists!", Toast.LENGTH_LONG).show();
                    login();
                } else {
                    Profile p = new Profile(uname);

                    mc = new MemoryController(this, uname);
                    mc.write(p);
                    dm.writeProfile(p);

                    startHubActivity();
                }
            }
        });
    }

    private void login() {
        // mm.exists() checks if the user has logged in on this device before
        if(!MemoryController.exists()) {
            // If the user has not logged in yet, show the fragment that asks for a username
            // This fragment's listener will call the write user method
            InputUnameLoginFragment frag = new InputUnameLoginFragment();
            frag.show(getSupportFragmentManager(), "GET_UNAME");
        } else {
            startHubActivity();
        }
    }

    private void startSettingsActivity() {
        Intent i = new Intent(this, SettingsActivity.class);
        startActivity(i);
    }

    private void startHubActivity() {
        Intent i = new Intent(this, StartingPageActivity.class);
        startActivity(i);
    }
}
