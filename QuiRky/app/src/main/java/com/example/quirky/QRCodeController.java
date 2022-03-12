package com.example.quirky;

import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.common.Barcode;

public class QRCodeController {
    private static final BarcodeScannerOptions barcodeOptions =
            new BarcodeScannerOptions.Builder().setBarcodeFormats(Barcode.FORMAT_QR_CODE).build();
    // TODO: Everything.

    // TODO: get qr code stuff from input image ();
}
