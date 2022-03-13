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
        nearbymap.setBuiltInZoomControls(true);
        nearbymap.setMultiTouchControls(true);
        IMapController mapController = nearbymap.getController();
        mapController.setZoom(15);
        GeoPoint startPoint = new GeoPoint(53.52682, -113.52449);
        mapController.setCenter(startPoint);

    }
    public void onResume(){
        super.onResume();
        nearbymap.onResume();

    }
    public void onPause(){
        super.onPause();
        nearbymap.onPause();  //needed for compass, my location overlays, v6.0.0 and up
    }
}

