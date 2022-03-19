package com.example.quirky;

import android.content.Context;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;

import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;//Tile source factory used for manipulating the map
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

/*
source: https://osmdroid.github.io/osmdroid/How-to-use-the-osmdroid-library.html
author: osmdroid team : https://github.com/osmdroid/osmdroid
Publish Date:2019-09-27
 */
public class MapActivity extends AppCompatActivity {
    MapView nearbymap = null;
    private MyLocationNewOverlay locationOverlay;
    private LocationManager mLocMgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
       
        super.onCreate(savedInstanceState);


        //Initialize the osmdroid configuration which can be done through
        Context cxt = getApplicationContext();
        Configuration.getInstance().load(cxt, PreferenceManager.getDefaultSharedPreferences(cxt));

        //Create a map
        setContentView(R.layout.activity_map_layout);
        nearbymap = (MapView) findViewById(R.id.map);
        nearbymap.setTileSource(TileSourceFactory.MAPNIK);
        //Make the map can zoom in or out
        nearbymap.setBuiltInZoomControls(true);
        nearbymap.setMultiTouchControls(true);
        IMapController mapController = nearbymap.getController();
        mapController.setZoom(15);
        //zoom into university of Alberta
        //Get current location automatically
        GpsMyLocationProvider provider = new GpsMyLocationProvider(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = mLocMgr.getLastKnownLocation(String.valueOf(provider));
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
    
    public void onResume(){
        super.onResume();
        nearbymap.onResume();

    }

    public void onPause(){
        super.onPause();
        nearbymap.onPause();  //Compass
    }

}
