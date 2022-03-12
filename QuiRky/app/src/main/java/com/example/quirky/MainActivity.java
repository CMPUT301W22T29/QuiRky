package com.example.quirky;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements InputUnameLoginFragment.LoginFragListener {

    DatabaseController dm;
    MemoryManager mm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button getStarted = findViewById(R.id.getStarted);
        Button settings = findViewById(R.id.setting);
        Button quit = findViewById(R.id.quit);

        getStarted.setOnClickListener(view -> login());
        settings.setOnClickListener(view -> startSettingsActivity());
        quit.setOnClickListener(view -> finishAffinity());
    }

    @Override
    public void confirm(String uname) {
        // TODO: Database read to check this username does not already exist
        Profile p = new Profile(uname);
        writeUser(p);
        startHubActivity();
    }

    private void login() {
        /*
        Code for getting unique device ID taken from:
        https://stackoverflow.com/a/2785493
        Written by user:
        https://stackoverflow.com/users/166712/anthony-forloney
        Published May 7 2010
        */
        // FIXME: this method may sometimes return null I think? But also a rare case? may need to find another method to id a device
        String id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        mm = new MemoryManager(this, id);

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


        /* FIXME: can't write user to database because dm.writeUser produces a null pointer exception.
            except that I have two seperate tests confirming that the profile is not null...
        */

        assert(user != null);

        // dm.writeUser(user); Commented out so it is at least runnable.
    }

    private void startHubActivity() {
        Intent i = new Intent(this, StartingPageActivity.class);
        startActivity(i);
    }
}
