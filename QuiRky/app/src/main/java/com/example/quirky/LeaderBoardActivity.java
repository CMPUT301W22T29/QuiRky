/*
 * Copyright (c) 2022. CMPUT301W22T29
 * Subject to MIT License
 * See full terms at https://github.com/CMPUT301W22T29/QuiRky/blob/main/LICENSE
 */

package com.example.quirky;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class LeaderBoardActivity extends AppCompatActivity {

    private Button sortPoints, sortScanned, sortGreatest, myRank, topRanks;
    private RecyclerView list;
    private QRAdapter adapter;

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

        adapter = new QRAdapter(data, new ArrayList<>(), this);
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

    private void sortByPoints() {
        players = lbc.getRankingPoints();
        data.clear();
        for(Profile p: players)
            data.add(p.getUname());

        position = lbc.findRankPoints(user);
        if(position == -1) {
            Toast.makeText(this, "You are not in the Leaderboard!", Toast.LENGTH_SHORT).show();
            position = 0;
        }
    }

    private void sortByScanned() {
        players = lbc.getRankingNumScanned();
        data.clear();
        for(Profile p: players)
            data.add(p.getUname());

        position = lbc.findRankScanned(user);
        if(position == -1) {
            Toast.makeText(this, "You are not in the Leaderboard!", Toast.LENGTH_SHORT).show();
            position = 0;
        }
    }

    private void sortByGreatestScanned() {
        players = lbc.getRankingLargestScanned();
        data.clear();
        for(Profile p: players)
            data.add(p.getUname());

        position = lbc.findRankLargest(user);
        if(position == -1) {
            Toast.makeText(this, "You are not in the Leaderboard!", Toast.LENGTH_SHORT).show();
            position = 0;
        }
    }
}