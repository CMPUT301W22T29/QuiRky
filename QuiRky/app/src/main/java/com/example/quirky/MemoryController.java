package com.example.quirky;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/*
Root directory is getApplicationContext.getFilesDir().
    Subdirectory in root directory named after the player's chosen username
    Several files in this directory holding information about the user's profile
    Files include:
        email: holds user's email
        phone: holds user's cell number
    This will likely be updated when we determine what we want to store.

    Diagram:
    context.getFilesDir()
        player1     (username)
            email
            phone
*/

/**
 * @author Jonathen Adsit
 * MemoryController: manages reading & writing from local memory.
 * There are two files MemoryController reads & writes to:
 *      One is the profile file. Here the Profile object representing the app-holder is stored.
 *      The other file is the name file, which stores the name of the profile seperately, for ease of access.
 */
public class MemoryController {

    private static File dir;
    private String user;

    /**
     * Constructor that takes in the app-holder's username. Only to be used when logging in with the app for the first time.
     * If the user already exists in memory, calling this constructor with a new username will overwrite the name stored in the name file.
     * @param ct Context to get the files directory
     * @param user Username of the app-holder
     */
    public MemoryController(Context ct, String user) {
        MemoryController.dir = ct.getFilesDir();
        this.user = user;

        // Delete the name file in memory, if it exists
        File file = new File(dir, "name");
        if(file.exists())
            file.delete();

        // Write to the name file the app-holder's username
        try {
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file, false);

            fos.write(user.getBytes());

            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Constructor assumes the user's folder already exists in memory
     * @param ct
     *  - Context to get the files directory
     * @throws AssertionError
     *  - Will throw an assertion error if the user's folder does not yet exist in memory
     */
    public MemoryController(Context ct) {
        MemoryController.dir = ct.getFilesDir();
        this.user = readUser();
    }

    /**
     * Write a profile to local memory
     * @param p The profile to be written to local memory. Will overwrite any existing data.
     * @return Boolean, representing if the write was successful.
     */
    public Boolean write(Profile p) {

        assert p != null : "A null object was passed to MemoryController.write(Profile p)!";
        File file = new File(dir, "profile");
        if(file.exists())
            file.delete();

        try {
            file.createNewFile();

            FileOutputStream fos = new FileOutputStream(file, false);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(p);

            oos.flush();
            fos.flush();

            oos.close();
            fos.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Read the Profile stored in memory.
     * @return The profile stored in memory
     */
    public Profile read() {
        File file = new File(dir, "profile");
        assert file.exists() : "Player's file does not exist, can not read it!";
        Profile p;

        try {
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);

            p = (Profile) ois.readObject();

            ois.close();
            fis.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Issue reading from local memory!");
        }

        return p;
    }

    /**
     * Read the app-holder's username from memory. Used only by the constructor
     * @return user's username
     */
    private String readUser() {
        File file = new File(dir, "name");

        String uname;
        FileInputStream fis;
        try {
            fis = new FileInputStream(file);

            // Loop to get the length of the file
            int length = 0;
            while(fis.available() > 0) {
                length += 1;
                fis.skip(1);
            }

            // We have already skipped to the end of the file, must reopen it to read data.
            fis.close();
            fis = new FileInputStream(file);

            // Initialize a byte array of the length we read in the while() loop
            byte[] temp = new byte[length];
            fis.read(temp);

            // Construct a string from the byte[]
            uname = new String(temp);

            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error reading from local memory!");
        }

        return uname;
    }

    /**
     * Getter for the username of the app-holder
     * @return Username of the app user
     */
    public String getUser() {
        return this.user;
    }

    public static Boolean exists() {
        File file = new File(dir, "name");
        return file.exists();
    }
}
