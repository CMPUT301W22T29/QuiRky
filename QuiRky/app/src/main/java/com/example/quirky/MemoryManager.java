package com.example.quirky;
import android.annotation.SuppressLint;
import android.content.Context;
import android.provider.Settings;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;

// TODO: determine if this class even needs to exist. Should QuiRky write to local memory at all?
// TODO: Also considering we have a database we can already store to.
/*
Root directory is getApplicationContext.getFilesDir().
    Subdirectory in root directory named after the device's ID
    Several files in this directory holding information about the user's profile
    Files include:
        name:  holds user profiles name
        email: holds user's email
        phone: holds user's cell number
    This will likely be updated when we determine what we want to store.
*/
public class MemoryManager {

    private final File dir;
    private final String id;
    private final Context ct;

    @SuppressLint("HardwareIds")
    public MemoryManager(Context ct) {
        /*
        Code for getting unique device ID taken from:
        https://stackoverflow.com/a/2785493
        Written by user:
        https://stackoverflow.com/users/166712/anthony-forloney
        Published May 7 2010
        */

        this.ct = ct;
        id = Settings.Secure.getString(ct.getContentResolver(), Settings.Secure.ANDROID_ID);
        this.dir = new File(ct.getFilesDir(), id);
    }

    // Read the user's profile from memory
    public Profile read() {
        Profile p;
        p = new Profile();

        File file;
        FileInputStream fis;

        String name, email, phone;

        // Read File: name - - - - - - - - - - - - - - - - - - - -
        file = new File(dir, "name");
        try {
            fis = new FileInputStream(file);

            // Loop to get the length of the file
            int length = 0;
            while(fis.available() > 0) {
                length += 1;
                fis.skip(1);
            }

            // Read the whole file at once. Typecast to string.
            // We have already skipped to the end of the file, must reopen it to reset cursor.
            fis.close();
            fis = new FileInputStream(file);
            byte[] temp = new byte[length];
            fis.read(temp);
            name = new String(temp);

            fis.close();
        } catch (IOException e) { e.printStackTrace(); }

        // Read File: email - - - - - - - - - - - - - - - - - - - -
        file = new File(dir, "email");
        try {
            fis = new FileInputStream(file);

            // Loop to get the length of the file
            int length = 0;
            while(fis.available() > 0) {
                length += 1;
                fis.skip(1);
            }

            // Read the whole file at once. Typecast to string.
            // We have already skipped to the end of the file, must reopen it to reset cursor.
            fis.close();
            fis = new FileInputStream(file);
            byte[] temp = new byte[length];
            fis.read(temp);
            name = new String(temp);

            fis.close();
        } catch (IOException e) { e.printStackTrace(); }

        // Read File: phone - - - - - - - - - - - - - - - - - - - -
        file = new File(dir, "phone");
        try {
            fis = new FileInputStream(file);

            // Loop to get the length of the file
            int length = 0;
            while(fis.available() > 0) {
                length += 1;
                fis.skip(1);
            }

            // Read the whole file at once. Typecast to string.
            // We have already skipped to the end of the file, must reopen it to reset cursor.
            fis.close();
            fis = new FileInputStream(file);
            byte[] temp = new byte[length];
            fis.read(temp);
            name = new String(temp);

            fis.close();
        } catch (IOException e) { e.printStackTrace(); }

        return p;
    }

    // Write a profile to local memory
    public void write(Profile p) {
        File file;
        FileOutputStream fos;

        // Write the name File
        file = new File(dir, "name");
        try {
            file.createNewFile();
            fos = new FileOutputStream(file, false);

            fos.write( p.getName().getBytes()  );

            fos.close();
        } catch (IOException e) { e.printStackTrace(); }

        // Write the email File
        file = new File(dir, "email");
        try {
            file.createNewFile();
            fos = new FileOutputStream(file, false);

            fos.write( p.getEmail().getBytes()  );

            fos.close();
        } catch (IOException e) { e.printStackTrace(); }

        // Write the phone File
        file = new File(dir, "phone");
        try {
            file.createNewFile();
            fos = new FileOutputStream(file, false);

            fos.write( p.getPhone().getBytes()  );

            fos.close();
        } catch (IOException e) { e.printStackTrace(); }
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
