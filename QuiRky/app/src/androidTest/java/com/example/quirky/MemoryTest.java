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
    Context ct;

    @BeforeClass
    public void setup() {
        ct = getApplicationContext();
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
    }

    @Before
    public void reset() {
        dir = new File(ct.getFilesDir(), "Test");
        mm = new MemoryManager(ct, "Test");
        f = null;
    }

    @Test
    public void TestDirectoryMake() {
        // Assert it's empty
        assertFalse(mm.exist());
        mm.make();
        // Assert the folder now exists
        assertTrue(mm.exist());

        // Check that the delete worked
        assertTrue(mm.delete());
        // Assert the directory is now gone
        assertFalse(mm.exist());
    }

    @Test
    public void TestWrite() {
        return;
    }

    @Test
    public void TestRead() {
        return;
    }

    @AfterClass
    private void delete() {
        // delete the user folder
    }
}
