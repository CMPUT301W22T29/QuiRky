/*
 * Copyright (c) 2022. CMPUT301W22T29
 * Subject to MIT License
 * See full terms at https://github.com/CMPUT301W22T29/QuiRky/blob/main/LICENSE
 */

package com.example.quirky.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.example.quirky.AdapterText;
import com.example.quirky.RecyclerClickerListener;
import com.example.quirky.controllers.LeaderBoardController;
import com.example.quirky.ListeningList;
import com.example.quirky.controllers.MemoryController;
import com.example.quirky.models.Profile;
import com.example.quirky.R;
import com.example.quirky.controllers.DatabaseController;

import java.util.ArrayList;

/**
 * Activity to show the global leaderboards. Players can see the population ranked by three statistics
 * Total Points the players have acquired, Most QRCodes Scanned, and Largest Single QRCode Scanned
 * @see LeaderBoardController
 */
public class LeaderBoardActivity extends AppCompatActivity {

    private Button sortPoints, sortScanned, sortGreatest, myRank, topRanks;
    private RecyclerView list;
    private AdapterText adapter;

    private Profile user;
    private ArrayList<String> data; // For use with the adapter
    private ArrayList<Profile> players;
    private LeaderBoardController lbc;
    private int position = 0;

    private RecyclerClickerListener listener;

    private static final int POINTS = 0;
    private static final int NUM_SCANNED = 1;
    private static final int LARGEST_FOUND = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        // Read the user from memory
        MemoryController mc = new MemoryController(this);
        user = mc.read();

        data = new ArrayList<>();

        // Initialise views
        list = findViewById(R.id.leaderboard_list);
        sortPoints = findViewById(R.id.sort_points_button);
        sortScanned = findViewById(R.id.sort_scanned_button);
        sortGreatest = findViewById(R.id.sort_largest_button);
        myRank = findViewById(R.id.my_ranking_button);
        topRanks = findViewById(R.id.top_rankings_button);

        // Read the player population
        DatabaseController dc = new DatabaseController();
        ListeningList<Profile> readResults = new ListeningList<>();
        readResults.setOnAddListener(listeningList -> {
            players = (ArrayList<Profile>) listeningList;
            doneReading();
        });

        dc.readAllUsers("", readResults);
    }

    /**
     * Called once the DatabaseController is finished reading all the users from FireStore.
     * Finishes setting up the Recycler & Adapter, and sets on click listeners for the buttons
     */
    private void doneReading() {

        lbc = new LeaderBoardController(players);
        listener = position -> {
            Profile p = players.get(position);
            startViewProfile(p);
        };

        adapter = new AdapterText(data, this, listener);
        list.setAdapter(adapter);
        list.setLayoutManager(adapter.getLayoutManager());

        sortPoints.setOnClickListener(view -> sort(POINTS));
        sortScanned.setOnClickListener(view -> sort(NUM_SCANNED));
        sortGreatest.setOnClickListener(view -> sort(LARGEST_FOUND));

        myRank.setOnClickListener(view -> list.scrollToPosition(position));
        topRanks.setOnClickListener(view -> list.scrollToPosition(0));

        players = lbc.getRankingPoints();
        sort(POINTS);
    }

    /**
     * Start viewing a profile clicked on in the rankings list
     * @param p The profile to be viewed
     */
    private void startViewProfile(Profile p) {
        Intent i = new Intent(this, ProfileActivity.class);
        i.putExtra("profile", p);
        startActivity(i);
    }

    /**
     * Tell the adapter to update the display, and find the user's new position in the rankings
     * @param criteria The criteria to sort the rankings with.
     *                 One of LeaderBoardActivity.POINTS, LeaderBoardActivity.NUM_SCANNED, or LeaderBoardActivity.LARGEST_FOUND
     */
    @SuppressLint("NotifyDataSetChanged")
    private void sort(int criteria) {

        if(criteria == POINTS) {
            players = lbc.getRankingPoints();
            position = lbc.findRankPoints(user);
        } else if(criteria == NUM_SCANNED) {
            players = lbc.getRankingNumScanned();
            position = lbc.findRankScanned(user);
        } else if(criteria == LARGEST_FOUND) {
            players = lbc.getRankingNumScanned();
            position = lbc.findRankLargest(user);
        }

        if(position == -1) {
            Toast.makeText(this, "You are not in the Leaderboard!", Toast.LENGTH_SHORT).show();
            position = 0;
        }

        data = new ArrayList<>();
        for(int i = 0; i < players.size(); i++) {
            String x = i+1 + " | " + players.get(i).getUname();
            data.add(x);
        }

        adapter.sortData(data);
        adapter.notifyDataSetChanged();
    }
}