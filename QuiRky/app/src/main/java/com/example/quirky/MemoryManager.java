package com.example.quirky;
import android.annotation.SuppressLint;
import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

/* TODO: consider if the folder that stores the user's profile information should...
        be named after the device id
        or simply be called the profile folder
 */
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
        preferences
            geolocation (some user settings we want to save)
            somethingelse
*/
public class MemoryManager {

    private File dir;
    private final String id;
    private final Context ct;

    @SuppressLint("HardwareIds")
    public MemoryManager(Context ct, String id) {
        this.ct = ct;
        this.id = id;
        this.dir = new File(ct.getFilesDir(), id);
    }

    public boolean exist() {
        return dir.exists();
    }
    public void make() { dir.mkdir(); }

    // Read a file from memory.
    // Uses: read("name"), read("email"), or read("phone").
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

    // Write a string to local memory
    public void write(String location, String data) {
        File file = new File(dir, location);
        FileOutputStream fos;

        try {
            file.createNewFile();
            fos = new FileOutputStream(file, false);

            fos.write( data.getBytes()  );

            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Sets the folder to a given folder. Presumably this will only be called with setFolder("preferences")
    public void setFolder(String dir) {
        this.dir = new File(ct.getFilesDir(), dir);
        this.dir.mkdir();
    }

    // Sets the folder back to the profile folder
    public void setFolder() {
        this.dir = new File(ct.getFilesDir(), id);
    }

    // Delete the user's folder from local memory
    public void delete() {
        File file;

        file = new File(dir, "name");
        file.delete();

        file = new File(dir, "email");
        file.delete();

        file = new File(dir, "phone");
        file.delete();

        dir.delete();
    }
}
