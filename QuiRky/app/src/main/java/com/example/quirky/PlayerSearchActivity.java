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

import java.util.ArrayList;

/**
 * Activity to let the user search for other players by username
 */
public class PlayerSearchActivity extends AppCompatActivity {

    private final String TAG = "PlayerSearchActivity says";

    private DatabaseController dc;
    ListeningList<Profile> queryResults;
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


        listener = position -> {
            Log.d(TAG, "Listener received item position" + position);
            startViewProfileActivity(position);
        };


        adapter = new QRAdapter(usernames, photos, this, listener);
        list.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        list.setAdapter(adapter);

        button.setOnClickListener(view -> QueryDatabase());
    }

    /**
     * Called when the user clicks the search icon
     * Begin searching the database for usernames similar to the inputted username.
     */
    public void QueryDatabase() {
        circle.setVisibility(View.VISIBLE);
        String username = input.getText().toString();

        queryResults = new ListeningList<>();
        queryResults.setOnAddListener(new OnAddListener<Profile>() {
            @Override
            public void onAdd(ListeningList<Profile> listeningList) {
                updateList();
                circle.setVisibility(View.INVISIBLE);
            }
        });

        dc.readAllUsers(username, queryResults);
    }

    private void updateList() {
        usernames.clear();
        for(Profile p : queryResults) {
            usernames.add( p.getUname() );
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * Starts up the ProfileViewerActivity. Called when an item in the RecyclerView is clicked on.
     * @param position The position of the item the user clicked on.
     */
    private void startViewProfileActivity(int position) {
        Profile p = queryResults.get(position);

        Intent i = new Intent(this, ProfileViewerActivity.class);
        i.putExtra("profile", p);
        startActivity(i);
    }
}