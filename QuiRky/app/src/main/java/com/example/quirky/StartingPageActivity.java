package com.example.quirky;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartingPageActivity extends AppCompatActivity {
    private Button back;
    private Button search;
    private Button QRCodes;
    private Button myProfile;
    private Button community;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting_page);
        back = findViewById(R.id.back);
        search = findViewById(R.id.search);
        QRCodes = findViewById(R.id.QRCodes);
        //myProfile = findViewById(R.id.);
        community = findViewById(R.id.community);
        Intent Intent_Community = new Intent(this, Community.class);
        Intent Intent_QRCodes = new Intent(this, QRCodes.class);
        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();

            }
        });
        search.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){


            }
        });
        QRCodes.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startActivity(Intent_QRCodes);

            }
        });
        /*/myProfile.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){


            }
        });

         */
        community.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startActivity(Intent_Community);

            }
        });





    }
}