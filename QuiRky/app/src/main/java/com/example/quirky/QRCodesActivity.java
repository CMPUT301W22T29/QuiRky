package com.example.quirky;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class QRCodesActivity extends AppCompatActivity {
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
                //startactivity(intent_ScanCodes);
            }
        });
        GenerateCodes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startactivity(intent_GenerateCodes);
            }
        });
    }
}