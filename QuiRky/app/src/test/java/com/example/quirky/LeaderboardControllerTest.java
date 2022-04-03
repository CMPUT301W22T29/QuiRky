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

    // Unfortunately there is no setter for the 3 Statistics in a Profile, so they must be carefully set by adding Strings to their list of scanned codes.
    // TODO: Double check how scoring algorithm works and double check your string combinations will work. This test is very likely to produce bugs, and we likely just need a method to directly set the stat values of a Profile.
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

        ArrayList<String> x = new ArrayList<>();

        bottom = new Profile("", "", "", x);

        x.add(scanned1);
        x.add(scanned2);
        x.add(scanned3);
        mostScanned = new Profile("", "", "", x);

        x.clear();
        x.add(scanned4);
        x.add(scanned5);
        mostPoints = new Profile("", "", "", x);

        x.clear();
        x.add(scanned6);
        HighRoller = new Profile("", "", "", x);


        // The profiles have been initialised with closely controlled statistics.
        // Time to initialise a LeaderBoardController with these profiles as a data set.

        ArrayList<Profile> players = new ArrayList<>();
        players.add(bottom);
        players.add(mostPoints);
        players.add(mostScanned);
        players.add(HighRoller);

        lc = new LeaderBoardController(players);
    }


    // Now that the effort of initialising a controller with a population of players has been done, we can now test the controller's sorting
    @Test
    public void TestSorting() { // TODO: Look up ArrayList<> documentation to double check how to manually sort one
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
        assertEquals("Sort by number scnned failed", expected, result);


        expected.clear();
        result.clear();

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
        ArrayList<Profile> result = lc.getRankingNumScanned();

        int position;

        position = lc.findPlayer(mostScanned);
        assertEquals(0, position);

        position = lc.findPlayer(bottom);
        assertEquals(4, position);

        position = lc.findPlayer(new Profile(""));
        assertEquals(-1, position);
    }
}
