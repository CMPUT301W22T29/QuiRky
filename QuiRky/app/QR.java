package com.example.dice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GameSeesions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_seesions);
        Button buttonRE = findViewById(R.id.button);
        buttonRE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReturnMain();
            }
        });
    }
    public void ReturnMain(){
        Intent intent1 = new Intent(this, MainActivity.class);
        startActivity(intent1);
    }
}