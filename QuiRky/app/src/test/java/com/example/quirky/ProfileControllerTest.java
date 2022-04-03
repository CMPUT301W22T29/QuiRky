/*
 * Copyright (c) 2022. CMPUT301W22T29
 * Subject to MIT License
 * See full terms at https://github.com/CMPUT301W22T29/QuiRky/blob/main/LICENSE
 */

package com.example.quirky;

import org.junit.*;

import static org.junit.Assert.*;

import java.util.ArrayList;

public class ProfileControllerTest {

    @Test
    public void CalculateStatsTest() {
        String id1 = "a";
        String id2 = "abc123";

        int score1 = QRCodeController.score(id1);
        int score2 = QRCodeController.score(id2);
        int sum = score1 + score2;

        ArrayList<String> scanned = new ArrayList<>();
        scanned.add(id1);
        scanned.add(id2);

        Profile p = new Profile("", "", "", scanned);

        assertEquals(2, ProfileController.calculateTotalScanned(p));
        assertEquals(sum, ProfileController.calculateTotalPoints(p));
        assertEquals(score2, ProfileController.calculateGreatestScore(p));
    }
}
