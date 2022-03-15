/*
 * CameraController.java
 *
 * Version 0.2.0
 * Version History:
 *      Version 0.1.0 -- Camera Previewing Works
 *      Version 0.2.0 -- QR Code Photo Capturing Works
 *
 * Date (v0.2.0): March 14, 2022
 *
 * Copyright (c) 2022. CMPUT301W22T29
 * Subject to MIT License
 * See full terms at https://github.com/CMPUT301W22T29/QuiRky/blob/main/LICENSE
 */

// Much help received from https://medium.com/swlh/introduction-to-androids-camerax-with-java-ca384c522c5
// TODO: Figure out if needs better citation.

package com.example.quirky;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.Image;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.common.InputImage;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Manages all android camera functionality in quirky.
 * <p>
 * Has methods for checking and requesting camera permissions, starting camera previews, which can
 * be implemented graphically with an android camera preview view, and capturing images, which can be
 * analyzed for QR codes in the QRCodeController class.
 * This class is intended to be used as a singleton, only one instance should be running at a time.
 *
 * @author Sean Meyers
 * @version 0.2.0
 * @see androidx.camera.core
 * @see CodeScannerActivity
 * @see QRCode
 * @see QRCodeController
 */
public class CameraController {
    private static final String[] CAMERA_PERMISSION = new String[]{Manifest.permission.CAMERA};
    private static final int CAMERA_REQUEST_CODE = 10;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private Preview preview;
    private ImageCapture imageCapture;
    private CameraSelector cameraSelector;

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
        imageCapture = new ImageCapture.Builder().build();
        cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK).build();
    }

    public void startCameraPreview(Preview.SurfaceProvider surfaceProvider, Context context) {
        cameraProviderFuture.addListener(new Runnable() {
            @Override
            public void run() {
                try {
                    ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                    preview.setSurfaceProvider(surfaceProvider);
                    cameraProvider.bindToLifecycle((LifecycleOwner) context, cameraSelector, imageCapture, preview);
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, ContextCompat.getMainExecutor(context));
    }

    @androidx.camera.core.ExperimentalGetImage
    public ArrayList<QRCode> captureQRCodes(Context context) throws NoSuchAlgorithmException {
        Log.d("captureQRCode", "enter method"); //TODO: get rid of.
        ArrayList<QRCode> codes = new ArrayList<>();
        imageCapture.takePicture(ContextCompat.getMainExecutor(context),
                new ImageCapture.OnImageCapturedCallback() {
                    @Override
                    public void onCaptureSuccess(@NonNull ImageProxy image) {
                        Log.d("captureQRCode", "onCaptureSuccess"); //TODO: get rid of.
                        Image mediaImage = image.getImage();
                        if (mediaImage != null) {
                            Log.d("captureQRCode", "mediaImage != null");   //TODO: get rid of.
                            InputImage inputImage = InputImage.fromMediaImage(mediaImage, image.getImageInfo().getRotationDegrees());
                            codes.addAll(QRCodeController.scanQRCodes(inputImage));
                        }
                        image.close();
                        Log.d("captureQRCode", "close image");  //TODO: get rid of.
                    }
                });
        Log.d("captureQRCode", "exit method");  //TODO: get rid of.
        return codes;
    }
}
