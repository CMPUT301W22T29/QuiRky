package com.example.quirky;

import org.junit.*;

import static org.junit.Assert.*;

public class QRControllerTest {
    String content;
    String hashed;

    // This test ensures that our hashes never produce a slash or period in the returned string.
    // Not sure how to test that the hash produces the expected content. Impossible maybe?
    @Test
    public void Hashing() {
        // Standard case
        content = "Stuff";
        hashed = ""; // How do we even predict what the hash will output?
                        // Our hashing algorithm is really personalised:
                        //  it takes a string, produces a byte array, turns it back into a string via UTF-8 encoded, and replaces all the slashes and dots with zeroes.
                        // How could there be any external sources that would give us the expected result to test our algorithm works as intended?

        hashed = QRCodeController.SHA256(content);
        assertEquals(hashed, QRCodeController.SHA256(content));     // Like this test can literally never fail. We need an external source with the same algorithm to test our own algorithm.


        // Another test we can do: make sure all '/' and '.' are parsed correctly
        for(int i = 0; i < 50; i++) {
            String j = String.valueOf(i);
            String result = QRCodeController.SHA256(j);

            assertFalse(result.contains("/"));
            assertFalse(result.contains("."));
        }
    }

    @Test
    public void HashingEdgeCases() {
        content = "";
        hashed = QRCodeController.SHA256("");   // This should also return an empty string? TODO: consider what this would actually return.
        assertEquals("Empty string test failed.", content, hashed);


    }

    @Test public void Scoring() {

    }
}
