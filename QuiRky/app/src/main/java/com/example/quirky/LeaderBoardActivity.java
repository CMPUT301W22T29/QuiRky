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

import java.util.ArrayList;

public class LeaderBoardActivity extends AppCompatActivity {

    private Button sortPoints, sortScanned, sortGreatest, myRank, topRanks;
    private RecyclerView list;
    private QRAdapter adapter;

    private Profile user;
    private ArrayList<Profile> players;
    private ArrayList<String> data = new ArrayList<>(); // For use with the adapter
    private LeaderBoardController lbc;

    private Boolean showTopPlayers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);

        MemoryController mc = new MemoryController(this);
        user = mc.read();

        list = findViewById(R.id.leaderboard_list);

        sortPoints = findViewById(R.id.sort_points_button);
        sortScanned = findViewById(R.id.sort_scanned_button);
        sortGreatest = findViewById(R.id.sort_largest_button);

        myRank = findViewById(R.id.my_ranking_button);
        topRanks = findViewById(R.id.top_rankings_button);

        sortPoints.setOnClickListener(view -> {
            sortByPoints();
            updateDisplay();
        });
        sortScanned.setOnClickListener(view -> {
            sortByScanned();
            updateDisplay();
        });
        sortGreatest.setOnClickListener(view -> {
            sortByGreatestScanned();
            updateDisplay();
        });

        myRank.setOnClickListener(view -> showTopPlayers = false);
        topRanks.setOnClickListener(view -> showTopPlayers = true);

        adapter = new QRAdapter(data, new ArrayList<>(), this);
        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        showTopPlayers = true;
        lbc = new LeaderBoardController(this);
        lbc.readAllPlayers().addOnCompleteListener(task -> sortByPoints());
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

//        // If there is fewer than 15 players, just display all of them.
//        if(players.size() <= 15) {
//            for(Profile p : players) {
//                if(p == null) continue;
//                data.add(p.getUname());
//            }
//            adapter.notifyDataSetChanged();
//            return;
//        }

        // There are more than 15 players
        // User can choose to show the top 10, or their own position within the rankings
        if(showTopPlayers) {
            for (int i = 0; i < 10; i++)
                data.add(players.get(i).getUname());
            adapter.notifyDataSetChanged();
            return;
        }

        // Show the 10 surrounding players around the app-holders position
        int position = lbc.findPlayer(user);
        if(position == -1) {
            data.add("You are not yet ranked on the leaderboard!");
        } else {

            // Show the 5 lower and 5 higher players on the leaderboard.
            for (int i = position - 5; i < position + 5; i++) {
                // Nested if statement prevents IndexOutOfBoundsException
                if( -1 < i && i < players.size() )
                    data.add(players.get(i).getUname());
            }
        }

        adapter.notifyDataSetChanged();
    }
}