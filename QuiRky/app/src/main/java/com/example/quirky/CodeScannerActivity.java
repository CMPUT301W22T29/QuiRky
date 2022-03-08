package com.example.quirky;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.view.PreviewView;

public class CodeScannerActivity extends AppCompatActivity {
    private PreviewView previewView;
    private CameraController cameraController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_scanner);
        previewView = findViewById(R.id.previewView);
        cameraController = new CameraController(this);
        cameraController.startCameraPreview(previewView.createSurfaceProvider(), this);
    }
}