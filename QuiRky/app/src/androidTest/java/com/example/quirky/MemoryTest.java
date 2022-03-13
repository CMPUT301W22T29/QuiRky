package com.example.quirky;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

import static org.junit.Assert.*;

import android.content.Context;

import org.junit.*;

import java.io.File;

// Code for getting context in a Unit Test taken from:
// https://stackoverflow.com/a/56514421
// Written by
// https://stackoverflow.com/users/7098269/ehsan
// Published June 9, 2019
public class MemoryTest {
    MemoryManager mm;
    File dir, f;

    @Before
    public void setup() {
        Context ct = getApplicationContext();
        dir = getApplicationContext().getFilesDir();

        // Empty the local memory 
        for(String s: dir.list()) {
            f = new File(dir, s);
            if(f.isDirectory()) {
                for(String t : f.list()) {
                    File x = new File(f, t);
                    x.delete();
                }
            }
            f.delete();
        }
        mm = new MemoryManager(ct, "Test");
    }

    @Test
    public void TestDirectoryMake() {
        assertFalse(mm.exist());
        mm.make();
        assertTrue(mm.exist());


        // do something
    }

    @AfterClass
    private void delete() {
        // delete the user folder
    }
}
