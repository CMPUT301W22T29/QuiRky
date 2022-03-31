/*
 * CodeScannerActivity.java
 *
 * Version 0.2.0
 * Version History:
 *      Version 0.1.0 -- Camera Previewing Works
 *      Version 0.2.0 -- QR Code Photo Capturing Works
 *      Version 0.3.0 -- There is now a dialogue to save QR codes, this class can now be made to work for the login thing too.
 *
 * Date (v0.3.0): March 30, 2022
 *
 * Copyright (c) 2022. CMPUT301W22T29
 * Subject to MIT License
 * See full terms at https://github.com/CMPUT301W22T29/QuiRky/blob/main/LICENSE
 */

package com.example.quirky;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.view.PreviewView;

import com.google.firebase.firestore.FirebaseFirestore;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;

/**
 * Previews camera feed and allows scanning QR codes.
 * <p>
 * Known Issues:
 *      Captures QR codes and opens a save dialogue, but does not save to the database yet. (v0.3.0)
 *      login method doesn't do anything yet.   (v0.3.0)
 *
 * @author Sean Meyers
 * @version 0.3.0
 * @see androidx.camera.core
 * @see CameraController
 * @see QRCode
 * @see QRCodeController
 */
public class CodeScannerActivity extends AppCompatActivity {

    private PreviewView previewView;
    private Button scan_button, cancel_button, save_button;
    private Switch location_switch, photo_switch;

    private CameraController cameraController;
    private DatabaseController databaseController;

    private Boolean login;

    private final String TAG = "CodeScannerActivity says";

    @Override
    @androidx.camera.core.ExperimentalGetImage
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.login = extras.getBoolean("login");
        }

        setContentView(R.layout.activity_code_scanner);

        previewView = findViewById(R.id.previewView);
        scan_button = findViewById(R.id.scan_button);
        cancel_button = findViewById(R.id.cancel_scan_button);
        save_button = findViewById(R.id.save_scan_button);
        location_switch = findViewById(R.id.keep_location_switch);
        photo_switch = findViewById(R.id.keep_photo_switch);

        cameraController = new CameraController(this);
        cameraController.startCamera(previewView.createSurfaceProvider(), this);

        //databaseController = new DatabaseController(FirebaseFirestore.getInstance(), this);

        scan_button.setOnClickListener(view -> scan());
    }

    //TODO: javadocs
    @androidx.camera.core.ExperimentalGetImage
    public void scan() {
        //Boolean shouldLogin = false;
        CodeList<QRCode> codes = new CodeList<>();
        codes.setOnCodeAddedListener(new OnCodeAddedListener<QRCode>() {

            @Override
            public void onCodeAdded(CodeList<QRCode> codeList) {
                if (login) {
                    login(codeList);
                } else {
                    showSavingInterface(codeList);
                }
            }

        });
        cameraController.captureQRCodes(this, codes);
    }

    private void showSavingInterface(CodeList<QRCode> codeList) {
        setVisibility(false);

        save_button.setOnClickListener(view -> {
            GeoPoint gp;
            Bitmap photo;
            if (location_switch.isChecked()) {
                gp = null;
                // GeoPoint gp = results.get(0).getLocation(); TODO: Figure out how to get location from inside the listener
            } else {
                gp = null;
            }

            if (photo_switch.isChecked()) {
                photo = null;
                // photo = results.get(0).getLocation(); // TODO: Figure out how to get the photo of the code
                //FIXME: Need to direct to a new photo capture activity, rather than saving the image of the QRCode
            } else {
                photo = null;
            }

            save(codeList.get(0), gp, photo);
            Toast.makeText(this, "QRCode saved!", Toast.LENGTH_LONG).show();
            setVisibility(true);
        });

        cancel_button.setOnClickListener(view -> {
            save_button.setOnClickListener(null);
            setVisibility(true);
        });
    }

    private void login(CodeList<QRCode> codeList) {

        // TODO: Jonathen, please look at.

        /* TODO:    implement the following, possibly as a func in the db controller which should be called here:
           get qr code hash from qr code in list
           search firestore for player accounts with a login qr variable set to that hash
                                Note: I recommend that this be a separate variable from where the usual qr codes are stored.
           if it exists
               log player in with that account
                               Alternatively, display account info and a button to login, so they can choose not to log in if it's the wrong account or whatever.
               set the login qr variable in firestore to null or something so that the login qr code can't be reused.
           else
               pop a toast that says they could not find an account with that code.
         */

        Toast.makeText(this, "Pretending to log in!", Toast.LENGTH_LONG).show();
    }

    public void save(QRCode qr, GeoPoint gp, Bitmap image) {
        DatabaseController dc = new DatabaseController(this);
        dc.writeQRCode(qr);

        if(gp != null) {
            // dc.saveLocation(qrcode, location);
        }
        if(image != null) {
            // dc.saveImage(qrcode, image);
        }
    }

    /**
     * Swap the visibility of the on screen buttons & switches
     * @param scanButtonVisible The visibility of the Scan! button. If this button is visible, the remaining buttons are invisible, and vice versa.
     */
    private void setVisibility(Boolean scanButtonVisible) {
        if(scanButtonVisible) {
            scan_button.setVisibility(View.VISIBLE);

            cancel_button.setVisibility(View.INVISIBLE);
            save_button.setVisibility(View.INVISIBLE);
            location_switch.setVisibility(View.INVISIBLE);
            photo_switch.setVisibility(View.INVISIBLE);
        } else {
            scan_button.setVisibility(View.INVISIBLE);

            cancel_button.setVisibility(View.VISIBLE);
            save_button.setVisibility(View.VISIBLE);
            location_switch.setVisibility(View.VISIBLE);
            photo_switch.setVisibility(View.VISIBLE);
        }
    }
}
