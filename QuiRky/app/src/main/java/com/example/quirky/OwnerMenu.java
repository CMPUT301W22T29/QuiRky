/*
 * Copyright (c) 2022. CMPUT301W22T29
 * Subject to MIT License
 * See full terms at https://github.com/CMPUT301W22T29/QuiRky/blob/main/LICENSE
 */

package com.example.quirky;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class OwnerMenu extends AppCompatActivity {
    private Button ownerDeleteQR;
    private Button ownerDeletePL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_menu);
        ownerDeletePL = findViewById(R.id.deletePlayers);
        ownerDeleteQR = findViewById(R.id.deleteQRCodes);

        ownerDeleteQR.setOnClickListener(view -> {
            Intent i = new Intent(this, delete_QRCodes.class);
            startActivity(i);
        });
        ownerDeletePL.setOnClickListener(view -> {
            Intent i = new Intent(this, delete_Players.class);
            startActivity(i);
        });
    }
}