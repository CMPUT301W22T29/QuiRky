/*
 * Copyright (c) 2022. CMPUT301W22T29
 * Subject to MIT License
 * See full terms at https://github.com/CMPUT301W22T29/QuiRky/blob/main/LICENSE
 */

package com.example.quirky;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * Activity to let the user search for other players by username
 */
public class PlayerSearchActivity extends AppCompatActivity {

    private final String TAG = "PlayerSearchActivity says";

    private DatabaseController dc;
    private ArrayList<String> usernames;
    private ArrayList<Drawable> photos;

    private QRAdapter adapter;

    private RecyclerView list;
    private ImageButton button;
    private EditText input;
    private ProgressBar circle;

    private RecyclerClickerListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_search);

        dc = new DatabaseController();

        usernames = new ArrayList<>();
        photos = new ArrayList<>();

        button = findViewById(R.id.commit_search_button);
        input = findViewById(R.id.search_input_field);
        circle = findViewById(R.id.search_progress_bar);
        list = findViewById(R.id.search_user_list);


        listener = new RecyclerClickerListener() {
            @Override
            public void OnClickListItem(int position) {
                Log.d(TAG, "Listener received item position" + position);
                String user = usernames.get(position);
                dc.readProfile(user).addOnCompleteListener(task -> {
                    Profile p = dc.getProfile(task);
                    assert p != null : "Something went wrong in the profile read!";
                    startViewProfileActivity(p);
                });
            }
        };


        adapter = new QRAdapter(usernames, photos, this, listener);
        list.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        list.setAdapter(adapter);

        button.setOnClickListener(view -> { QueryDatabase(); });
    }

    /**
     * Called when the user clicks the search icon
     * Begin searching the database for usernames similar to the inputted username.
     */
    public void QueryDatabase() {
        circle.setVisibility(View.VISIBLE);
        usernames.clear();
        String username = input.getText().toString();

        if(username.length() == 0)
            return;

        // Start the Query and set an on complete listener.
        dc.startUserSearchQuery(username)
                .addOnCompleteListener(task -> {
            ArrayList<Profile> results = dc.getUserSearchQuery(task);

            if(results.size() == 0) {
                Toast.makeText(PlayerSearchActivity.this, "No results found!", Toast.LENGTH_LONG).show();

            } else {
                for(Profile p : results) {
                    if(p == null) continue;
                    usernames.add(p.getUname());
                }
                adapter.notifyDataSetChanged();
            }

            circle.setVisibility(View.INVISIBLE);
        });
    }

    /**
     * Starts up the ProfileViewerActivity with a given profile
     * @param p The profile to be viewed
     */
    private void startViewProfileActivity(Profile p) {
        Intent i = new Intent(this, ProfileViewerActivity.class);
        i.putExtra("profile", p);
        startActivity(i);
    }
}