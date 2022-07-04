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

    /**
     * Checks (and requests if needed) location permissions before running some code
     *
     * If we don't have location permissions, save the code we want to run for later and request
     * permissions, once the user has given permissions, onLocationPermissionRequestResult() will
     * run the code that needed the permissions.
     *
     * @param runnable The code to run once we have verified the location permissions are granted
     */
    public void permissionsThenRun(Runnable runnable) {
        Log.d("map", "permissionsThenRun");

        if (hasLocationPermissions(context)) {
            // Run the runnable if we have location permissions
            runnable.run();
        } else {
            // If we do not have location permissions, save the runnable for later and request permissions
            runnables.push(runnable);
            requestLocationPermission(context);
        }
    }

    /**
     * Handle the results of a location permission request
     *
     * This method is intended to be called from an onRequestPermissionsResult callback.
     * If permissions were granted, this method will run the code that caused the permissions
     * dialogue to appear, without the user having to re-press any buttons or whatever.
     *
     * @param requestCode Internal number representing the type of permission being requested
     * @param grantResults Array containing the results of one or more permission requests.
     */
    public void onLocationPermissionRequestResult(int requestCode, @NonNull int[] grantResults) {
        // Check that we are requesting location permissions
        if (MapController.requestingLocationPermissions(requestCode)) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // If permissions were granted, run the code that required the permissions
                runnables.pop().run();

            } else {
                // If permissions were not granted, discard the code that required the permissions
                // and notify the user.
                runnables.pop();
                Toast.makeText(context,
                        "Enable location permissions to do cool location things",
                                                                          Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * Get the current location of the user
     *
     * Uses different approaches for getting the user's location depending on their android version.
     * If the user's GPS provider is not enabled, this will only tell the user that their GPS is
     * disabled, otherwise it will attempt to get their location, and store it in the provided
     * ListeningList, which calls onAdd whenever something is added to it. Make sure to set an
     * onAddListener and override onAdd!
     *
     * @param locations A listening list to add the location to
     */
    @SuppressLint("MissingPermission")
    public void getLocation(ListeningList<GeoLocation> locations) {
        Log.d("map", "getLocation");

        // Check and/or get location permissions, then try to get the current location
        permissionsThenRun(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.R)
            @Override
            public void run() {
                Log.d("map", "runGetLocation");

                // Make sure GPS is enabled
                if(locationManager.isProviderEnabled(PROVIDER)) {

                    // Different Android versions require different ways of getting location
                    if (Integer.parseInt(android.os.Build.VERSION.SDK) < 30) {

                        // Ask our location provider to hunt us down and tell us where we are
                        locationManager.requestSingleUpdate(PROVIDER, new LocationListenerCompat() {
                            @Override
                            public void onLocationChanged(@NonNull Location location) {

                                // Once we know our location, drop it in the ListeningList so other code can use it.
                                locations.add( new GeoLocation(location) );
                            }
                        },null);

                    }//RequestLocationUpdate once
                        //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,0, (LocationListener) context);

                    // Newer Android versions use this code
                    else {
                        // Ask our provider to hunt us down, then, we'll have a Consumer deal with the location info
                        locationManager.getCurrentLocation(PROVIDER, null, ContextCompat.getMainExecutor(context), new Consumer<Location>() {
                            @SuppressLint("MissingPermission")
                            @Override
                            public void accept(Location location) {

                                // Once we know our location, drop it in the ListeningList so other code can use it.
                                locations.add( new GeoLocation(location) );
                            }
                        });
                    }

                } else {    // If GPS is not enabled, complain to the user
                    Toast.makeText(context, "Your GPS is probably off, turn it on to use this feature!",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
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



