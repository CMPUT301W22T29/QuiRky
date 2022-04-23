/*
 * Copyright (c) 2022. CMPUT301W22T29
 * Subject to MIT License
 * See full terms at https://github.com/CMPUT301W22T29/QuiRky/blob/main/LICENSE
 */

package com.example.quirky.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.view.PreviewView;

import com.example.quirky.controllers.DatabaseController;
import com.example.quirky.ListeningList;
import com.example.quirky.controllers.MapController;
import com.example.quirky.controllers.MemoryController;
import com.example.quirky.models.Profile;
import com.example.quirky.models.QRCode;
import com.example.quirky.controllers.QRCodeController;
import com.example.quirky.R;
import com.example.quirky.controllers.CameraController;

import org.osmdroid.util.GeoPoint;

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
public class CodeScannerActivity extends AppCompatActivity {

    private PreviewView previewView;
    private Button scan_button, cancel_button, save_button;
    private Switch location_switch, photo_switch;


    private CameraController cameraController;
    private DatabaseController dc;
    private MemoryController mc;

    private ListeningList<Bitmap> capture;

    private Boolean login;


    @Override
    @androidx.camera.core.ExperimentalGetImage
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_scanner);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.login = extras.getBoolean("login");
        } else {
            this.login = false;
        }

        previewView = findViewById(R.id.previewView);
        scan_button = findViewById(R.id.scan_button);
        cancel_button = findViewById(R.id.cancel_scan_button);
        save_button = findViewById(R.id.save_scan_button);
        location_switch = findViewById(R.id.keep_location_switch);
        photo_switch = findViewById(R.id.keep_photo_switch);

        cameraController = new CameraController(this);
        cameraController.startCamera(previewView.createSurfaceProvider(), this);

        dc = new DatabaseController();
        mc = new MemoryController(this);

        scan_button.setOnClickListener(view -> scan());
    }

    /**
     * Use the camera to take a picture, then scan the image for a QRCode. If one is found, continue at scannedCode()
     */
    @androidx.camera.core.ExperimentalGetImage
    public void scan() {
        capture = new ListeningList<>();
        ListeningList<QRCode> code = new ListeningList<>();

        capture.setOnAddListener(listeningListPhoto ->
                cameraController.scanFromBitmap(listeningListPhoto.get(0), code, this)
        );

        code.setOnAddListener(listeningList -> {
            if(listeningList.size() != 1)
                return;
            scannedCode(listeningList.get(0));
        });

        cameraController.captureImage(this, capture);
    }

    public void scannedCode(QRCode qr) {

        if(login) {

            ListeningList<Profile> user = new ListeningList<>();
            user.setOnAddListener(listeningList -> {
                Profile p = listeningList.get(0);
                if(p == null) {
                    Toast.makeText(this, "No users with that QRCode!", Toast.LENGTH_SHORT).show();
                    return;
                }

                mc.writeUser(p);

                Intent i = new Intent(this, HubActivity.class);
                startActivity(i);
            });

            dc.readLoginHash( qr.getId(), user);

        } else {
            setVisibility(false);

            cancel_button.setOnClickListener(view -> setVisibility(true));

            save_button.setOnClickListener(view -> {

                if(location_switch.isChecked()) {
                    MapController mapController = new MapController(this);

                    ListeningList<Location> currentLocation = new ListeningList<>();
                    currentLocation.setOnAddListener(listeningList -> {
                        GeoPoint gp = new GeoPoint( listeningList.get(0) );
                        qr.addLocation( gp );
                        save(qr);
                    });
                    mapController.getLocation(currentLocation);
                } else {
                    save(qr);
                }
            });
        }
    }

    /**
     * Save a QRCode to the Database. Update the player's profile to include the newly scanned code.
     * @param qr The QRCode to be saved
     */
    public void save(QRCode qr) {
        Profile p = mc.read();

        if(! p.addScanned(qr.getId())) {
            Toast.makeText(this, "You already have that QRCode!", Toast.LENGTH_SHORT).show();
            return;
        }

        qr.addScanner(p.getUname());
        dc.writeQRCode(qr);
        mc.writeUser(p);
        dc.writeProfile(p);

        if(photo_switch.isChecked()) {
            dc.writePhoto( qr.getId(), capture.get(0) );
        }

        Toast.makeText(this, "QRCode saved!", Toast.LENGTH_SHORT).show();

        setVisibility(true);
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
