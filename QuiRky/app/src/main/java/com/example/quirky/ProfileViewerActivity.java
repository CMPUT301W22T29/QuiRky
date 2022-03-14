package com.example.quirky;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

//There will be an import of database

public class ProfileViewerActivity extends AppCompatActivity {
    private Button backBt, changeUName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        TextView editableName;
        editableName = findViewById(R.id.editableName);

        SharedPreferences sp = getApplicationContext().getSharedPreferences("UserNamePre", Context.MODE_PRIVATE);
        String Username = sp.getString("Username","");

        editableName.setText(Username);


        backBt = (Button)findViewById(R.id.back2);
        backBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnToPrev();
            }
        });
        changeUName = (Button) findViewById(R.id.change_a_new_one);
        changeUName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeUserName();
            }
        });
    }
    public void returnToPrev(){ //to previous page
        Intent intent1 = new Intent(this, MainActivity.class);
        startActivity(intent1);
    }
    public void changeUserName(){
        Intent intent2 = new Intent(this, EditProfileActivity.class);
        startActivity(intent2);
    }


}