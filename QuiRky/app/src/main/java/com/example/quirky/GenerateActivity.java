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

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.Random;

public class GenerateActivity extends AppCompatActivity {

    Button qrGeneraBtn1, getBack;
    ImageView qrImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate);
        qrGeneraBtn1 = findViewById(R.id.generateButton1);
        qrImage = findViewById(R.id.qrfield);


        qrGeneraBtn1.setOnClickListener(v->{
            Bitmap generated =null;
            generated = QRCodeController.generateQR(QRCodeController.getRandomString(18));
            qrImage.setImageBitmap(generated);
        });
        getBack = findViewById(R.id.randomGenerBack);
        getBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnToPre();
            }
    });
    }
    public void returnToPre() { //to previous page
        Intent intent1 = new Intent(this, StartingPageActivity.class);
        startActivity(intent1);
    }
}