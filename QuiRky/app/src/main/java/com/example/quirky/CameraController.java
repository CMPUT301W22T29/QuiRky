/*
 * CameraController.java
 *
 * Version 0.2.0
 * Version History:
 *      Version 0.1.0 -- Camera Previewing Works
 *      Version 0.2.0 -- QR Code Photo Capturing Works
 *      Version 0.2.1 -- Refined Permissions and QR Capturing
 *
 * Date (v0.2.1): March 19, 2022
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
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
 * @version 0.2.1
 * @see androidx.camera.core
 * @see CodeScannerActivity
 * @see QRCode
 * @see QRCodeController
 */
public class CameraController {
    private static final String[] CAMERA_PERMISSION = new String[]{Manifest.permission.CAMERA};
    private static final int CAMERA_REQUEST_CODE = 10;
    private static final BarcodeScanner codeScanner = BarcodeScanning.getClient(
            new BarcodeScannerOptions.Builder().setBarcodeFormats(Barcode.FORMAT_QR_CODE).build());
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private Preview preview;
    private ImageCapture imageCapture;
    private CameraSelector cameraSelector;
    private MemoryController memoryController;

    /**
     * Check if the app has permission to use the camera.
     *
     * @param context
     *      - The usual context. Just pass in the instance of the calling activity for this.
     * @return
     *      - true if the app has permission to access the camera, false otherwise.
     */
    protected static boolean hasCameraPermission(Context context) {
        return ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Request the user for permission to use the camera.
     *
     * @param context
     *      - The usual context. Just pass in the instance of the calling activity for this.
     */
    protected static void requestCameraPermission(Context context) {
        ActivityCompat.requestPermissions(
                                        (Activity) context, CAMERA_PERMISSION, CAMERA_REQUEST_CODE);
    }

    /**
     * Checks if the permissions being requested are camera permissions.
     *
     * @param requestCode
     *      - The request code of the permission request we are checking.
     * @return
     *      - true if permissions being requested are for the camera, false otherwise.
     * @see com.example.quirky.StartingPageActivity
     * @see ActivityCompat.OnRequestPermissionsResultCallback
     */
    protected static boolean requestingCameraPermissions(int requestCode) {
        return (requestCode == CAMERA_REQUEST_CODE);
    }

    /**
     * Initialize stuff needed to do camera things.
     *
     * This was designed such that it only be called once, do not instantiate multiple instances of
     * this class at once, it is a singleton.
     * TODO: May need an overhaul once image saving from other activities is implemented.
     *
     * @param context
     */
    public CameraController(Context context) {
        assert hasCameraPermission(context);
        cameraProviderFuture = ProcessCameraProvider.getInstance(context);
        preview = new Preview.Builder().build();
        imageCapture = new ImageCapture.Builder().build();
        cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK).build();
        memoryController = new MemoryController(context);
    }

    /**
     * Start the camera with the use cases initialized in the CameraController's constructor.
     *
     * @param surfaceProvider
     *      - The surfaceProvider for the preview view. Necessary to display camera feed while
     *        scanning.
     * @param context
     *      - The activity responsible for doing camera things, it should contain a preview view and
     *        a button or something to capture images.
     * @see CodeScannerActivity
     */
    public void startCamera(Preview.SurfaceProvider surfaceProvider, Context context) {
        cameraProviderFuture.addListener(new Runnable() {
            @Override
            public void run() {
                try {
                    ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                    preview.setSurfaceProvider(surfaceProvider);
                    cameraProvider.bindToLifecycle(
                                    (LifecycleOwner) context, cameraSelector, imageCapture, preview);
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, ContextCompat.getMainExecutor(context));
    }

    /**
     * Capture images and construct <code>QRCode</code> instances from all QR codes in the image.
     *
     * <code>QRCode</code> construction is done in <code>QRCodeController</code>.
     * The returned <code>ArrayList</code> will be empty until QR code processing is done. This does not take
     * long, but it is worth noting as it could affect whether operating on the list will do what is
     * expected.
     *
     * @param context
     *      - The activity that the user is interacting with to scan QR codes, same as in
     *        <code>startCamera</code>.
     * @return
     *      - An <code>ArrayList</code> of <code>QRCode</code>s generated from codes found in the
     *        captured image.
     * @see QRCodeController
     */
    @androidx.camera.core.ExperimentalGetImage
    public void captureQRCodes(Context context, ListeningList<QRCode> codes) {
        // TODO: edit javadoc
        imageCapture.takePicture(ContextCompat.getMainExecutor(context),
                new ImageCapture.OnImageCapturedCallback() {
                    @Override
                    public void onCaptureSuccess(@NonNull ImageProxy image) {
                        Image mediaImage = image.getImage();
                        if (mediaImage != null) {
                            InputImage inputImage = InputImage.fromMediaImage(
                                             mediaImage, image.getImageInfo().getRotationDegrees());
                            CameraController.scanQRCodes(inputImage, codes, context);
                        }
                        image.close();
                    }
                });
    }

    public void capturePhoto(Context context) {
        ImageCapture.OutputFileOptions outputFileOptions = new ImageCapture.OutputFileOptions.Builder(memoryController.newPhotoFile()).build();
        imageCapture.takePicture(outputFileOptions, ContextCompat.getMainExecutor(context), new ImageCapture.OnImageSavedCallback() {
            @Override
            public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {

            }

            @Override
            public void onError(@NonNull ImageCaptureException exception) {

            }
        });
    }

    /**
     * Analyzes an image for qr codes, and constructs <code>QRCode</code>s from their data.
     *
     * @param inputImage
     *      - The image to analyze.
     * @param codes
     *      - The list in which the <code>QRCode</code>s will be stored once they are constructed.
     * @param context
     *      - The activity that the user is interacting with to capture QR code images.
     * @see CameraController
     */
    public static void scanQRCodes(InputImage inputImage, ListeningList<QRCode> codes, Context context) {
        // TODO: edit javadoc
        Task<List<Barcode>> result = codeScanner.process(inputImage)
                .addOnSuccessListener(barcodes -> {
                    // Construct a QRCode with the scanned raw data
                    for (Barcode barcode: barcodes) {
                        codes.add(new QRCode(barcode.getRawValue()));
                    }
                    if (codes.size() == 0) {
                        String text
                                = "Could not find any QR codes. Move closer or further and try scanning again.";
                        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
                    }
                });
    }
}
