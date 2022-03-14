package com.example.quirky;

import android.content.Context;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;//Tile source factory used for manipulating the map
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayItem;

/*
source: https://osmdroid.github.io/osmdroid/How-to-use-the-osmdroid-library.html
author: osmdroid team : https://github.com/osmdroid/osmdroid
Publish Date:2019-09-27
 */
public class MapActivity extends AppCompatActivity {
    MapView nearbymap = null;
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
        GeoPoint startPoint = new GeoPoint(53.52682, -113.52449);
        mapController.setCenter(startPoint);
        //set a marker on our current location
        Marker qrmarker = new Marker(nearbymap);
        qrmarker.setPosition(startPoint);
        qrmarker.setAnchor(Marker.ANCHOR_CENTER,Marker.ANCHOR_BOTTOM);
        nearbymap.getOverlays().add(qrmarker);
        qrmarker.setTitle("Current location");
        // use this to assign QR codes images to our marker
        // qrmarker.setImage();
        /*qrmarker.setOnMarkerClickListener(final Marker.OnMarkerClickListener listener){
            @Override
            public boolean onMarkerClick(Marker marker){
                if(marker.equals(qrmarker)){
                    qrmarker.setTitle("Current location");
                    return true;
                }
                return false;
            }
        }*/


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
