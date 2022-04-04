/*
 * Copyright (c) 2022. CMPUT301W22T29
 * Subject to MIT License
 * See full terms at https://github.com/CMPUT301W22T29/QuiRky/blob/main/LICENSE
 */

package com.example.quirky;

import java.util.ArrayList;
import java.util.Comparator;

public class LeaderBoardController {
    private final String TAG = "LeaderboardController says";
    private final ArrayList<Profile> HighestPoints;
    private final ArrayList<Profile> MostScanned;
    private final ArrayList<Profile> LargestCode;

    public LeaderBoardController(ArrayList<Profile> players) {
        this.HighestPoints = sortPlayersByPoints(players);
        this.MostScanned = sortPlayersByNumScanned(players);
        this.LargestCode = sortPlayersByLargestScanned(players);
    }

    private ArrayList<Profile> sortPlayersByPoints(ArrayList<Profile> players) {
        Comparator<Profile> sorter = new Comparator<Profile>() {
            @Override
            public int compare(Profile profile, Profile t1) {
                return t1.getPointsOfScannedCodes()
                        - profile.getPointsOfScannedCodes();
            }
        };

        players.sort(sorter);
        return players;
    }

    private ArrayList<Profile> sortPlayersByNumScanned(ArrayList<Profile> players) {
        Comparator<Profile> sorter = new Comparator<Profile>() {
            @Override
            public int compare(Profile profile, Profile t1) {
                return t1.getNumberCodesScanned()
                        - profile.getNumberCodesScanned();
            }
        };

        players.sort(sorter);
        return players;
    }

    private ArrayList<Profile> sortPlayersByLargestScanned(ArrayList<Profile> players) {
        Comparator<Profile> sorter = new Comparator<Profile>() {
            @Override
            public int compare(Profile profile, Profile t1) {
                return t1.getPointsOfLargestCodes()
                        - profile.getPointsOfLargestCodes();
            }
        };

        players.sort(sorter);
        return players;
    }

    public ArrayList<Profile> getRankingPoints() {
        return HighestPoints;
    }

    public ArrayList<Profile> getRankingNumScanned() {
        return MostScanned;
    }

    public ArrayList<Profile> getRankingLargestScanned() {
        return LargestCode;
    }

    public int findRankPoints(Profile p) {
        for (int i = 0; i < HighestPoints.size(); i++) {
            if (p.getUname().equals(HighestPoints.get(i).getUname()))
                return i;
        }
        return -1;
    }

    public int findRankScanned(Profile p) {
        for (int i = 0; i < MostScanned.size(); i++) {
            if(p.getUname().equals(MostScanned.get(i).getUname()))
                return i;
        }

        return -1;
    }

    public int findRankLargest(Profile p) {
        for (int i = 0; i < LargestCode.size(); i++) {
            if(p.getUname().equals(LargestCode.get(i).getUname()))
                return i;
        }

        return -1;
    }
}
