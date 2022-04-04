
package com.example.quirky;

import android.annotation.SuppressLint;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;//Tile source factory used for manipulating the map
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;
import java.util.function.Consumer;



/*
source: https://osmdroid.github.io/osmdroid/How-to-use-the-osmdroid-library.html
author: osmdroid team : https://github.com/osmdroid/osmdroid
Publish Date:2019-09-27
 */
/*source:https://stackoverflow.com/questions/40142331/how-to-request-location-permission-at-runtime*/


public class MapActivity extends AppCompatActivity {
    private MapView nearbymap;
    private MapController mapController;
    private LocationManager locationManager;
    private IMapController iMapController;
    private DatabaseController dc;


    @SuppressLint("MissingPermission")
    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Initialize the osmdroid configuration which can be done through
        //Activity for the following two lines
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        //Create a map
        setContentView(R.layout.activity_map_layout);
        Location location;
        dc = new DatabaseController(this);
        nearbymap = (MapView) findViewById(R.id.map1);
        iMapController = nearbymap.getController();
        mapController = new MapController(this);
        nearbymap.setTileSource(TileSourceFactory.MAPNIK);
        nearbymap.setBuiltInZoomControls(true);
        nearbymap.setMultiTouchControls(true);
        iMapController.setZoom((double) 15);
        locationManager = mapController.getLocationManager();
        //Set a oncode listener, it's a call back when something is added to the branch
        CodeList<Location> locations = new CodeList<>();
        locations.setOnCodeAddedListener(new OnCodeAddedListener<Location>() {
            @Override
            public void onCodeAdded(CodeList<Location> codeList) {
                Log.d("map", "onCodeAdded");
                Location location = codeList.get(0);
                GeoPoint startPoint = new GeoPoint((double) location.getLatitude(), (double) location.getLongitude());
                iMapController.setCenter(startPoint);
                mapController.qrMarkerOnMap(startPoint,nearbymap,"Current location");
                mapController.writeQrCodesToMap(dc,nearbymap,"Nearby QR code located");
            }
        });
        mapController.requestLocation(locations,this);
    }



    /*@SuppressLint("MissingPermission")
    @Override
    public void onLocationChanged(@NonNull Location location) {
        Marker qrmarker;
        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Toast.makeText(this,"Current Location:"+String.valueOf(location.getLatitude())+","+String.valueOf(location.getLongitude()),Toast.LENGTH_LONG).show();
        GeoPoint startPoint = new GeoPoint((double)location.getLatitude(),(double)location.getLongitude());
        iMapController.setCenter(startPoint);
        qrmarker = new Marker(nearbymap);
        qrmarker.setPosition(startPoint);
        qrmarker.setAnchor(Marker.ANCHOR_CENTER,Marker.ANCHOR_BOTTOM);
        nearbymap.getOverlays().add(qrmarker);
        qrmarker.setTitle("Current location");
    }*/
        public void onResume () {
            super.onResume();
            nearbymap.onResume();
        }
        public void onPause () {
            super.onPause();
            nearbymap.onPause();  //Compass
        }
    }

    //MapActivity test.
    /*Case1:I open the MapActivity and allow the device to read my location, It directs me to my current location and
    and I see a marker on the map that shows my current location and nearby qr codes stored in the firebase store.
    I can zoom in and out with my finger and move around the map with my finger and the map will update my location as long
    as I move.

    Case2: I open the app and goes to MapActivity, I deny it's request to ask for accessing location permission I'm
    not able to enter MapActivity and the app will prompt a Toast to encourage me to allow it's request.
     */




