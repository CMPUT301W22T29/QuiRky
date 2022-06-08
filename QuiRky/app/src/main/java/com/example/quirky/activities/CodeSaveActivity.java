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
import com.example.quirky.models.GeoLocation;
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

public class CodeSaveActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    QRCode code;
    DatabaseController dc;
    Bitmap photo;
    MapController mapController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_save);

        Intent i = getIntent();
        code = (QRCode) i.getExtras().get("code");
        if(i.getExtras().getBoolean("keep_photo")) {
            photo = i.getExtras().getParcelable("photo");
            ImageView image = findViewById(R.id.taken_photo);
            image.setImageBitmap(photo);
        } else
            photo = null;

        dc = new DatabaseController();

        TextView content = findViewById(R.id.saved_code_content);
        content.setText( code.getContent() );

        Button cancel = findViewById(R.id.code_save_cancel_button);
        cancel.setOnClickListener(view -> finish());

        Button save = findViewById(R.id.code_save_button);
        save.setOnClickListener(view -> getData());
    }

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    public void getData() {

        EditText titleView = findViewById(R.id.input_code_title);
        String title = String.valueOf(titleView.getText());
        if (!title.equals(""))
            code.addTitle(title);

        Switch locationSwitch = findViewById(R.id.keep_location_switch);
        if (locationSwitch.isChecked()) {
            mapController = new MapController(this);

            ListeningList<GeoLocation> currentLocation = new ListeningList<>();
            currentLocation.setOnAddListener(listeningList -> {
                code.addLocation(listeningList.get(0));
                save();
            });
            mapController.getLocation(currentLocation);
        } else
            save();
    }

    public void save() {
        dc.writeQRCode(code);
        if(photo != null)
            dc.writePhoto(code.getId(), photo, this);

        finish();
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        mapController.onLocationPermissionRequestResult(requestCode, grantResults);
    }
}