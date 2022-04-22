/*
 * Copyright (c) 2022. CMPUT301W22T29
 * Subject to MIT License
 * See full terms at https://github.com/CMPUT301W22T29/QuiRky/blob/main/LICENSE
 */

package com.example.quirky.controllers;

import android.content.Context;

import com.example.quirky.models.Profile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/*
Data Storage is as follows:
    Context.getFilesDir()    (directory)
        player_data          (directory)
            name             (file)
                The player's username stored separately for ease of access

            profile          (file)
                Stores the serialized Profile object representing the player
*/

/**
 * @author Jonathen Adsit
 * MemoryController: manages reading & writing from local memory.
 * There are two files MemoryController reads & writes to:
 *      One is the profile file. Here the Profile object representing the app-holder is stored.
 *      The other is the name file, which stores the name of the profile seperately, for ease of access.
 */
public class MemoryController {

    private final File dir;

    /**
     * MemoryController Constructor. Gets the directory for the app's files using the passed context.
     * @param ct Context to get the files directory
     */
    public MemoryController(Context ct) {
        this.dir = new File(ct.getFilesDir(), "player_data");
        if(!dir.exists())
            dir.mkdir();
    }

    /**
     * Write a profile to local memory
     * @param p The profile to be written to local memory. Will overwrite any existing data.
     * @return True if the write was successful, false otherwise
     */
    public Boolean writeUser(Profile p) {

        assert p != null : "A null object was passed to MemoryController.write(Profile p)!";
        File file = new File(dir, "profile");

        if(file.exists())
            file.delete();

        try {
            file.createNewFile();

            /*
            ObjectOutputStream code taken from
            https://www.youtube.com/watch?v=4vEBgNFvuIw
            Made by:
            https://www.youtube.com/channel/UCicaZPOiZ3WLvfI3i9OQwUg
            Published December 1, 2015
             */
            FileOutputStream fos = new FileOutputStream(file, false);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(p);

            oos.close();
            fos.close();

            return writeUsername( p.getUname() );
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Write a username to memory. Will overwrite any data already stored in the 'name' file.
     * @param user The new username to write
     */
    private boolean writeUsername(String user) {
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

            /*
            ObjectInputStream code taken from:
            https://www.youtube.com/watch?v=vzfSQVkmgBw
            By Author:
            https://www.youtube.com/channel/UCicaZPOiZ3WLvfI3i9OQwUg
            Published Dec 1, 2015
             */
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
     * Read the app-holder's username from memory.
     * @return user's username
     */
    public String readUser() {
        File file = new File(dir, "name");

        String user;

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
            user = new String(temp);

            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error reading from local memory!");
        }

        return user;
    }

    /**
     * Checks if the 'name' file under the dir/player_data exists
     * @return True if the file exists, false otherwise.
     */
    public boolean exists() {
        File file = new File(dir, "name");
        return file.exists();
    }

    public boolean deleteUser() {
        File file = new File(dir, "profile");
        if(file.exists())
            file.delete();

        file = new File(dir, "name");
        if(file.exists())
            file.delete();

        return dir.delete();
    }
}
