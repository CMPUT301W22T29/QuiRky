package com.example.quirky;
import static androidx.core.content.ContextCompat.getSystemService;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.Manifest;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Marker;

import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import static android.content.ContentValues.TAG;
import static android.content.Context.LOCATION_SERVICE;

import java.util.ArrayList;
import java.util.function.Consumer;


/*
source: https://osmdroid.github.io/osmdroid/How-to-use-the-osmdroid-library.html
author: osmdroid team : https://github.com/osmdroid/osmdroid
Publish Date:2019-09-27
 */
/*source:https://stackoverflow.com/questions/40142331/how-to-request-location-permission-at-runtime*/
public class MapController{
    public static final int LOCATION_REQUEST_CODE = 99;
    private LocationManager locationManager;
    private static final String[] LOCATION_PERMISSION_FINE = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
    private static final String[] LOCATION_PERMISSION_COARSE = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION};

    public MapController(Context context) {
        this.locationManager = setLocationManager(context);
    }

    public static boolean requestingLocationPermissions(int request_code){
        return(request_code == LOCATION_REQUEST_CODE);

    }
    protected static boolean hasLocationPermissions(Context context) {
        boolean fineGranted
                = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
        boolean coarseGranted
                = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
        return fineGranted && coarseGranted;
    }

    protected static void requestLocationPermission(Context context) {
        ActivityCompat.requestPermissions((Activity) context, new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQUEST_CODE);
    }

    @SuppressLint("MissingPermission")
    public LocationManager setLocationManager(Context context){
        if(locationManager == null){
            locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        }
        //if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
        //    if (hasLocationPermissions(context)){
        //        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,0, (LocationListener) context);
        //    }
        //}
        return locationManager;
    }
    @SuppressLint("MissingPermission")
    public ArrayList<Location> requestLocation(ArrayList<Location> locations, Context context){
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            if (hasLocationPermissions(context)){
                locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, new LocationListener() {
                    @Override
                    public void onLocationChanged(@NonNull Location location) {
                        locations.add(location);
                    }
                },null);//RequestLocationUpdate once
                //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,0, (LocationListener) context);
            }
        }
        return locations;
    }

    @SuppressLint("MissingPermission")
    @RequiresApi(api = Build.VERSION_CODES.R)
    public ArrayList<Location> requestLocationModern(ArrayList<Location> locations, Context context){
        if (hasLocationPermissions(context)){
            locationManager.getCurrentLocation(LocationManager.GPS_PROVIDER, null, ContextCompat.getMainExecutor(context), new Consumer<Location>() {
                @SuppressLint("MissingPermission")
                @Override
                public void accept(Location location) {
                    locations.add(location);
                }
            });
        }
        return locations;
    }

    public LocationManager getLocationManager() {
        return locationManager;
    }
}



