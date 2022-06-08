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
import android.media.Image;
import android.util.Log;
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

import com.example.quirky.activities.CameraActivity;
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
 * @see CameraActivity
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
     * @see CameraActivity
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

    @androidx.camera.core.ExperimentalGetImage
    public void captureImage(Context context, ListeningList<Bitmap> photo) {
        imageCapture.takePicture(ContextCompat.getMainExecutor(context),
                new ImageCapture.OnImageCapturedCallback() {
                    @Override
                    public void onCaptureSuccess(@NonNull ImageProxy proxy) {
                        Image image = proxy.getImage();
                        if (image != null) {
                            logImageType(image);
                            Bitmap bitmap = JPEGToBitmap(image);
                            photo.add(bitmap);
                        }
                        proxy.close();
                    }
                });
    }

    public void scanFromBitmap(Bitmap bitmap, ListeningList<QRCode> codes, Context context) {
        InputImage inputImage = InputImage.fromBitmap(bitmap, 90);
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
     * Convert a JPEG Image object into a Bitmap
     * @param image An Image object whose format is type JPEG
     * @return The corresponding Bitmap
     */
    private static Bitmap JPEGToBitmap(Image image) {
        assert ( image.getFormat() == ImageFormat.JPEG ) : "You must pass an Image of format JPEG! Check image type with logImageType(image)";

        Image.Plane x = image.getPlanes()[0];
        ByteBuffer buff = x.getBuffer();
        byte[] y = new byte[buff.capacity()];
        buff.get(y);

        return BitmapFactory.decodeByteArray(y, 0, y.length);
    }


    private void logImageType(Image i) {
        int type = i.getFormat();
        Log.d(" - - - - - CameraController says", "image is of type\n");

        switch (type) {
            case ImageFormat.DEPTH16:
                Log.d("", "\tDepth16\n");
            case ImageFormat.DEPTH_JPEG:
                Log.d("", "\tDepth_Jpeg\n");
            case ImageFormat.DEPTH_POINT_CLOUD:
                Log.d("", "\tDepth Point Cloud\n");
            case ImageFormat.FLEX_RGBA_8888:
                Log.d("", "\tFlex \n");
            case ImageFormat.FLEX_RGB_888:
                Log.d("", "\tFlex 888\n");
            case ImageFormat.HEIC:
                Log.d("", "\tHeic\n");
            case ImageFormat.JPEG:
                Log.d("", "\tJpeg\n");
            case ImageFormat.NV16:
                Log.d("", "\tNV16\n");
            case ImageFormat.NV21:
                Log.d("", "\tNV21\n");
            case ImageFormat.PRIVATE:
                Log.d("", "\tPrivate\n");
            case ImageFormat.RAW10:
                Log.d("", "\tRAW10\n");
            case ImageFormat.RAW12:
                Log.d("", "\tRAW12\n");
            case ImageFormat.RAW_PRIVATE:
                Log.d("", "\tRAW PRIVATE\n");
            case ImageFormat.RAW_SENSOR:
                Log.d("", "\tRAW SENSOR\n");
            case ImageFormat.UNKNOWN:
                Log.d("", "\t?????????\n");
            case ImageFormat.Y8:
                Log.d("", "\tY8\n");
            case ImageFormat.YCBCR_P010:
                Log.d("", "\tYCBCR PO10\n");
            case ImageFormat.YUV_420_888:
                Log.d("", "\tYUV 420\n");
            case ImageFormat.YUV_422_888:
                Log.d("", "\tYUV 422\n");
            case ImageFormat.YUV_444_888:
                Log.d("", "\tYUV 444\n");
            case ImageFormat.YUY2:
                Log.d("", "\tYUY2\n");
            case ImageFormat.YV12:
                Log.d("", "\tYV12\n");
        }
    }
}
