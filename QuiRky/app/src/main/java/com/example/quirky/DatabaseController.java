package com.example.quirky;

import android.util.Log;


import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Jonathen Adsit
 * This is a controller class that does all the reading and writing to FireStore
 * Our database has these collections:
 *  - users
 *      Holds a document for each player holding their unique username, contact information, etc.
 *  - QRcodes
 *      Holds a document for each QRCode that has been scanned with the app, holding it's content, hash, score, location...
 *      Has two inner collections:
 *          - comments:
 *              Holds a document for each comment the QRCode has
 *          - images: (?), verify this is how it will be stored
 *              Holds an image for each player that scanned the code and chose to save the image.
 */
public class DatabaseController {
    private final String TAG = "DatabaseController says: ";
    private final FirebaseFirestore db;
    private CollectionReference collection;
    private final OnCompleteListener writeListener;

    public DatabaseController() {
        this.db = FirebaseFirestore.getInstance();
        this.writeListener = task -> {
            if(task.isSuccessful())
                Log.d(TAG, "Last write was a success");
            else
                Log.d(TAG, "Last write was a fail"); // TODO: find a way to determine what the last write was. Should be easy.
        };
    }

    public void writeComment(Comment c, String id) {
        collection = db.collection("QRcodes");
        Map<String, Object> data = new HashMap<>();

        // We update the 'exists' field in the QRCode's document, because a document in Firestore needs at least one field in order to exist.
        // The 'exists' field only exists because we want to ensure the QRCode document exists.
        data.put("exists", true);
        collection.document(id).update(data);

        collection = collection.document(id).collection("comments");
        collection.document(c.getTimestamp().toString()).set(c).addOnCompleteListener(writeListener);
    }

    public void writeQRCode(QRCode qr) {
        if(qr == null)
            throw new RuntimeException(TAG + "You can't pass a null value to the write method!\t\t<---");
        String s = qr.getId();
        collection = db.collection("QRcodes");
        collection.document(s).set(qr).addOnCompleteListener(writeListener);
    }

    public void writeProfile(Profile p) {
        collection = db.collection("users");
        collection.document(p.getUname()).set(p).addOnCompleteListener(writeListener);
    }
}
