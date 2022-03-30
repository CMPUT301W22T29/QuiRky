package com.example.quirky;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class GenerateActivity extends AppCompatActivity {
    EditText textGener, intGener, urlGener;
    Button backBt, textGenBt,intGenBt, urlGenBt;
    ImageView qrImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate);
        textGener = findViewById(R.id.editTextGenQrTxt);
        intGener = findViewById(R.id.editTextGenQrInt);
        urlGener = findViewById(R.id.editTextGenQrUrl);

        backBt = findViewById(R.id.generateBackButton);
        textGenBt = findViewById(R.id.textGenerate);
        intGenBt = findViewById(R.id.integerGenerate);
        urlGenBt = findViewById(R.id.URLGenerate);

        textGenBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get input value form edit text;
                String data1 = textGener.getText().toString().trim();
                //initialize multi format writer
                MultiFormatWriter aWriter = new MultiFormatWriter();
                try{
                    BitMatrix aMatrix = aWriter.encode(data1, BarcodeFormat.QR_CODE, 400,400);

                    BarcodeEncoder aEncoder = new BarcodeEncoder();

                    Bitmap aBitmap = aEncoder.createBitmap(aMatrix);

                    qrImage.setImageBitmap(aBitmap);
                    //to hide the keyboard
                    InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                    manager.hideSoftInputFromWindow(textGener.getApplicationWindowToken(),0);
                } catch (WriterException e) {
                    e.printStackTrace();
                }


            }
        });
        intGenBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get input value form edit text;
                String data2 = intGener.getText().toString().trim();
                //initialize multi format writer
                MultiFormatWriter bWriter = new MultiFormatWriter();
                try{
                    BitMatrix bMatrix = bWriter.encode(data2, BarcodeFormat.QR_CODE, 400,400);

                    BarcodeEncoder bEncoder = new BarcodeEncoder();

                    Bitmap bBitmap = bEncoder.createBitmap(bMatrix);

                    qrImage.setImageBitmap(bBitmap);
                    //to hide the keyboard
                    InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                    manager.hideSoftInputFromWindow(textGener.getApplicationWindowToken(),0);
                } catch (WriterException e) {
                    e.printStackTrace();
                }


            }
        });
        urlGenBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get input value form edit text;
                String data3 = urlGener.getText().toString().trim();
                //initialize multi format writer
                MultiFormatWriter cWriter = new MultiFormatWriter();
                try{
                    BitMatrix cMatrix = cWriter.encode(data3, BarcodeFormat.QR_CODE, 400,400);

                    BarcodeEncoder cEncoder = new BarcodeEncoder();

                    Bitmap cBitmap = cEncoder.createBitmap(cMatrix);

                    qrImage.setImageBitmap(cBitmap);
                    //to hide the keyboard
                    InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                    manager.hideSoftInputFromWindow(textGener.getApplicationWindowToken(),0);
                } catch (WriterException e) {
                    e.printStackTrace();
                }


            }
        });
    }
}