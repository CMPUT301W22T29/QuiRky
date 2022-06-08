/*
 * Copyright (c) 2022. CMPUT301W22T29
 * Subject to MIT License
 * See full terms at https://github.com/CMPUT301W22T29/QuiRky/blob/main/LICENSE
 */

package com.example.quirky.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.view.PreviewView;

import com.example.quirky.controllers.DatabaseController;
import com.example.quirky.ListeningList;
import com.example.quirky.controllers.MemoryController;
import com.example.quirky.models.Profile;
import com.example.quirky.models.QRCode;
import com.example.quirky.controllers.QRCodeController;
import com.example.quirky.R;
import com.example.quirky.controllers.CameraController;

/**
 * Previews camera feed and allows scanning QR codes.
 *
 * @author Sean Meyers
 * @author Jonathen Adsit
 * @version 0.3.0
 * @see androidx.camera.core
 * @see CameraController
 * @see QRCode
 * @see QRCodeController
 */
public class CameraActivity extends AppCompatActivity {

    private Switch photo_switch;

    private CameraController cameraController;
    private DatabaseController dc;
    private MemoryController mc;

    private ListeningList<Bitmap> capture;

    private Boolean login;


    @Override
    @androidx.camera.core.ExperimentalGetImage
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.login = extras.getBoolean("login");
        } else {
            this.login = false;
        }

        PreviewView previewView = findViewById(R.id.previewView);
        Button scan_button = findViewById(R.id.scan_button);
        photo_switch = findViewById(R.id.keep_photo_switch);

        cameraController = new CameraController(this);
        cameraController.startCamera(previewView.createSurfaceProvider(), this);

        dc = new DatabaseController();
        mc = new MemoryController(this);

        scan_button.setOnClickListener(view -> getCode());
    }

    /**
     * Use the camera to take a picture, then scan the image for a QRCode. If one is found, continue at scannedCode()
     */
    @androidx.camera.core.ExperimentalGetImage
    public void getCode() {
        capture = new ListeningList<>();
        ListeningList<QRCode> code = new ListeningList<>();

        capture.setOnAddListener(listeningListPhoto ->
                cameraController.scanFromBitmap(listeningListPhoto.get(0), code, this)
        );

        code.setOnAddListener(listeningList -> {
            if(listeningList.size() != 1)
                return;
            useCode(listeningList.get(0));
        });

        cameraController.captureImage(this, capture);
    }

    public void useCode(QRCode qr) {
        if(login) {

            ListeningList<Profile> user = new ListeningList<>();
            user.setOnAddListener(listeningList -> {
                if(listeningList.size() == 0) {
                    Toast.makeText(this, "No users with that QRCode!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Profile p = listeningList.get(0);
                mc.writeUser(p);

                Intent i = new Intent(this, HubActivity.class);
                startActivity(i);
            });

            dc.readLoginHash( qr.getId(), user);

        } else {

            Intent i = new Intent(this, CodeSaveActivity.class);
            i.putExtra("code", qr);

            if(photo_switch.isChecked()) {
                i.putExtra("keep_photo", true);
                i.putExtra("photo", capture.get(0));
            } else {
                i.putExtra("keep_photo", false);
            }

            startActivity(i);
        }
    }
}
