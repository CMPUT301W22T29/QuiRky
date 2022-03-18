package com.example.quirky;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
        i = getParentActivityIntent();  // FIXME: Determine if this works. Been so long since I did intent passing...
        p = i.getExtras().getParcelable("profile");

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
        rank1.setText(p.getRankingPoints());
        rank2.setText(p.getRankingScanned());
        rank3.setText(p.getRankingBiggestCode());
        changeProfile.setOnClickListener(view -> changeProfile());
    }

    public void changeProfile() {
        Intent intent = new Intent(this, EditProfileActivity.class);
        startActivity(intent);
    }
}