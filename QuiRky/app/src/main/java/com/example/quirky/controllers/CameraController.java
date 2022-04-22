/*
 * Copyright (c) 2022. CMPUT301W22T29
 * Subject to MIT License
 * See full terms at https://github.com/CMPUT301W22T29/QuiRky/blob/main/LICENSE
 */

// Much help received from https://medium.com/swlh/introduction-to-androids-camerax-with-java-ca384c522c5
// TODO: Figure out if needs better citation.

package com.example.quirky.controllers;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.media.Image;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.example.quirky.activities.CodeScannerActivity;
import com.example.quirky.ListeningList;
import com.example.quirky.activities.HubActivity;
import com.example.quirky.models.QRCode;
import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
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
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;

    private static final BarcodeScanner codeScanner = BarcodeScanning.getClient(
            new BarcodeScannerOptions.Builder().setBarcodeFormats(Barcode.FORMAT_QR_CODE).build());

    private Preview preview;
    private ImageCapture imageCapture;
    private CameraSelector cameraSelector;

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
     * @see HubActivity
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
     */
    @androidx.camera.core.ExperimentalGetImage
    public void captureQRCode(Context context, ListeningList<QRCode> codes) {
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


    @androidx.camera.core.ExperimentalGetImage
    public void captureImage(Context context, ListeningList<Bitmap> photo) {
        imageCapture.takePicture(ContextCompat.getMainExecutor(context),
                new ImageCapture.OnImageCapturedCallback() {
                    @Override
                    public void onCaptureSuccess(@NonNull ImageProxy proxy) {
                        Image image = proxy.getImage();
                        if (image != null) {
                            Bitmap bitmap = imageToBitmap(image);
                            photo.add(bitmap);
                        }
                        proxy.close();
                    }
                });
    }

    public void scanFromBitmap(Bitmap bitmap, ListeningList<QRCode> codes, Context context) {
        InputImage inputImage = InputImage.fromBitmap(bitmap, 0);
        Task<List<Barcode>> result = codeScanner.process(inputImage);
        result.addOnSuccessListener(barcodes -> {
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

    /**
     * Convert an Image object into a Bitmap object
     * @param image The Image object
     * @return The corresponding Bitmap
     */
    private static Bitmap imageToBitmap(Image image) {
        // Converting to Bitmap taken directly from
        // https://stackoverflow.com/a/58568495
        // Created by https://stackoverflow.com/users/10205791/mike-a
        // Converted to java by https://stackoverflow.com/users/7001213/ahwar
        // Published June 28, 2019
        Image.Plane[] planes = image.getPlanes();
        ByteBuffer yBuffer = planes[0].getBuffer();
        ByteBuffer uBuffer = planes[1].getBuffer();
        ByteBuffer vBuffer = planes[2].getBuffer();

        int ySize = yBuffer.remaining();
        int uSize = uBuffer.remaining();
        int vSize = vBuffer.remaining();

        byte[] nv21 = new byte[ySize + uSize + vSize];
        //U and V are swapped
        yBuffer.get(nv21, 0, ySize);
        vBuffer.get(nv21, ySize, vSize);
        uBuffer.get(nv21, ySize + vSize, uSize);

        YuvImage yuvImage = new YuvImage(nv21, ImageFormat.NV21, image.getWidth(), image.getHeight(), null);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        yuvImage.compressToJpeg(new Rect(0, 0, yuvImage.getWidth(), yuvImage.getHeight()), 75, out);

        byte[] imageBytes = out.toByteArray();
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
    }
}
