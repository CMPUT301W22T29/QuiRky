/*
 * Copyright (c) 2022. CMPUT301W22T29
 * Subject to MIT License
 * See full terms at https://github.com/CMPUT301W22T29/QuiRky/blob/main/LICENSE
 */

package com.example.quirky;

import android.app.Activity;
import android.app.Instrumentation;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class IntentTests {

    private Solo solo;

    @Rule
    public ActivityTestRule rule = new ActivityTestRule(MainActivity.class,true,true);

    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }

    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
    }


    @Test
    public void checkLogin(){
        solo.assertCurrentActivity("Wrong Activity",MainActivity.class);
        solo.clickOnButton("Login");
    }
    @Test
    public void checkButtons(){
        solo.assertCurrentActivity("Wrong Activity",MainActivity.class);
        solo.clickOnButton("Login");
        solo.assertCurrentActivity("Wrong Activity", StartingPageActivity.class);
    }
    @Test
    public void checkSettings(){
        solo.assertCurrentActivity("Wrong Activity",MainActivity.class);
        solo.clickOnButton("Settings");
        solo.assertCurrentActivity("Wrong Activity", SettingsActivity.class);
    }

}
