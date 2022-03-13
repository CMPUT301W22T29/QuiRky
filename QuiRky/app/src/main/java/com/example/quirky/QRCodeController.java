package com.example.quirky;

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

import java.util.List;

public class QRCodeController {
    private static final BarcodeScanner codeScanner = BarcodeScanning.getClient(
            new BarcodeScannerOptions.Builder().setBarcodeFormats(Barcode.FORMAT_QR_CODE).build());

    // TODO: Everything.

    public static QRCode scanQRCode(InputImage inputImage) {
        final QRCode[] code = new QRCode[1];
        Task<List<Barcode>> result = codeScanner.process(inputImage).addOnSuccessListener(new OnSuccessListener<List<Barcode>>() {
            @Override
            public void onSuccess(List<Barcode> barcodes) {
                // Construct a QRCode with the scanned raw data
                code[0] = new QRCode(barcodes.get(0).getRawValue());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //TODO: Do something, possibly bring up a fragment saying to try retaking the picture.
            }
        }).addOnCompleteListener(new OnCompleteListener<List<Barcode>>() {
            @Override
            public void onComplete(@NonNull Task<List<Barcode>> task) {
                inputImage.getMediaImage().close();
            }
        });
        return code[0];
    }
}
