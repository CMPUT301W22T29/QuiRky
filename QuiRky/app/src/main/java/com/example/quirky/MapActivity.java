package com.example.quirky;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import org.osmdroid.views.MapView;

public class MapActivity extends AppCompatActivity {
    private MapController mapController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_layout);
        mapController = new MapController();
        mapController.requestlocation();
    }
}

