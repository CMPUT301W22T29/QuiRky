/*
 * Copyright (c) 2022. CMPUT301W22T29
 * Subject to MIT License
 * See full terms at https://github.com/CMPUT301W22T29/QuiRky/blob/main/LICENSE
 */

package com.example.quirky;

import org.junit.*;

import static org.junit.Assert.*;

import com.example.quirky.controllers.ProfileController;
import com.example.quirky.controllers.QRCodeController;
import com.example.quirky.models.Profile;

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

    // This test checks that the ValidUsername method works properly.
    @Test
    public void TestValidUsername() {
        String username;
        boolean valid;

        username = "abc123";
        valid = ProfileController.validUsername(username);
        assertTrue("Failed abc123", valid);

        // Test boundaries
        username = "az09AZ";
        valid = ProfileController.validUsername(username);
        assertTrue("Failed az09AZ", valid);

        // Test failing cases
        username = "a`z";
        valid = ProfileController.validUsername(username);
        assertFalse("Failed a`z", valid);

        username = "Z[a";
        valid = ProfileController.validUsername(username);
        assertFalse("Failed Z[a", valid);

        username = "{abc13";
        valid = ProfileController.validUsername(username);
        assertFalse("Failed {abc13", valid);

        username = "yes+";
        valid = ProfileController.validUsername(username);
        assertFalse("Failed yes+", valid);

        username = "Spaces Test";
        valid = ProfileController.validUsername(username);
        assertFalse("Failed spaces test", valid);
    }
}
