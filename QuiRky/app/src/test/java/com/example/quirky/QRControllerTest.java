package com.example.quirky;

import org.junit.*;

import static org.junit.Assert.*;

import android.location.Location;

public class QRControllerTest {
    QRCode qr;
    QRCodeController qrcc;

    @Before
    public void setup() {
        qr = new QRCode("content", new Location("place"));
        qrcc = new QRCodeController();
    }

    @Test
    public void HappyPathStringHashing() {
        String content = "Hash me!";
        assertEquals("F4D945F9C464C87568E04C04A2434CA0F0082F7B42A5D63DFD5CB88B5BC57481", qrcc.SHA256(content));

        content = "Plez do not fail";
        assertEquals("9CAB066D5AF0E5C233FCCEFFFD707AAF905B67F621D6F09E960CFB20FD77EC75", qrcc.SHA256(content));

        content = " ";
        assertEquals("36A9E7F1C95B82FFB99743E0C5C4CE95D83C9A430AAC59F84EF3CBFAB6145068", qrcc.SHA256(content));
    }

    @Test
    public void InvalidStringsHashing() {
        String content = "";
        assertEquals("", qrcc.SHA256(content));

        // TODO: create a test case with a string that uses characters outside of UTF-8.
    }

    @Test
    public void HappyPathScoring() {
        String content = "Score me!";
        assertEquals(83, qrcc.score(content));   // FIXME: double check the expected is correct

        content = "Plez do not fail";
        assertEquals(67, qrcc.score(content));  // FIXME: double check the expected is correct

        content = " ";
        assertEquals(32, qrcc.score(content));
    }
    @Test
    public void InvalidStringsScoring() {
        String content = "";
        assertEquals(0, qrcc.score(content));

        // TODO: create more cases of invalid strings
    }
}
