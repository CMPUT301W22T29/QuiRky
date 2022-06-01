/*
 * Copyright (c) 2022. CMPUT301W22T29
 * Subject to MIT License
 * See full terms at https://github.com/CMPUT301W22T29/QuiRky/blob/main/LICENSE
 */

package com.example.quirky.controllers;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.Manifest;

import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.location.LocationListenerCompat;

import static android.content.Context.LOCATION_SERVICE;

import com.example.quirky.ListeningList;
import com.example.quirky.activities.MapActivity;

import com.example.quirky.models.GeoLocation;

import java.util.ArrayDeque;
import java.util.ArrayList;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.function.Consumer;


/*
source: https://osmdroid.github.io/osmdroid/How-to-use-the-osmdroid-library.html
author: osmdroid team : https://github.com/osmdroid/osmdroid
Publish Date:2019-09-27
 */
/*source:https://stackoverflow.com/questions/40142331/how-to-request-location-permission-at-runtime*/

/**
 * @author Sean
 * @author HengYuan
 * Controller class to manage computations that MapActivity needs.
 * @see MapActivity
 */
public class MapController {
    public static final int LOCATION_REQUEST_CODE = 99;
    private final LocationManager locationManager;
    private static final String[] LOCATION_PERMISSIONS = new String[] {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    private final Context context;
    private static final String PROVIDER = (Build.VERSION.SDK_INT > 30) ? LocationManager.FUSED_PROVIDER
                                                                        : LocationManager.GPS_PROVIDER;
    private final ArrayDeque<Runnable> runnables;


    /**
     * Constructor initialised with context
     */
    public MapController(Context context) {
        this.context = context;
        locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        runnables = new ArrayDeque<>();
    }

    public static boolean requestingLocationPermissions(int request_code) {
        return (request_code == LOCATION_REQUEST_CODE);
    }

    /**
     * Checks if the user has granted location permissions
     * @param context Context
     * @return If the user has granted location permissions
     */
    public static boolean hasLocationPermissions(Context context) {
        boolean fineGranted
                = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
        boolean coarseGranted
                = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
        return fineGranted && coarseGranted;
    }

    /**
     * Request location permissions from the user
     * @param context context
     */
    public static void requestLocationPermission(Context context) {
        ActivityCompat.requestPermissions(
                                   (Activity) context, LOCATION_PERMISSIONS, LOCATION_REQUEST_CODE);
    }

    public void permissionsThenRun(Runnable runnable) {
        Log.d("map", "permissionsThenRun");
        if (hasLocationPermissions(context)) {
            if (!locationManager.isProviderEnabled(PROVIDER)) {
                Toast.makeText(context, "This might not do anything since your GPS is off!",
                                                                            Toast.LENGTH_LONG).show();
            }
            runnable.run();
        } else {
            runnables.push(runnable);
            requestLocationPermission(context);
        }
    }

    public void onLocationPermissionRequestResult(int requestCode, @NonNull int[] grantResults) {
        if (MapController.requestingLocationPermissions(requestCode)) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                runnables.pop().run();
            } else {
                runnables.pop();
                Toast.makeText(context,
                        "Enable location permissions to do cool location things",
                                                                          Toast.LENGTH_LONG).show();
            }
        }
    }

    // TODO: someone who knows how this works, javadoc it
    /**
     * Get the current location of the user
     * @param locations A listening list to add the location to
     */
    @SuppressLint("MissingPermission")
    public void getLocation(ListeningList<GeoLocation> locations) {
        Log.d("map", "getLocation");
        permissionsThenRun(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.R)
            @Override
            public void run() {
                Log.d("map", "runGetLocation");
                if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                    if (hasLocationPermissions(context)){
                        if (Integer.parseInt(android.os.Build.VERSION.SDK) < 30){
                        locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, new LocationListenerCompat() {
                            @Override
                            public void onLocationChanged(@NonNull Location location) {
                                locations.add( new GeoLocation(location) );
                            }
                        },null);
                        }//RequestLocationUpdate once
                        //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,0, (LocationListener) context);
                    else {
                            locationManager.getCurrentLocation(LocationManager.GPS_PROVIDER, null, ContextCompat.getMainExecutor(context), new Consumer<Location>() {
                                @SuppressLint("MissingPermission")
                                @Override
                                public void accept(Location location) {
                                    locations.add( new GeoLocation(location) );
                                }
                            });
                        }
                    }
                }
            }
        });
    }

    public void writeQrCodesToMap(MapView map){
        ArrayList<GeoLocation> locations = new ArrayList<>();
        locations.add(new GeoLocation(10.0, 10.0));

        for (GeoLocation location : locations)
            setMarker(location, map, location.getDescription(), true);
    }

    public void setMarker(GeoLocation point, MapView map, String text, boolean translucent) {
        Marker marker = new Marker(map);

        marker.setPosition(point.toGeoPoint());
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);

        if(translucent)
            marker.setAlpha((float) 0.6);
        else
            marker.setAlpha(1);

        map.getOverlays().add(marker);
        marker.setTitle(text);
    }
}



