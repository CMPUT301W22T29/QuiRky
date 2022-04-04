package com.example.quirky;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Activity to view a profile
 */
public class ProfileViewerActivity extends AppCompatActivity {

    private Intent i;
    private Profile p;
    private MemoryController mc;

    private Button changeProfile;
    private TextView title, email, phone, rank1, rank2, rank3;

    boolean view_self; // Is the player viewing themself?

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        i = getIntent();
        p = (Profile) i.getSerializableExtra("profile");

        title = findViewById(R.id.profile_name);
        email = findViewById(R.id.email2);
        phone = findViewById(R.id.phone2);
        rank1 = findViewById(R.id.rank_points_field);
        rank2 = findViewById(R.id.rank_scanned_field);
        rank3 = findViewById(R.id.rank_largest_field);
        changeProfile = findViewById(R.id.change_profile_button);

        rank1.setText(String.valueOf(p.getPointsOfScannedCodes()));
        rank2.setText(String.valueOf(p.getNumberCodesScanned()));
        rank3.setText(String.valueOf(p.getPointsOfLargestCodes()));

        mc = new MemoryController(this);

        // Check if the username in local memory matches the username passed to this activity
        view_self = ( mc.readUser() ).equals(p.getUname());

        // If the user is viewing their own profile, show the profile button
        if( view_self ) {
            changeProfile.setOnClickListener(view -> changeProfile());
        } else {
            changeProfile.setVisibility(View.INVISIBLE);
        }
    }

    // Displayed information must be updated onResume in the event that the user is returning from EditProfileActivity
    @Override
    public void onResume() {
        super.onResume();

        if(view_self)
            p = mc.read();

        title.setText(p.getUname());
        email.setText(p.getEmail());
        phone.setText(p.getPhone());
    }

    /**
     * Start up an activity to let the user edit their profile
     */
    public void changeProfile() {
        Intent intent = new Intent(this, EditProfileActivity.class);
        intent.putExtra("profile", p);
        startActivity(intent);
    }
}