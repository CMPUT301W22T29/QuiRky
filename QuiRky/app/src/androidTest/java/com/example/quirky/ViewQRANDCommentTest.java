/*
 * Copyright (c) 2022. CMPUT301W22T29
 * Subject to MIT License
 * See full terms at https://github.com/CMPUT301W22T29/QuiRky/blob/main/LICENSE
 */

package com.example.quirky;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.quirky.activities.StartingPageActivity;
import com.example.quirky.activities.ViewQRActivity;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class ViewQRANDCommentTest {

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
    //Need a QRCode in the ManageCodes to click on
    @Test
    public void checkComments(){
        solo.clickOnButton("Manage Codes");
        solo.clickOnText("1115083");//QRCode that you scanned before testing
        solo.assertCurrentActivity("Wrong Activity", ViewQRActivity.class);
        solo.clickOnButton("View Comments");
        solo.enterText((EditText) solo.getView(R.id.editText_comment), "TestComments");
        solo.clickOnButton("Save");
        assertTrue(solo.searchText("TestComments"));
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

