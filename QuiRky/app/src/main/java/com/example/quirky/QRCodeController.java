package com.example.quirky;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;

import java.util.ArrayList;
import java.util.List;

public class QRCodeController {
    private static final BarcodeScanner codeScanner = BarcodeScanning.getClient(
            new BarcodeScannerOptions.Builder().setBarcodeFormats(Barcode.FORMAT_QR_CODE).build());

    // TODO: Everything.

    public static ArrayList<QRCode> scanQRCodes(InputImage inputImage) {
        Log.d("scanQRCode", "enter method");    //TODO: get rid of.
        ArrayList<QRCode> codes = new ArrayList<>();
        Task<List<Barcode>> result = codeScanner.process(inputImage).addOnSuccessListener(barcodes -> {
            Log.d("scanQRCode", "onSuccess");   //TODO: get rid of.
            // Construct a QRCode with the scanned raw data
            for (Barcode barcode: barcodes) {
                codes.add(new QRCode(barcode.getRawValue()));
                Log.d("scanQRCode", codes.get(0).getContent()); //TODO: get rid of.
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //TODO: Do something, possibly bring up a fragment saying to try retaking the picture.
                Log.d("scanQRCode", "onFailure");   //TODO: get rid of.
            }
        });
        Log.d("scanQRCode", "exit method"); //TODO: get rid of.
        return codes;
    }
}
