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
    private Context ct;
    private DatabaseController dc;
    private ArrayList<Profile> players;

    public LeaderBoardController(Context ct) {
        this.ct = ct;
        this.dc = new DatabaseController(ct);
        readAllPlayers();
    }

    private void readAllPlayers() {
        dc.readAllProfiles().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                players = dc.getAllProfiles(task);
            }
        });
    }

    private void sortPlayersByPoints() {
        players.sort(new Comparator<Profile>() {
            @Override
            public int compare(Profile profile, Profile t1) {
                return ProfileController.calculateTotalPoints(profile)
                        - ProfileController.calculateTotalPoints(t1);
            }
        });
    }

    private void sortPlayersByNumScanned() {
        players.sort(new Comparator<Profile>() {
            @Override
            public int compare(Profile profile, Profile t1) {
                return ProfileController.calculateTotalScanned(profile)
                        - ProfileController.calculateTotalScanned(t1);
            }
        });
    }

    private void sortPlayersByLargestScanned() {
        players.sort(new Comparator<Profile>() {
            @Override
            public int compare(Profile profile, Profile t1) {
                return ProfileController.calculateGreatestScore(profile)
                        - ProfileController.calculateGreatestScore(t1);
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
}
