/*
 * Copyright (c) 2022. CMPUT301W22T29
 * Subject to MIT License
 * See full terms at https://github.com/CMPUT301W22T29/QuiRky/blob/main/LICENSE
 */

package com.example.quirky.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.quirky.ListeningList;
import com.example.quirky.controllers.MemoryController;
import com.example.quirky.models.Profile;
import com.example.quirky.controllers.ProfileController;
import com.example.quirky.R;
import com.example.quirky.controllers.DatabaseController;

/**
 * Activity to change a profile's information. Can change the username, email, and phone number.
 * Changes are saved to local memory and FireStore
 */
public class EditProfileActivity extends AppCompatActivity {
    EditText name, email, phone;
    Button cancel, save;
    Profile p;
    MemoryController mc;
    DatabaseController dc;

    String original_username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Intent i = getIntent();
        p = (Profile) i.getSerializableExtra("profile");
        if(p == null)
            ExitWithError();

        original_username = p.getUname();

        mc = new MemoryController(this);
        dc = new DatabaseController();

        name = findViewById(R.id.EditProfileInput1);
        email = findViewById(R.id.EditProfileInput3);
        phone = findViewById(R.id.EditProfileInput2);
        save = findViewById(R.id.save_new_profile);
        cancel = findViewById(R.id.cancel_save_profile);

        name.setText(p.getUname());
        email.setText(p.getEmail());
        phone.setText(p.getPhone());

        save.setOnClickListener(view -> checkName());
        cancel.setOnClickListener(view -> exit());
    }

    /**
     * Method called when data is passed to this activity incorrectly, or when there is an issue reading the data from FireStore.
     * Makes a toast and then finishes the activity.
     */
    private void ExitWithError() {
        Toast.makeText(this, "User was passed incorrectly!", Toast.LENGTH_SHORT).show();
        finish();
    }

    /**
     * Called once the user clicks the save button.
     * Gets the value from the username field and checks if it is new.
     * If so, checks if the username is valid and checks if it is already taken
     * Calls write() to do the saving
     */
    private void checkName() {
        String new_name = name.getText().toString();

        if(!p.getUname().equals(new_name)) {
            // First check that the username is valid
            if (!ProfileController.validUsername(new_name) ) {
                Toast.makeText(this, "That's not a valid username!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Then check if the username is taken by attempting to read the profile
            ListeningList<Boolean> result = new ListeningList<>();
            result.setOnAddListener(listeningList -> {
                if(listeningList.get(0)) {
                    Toast.makeText(getApplicationContext(), "That username is taken!", Toast.LENGTH_SHORT).show();
                } else {
                    p.setUname(new_name);
                    write();
                }
            });
            dc.userExists(new_name, result);
        } else {
            write();
        }
    }

    /**
     * Writes the new information to local memory and FireStore
     */
    private void write() {
        String new_mail = email.getText().toString();
        String new_phone = phone.getText().toString();

        p.setEmail(new_mail);
        p.setPhone(new_phone);

        mc.writeUser(p);
        dc.writeProfile(original_username, p);
        Toast.makeText(this, "Profile Updated", Toast.LENGTH_SHORT).show();
        exit();
    }

    /**
     * Finishes the activity
     */
    private void exit() {
        finish();
    }
}