
/*
 * Copyright (c) 2022. CMPUT301W22T29
 * Subject to MIT License
 * See full terms at https://github.com/CMPUT301W22T29/QuiRky/blob/main/LICENSE
 */

package com.example.quirky.activities;

import android.annotation.SuppressLint;
import android.location.Location;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.quirky.ListeningList;
import com.example.quirky.controllers.MapController;
import com.example.quirky.OnAddListener;
import com.example.quirky.R;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;//Tile source factory used for manipulating the map




/*
source: https://osmdroid.github.io/osmdroid/How-to-use-the-osmdroid-library.html
author: osmdroid team : https://github.com/osmdroid/osmdroid
Publish Date:2019-09-27
 */
/*source:https://stackoverflow.com/questions/40142331/how-to-request-location-permission-at-runtime*/

/**
 * @Author HengYuan
 * @Author Sean
 * Activity to display a map of the surrounding region. Will also mark nearby QRCodes.
 * @See MapController
 */
public class MapActivity extends AppCompatActivity implements /*LocationListener,*/ ActivityCompat.OnRequestPermissionsResultCallback {
    private MapView nearbyMap;
    private MapController mapController;
    private IMapController iMapController;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Initialize the osmdroid configuration which can be done through
        //Activity for the following two lines
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));

        //Create a map
        setContentView(R.layout.activity_map_layout);

        Location location;
        nearbyMap = (MapView) findViewById(R.id.map1);
        iMapController = nearbyMap.getController();
        mapController = new MapController(this);
        nearbyMap.setTileSource(TileSourceFactory.MAPNIK);
        nearbyMap.setBuiltInZoomControls(true);
        nearbyMap.setMultiTouchControls(true);
        iMapController.setZoom((double) 15);

        Log.d("map", "mapOnCreate");

        //Set a oncode listener, it's a call back when something is added to the list
        ListeningList<Location> locations = new ListeningList<>();
        locations.setOnAddListener(new OnAddListener<Location>() {

            @Override
            public void onAdd(ListeningList<Location> listeningList) {
                Log.d("map", "onCodeAdded");
                Location location = listeningList.get(0);
                GeoPoint startPoint = new GeoPoint((double) location.getLatitude(), (double) location.getLongitude());
                iMapController.setCenter(startPoint);
                MapController.qrMarkerOnMap(startPoint, nearbyMap, "Current location");
            }

        });

        Log.d("map", "map getLocation");
        mapController.getLocation(locations);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                                                      @NonNull int[] grantResults) {
        mapController.onLocationPermissionRequestResult(requestCode, grantResults);
    }

    public void onResume () {
        super.onResume();
        nearbyMap.onResume();
    }
    public void onPause () {
        super.onPause();
        nearbyMap.onPause();  //Compass
    }
}



