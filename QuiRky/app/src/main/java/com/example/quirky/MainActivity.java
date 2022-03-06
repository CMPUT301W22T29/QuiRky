package com.example.quirky;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button myProfile, myQRCodes, myStats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myProfile = (Button) findViewById(R.id.myprofile);
        //below are several clicks that open some new pages

        myProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                openMyProfile();
            }

        });
        myStats = (Button)findViewById(R.id.mystats);
        myStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view2) {
                openMyStats();

            }
        });
        myQRCodes = (Button) findViewById(R.id.qrcodes1);
        myQRCodes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMyQrCode();
            }
        });
    }
    //Actually can merge these three functions, but not sure whether need
    public void openMyProfile(){
        Intent intent1 = new Intent(this, MyProfile1.class);
        startActivity(intent1);
    }
    public void openMyStats(){
        Intent stats = new Intent(this, myStats.class);
        startActivity(stats);
    }
    public void openMyQrCode() {
        Intent qrcode = new Intent(this, MyQRCode.class);
        startActivity(qrcode);
    }
    //there should be a back button that return to the main page.
}