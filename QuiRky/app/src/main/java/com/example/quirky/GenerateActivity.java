package com.example.quirky;

import androidx.appcompat.app.AppCompatActivity;

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

public class GenerateActivity extends AppCompatActivity {
    EditText userNameField;
    Button qrGeneraBtn1;
    ImageView qrImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate);

        userNameField = findViewById(R.id.userGenerate);
        qrGeneraBtn1 = findViewById(R.id.generateButton1);
        qrImage = findViewById(R.id.qrfield);

        qrGeneraBtn1.setOnClickListener(v->{
            generateQR();
        });

    }
    private void generateQR(){
        String text = userNameField.getText().toString().trim();
        MultiFormatWriter writer = new MultiFormatWriter();
        try {
            BitMatrix matrix = writer.encode(text, BarcodeFormat.QR_CODE, 400, 400);
            BarcodeEncoder encoder = new BarcodeEncoder();
            Bitmap bitmap = encoder.createBitmap(matrix);

            qrImage.setImageBitmap(bitmap);
        }catch (WriterException e)
        {
            e.printStackTrace();
        }
    }
}