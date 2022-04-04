/*
 * Copyright (c) 2022. CMPUT301W22T29
 * Subject to MIT License
 * See full terms at https://github.com/CMPUT301W22T29/QuiRky/blob/main/LICENSE
 */

package com.example.quirky;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.app.Activity;


import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class Owner_DeletePlayerTest {

    private Solo solo;

    @Rule
    public ActivityTestRule rule = new ActivityTestRule(delete_Players.class,true,true);

    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }

    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
    }
    //this will delete jiawei from player list
    //Before test it out, need to add an account using jiawei as username first
    @Test
    public void checkDeletePlayer(){
        solo.assertCurrentActivity("Wrong Activity",delete_Players.class);
        assertTrue(solo.searchText("jiawei"));
        solo.clickOnText("jiawei");
        solo.clickOnButton("Yes");
        assertFalse(solo.searchText("jiawei"));
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

