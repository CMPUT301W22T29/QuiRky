package com.example.quirky;

import android.util.Log;



import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

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
    private final OnCompleteListener deleteListener;

    public DatabaseController(FirebaseFirestore db) {
        this.db = db;
        this.writeListener = task -> {
            if(task.isSuccessful())
                Log.d(TAG, "Last write was a success");
            else
                Log.d(TAG, "Last write was a fail"); // TODO: find a way to determine what the last write was. Should be easy.
        };

        this.deleteListener = task -> {
            if(task.isSuccessful())
                Log.d(TAG, "Deletion from database success!");
            else
                Log.d(TAG, "Deletion from database failed!!"); // TODO: find a way to determine what the last write was. Should be easy.
        };
    }

    public void addComment(Comment c, String id) {
        collection = db.collection("QRcodes");

        // We update the 'exists' field in the QRCode's document, because a document in Firestore needs at least one field in order to exist.
        // The 'exists' field only exists because we want to ensure the QRCode document exists.
        Map<String, Object> data = new HashMap<>();
        data.put("exists", true);
        collection.document(id).update(data);

        collection = collection.document(id).collection("comments");
        collection.document(c.getId()).set(c).addOnCompleteListener(writeListener);
    }

    public void removeComment(Comment c, String id) {   // TODO: consider making this method take the comment's id, rather than the object itself.
        collection = db.collection("QRcodes");

        // We update the 'exists' field in the QRCode's document, because a document in Firestore needs at least one field in order to exist.
        // The 'exists' field only exists because we want to ensure the QRCode document exists.
        Map<String, Object> data = new HashMap<>();
        data.put("exists", true);
        collection.document(id).update(data);

        collection = collection.document(id).collection("comments");
        collection.document(c.getId()).delete().addOnCompleteListener(deleteListener);
    }

    public void writeQRCode(QRCode qr) {
        assert(qr != null) : "You can't write a null object to the database!";

        // A batch write: a group of write operations to be done at once. Create the batch
        WriteBatch batch = db.batch();
        DocumentReference doc;

        // The Score field
        Map<String, Object> data = new HashMap<>();
        data.put("score", qr.getScore());
        // Set the location
        collection = db.collection("QRcodes");
        doc = collection.document(qr.getId());
        // Add to the batch
        batch.update(doc, data);

        collection = doc.collection("comments");
        // Add the comments to the batch. Skip any null comments.
        for(Comment c : qr.getComments()) {
            if(c==null) continue;
            batch.set(collection.document(c.getId()), c);
        }

        // Add the geolocation & photo to the batch
        collection = doc.collection("userdata");
        String user = "user"; // TODO: find a way to get the current user's username without context
        doc = collection.document(user);

        data = new HashMap<>();
        data.put("location", qr.getGeolocation());
        data.put("photo", 3); // TODO: write the photo correctly.

        batch.set(doc, data);

        // Issue the batch write
        batch.commit().addOnCompleteListener(writeListener);

    }

    public void writeProfile(Profile p) {
        collection = db.collection("users");
        collection.document(p.getUname()).set(p).addOnCompleteListener(writeListener);
    }
}
