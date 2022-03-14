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

    @Ignore("FIXME: @BeforeClass and @AfterClass want clearMem() to be static, but then it can't use context and therefore access files")
    //@BeforeClass
    //@AfterClass
    public void clearMem() {
        ct = getApplicationContext();
        dir = getApplicationContext().getFilesDir();

        // Empty the local memory. Naive double loop approach to clear every directory.
        for(String directory: dir.list()) {
            if(directory == null) break;
            f = new File(dir, directory);
            if(f.isDirectory()) {
                for(String filename : f.list()) {
                    if(filename == null) break;
                    File file = new File(f, filename);
                    file.delete();
                }
            }
            f.delete();
        }
    }

    @Before
    public void reset() {
        ct = getApplicationContext();
        dir = new File(ct.getFilesDir(), "Test");
        mm = new MemoryManager(ct, "Test");
        f = null;
    }

    @Test
    public void TestDirectoryMake() {
        // Assert it's empty
        assertFalse("Test directory already exists!", mm.exist());
        mm.make();
        // Assert the folder now exists
        assertTrue("The folder was not made correctly!", mm.exist());

        // Assert the directory is now gone
        assertFalse("The folder deletion did not work!", mm.exist());
    }

    @Test
    public void TestReadWriteHappyPath() {
        if(!mm.exist()) mm.make();
        assertTrue(mm.write("sample", "data"));

        assertEquals("The read value was not equal to the written value","sample", mm.read("sample"));
    }
}
