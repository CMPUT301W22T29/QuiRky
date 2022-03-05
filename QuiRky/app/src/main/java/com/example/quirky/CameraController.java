// Much help received from https://medium.com/swlh/introduction-to-androids-camerax-with-java-ca384c522c5
// TODO: Figure out if needs better citation.

package com.example.quirky;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.camera.core.CameraSelector;
import androidx.camera.core.Preview;
import androidx.camera.core.UseCase;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class CameraController {
    private static final String[] CAMERA_PERMISSION = new String[]{Manifest.permission.CAMERA};
    private static final int CAMERA_REQUEST_CODE = 10;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private Preview preview;
    private CameraSelector cameraSelector;
    //private static CameraController instance;

    /*public static CameraController getInstance() {
        return instance;
    }*/

    protected static boolean hasCameraPermission(Context context) {
        return ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED;
    }

    /* Requests permissions.
       Returns true if permissions were granted.
     */
    protected static boolean requestCameraPermission(Context context) {
        ActivityCompat.requestPermissions(
                                        (Activity) context, CAMERA_PERMISSION, CAMERA_REQUEST_CODE);
        return hasCameraPermission(context);
    }

    public CameraController(Context context) {
        assert hasCameraPermission(context);    // I have no idea if this is a good idea or not.
        cameraProviderFuture = ProcessCameraProvider.getInstance(context);
        preview = new Preview.Builder().build();
        cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK).build();
        //instance = this;
    }

    public void startCameraPreview(Preview.SurfaceProvider surfaceProvider, Context context) {
        cameraProviderFuture.addListener(new Runnable() {
            @Override
            public void run() {
                try {
                    ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                    preview.setSurfaceProvider(surfaceProvider);
                    cameraProvider.bindToLifecycle((LifecycleOwner) context, cameraSelector, preview);
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, ContextCompat.getMainExecutor(context));
    }
}
