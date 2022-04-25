/*
 * Copyright (c) 2022. CMPUT301W22T29
 * Subject to MIT License
 * See full terms at https://github.com/CMPUT301W22T29/QuiRky/blob/main/LICENSE
 */

package com.example.quirky.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quirky.AdapterPhoto;
import com.example.quirky.ListeningList;
import com.example.quirky.R;
import com.example.quirky.controllers.DatabaseController;
import com.example.quirky.controllers.MemoryController;
import com.example.quirky.models.Profile;
import com.example.quirky.models.QRCode;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;

/**
 * Activity to view a QRCode. Holds two fragments: one fragment to see the QRCode location, & start up the comments activity
 * Another fragment to see all other players who have scanned the QRCode
 * */
public class ViewQRActivity extends AppCompatActivity {

    private final String TAG = "ViewQRActivity says";

    DatabaseController dc;
    ArrayList<String> players;
    ArrayList<GeoPoint> locations;
    ListeningList<Bitmap> photos;

    Intent i;
    QRCode qr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_qr);

        dc = new DatabaseController();

        i = getIntent();
        qr = i.getParcelableExtra("qr");
        if(qr == null)
            ExitWithError();

        players = qr.getScanners();
        locations = qr.getLocations();

        photos = new ListeningList<>();
        photos.setOnAddListener(listeningList -> {
            Toast.makeText(this, "Done reading", Toast.LENGTH_SHORT).show();
            setPhotos();
        });
        dc.readPhotos( qr.getId(), photos, 3);
    }

    public void setPhotos() {
        RecyclerView photo_list = findViewById(R.id.code_photo_list);
        AdapterPhoto adapterPhoto = new AdapterPhoto(photos, this);
        photo_list.setAdapter(adapterPhoto);
        photo_list.setLayoutManager( adapterPhoto.getLayoutManager() );
        ProgressBar bar = findViewById(R.id.progressBar3);
        bar.setVisibility(View.GONE);
    }

    /**
     * Deletes the QRCode from the list of codes the user has scanned. Exits the activity.
     */
    public void RemoveFromAcc() {
        MemoryController mc = new MemoryController(this);
        Profile p = mc.read();

        if(p.removeScanned(qr.getId())) {
            mc.writeUser(p);
            dc.writeProfile(p);

            qr.removeScanner(p.getUname());
            dc.writeQRCode(qr);

            Toast.makeText(this, "Removed from your scanned codes!", Toast.LENGTH_SHORT).show();

            Intent i = new Intent(this, HubActivity.class);
            startActivity(i);
        } else {
            Toast.makeText(this, "You did not have that code anyways!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Method called when data is passed to this activity incorrectly, or when there is an issue reading the data from FireStore.
     * Makes a toast and then finishes the activity.
     */
    private void ExitWithError() {
        Toast.makeText(this, "QRCode was passed incorrectly, or not found in FireStore!", Toast.LENGTH_SHORT).show();
        finish();
    }
}