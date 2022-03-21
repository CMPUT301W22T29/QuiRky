package com.example.quirky;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ProfileViewerActivity extends AppCompatActivity {

    Intent i;
    Profile p;
    private Button changeProfile;
    private TextView title, email, phone, rank1, rank2, rank3;

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

        title.setText(p.getUname());
        email.setText(p.getEmail());
        phone.setText(p.getPhone());
        rank1.setText(String.valueOf(p.getRankingPoints()));
        rank2.setText(String.valueOf(p.getRankingScanned()));
        rank3.setText(String.valueOf(p.getRankingBiggestCode()));

        //MemoryController mc = new MemoryController(this);
        //String appholder = mc.getUser();
        String appholder = p.getUname(); // TODO: Refactor MC to get username of appholder more easily.

        // If the user is viewing their own profile, show the profile button
        if(appholder == p.getUname()) {
            changeProfile.setOnClickListener(view -> changeProfile());
        } else {
            changeProfile.setVisibility(View.INVISIBLE);
        }
    }

    public void changeProfile() {
        Intent intent = new Intent(this, EditProfileActivity.class);
        startActivity(intent);
    }
}