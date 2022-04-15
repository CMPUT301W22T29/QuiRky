/*
 * Copyright (c) 2022. CMPUT301W22T29
 * Subject to MIT License
 * See full terms at https://github.com/CMPUT301W22T29/QuiRky/blob/main/LICENSE
 */

package com.example.quirky;

import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.quirky.activities.EditProfileActivity;
import com.example.quirky.activities.GenerateActivity;
import com.example.quirky.activities.HubActivity;
import com.example.quirky.activities.LeaderBoardActivity;
import com.example.quirky.activities.ManageCodesActivity;
import com.example.quirky.activities.MapActivity;
import com.example.quirky.activities.PlayerSearchActivity;
import com.example.quirky.activities.ProfileActivity;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class StartingPageTest {

    private Solo solo;

    @Rule
    public ActivityTestRule rule = new ActivityTestRule(HubActivity.class,true,true);

    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }

    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
    }
//Need to Scan Some Codes If this is a new account
    @Test
    public void checkManageCodes(){
        solo.assertCurrentActivity("Wrong Activity", HubActivity.class);
        solo.clickOnButton("Manage Codes");
        solo.assertCurrentActivity("Wrong Activity", ManageCodesActivity.class);
        assertTrue(solo.searchText("1115083"));//QRCode that you scanned
    }
    @Test
    public void checkGenerateCodes(){
        solo.assertCurrentActivity("Wrong Activity", HubActivity.class);
        solo.clickOnButton("Generate Codes");
        solo.assertCurrentActivity("Wrong Activity", GenerateActivity.class);
        solo.enterText((EditText) solo.getView(R.id.genertateByTextField), "testTest");
        //it generated a QR Code for you to see
    }
    //In order to test this, we need to create a user name jiawei3
    @Test
    public void checkMyProfileInfo(){
        solo.assertCurrentActivity("Wrong Activity", HubActivity.class);
        solo.clickOnButton("My Profile");
        solo.assertCurrentActivity("Wrong Activity", HubActivity.class);
        solo.clickOnButton("My Profile Info");
        solo.assertCurrentActivity("Wrong Activity", ProfileActivity.class);
        assertTrue(solo.searchText("jiawei3"));//user name
        solo.clickOnButton("Change Profile");
        solo.assertCurrentActivity("Wrong Activity", EditProfileActivity.class);
        solo.clearEditText((EditText) solo.getView(R.id.EditProfileInput1));
        solo.clearEditText((EditText) solo.getView(R.id.EditProfileInput2));
        solo.clearEditText((EditText) solo.getView(R.id.EditProfileInput3));
        solo.enterText((EditText) solo.getView(R.id.EditProfileInput1), "jiawei4");
        solo.enterText((EditText) solo.getView(R.id.EditProfileInput3), "QuiRky@ualberta.ca");
        solo.enterText((EditText) solo.getView(R.id.EditProfileInput2), "1233455678");
        solo.clickOnButton("Save");
        solo.assertCurrentActivity("Wrong Activity", ProfileActivity.class);
        assertTrue(solo.searchText("jiawei4"));
        assertTrue(solo.searchText("QuiRky@ualberta.ca"));
        assertTrue(solo.searchText("1233455678"));
    }

    @Test
    public void checkSearchOtherUsers(){
        solo.assertCurrentActivity("Wrong Activity", HubActivity.class);
        solo.clickOnButton("Community");
        solo.assertCurrentActivity("Wrong Activity", HubActivity.class);
        solo.clickOnButton("Search Other Users");
        solo.assertCurrentActivity("Wrong Activity", PlayerSearchActivity.class);
    }
    //Before testing on Buttons FIND MY POSITION, you need to create an account
    @Test
    public void checkTheLeaderBoards(){
        solo.assertCurrentActivity("Wrong Activity", HubActivity.class);
        solo.clickOnButton("Community");
        solo.assertCurrentActivity("Wrong Activity", HubActivity.class);
        solo.clickOnText("The Leaderboards");
        solo.assertCurrentActivity("Wrong Activity", LeaderBoardActivity.class);
        solo.clickOnButton("Find My Position");
        assertTrue(solo.searchText("jiawei3"));//username of your account
        solo.clickOnButton("Top Players");
        assertTrue(solo.searchText("JJ"));//The top player in the game when you run the test
    }

    @Test
    public void checkNearbyQRCodes(){
        solo.assertCurrentActivity("Wrong Activity", HubActivity.class);
        solo.clickOnButton("Community");
        solo.assertCurrentActivity("Wrong Activity", HubActivity.class);
        solo.clickOnText("Nearby QR Codes");
        solo.assertCurrentActivity("Wrong Activity", MapActivity.class);
    }
    /**
     * Close activity after each test
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}
