package com.example.quirky;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class QRCodes extends AppCompatActivity {
    private Button Back;
    private Button ManagesCodes;
    private Button ScanCodes;
    private Button GenerateCodes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcodes);
        Back = findViewById(R.id.button_back);
        ManagesCodes = findViewById(R.id.button_ManageCodes);
        ScanCodes = findViewById(R.id.button_ScanCodes);
        GenerateCodes = findViewById(R.id.button_GenerateCodes);
        Context context = this;

        // init intent here

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ManagesCodes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startactivity(intent_ManageCodes);
            }
        });
        ScanCodes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get camera permissions, start code scanner activity
                if (CameraController.hasCameraPermission(context)) {
                    startCodeScannerActivity();
                } else if (CameraController.requestCameraPermission(context)) {
                    startCodeScannerActivity();
                }
            }
        });
        GenerateCodes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startactivity(intent_GenerateCodes);
            }
        });
    }

    private void startCodeScannerActivity() {
        assert CameraController.hasCameraPermission(this);
        Intent intent = new Intent(this, CodeScannerActivity.class);
        startActivity(intent);
    }
}