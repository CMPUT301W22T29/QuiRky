package com.example.quirky;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.Manifest;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;

import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;//Tile source factory used for manipulating the map
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;


/*
source: https://osmdroid.github.io/osmdroid/How-to-use-the-osmdroid-library.html
author: osmdroid team : https://github.com/osmdroid/osmdroid
Publish Date:2019-09-27
 */
/*source:https://stackoverflow.com/questions/40142331/how-to-request-location-permission-at-runtime*/
public class MapActivity extends AppCompatActivity {
    MapView nearbymap = null;
    private MyLocationNewOverlay locationOverlay;
    private LocationManager mLocMgr;
    private static final String[] LOCATION_PERMISSION = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
    public static final int LOCATION_REQUEST_CODE = 99;
    private Object showExplanation;


    /*private void ShowLocationStatus() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            }
        } else {
            Toast.makeText(MapActivity.this, "Permission is Granted", Toast.LENGTH_SHORT).show();
        }
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Initialize the osmdroid configuration which can be done through
        Context cxt = getApplicationContext();
        Configuration.getInstance().load(cxt, PreferenceManager.getDefaultSharedPreferences(cxt));

        //Create a map
        setContentView(R.layout.activity_map_layout);
        GpsMyLocationProvider provider = new GpsMyLocationProvider(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQUEST_CODE);
            return;
        } else {
            Toast.makeText(MapActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
        }
        Location location = mLocMgr.requestLocationUpdates(provider,5000,5,this);
        setmap(location);
    }
    public void setmap(Location location) {
        nearbymap = (MapView) findViewById(R.id.map);
        nearbymap.setTileSource(TileSourceFactory.MAPNIK);
        //Make the map can zoom in or out
        nearbymap.setBuiltInZoomControls(true);
        nearbymap.setMultiTouchControls(true);
        IMapController mapController = nearbymap.getController();
        mapController.setZoom(15);
        //Get current location automatically
        GeoPoint startPoint = new GeoPoint(location.getLatitude(),location.getLongitude());
        mapController.setCenter(startPoint);
        Marker qrmarker = new Marker(nearbymap);
        qrmarker.setPosition(startPoint);
        qrmarker.setAnchor(Marker.ANCHOR_CENTER,Marker.ANCHOR_BOTTOM);
        nearbymap.getOverlays().add(qrmarker);
        qrmarker.setTitle("Current location");
        //provider.addLocationSource(LocationManager.NETWORK_PROVIDER);
        //locationOverlay = new MyLocationNewOverlay(provider, nearbymap);
        //locationOverlay.enableFollowLocation();
        //nearbymap.getOverlayManager().add(locationOverlay);
        //set a marker on our current location
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case LOCATION_REQUEST_CODE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(MapActivity.this, "Permission Granted!", Toast.LENGTH_SHORT).show();

                } else{
                    Toast.makeText(MapActivity.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
        }
    }

    public void onResume(){
        super.onResume();
        nearbymap.onResume();

    }

    public void onPause(){
        super.onPause();
        nearbymap.onPause();  //Compass
    }

}
