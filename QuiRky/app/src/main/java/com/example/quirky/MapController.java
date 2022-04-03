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
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import static android.content.ContentValues.TAG;
import static android.content.Context.LOCATION_SERVICE;

import java.util.ArrayDeque;
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
    private Context context;
    private ArrayDeque<Runnable> runnables;

    public MapController(Context context) {
        this.context = context;
        //this.locationManager = setLocationManager(context);
        locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        runnables = new ArrayDeque<>();
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


    //May not need setLocationManager any longer since the constructor has already done setManager's job.
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
    public void permissionsThenRun(Runnable runnable) {
        Log.d("map", "permissionsThenRun");
        if (hasLocationPermissions(context)) {
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Toast.makeText(context, "This might not do anything since your GPS is off!", Toast.LENGTH_LONG).show();
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
    @SuppressLint("MissingPermission")
    public void requestLocation(CodeList<Location> locations, Context context){
        Log.d("map", "getLocation");
        permissionsThenRun(new Runnable() {
            @Override
            public void run() {
                Log.d("map", "runGetLocation");
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
            }
        });
    }

    @SuppressLint("MissingPermission")
    @RequiresApi(api = Build.VERSION_CODES.R)
    public void requestLocationModern(CodeList<Location> locations, Context context){
        Log.d("map", "getLocation");
        permissionsThenRun(new Runnable() {
            @Override
            public void run() {
                if (hasLocationPermissions(context)){
                    locationManager.getCurrentLocation(LocationManager.GPS_PROVIDER, null, ContextCompat.getMainExecutor(context), new Consumer<Location>() {
                        @SuppressLint("MissingPermission")
                        @Override
                        public void accept(Location location) {
                            locations.add(location);
                        }
                    });
                }
            }
        });
    }

    public LocationManager getLocationManager() {

        return locationManager;
    }
}



