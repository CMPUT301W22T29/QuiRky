
/*
 * Copyright (c) 2022. CMPUT301W22T29
 * Subject to MIT License
 * See full terms at https://github.com/CMPUT301W22T29/QuiRky/blob/main/LICENSE
 */

package com.example.quirky.activities;

import android.annotation.SuppressLint;

import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.quirky.ListeningList;
import com.example.quirky.OnAddListener;
import com.example.quirky.controllers.DatabaseController;
import com.example.quirky.controllers.MapController;
import com.example.quirky.R;
import com.example.quirky.models.GeoLocation;
import com.example.quirky.models.UserOwnedQRCode;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
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
    private MapView map;
    private MapController mapController;
    private IMapController iMapController;
    private DatabaseController dc;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize the osmdroid configuration stuff
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        setContentView(R.layout.activity_map_layout);

        map = (MapView) findViewById(R.id.map1);
        iMapController = map.getController();
        mapController = new MapController(this);

        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        iMapController.setZoom((double) 15);

        dc = new DatabaseController();

        // Create ListeningLists to read location data into
        ListeningList<UserOwnedQRCode> nearbyCodes = new ListeningList<>(); // List for QRCodes near the user
        ListeningList<GeoLocation> locations = new ListeningList<>(); // List for the user's current location

        // Specify what happens when a nearby QRCode is retrieved from the database
        nearbyCodes.setOnAddListener(new OnAddListener<UserOwnedQRCode>() {
            @Override
            public void onAdd(ListeningList<UserOwnedQRCode> listeningList) {
                GeoLocation location = listeningList.get(listeningList.size() - 1).getLocation();

                // Yeet QRCode the map.
                mapController.setMarker(location, map, location.getDescription(), true);
            }
        });

        // Specify what happens once we receive current location data from a provider.
        locations.setOnAddListener(listeningList -> {
            iMapController.setCenter( listeningList.get(0).toGeoPoint() );
            mapController.setMarker(listeningList.get(0), map, "Current location", false);

            // Use our location to find nearby QRCodes
            //dc.readNearbyCodes(listeningList.get(0), nearbyCodes);

            //sike, hardcoded long and lat:
            //FIXME: Implement the other needed stuff and take away this hard coded crap.
            dc.readNearbyCodes(new GeoLocation(), nearbyCodes);
        });

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
        map.onResume();
    }
    public void onPause () {
        super.onPause();
        map.onPause();  //Compass
    }
}




