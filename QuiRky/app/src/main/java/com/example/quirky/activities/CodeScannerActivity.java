/*
 * Copyright (c) 2022. CMPUT301W22T29
 * Subject to MIT License
 * See full terms at https://github.com/CMPUT301W22T29/QuiRky/blob/main/LICENSE
 */

package com.example.quirky.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.view.PreviewView;

import com.example.quirky.controllers.DatabaseController;
import com.example.quirky.ListeningList;
import com.example.quirky.controllers.MemoryController;
import com.example.quirky.OnAddListener;
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

        dc = new DatabaseController();
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
                    ListeningList<Profile> profile = new ListeningList<>();
                    profile.setOnAddListener(new OnAddListener<Profile>() {
                        @Override
                        public void onAdd(ListeningList<Profile> listeningList) {
                            Profile p = listeningList.get(0);
                            login(p);
                        }
                    });
                    dc.readLoginHash(password, profile);
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
        if(listeningList.get(0) == null)
            Log.d(TAG, "QRCode is null for some goddamn reason");
        else
            Log.d(TAG, "QRCode is apparently not null...");
        QRCode qr = listeningList.get(0);

        save_button.setOnClickListener(view -> {
            save(qr);
            setVisibility(true);
        });

        cancel_button.setOnClickListener(view -> {
            save_button.setOnClickListener(null);
            setVisibility(true);
        });
    }

    /**
     * Attempt to login to an existing profile. Writes the profile to local memory and starts up a new activity
     * @param p The profile to log in with. May be null, if the scan matched no user.
     */
    private void login(Profile p) {
        if (p == null) {
            Toast.makeText(this, "That QRCode did not match any users!", Toast.LENGTH_LONG).show();
            return;
        }

        Toast.makeText(this, "Logging in as: " + p.getUname(), Toast.LENGTH_LONG).show();
        mc.write(p);
        mc.writeUser(p.getUname());

        Intent i = new Intent(this, HubActivity.class);
        startActivity(i);
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

        if (location_switch.isChecked()) {
            // TODO: GeoPoint gp = MapController.getLocation();
            GeoPoint gp = new GeoPoint(10.0, 10.0);
            qr.addLocation(gp);
        }

        if (photo_switch.isChecked()) {
            // TODO: photo = takePhoto();
            // TODO: dc.writePhoto(codeId, photo);
        }


        qr.addScanner(p.getUname());
        dc.writeQRCode(qr);
        mc.write(p);
        dc.writeProfile(p);

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
