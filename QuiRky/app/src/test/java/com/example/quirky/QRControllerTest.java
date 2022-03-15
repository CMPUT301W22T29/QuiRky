package com.example.quirky;

import org.junit.*;
import org.osmdroid.util.GeoPoint;

import static org.junit.Assert.*;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;


public class QRControllerTest {
    QRCode qr;

    @Before
    public void setup() throws NoSuchAlgorithmException {
        qr = new QRCode("content",  new GeoPoint(0.0, 0.0), null);
    }

    @Test
    public void HappyPathStringHashing() throws NoSuchAlgorithmException {
        /*
        byte[] content = "Hash me!".getBytes();
        assertEquals("F4D945F9C464C87568E04C04A2434CA0F0082F7B42A5D63DFD5CB88B5BC57481", QRCodeController.SHA256(content));

        content = "Plez do not fail".getBytes();
        assertEquals("9CAB066D5AF0E5C233FCCEFFFD707AAF905B67F621D6F09E960CFB20FD77EC75", QRCodeController.SHA256(content));

        content = " ".getBytes();
        assertEquals("36A9E7F1C95B82FFB99743E0C5C4CE95D83C9A430AAC59F84EF3CBFAB6145068", QRCodeController.SHA256(content));

         */
    }

    @Test
    public void InvalidStringsHashing() throws NoSuchAlgorithmException {
        /*
        String content = "";
        assertEquals("", QRCodeController.SHA256(content));

         */

        // TODO: create a test case with a string that uses characters outside of UTF-8.
    }

    @Test
    public void HappyPathScoring() {
        byte[] content = "Score me!".getBytes();
        assertEquals(83, QRCodeController.score(content));   // FIXME: double check the expected is correct

        content = "Plez do not fail".getBytes();
        assertEquals(67, QRCodeController.score(content));  // FIXME: double check the expected is correct

        content = " ".getBytes();
        assertEquals(32, QRCodeController.score(content));
    }

    @Test
    public void InvalidStringsScoring() {
        byte[] content = "".getBytes();
        assertEquals(0, QRCodeController.score(content));

        // TODO: create more cases of invalid inputs
    }
}
