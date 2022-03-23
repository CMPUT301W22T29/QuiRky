package com.example.quirky;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.Manifest;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;

import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


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
public class MapActivity extends AppCompatActivity implements LocationListener{
    private MapView nearbymap = null;
    private MyLocationNewOverlay locationOverlay;
    private LocationManager locationManager;
    private static final String[] LOCATION_PERMISSION = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
    public static final int LOCATION_REQUEST_CODE = 99;
    //Context context;



    @RequiresApi(api = VERSION_CODES.BASE)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Initialize the osmdroid configuration which can be done through
        Context cxt = getApplicationContext();
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        //Create a map
        //setContentView(R.layout.activity_map_layout);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQUEST_CODE);
            return;
        } else {
            try {
                setmap();
            }catch(NullPointerException ex){
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case LOCATION_REQUEST_CODE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(MapActivity.this, "Permission Granted!", Toast.LENGTH_SHORT).show();
                    setmap();

                } else{
                    Toast.makeText(MapActivity.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
        }
    }
    @SuppressLint("MissingPermission")
    public void setmap() {
        setContentView(R.layout.activity_map_layout);
        IMapController mapController = null;
        nearbymap = (MapView) findViewById(R.id.map);
        try{
            nearbymap.setTileSource(TileSourceFactory.MAPNIK);
            nearbymap.setBuiltInZoomControls(true);
            nearbymap.setMultiTouchControls(true);
            mapController = nearbymap.getController();
            mapController.setZoom(20);
        }catch(NullPointerException exception){

        }

        //Make the map can zoom in or out


        if(locationManager == null){
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        }
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,10000,1000,this);

        }

        //Get current location automatically
        /*GeoPoint startPoint = new GeoPoint(location.getLatitude(),location.getLongitude());
        mapController.setCenter(startPoint);
        Marker qrmarker = new Marker(nearbymap);
        qrmarker.setPosition(startPoint);
        qrmarker.setAnchor(Marker.ANCHOR_CENTER,Marker.ANCHOR_BOTTOM);
        nearbymap.getOverlays().add(qrmarker);
        qrmarker.setTitle("Current location");*/
        //provider.addLocationSource(LocationManager.NETWORK_PROVIDER);
        //locationOverlay = new MyLocationNewOverlay(provider, nearbymap);
        //locationOverlay.enableFollowLocation();
        //nearbymap.getOverlayManager().add(locationOverlay);
        //set a marker on our current location
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        nearbymap = (MapView) findViewById(R.id.map);
        IMapController mapController = null;
        Marker qrmarker = null;
        try{
            mapController = nearbymap.getController();
            GeoPoint startPoint = new GeoPoint(location.getLatitude(),location.getLongitude());
            mapController.setCenter(startPoint);
            mapController.setZoom(20);
            qrmarker = new Marker(nearbymap);
            qrmarker.setPosition(startPoint);
            qrmarker.setAnchor(Marker.ANCHOR_CENTER,Marker.ANCHOR_BOTTOM);
            nearbymap.getOverlays().add(qrmarker);
            qrmarker.setTitle("Current location");
        }catch(NullPointerException ex3){

        }


    }


    public void onResume(){
        super.onResume();
        try {
            nearbymap.onResume();
        }catch(NullPointerException ex1){

        }

    }

    public void onPause(){
        super.onPause();
        try{
            nearbymap.onPause();  //Compass
        }catch(NullPointerException ex2){

        }
    }

}
