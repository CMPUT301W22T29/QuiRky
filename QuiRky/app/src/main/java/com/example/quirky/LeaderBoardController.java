/*
 * Copyright (c) 2022. CMPUT301W22T29
 * Subject to MIT License
 * See full terms at https://github.com/CMPUT301W22T29/QuiRky/blob/main/LICENSE
 */

package com.example.quirky;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * @Author Jonathen Adsit
 * Controller class to sort the player population by the three statistics,
 * which are Most Total Points, Most Codes Scanned, and Largest Single QRCode Found
 */
public class LeaderBoardController {
    private final String TAG = "LeaderboardController says";
    private final ArrayList<Profile> HighestPoints;
    private final ArrayList<Profile> MostScanned;
    private final ArrayList<Profile> LargestCode;

    /**
     * Constructor to initialise the rankings with the player population
     * @param players The population of players to sort
     */
    public LeaderBoardController(ArrayList<Profile> players) {
        this.HighestPoints = sortPlayersByPoints(players);
        this.MostScanned = sortPlayersByNumScanned(players);
        this.LargestCode = sortPlayersByLargestScanned(players);
    }

    /**
     * Sort the population of players by total points
     * @param players The population of players
     * @return The sorted population
     */
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

    /**
     * Sort the population of players by most codes scanned
     * @param players The population of players
     * @return The sorted population
     */
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

    /**
     * Sort the population of players by largest code found
     * @param players The population of players
     * @return The sorted population
     */
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

    /**
     * Getter for the population ranked by total points
     * @return The sorted population
     */
    public ArrayList<Profile> getRankingPoints() {
        return HighestPoints;
    }

    /**
     * Getter for the population ranked by total codes scanned
     * @return The sorted population
     */
    public ArrayList<Profile> getRankingNumScanned() {
        return MostScanned;
    }

    /**
     * Getter for the population ranked by greatest code scanned
     * @return The sorted population
     */
    public ArrayList<Profile> getRankingLargestScanned() {
        return LargestCode;
    }

    /**
     * Find a player's ranking within the Total Points category
     * @param p The profile to find
     * @return The player's ranking, or -1, if the player does not exist
     */
    public int findRankPoints(Profile p) {
        for (int i = 0; i < HighestPoints.size(); i++) {
            if (p.getUname().equals(HighestPoints.get(i).getUname()))
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
        for (int i = 0; i < MostScanned.size(); i++) {
            if(p.getUname().equals(MostScanned.get(i).getUname()))
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
        for (int i = 0; i < LargestCode.size(); i++) {
            if(p.getUname().equals(LargestCode.get(i).getUname()))
                return i;
        }

        return -1;
    }
}
