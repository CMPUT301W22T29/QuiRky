/*
 * Copyright (c) 2022. CMPUT301W22T29
 * Subject to MIT License
 * See full terms at https://github.com/CMPUT301W22T29/QuiRky/blob/main/LICENSE
 */

package com.example.quirky.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.quirky.controllers.MemoryController;
import com.example.quirky.controllers.QRCodeController;
import com.example.quirky.R;
import com.example.quirky.controllers.DatabaseController;

/**
 * Activity to generate QRCodes
 */
public class GenerateActivity extends AppCompatActivity {

    Button generateLogin, generateViewProfile, generateText;
    EditText inputField;
    ImageView qrImage;

    String user;
    DatabaseController dc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate);

        generateLogin = findViewById(R.id.generateLoginCodeButton);
        generateViewProfile = findViewById(R.id.GenerateViewProfileCodeButton);
        generateText = findViewById(R.id.generateByTextButton);
        inputField = findViewById(R.id.genertateByTextField);
        qrImage = findViewById(R.id.qrfield);

        dc = new DatabaseController();
        user = new MemoryController(this).readUser();

        generateLogin.setOnClickListener(v -> {
            String loginPassword = QRCodeController.getRandomString(15);

            Bitmap generated = QRCodeController.generateQR(loginPassword);
            qrImage.setImageBitmap(generated);

            String hashedPassword = QRCodeController.SHA256(loginPassword);
            dc.writeLoginHash(hashedPassword, user);

            Toast.makeText(this, "Save this code! You will need it to login again!", Toast.LENGTH_LONG).show();
        });

        generateText.setOnClickListener(v -> {
            String text = inputField.getText().toString();
            if (text.length() > 0) {
                Bitmap generated = QRCodeController.generateQR(text);
                qrImage.setImageBitmap(generated);
            }
        });

        generateViewProfile.setOnClickListener(v -> {
            String text = "QuiRky.QRCODE.viewProfile:" + user;
            Bitmap generated = QRCodeController.generateQR(text);
            qrImage.setImageBitmap(generated);
        });

        generateViewProfile.setOnClickListener(v -> {
            String text = "quirky.view.profile." + user;
            Bitmap generated = QRCodeController.generateQR(text);
            qrImage.setImageBitmap(generated);
        });
    }
}