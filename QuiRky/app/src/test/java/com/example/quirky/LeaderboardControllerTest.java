/*
 * Copyright (c) 2022. CMPUT301W22T29
 * Subject to MIT License
 * See full terms at https://github.com/CMPUT301W22T29/QuiRky/blob/main/LICENSE
 */

package com.example.quirky;

import org.junit.*;

import static org.junit.Assert.*;

import com.example.quirky.controllers.LeaderBoardController;
import com.example.quirky.models.Profile;

import java.util.ArrayList;

public class LeaderboardControllerTest {
    static LeaderBoardController lc;


    static Profile mostScanned;
    static Profile mostPoints;
    static Profile HighRoller;
    static Profile bottom;
    static ArrayList<Profile> players;

    @BeforeClass
    public static void setup() {
        mostPoints = new Profile("mostPoints");
        mostPoints.updateStats(999, 2, 50);

        mostScanned = new Profile("mostScanned");
        mostScanned.updateStats(50, 999, 2);

        HighRoller = new Profile("HighRoller");
        HighRoller.updateStats(2, 50, 999);

        bottom = new Profile("bottom");
        bottom.updateStats(0, 0, 0);

        players = new ArrayList<>();
        players.add(bottom);
        players.add(mostPoints);
        players.add(mostScanned);
        players.add(HighRoller);
    }


    // The population has been correctly initialised, test if the leaderboard correctly sorts by the points the profile's have acquired
    @Test
    public void TestSortPoints() {
        lc = new LeaderBoardController(players);
        ArrayList<Profile> result = new ArrayList<>();
        ArrayList<Profile> expected = new ArrayList<>();

        result.ensureCapacity(4);
        expected.ensureCapacity(4);

        expected.add(0, mostPoints);
        expected.add(1, mostScanned);
        expected.add(2, HighRoller);
        expected.add(3, bottom);
        result = lc.getRankingPoints();
        assertEquals("Sort players by most points failed\n", expected, result);
    }


    // Test if the controller correctly sorts the population by number of qr codes scanned
    @Test
    public void TestSortScanned() {
        lc = new LeaderBoardController(players);
        ArrayList<Profile> result = new ArrayList<>();
        ArrayList<Profile> expected = new ArrayList<>();

        result.ensureCapacity(4);
        expected.ensureCapacity(4);

        expected.add(0, mostScanned);
        expected.add(1, HighRoller);
        expected.add(2, mostPoints);
        expected.add(3, bottom);
        result = lc.getRankingNumScanned();
        assertEquals("Sort players by scanned failed\n", expected, result);
    }

    // Test if the controller correctly sorts the population by largest code scanned
    @Test
    public void TestSortLargest() {
        lc = new LeaderBoardController(players);
        ArrayList<Profile> result = new ArrayList<>();
        ArrayList<Profile> expected = new ArrayList<>();

        result.ensureCapacity(4);
        expected.ensureCapacity(4);

        expected.add(0, HighRoller);
        expected.add(1, mostPoints);
        expected.add(2, mostScanned);
        expected.add(3, bottom);
        result = lc.getRankingLargestScanned();
        assertEquals("Sort by largest single qrcode scanned failed\n", expected, result);
    }



    // Test that looking up a player in the leaderboards returns the correct position
    @Test
    public void TestLookup() {
        lc = new LeaderBoardController(players);
        int position;

        position = lc.findRankScanned(mostScanned);
        assertEquals("Lookup case 1 failed", 0, position);

        position = lc.findRankScanned(bottom);
        assertEquals("Lookup case 2 failed", 3, position);

        position = lc.findRankScanned(new Profile(""));
        assertEquals("Lookup case 3 failed", -1, position);
    }
}
