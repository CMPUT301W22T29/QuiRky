/*
 * Copyright (c) 2022. CMPUT301W22T29
 * Subject to MIT License
 * See full terms at https://github.com/CMPUT301W22T29/QuiRky/blob/main/LICENSE
 */

package com.example.quirky.controllers;

import com.example.quirky.models.Profile;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * @Author Jonathen Adsit
 * Controller class to sort the player population by the three statistics,
 * which are Most Total Points, Most Codes Scanned, and Largest Single QRCode Found
 */
public class LeaderBoardController {
    private final String TAG = "LeaderboardController says";
    private final ArrayList<Profile> players;

    /**
     * Constructor to initialise the rankings with the player population
     * @param players The population of players to sort
     */
    public LeaderBoardController(ArrayList<Profile> players) {
        this.players = players;
        sortPlayersByPoints();
    }

    /**
     * Sort the population of players by total points
     */
    private void sortPlayersByPoints() {
        players.sort(new Comparator<Profile>() {
            @Override
            public int compare(Profile profile, Profile t1) {
                return t1.getPointsOfScannedCodes()
                        - profile.getPointsOfScannedCodes();
            }
        });
    }

    /**
     * Sort the population of players by most codes scanned
     */
    private void sortPlayersByNumScanned() {
        players.sort(new Comparator<Profile>() {
            @Override
            public int compare(Profile profile, Profile t1) {
                return t1.getNumberCodesScanned()
                        - profile.getNumberCodesScanned();
            }
        });
    }

    /**
     * Sort the population of players by largest code found
     */
    private void sortPlayersByLargestScanned() {
        players.sort(new Comparator<Profile>() {
            @Override
            public int compare(Profile profile, Profile t1) {
                return t1.getPointsOfLargestCodes()
                        - profile.getPointsOfLargestCodes();
            }
        });
    }

    /**
     * Getter for the population ranked by total points
     * @return The sorted population
     */
    public ArrayList<Profile> getRankingPoints() {
        sortPlayersByPoints();
        return players;
    }

    /**
     * Getter for the population ranked by total codes scanned
     * @return The sorted population
     */
    public ArrayList<Profile> getRankingNumScanned() {
        sortPlayersByNumScanned();
        return players;
    }

    /**
     * Getter for the population ranked by greatest code scanned
     * @return The sorted population
     */
    public ArrayList<Profile> getRankingLargestScanned() {
        sortPlayersByLargestScanned();
        return players;
    }

    /**
     * Find a player's ranking within the Total Points category
     * @param p The profile to find
     * @return The player's ranking, or -1, if the player does not exist
     */
    public int findRankPoints(Profile p) {
        sortPlayersByPoints();
        for (int i = 0; i < players.size(); i++) {
            if (p.getUname().equals(players.get(i).getUname()))
                return i;
        }
        return -1;
    }

    /**
     * Find a player's ranking within the Most Scanned category
     * @param p The profile to find
     * @return The player's ranking, or -1, if the player does not exist
     */
    public int findRankScanned(Profile p) {
        sortPlayersByNumScanned();
        for (int i = 0; i < players.size(); i++) {
            if(p.getUname().equals(players.get(i).getUname()))
                return i;
        }

        return -1;
    }

    /**
     * Find a player's ranking within the largest code category
     * @param p The profile to find
     * @return The player's ranking, or -1, if the player does not exist
     */
    public int findRankLargest(Profile p) {
        sortPlayersByLargestScanned();
        for (int i = 0; i < players.size(); i++) {
            if(p.getUname().equals(players.get(i).getUname()))
                return i;
        }

        return -1;
    }
}
