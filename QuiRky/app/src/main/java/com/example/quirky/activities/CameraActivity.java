/*
 * Copyright (c) 2022. CMPUT301W22T29
 * Subject to MIT License
 * See full terms at https://github.com/CMPUT301W22T29/QuiRky/blob/main/LICENSE
 */

package com.example.quirky.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.view.PreviewView;

import com.example.quirky.OnAddListener;
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

        //     Create a ListeningList<> to put the camera photo into,
        // and create a ListeningList<> to put the target QRCode into
        capture = new ListeningList<>();
        ListeningList<QRCode> scannedCode = new ListeningList<>();

        // When the photo is added to the list, call camera controller to scan for QRCodes
        capture.setOnAddListener(listeningListPhoto ->
            cameraController.scanFromBitmap(listeningListPhoto.get(0), scannedCode, this)
        );

        // When a code is added to the list, call the followup method
        scannedCode.setOnAddListener(listeningList -> {
            if(listeningList.size() != 1)
                return;
            useCode(listeningList.get(0));
        });

        cameraController.captureImage(this, capture);
    }

    /**
     * Followup method that is called once the QRCode has been scanned from the taken photo.
     * Will attempt to login with the QRCode or start CodeSaveActivity
     */
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
            // Check if the QRCode is a player profile code
            // If it is start the profile activity. If it isn't, start the CodeSave activity
            String content = qr.getContent();
            if( content.contains("QuiRky.QRCODE.viewProfile:") ) {
                viewProfile(content);
            } else {
                saveCode(qr);
            }
        }
    }

    public void viewProfile(String content) {
        assert content.length() > "QuiRky.QRCODE.viewProfile:".length() : "For some reason this code does not have a username extension?";
        String username = content.substring(26);

        ListeningList<Profile> ReadUser = new ListeningList<>();
        ReadUser.setOnAddListener(listeningList -> {
            if(listeningList.size() == 0)
                return;

            Intent i = new Intent(this, ProfileActivity.class);
            i.putExtra("profile", listeningList.get(0));
            startActivity(i);
        });

        dc.readProfile(username, ReadUser);
    }

    public void saveCode(QRCode qr) {
        // Check that the user does not have the code already
        Profile p = mc.read();
        if(! p.addScanned(qr.getId())) {
            Toast.makeText(this, "You already have that QRCode!", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent i = new Intent(this, CodeSaveActivity.class);
        i.putExtra("code", qr);

        if(photo_switch.isChecked()) {
            i.putExtra("uri", mc.savePhoto( capture.get(0), "taken_photo"));
        }

        startActivity(i);
    }
}
