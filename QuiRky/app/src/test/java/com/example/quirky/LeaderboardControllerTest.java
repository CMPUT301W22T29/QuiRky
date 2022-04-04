/*
 * Copyright (c) 2022. CMPUT301W22T29
 * Subject to MIT License
 * See full terms at https://github.com/CMPUT301W22T29/QuiRky/blob/main/LICENSE
 */

package com.example.quirky;

import org.junit.*;

import static org.junit.Assert.*;

import java.util.ArrayList;

// FIXME: this test is very likely to be buggy, and return pessimistically inaccurate results. Implement setters for a Profile's stats to make setup easier and less error prone.
public class LeaderboardControllerTest {
    static LeaderBoardController lc;


    static Profile mostScanned;
    static Profile mostPoints;
    static Profile HighRoller;
    static Profile bottom;
    static ArrayList<Profile> players;

    // Unfortunately there is no setter for the 3 Statistics in a Profile, so they must be carefully set by adding Strings to their list of scanned codes.
    /* Attempted to recreate the following ordering:
        most points acquired:
            mostPoints
            HighRoller
            mostScanned
            bottom


        most scanned codes:
            mostScanned
            mostPoints
            HighRoller
            bottom

        largest single code scanned:
            HighRoller
            mostPoints
            mostScanned
            bottom
     */
    @BeforeClass
    public static void setup() {
        String scanned1 = "a";
        String scanned2 = "b";  // Can not add duplicate strings to a profile's list of scanned codes
        String scanned3 = "c";  // So several unique strings are needed

        String scanned4 = "ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ";    // Longer strings will result in a larger score
        String scanned5 = "ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZY";
        String scanned6 = "ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ"; // This should be the highest scoring string

        ArrayList<String> scanned = new ArrayList<>();

        bottom = new Profile("", "", "", scanned);

        scanned.add(scanned1);
        scanned.add(scanned2);
        scanned.add(scanned3);
        mostScanned = new Profile("", "", "", scanned);

        scanned.clear();
        scanned.add(scanned4);
        scanned.add(scanned5);
        mostPoints = new Profile("", "", "", scanned);

        scanned.clear();
        scanned.add(scanned6);
        HighRoller = new Profile("", "", "", scanned);


        // The profiles have been initialised with closely controlled statistics.
        // Add them to the population of players

        players = new ArrayList<>();
        players.add(bottom);
        players.add(mostPoints);
        players.add(mostScanned);
        players.add(HighRoller);
    }


    // The population has been correctly initialised, test if the leaderboard correctly sorts by the points the profile's have acquired
    @Test
    public void TestSortA() {
        lc = new LeaderBoardController(players);
        ArrayList<Profile> result = new ArrayList<>();
        ArrayList<Profile> expected = new ArrayList<>();

        result.ensureCapacity(4);
        expected.ensureCapacity(4);

        expected.add(0, mostPoints);
        expected.add(1, HighRoller);
        expected.add(2, mostScanned);
        expected.add(3, bottom);
        result = lc.getRankingPoints();
        assertEquals("Sort players by most points failed", expected, result);


        expected.clear();
        result.clear();

        expected.add(0, mostScanned);
        expected.add(1, mostPoints);
        expected.add(2, HighRoller);
        expected.add(3, bottom);
        result = lc.getRankingNumScanned();
        assertEquals("Sort by number scanned failed", expected, result);


        expected.clear();
        result.clear();

        expected.add(0, HighRoller);
        expected.add(1, mostPoints);
        expected.add(2, mostScanned);
        expected.add(3, bottom);
        result = lc.getRankingLargestScanned();
        assertEquals("Sort by largest single qrcode scanned failed", expected, result);
    }


    // Test if the controller correctly sorts the population by number of qr codes scanned
    @Test
    public void TestSortB() {
        lc = new LeaderBoardController(players);
        ArrayList<Profile> result = new ArrayList<>();
        ArrayList<Profile> expected = new ArrayList<>();
        result.ensureCapacity(4);
        expected.ensureCapacity(4);

        expected.add(0, mostScanned);
        expected.add(1, mostPoints);
        expected.add(2, HighRoller);
        expected.add(3, bottom);
        result = lc.getRankingNumScanned();
        assertEquals("Sort by number scanned failed", expected, result);
    }

    // Test if the controller correctly sorts the population by largest code scanned
    @Test
    public void TestSortC() {
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
        assertEquals("Sort by largest single qrcode scanned failed", expected, result);
    }



    // Test that looking up a player in the leaderboards returns the correct position
    @Test
    public void TestLookup() {
        lc = new LeaderBoardController(players);
        ArrayList<Profile> result = lc.getRankingNumScanned();

        int position;

        position = lc.findRankScanned(mostScanned);
        assertEquals(0, position);

        position = lc.findRankScanned(bottom);
        assertEquals(3, position);

        position = lc.findRankScanned(new Profile(""));
        assertEquals(-1, position);
    }
}
