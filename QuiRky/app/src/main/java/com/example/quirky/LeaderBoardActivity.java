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
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class LeaderBoardActivity extends AppCompatActivity {

    private Button sortPoints, sortScanned, sortGreatest, myRank, topRanks;
    private RecyclerView list;
    private QRAdapter adapter;
    private RecyclerClickerListener listener;

    private Profile user;
    private ArrayList<String> data; // For use with the adapter
    private ArrayList<Profile> players;
    private LeaderBoardController lbc;
    private int position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);

        // Read the user from memory
        MemoryController mc = new MemoryController(this);
        user = mc.read();

        data = new ArrayList<>();

        // Initialise views
        list = findViewById(R.id.leaderboard_list);
        // Buttons
        sortPoints = findViewById(R.id.sort_points_button);
        sortScanned = findViewById(R.id.sort_scanned_button);
        sortGreatest = findViewById(R.id.sort_largest_button);
        myRank = findViewById(R.id.my_ranking_button);
        topRanks = findViewById(R.id.top_rankings_button);

        // Read the player population
        DatabaseController dc = new DatabaseController(this);
        dc.readAllProfiles().addOnCompleteListener(task -> {
            ArrayList<Profile> result = dc.getAllProfiles(task);
            doneReading(result);
        });
    }

    // Continue setup of activity once done reading.
    private void doneReading(ArrayList<Profile> players) {

        lbc = new LeaderBoardController(players);
        sortByPoints();

        listener = new RecyclerClickerListener() {
            @Override
            public void OnClickListItem(int position) {
                Profile p = players.get(position);
                startViewProfile(p);
            }
        };

        adapter = new QRAdapter(data, new ArrayList<>(), this, listener);
        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));


        sortPoints.setOnClickListener(view -> {
            sortByPoints();
            adapter.notifyDataSetChanged();
        });
        sortScanned.setOnClickListener(view -> {
            sortByScanned();
            adapter.notifyDataSetChanged();
        });
        sortGreatest.setOnClickListener(view -> {
            sortByGreatestScanned();
            adapter.notifyDataSetChanged();
        });

        myRank.setOnClickListener(view -> list.scrollToPosition(position));
        topRanks.setOnClickListener(view -> list.scrollToPosition(0));
    }

    private void startViewProfile(Profile p) {
        Intent i = new Intent(this, ProfileViewerActivity.class);
        i.putExtra("profile", p);
        startActivity(i);
    }

    private void sortByPoints() {
        players = lbc.getRankingPoints();
        updateDisplay();
    }

    private void sortByScanned() {
        players = lbc.getRankingNumScanned();
        updateDisplay();
    }

    private void sortByGreatestScanned() {
        players = lbc.getRankingLargestScanned();
        updateDisplay();
    }

    private void updateDisplay() {
        data.clear();
        for(int i = 0; i < players.size(); i++) {
            String x = i + " | " + players.get(i).getUname();
            data.add(x);
        }

        position = lbc.findRankLargest(user);
        if(position == -1) {
            Toast.makeText(this, "You are not in the Leaderboard!", Toast.LENGTH_SHORT).show();
            position = 0;
        }
    }
}