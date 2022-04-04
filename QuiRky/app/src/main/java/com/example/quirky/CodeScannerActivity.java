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

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

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
    MemoryController mc;

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

        dc = new DatabaseController(this);
        mc = new MemoryController(this);

        scan_button.setOnClickListener(view -> scan());
    }

    /**
     * Create a ListeningList to listen for a QRCode to be added to it. Calls CameraController to scan for a QRCode to add to ListeningList.
     * This method will be followed by either showSavingInterface() or login(), depending on whether the user was using this activity to login or not.
     */
    @androidx.camera.core.ExperimentalGetImage
    public void scan() {
        ListeningList<QRCode> codes = new ListeningList<>();
        codes.setOnAddListener(new OnAddListener<QRCode>() {

            @Override
            public void onAdd(ListeningList<QRCode> listeningList) {
                if (login) {
                    String password = listeningList.get(0).getId();
                    dc.readLoginHash(password).addOnCompleteListener(task -> login( dc.getProfileWithHash(task)) );
                } else {
                    showSavingInterface(listeningList);
                }
            }

        });
        cameraController.captureQRCodes(this, codes);
    }

    /**
     * Displays the save buttons and hide the Scan button. Set on click listeners to save the QRCode if the user chooses
     * @param listeningList The listening list that holds the QRCode scanned by CameraController
     */
    private void showSavingInterface(ListeningList<QRCode> listeningList) {
        setVisibility(false);

        save_button.setOnClickListener(view -> {
            GeoPoint gp = null;
            Bitmap photo = null;
            MapView nearbymap = (MapView) findViewById(R.id.map1);
            ListeningList<Location> locations = new ListeningList<>();
            locations.setOnAddListener(new OnAddListener<Location>() {
                public void onAdd(ListeningList<Location> listeningList1) {
                    Bitmap photo = null;
                   Location location = listeningList1.get(0);
                   GeoPoint gp = new GeoPoint((double) location.getLatitude(),(double)location.getLongitude());
                   dc.writeQRCodeLocation(listeningList.get(0).getId(),mc.readUser(),gp);
                   save(listeningList.get(0), gp, photo);
                }
            });
            if (location_switch.isChecked()) {
                MapController mapController = new MapController(this);
                mapController.getLocation(locations);
                //mapController.qrMarkerOnMap(gp,nearbymap,"A QR code located");

                //TODO: Make a controller class that gets location.
            } else {
                save(listeningList.get(0), gp, photo);
                gp = null;
            }

            if (photo_switch.isChecked()) {
                photo = null;
                // TODO: Figure out how to get the photo of the code
                //FIXME: Need to direct to a new photo capture activity, rather than saving the image of the QRCode
            } else {
                photo = null;
            }

            save(listeningList.get(0), gp, photo);
            setVisibility(true);
        });

        cancel_button.setOnClickListener(view -> {
            save_button.setOnClickListener(null);
            setVisibility(true);
        });
    }

    /**
     * Attempt to login to a profile. Writes the profile to local memory and starts up a new activity
     * @param p
     */
    private void login(Profile p) {
        if (p == null) {
            Toast.makeText(this, "That QRCode did not match any users!", Toast.LENGTH_LONG).show();
            return;
        }

        Toast.makeText(this, "Logging in as: " + p.getUname(), Toast.LENGTH_LONG).show();
        mc.write(p);
        mc.writeUser(p.getUname());

        Intent i = new Intent(this, StartingPageActivity.class);
        startActivity(i);
    }

    /**
     * Save a QRCode to the Database. Update the player's profile to include the newly scanned code.
     * @param qr The QRCode to be saved
     * @param gp The location of the user, if they want their location to be saved
     * @param image The photo of the QRCode, if the user wants the photo saved.
     */
    public void save(QRCode qr, GeoPoint gp, Bitmap image) {
        Profile p = mc.read();

        if(! p.addScanned(qr.getId())) {
            Toast.makeText(this, "You already have that QRCode!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Update the local memory and database, because the player's statistics have changed.
        dc.writeQRCode(qr, p.getUname());
        mc.write(p);
        dc.writeProfile(p);

        if(gp != null) {
            // dc.saveLocation(qrcode, location);
        }
        if(image != null) {
            // dc.saveImage(qrcode, image);
        }
        Toast.makeText(this, "QRCode saved!", Toast.LENGTH_LONG).show();
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
