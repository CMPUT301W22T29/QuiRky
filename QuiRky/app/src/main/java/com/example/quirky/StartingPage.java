package com.example.quirky;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartingPage extends AppCompatActivity {
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
        myProfile = findViewById(R.id.back);
        community = findViewById(R.id.community);
        /*/back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){


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


            }
        });
        myProfile.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){


            }
        });
        Community.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){


            }
        });

        */



    }
}