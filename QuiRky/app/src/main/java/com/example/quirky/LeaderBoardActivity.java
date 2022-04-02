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

        sortPoints.setOnClickListener(view -> sortByPoints());
        sortScanned.setOnClickListener(view -> sortByScanned());
        sortGreatest.setOnClickListener(view -> sortByGreatestScanned());

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


        if(players.size() <= 10) {
            for(Profile p : players) {
                if(p == null) continue;
                data.add(p.getUname());
            }
            adapter.notifyDataSetChanged();
            return;
        }

        if(showTopPlayers) {
            // Show the Top 10 players on the leaderboard
            for(int i = 0; i < 10; i++) {
                data.add( players.get(i).getUname() );
            }
        } else {
            // Show the 10 surrounding players around the app-holders position
            int position = lbc.findPlayer(user);

            if(position == -1) {
                data.add("You are not yet ranked on the leaderboard!");
            } else {
                for (int i = position - 5; i < position + 5; i++) {
                    data.add(players.get(i).getUname());
                }
            }
        }

        adapter.notifyDataSetChanged();
    }
}