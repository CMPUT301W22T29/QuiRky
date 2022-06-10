/*
 * Copyright (c) 2022. CMPUT301W22T29
 * Subject to MIT License
 * See full terms at https://github.com/CMPUT301W22T29/QuiRky/blob/main/LICENSE
 */

package com.example.quirky.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.quirky.ListeningList;
import com.example.quirky.R;
import com.example.quirky.controllers.DatabaseController;
import com.example.quirky.controllers.MapController;
import com.example.quirky.controllers.MemoryController;
import com.example.quirky.models.GeoLocation;
import com.example.quirky.models.Profile;
import com.example.quirky.models.QRCode;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class CodeSaveActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    QRCode qrcode;
    DatabaseController dc;
    MemoryController mc;
    Bitmap photo;
    MapController mapController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_save);

        // Initialise views & controllers
        dc = new DatabaseController();
        mc = new MemoryController(this);


        // Get the intent and the passed parameters
        // This activity expects a QRCode and an optional URI pointing to a photo in local memory
        Intent i = getIntent();
        qrcode = (QRCode) i.getExtras().get("code");

        // If there are two extras in the intent, the second must be the optional URI
        if(i.getExtras().size() == 2) {
            photo = mc.loadPhoto( i.getExtras().getParcelable("uri") );
            ImageView image = findViewById(R.id.taken_photo);
            image.setImageBitmap(photo);
        } else
            photo = null;


        TextView content = findViewById(R.id.saved_code_content);
        content.setText( qrcode.getContent() );

        Button cancel = findViewById(R.id.code_save_cancel_button);
        cancel.setOnClickListener(view -> finish());

        Button save = findViewById(R.id.code_save_button);
        save.setOnClickListener(view -> getData());
    }

    /**
     * Get the data that the user has chosen to save
     * Will try to fetch a title from the EditText input field, and will try to fetch the user's location
     * If the user chooses to save their location.
     */
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    public void getData() {

        EditText titleView = findViewById(R.id.input_code_title);
        String title = String.valueOf(titleView.getText());
        if (!title.equals(""))
            qrcode.addTitle(title);

        Switch locationSwitch = findViewById(R.id.keep_location_switch);
        if (locationSwitch.isChecked()) {
            mapController = new MapController(this);

            ListeningList<GeoLocation> currentLocation = new ListeningList<>();
            currentLocation.setOnAddListener(listeningList -> {
                qrcode.addLocation(listeningList.get(0));
                save();
            });
            mapController.getLocation(currentLocation);
        } else
            save();
    }

    /**
     * Method to do the actual saving & writing. Will add the player to the QRCode, add the QRCode
     * to the player, and write the updated objects to local storage and firebase
     */
    public void save() {

        Profile p = mc.read();

        p.addScanned( qrcode.getId() );
        qrcode.addScanner(p.getUname());

        dc.writeQRCode(qrcode);
        mc.writeUser(p);
        dc.writeProfile(p.getUname(), p);

        if(photo != null)
            dc.writePhoto(qrcode.getId(), photo, this);

        Toast.makeText(this, "QRCode saved!", Toast.LENGTH_SHORT).show();
        finish();
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        mapController.onLocationPermissionRequestResult(requestCode, grantResults);
    }
}