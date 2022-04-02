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

import java.util.Random;

public class GenerateActivity extends AppCompatActivity {

    Button qrGeneraBtn1;
    ImageView qrImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate);
        qrGeneraBtn1 = findViewById(R.id.generateButton1);
        qrImage = findViewById(R.id.qrfield);

        qrGeneraBtn1.setOnClickListener(v->{
            generateQR();
        });

    }
    public static String getRandomString(int length){
        StringBuilder val = new StringBuilder();
        Random random = new Random();
        String finalString;
        for (int i = 0; i<length;i++){
            int chatTypa = random.nextInt(3);
            switch (chatTypa){
                case 0:
                    val.append(random.nextInt(10));
                    break;
                case 1:
                    val.append((char) (random.nextInt(26)+97));
                    break;
                case 2:
                    val.append((char)(random.nextInt(26)+65));
            }
        }
        finalString = val.toString();
        return  finalString;
    }
    private void generateQR(){
        String text = getRandomString(18);
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