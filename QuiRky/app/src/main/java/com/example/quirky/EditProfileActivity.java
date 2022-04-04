package com.example.quirky;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Activity to change a profile's information. Can change the username, email, and phone number.
 * Changes are saved to local memory and FireStore
 */
public class EditProfileActivity extends AppCompatActivity {
    EditText name, email, phone;
    Button cancel, save;
    Profile p;
    String originalUsername;
    MemoryController mc;
    DatabaseController dc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_profile);

        Intent i = getIntent();
        p = (Profile) i.getSerializableExtra("profile");

        originalUsername = p.getUname();

        mc = new MemoryController(this);
        dc = new DatabaseController(this);

        name = findViewById(R.id.EditProfileInput1);
        email = findViewById(R.id.EditProfileInput3);
        phone = findViewById(R.id.EditProfileInput2);
        save = findViewById(R.id.save_new_profile);
        cancel = findViewById(R.id.cancel_save_profile);

        name.setText(p.getUname());
        email.setText(p.getEmail());
        phone.setText(p.getPhone());

        save.setOnClickListener(view -> updateProfile());
        cancel.setOnClickListener(view -> exit());
    }

    /**
     * Called once the user clicks the save button.
     * Gets the new values from the input fields and determines if a new username was inputted.
     * If so, checks FireStore if the username was taken
     * Calls write() to do the saving
     */
    private void updateProfile() {
            String new_name = name.getText().toString();
            String new_mail = email.getText().toString();
            String new_phone = phone.getText().toString();

            p.setUname(new_name);
            p.setEmail(new_mail);
            p.setPhone(new_phone);

            // Check if user wants to change their username. Changing username complicates things.
            if(!p.getUname().equals(originalUsername)) {

                // Must check that the new username is not taken
                dc.startCheckProfileExists(p.getUname()).addOnCompleteListener(task -> {
                    boolean exists = dc.checkProfileExists(task);

                    if(exists) {
                        Toast.makeText(this, "That username is taken!", Toast.LENGTH_LONG).show();
                    } else {
                        write(true);
                    }

                });
            }

            write(false);
    }

    /**
     * Writes the new information to local memory and FireStore. If a new username was selected, creates a new profile in FireStore with the username, and deletes the old profile.
     * @param newUsername If a new username was selected
     */
    private void write(Boolean newUsername) {
        mc.write(p);
        mc.writeUser(p.getUname());
        dc.writeProfile(p);

        if(newUsername) {
            dc.deleteProfile(originalUsername);
        }

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