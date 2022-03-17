package com.example.quirky;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements InputUnameLoginFragment.LoginFragListener {

    DatabaseController dm;
    MemoryManager mm;
    String id;

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

        // FIXME: this may need to go in a controller? But cannot use 'this.getContentResolver' outside an activity
        id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        mm = new MemoryManager(this, id);

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
                    Toast.makeText(this, "This username already exists!", Toast.LENGTH_SHORT).show();
                    login();
                } else {
                    Profile p = new Profile(uname);
                    writeUser(p);
                    startHubActivity();
                }
            }
        });
    }

    private void login() {
        // mm.exist() checks if the user has logged in on this device before
        if(!mm.exist()) {
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

    private void writeUser(Profile user) {
        mm.make();
        mm.write("name", user.getUname());
        mm.write("email", "");
        mm.write("phone", "");

        dm.writeProfile(user);
    }

    private void startHubActivity() {
        Intent i = new Intent(this, StartingPageActivity.class);
        startActivity(i);
    }

    private void test() {
        // EXAMPLE CASE OF READING FROM FIRESTORE:
        dm.readProfile("IDONOTEXIST").addOnCompleteListener(task -> {
            if(!task.isSuccessful()) {
                task.getException().printStackTrace();
            } else {
                Map<String, Object> data = task.getResult().getData();
                if(data == null)
                Log.d("MainActivity: ", "Read was successful and the data is null ");
            }
        });
    }
}
