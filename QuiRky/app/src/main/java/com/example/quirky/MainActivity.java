package com.example.quirky;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.QuiRky.MESSAGE";


    Button settings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button getStarted = findViewById(R.id.getStarted);
        settings = findViewById(R.id.setting);
        Button quit = findViewById(R.id.quit);
        Button comment = findViewById(R.id.comment);

        getStarted.setOnClickListener(view -> startHubActivity());
        settings.setOnClickListener(view -> startSettingsActivity());
        quit.setOnClickListener(view -> finish());
        comment.setOnClickListener(view -> comment()); // For Testing Comments out

    }

    // For Testing comments out
    private void comment() {
        Intent i = new Intent(this, ViewCommentsActivity.class);
        String qrCodeID = "Raymoney Johnson";
        i.putExtra(EXTRA_MESSAGE, qrCodeID);
        startActivity(i);
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
        }

        DatabaseManager dm = new DatabaseManager();
        Task<QuerySnapshot> task = dm.readComments("sample");

        // We need to get Listeners in our classes in order to receive data
        task.addOnSuccessListener(queryDocumentSnapshots -> {
            ArrayList<Comment> comments = dm.getComments(task);
            settings.setText(comments.get(0).getContent());
        });
	startActivity(i);
    }

    private void startSettingsActivity() {
        Intent i = new Intent(this, SettingsActivity.class);
        startActivity(i);
    }
}
