package com.example.quirky;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

/*
Root directory is getApplicationContext.getFilesDir().
    Subdirectory in root directory named after the device's ID
    Several files in this directory holding information about the user's profile
    Files include:
        name:  holds user profiles name
        email: holds user's email
        phone: holds user's cell number
    This will likely be updated when we determine what we want to store.

    Diagram:
    context.getFilesDir()
        am43xg54op (some hardware id)
            name
            email
            phone
*/

/**
 * @author Jonathen Adsit
 * MemoryController: manages reading & writing from local memory.
 * In memory there is only one directory, named after the hardware ID of the device
 * In this directory there is one file each to save the following user information:
 *  name
 *  email
 *  phone
 */
public class MemoryManager {

    private final File dir;
    private final String id;
    private final Context ct;

    public MemoryManager(Context ct, String id) {
        this.ct = ct;
        this.id = id;
        this.dir = new File(ct.getFilesDir(), id);
    }

    /**
     * Constructor assumes the user's folder already exists in memory
     * @param ct
     *  - Context to get the files directory
     * @throws AssertionError
     *  - Will throw an assertion error if the user's folder does not already exist in memory
     */
    public MemoryManager(Context ct) throws AssertionError {
        this.ct = ct;

        String[] temp = ct.getFilesDir().list();
        assert temp != null : "Something went wrong with the memory reader!";
        assert temp.length > 0 : "Cannot use the empty constructor if the user's file has not been created yet!";
        this.id = temp[0];

        this.dir = new File(ct.getFilesDir(), id);
    }

    /**
     * Checks if the user's folder exists in memory yet
     * @return
     *      Boolean representing whether the folder exists
     */
    public boolean exist() {
        return dir.exists();
    }

    /**
     * Make a folder in the files directory named after the ID of the android device
     */
    public void make() {
        dir.mkdir();
    }

    /**
     * Read a file in the user's folder. Reads the entire file as one string.
     * @param field
     *      - Name of the file to be read
     * @return
     *      - The string read from the file.
     */
    public String read(String field) {
        File file = new File(dir, field);
        FileInputStream fis;

        try {
            fis = new FileInputStream(file);
            String data;

            // Loop through to get the length of the file
            int length = 0;
            while(fis.available() > 0) {
                length += 1;
                fis.skip(1);
            }

            // We have already skipped to the end of the file, must reopen it to reset cursor back to start of file.
            fis.close();
            fis = new FileInputStream(file);

            // Read the whole file at once. Typecast to string.
            byte[] temp = new byte[length];
            fis.read(temp);
            data = Arrays.toString(temp);

            fis.close();
            return data;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ""; // This return statement will never be reached. It exists so AndroidStudio will stop complaining.
    }

    /**
     * Write a single string to a file in local memory. Write operation overwrites any existing data.
     * @param location
     *      - The name of the file to write to
     * @param data
     *      - The string to write to the file.
     */
    public void write(String location, String data) {
        File file = new File(dir, location);
        FileOutputStream fos;

        if(file.exists())
            file.delete();

        try {
            file.createNewFile();
            fos = new FileOutputStream(file, false);

            fos.write( data.getBytes()  );

            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
