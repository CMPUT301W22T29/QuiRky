package com.example.quirky;

import static com.example.quirky.QRCodeController.generateQR;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.Random;

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

        dc = new DatabaseController(this);
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
            String text = "quirky.view.profile." + user;
            Bitmap generated = QRCodeController.generateQR(text);
            qrImage.setImageBitmap(generated);
        });
    }
}