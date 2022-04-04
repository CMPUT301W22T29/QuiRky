/*
 * Copyright (c) 2022. CMPUT301W22T29
 * Subject to MIT License
 * See full terms at https://github.com/CMPUT301W22T29/QuiRky/blob/main/LICENSE
 */

package com.example.quirky;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Comparator;

public class LeaderBoardController {
    private DatabaseController dc;
    private ArrayList<Profile> players;
    private final String ErrorTag = "You must call readAllPlayers() before you can sort the players!";

    public LeaderBoardController(Context ct) {
        this.dc = new DatabaseController(ct);
    }

    // Constructor with Dependency Injection for testing purposes.
    public LeaderBoardController(ArrayList<Profile> players) {
        this.players = players;
    }

    public Task<QuerySnapshot> readAllPlayers() {
        Task<QuerySnapshot> read = dc.readAllProfiles();
        read.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                players = dc.getAllProfiles(task);
            }
        });
        return read;
    }

    private void sortPlayersByPoints() {
        players.sort(new Comparator<Profile>() {
            @Override
            public int compare(Profile profile, Profile t1) {
                return t1.getPointsOfScannedCodes()
                        - profile.getPointsOfScannedCodes();
            }
        });
    }

    private void sortPlayersByNumScanned() {
        players.sort(new Comparator<Profile>() {
            @Override
            public int compare(Profile profile, Profile t1) {
                return t1.getNumberCodesScanned()
                        - profile.getNumberCodesScanned();
            }
        });
    }

    private void sortPlayersByLargestScanned() {
        players.sort(new Comparator<Profile>() {
            @Override
            public int compare(Profile profile, Profile t1) {
                return t1.getPointsOfLargestCodes()
                        - profile.getPointsOfLargestCodes();
            }
        });
    }

    public ArrayList<Profile> getRankingPoints() {
        assert players != null : "Somehow the players have not yet been initialized!";
        sortPlayersByPoints();
        return players;
    }

    public ArrayList<Profile> getRankingNumScanned() {
        assert players != null : "Somehow the players have not yet been initialized!";
        sortPlayersByNumScanned();
        return players;
    }

    public ArrayList<Profile> getRankingLargestScanned() {
        assert players != null : "Somehow the players have not yet been initialized!";
        sortPlayersByLargestScanned();
        return players;
    }

    public int findPlayer(Profile p) {
        return players.indexOf(p);
    }
}
