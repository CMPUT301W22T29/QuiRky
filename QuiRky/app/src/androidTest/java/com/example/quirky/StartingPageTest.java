/*
 * Copyright (c) 2022. CMPUT301W22T29
 * Subject to MIT License
 * See full terms at https://github.com/CMPUT301W22T29/QuiRky/blob/main/LICENSE
 */

package com.example.quirky;

import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.app.Instrumentation;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class StartingPageTest {

    private Solo solo;

    @Rule
    public ActivityTestRule rule = new ActivityTestRule(StartingPageActivity.class,true,true);

    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }

    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
    }

    @Test
    public void checkManageCodes(){
        solo.assertCurrentActivity("Wrong Activity",StartingPageActivity.class);
        solo.clickOnButton("Manage Codes");
        solo.assertCurrentActivity("Wrong Activity", ManageCodesActivity.class);
        assertTrue(solo.searchText("1115083"));
    }

}
