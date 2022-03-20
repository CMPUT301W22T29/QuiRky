package com.example.quirky;

import android.app.Activity;
import android.content.Context;
import android.util.Log;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Jonathen Adsit
 * This is a controller class that does all the reading and writing to FireStore
 * Our database has these collections:
 *  - users
 *      Holds a document for each player holding their unique username, contact information, etc.
 *  - QRcodes
 *      Has a document for each QRCode that has been scanned with the app.
 *          Each document contains:
 *              - The score of the QRCode
 *              - Two inner collections:
 *                  - comments:
 *                      Holds a document for each comment the QRCode has.
 *                      The documents has 3 fields: content, user, and time
 *                  - userdata:
 *                      Holds a document for each user that has scanned this code.
 *                      The document contains: the photo the user took, and the location the user found the code.
 *
 */

// Reading & Writing Custom Objects to FireStore taken from:
// https://youtu.be/jJnm3YKfAUI
// Made By:
// https://www.youtube.com/channel/UC_Fh8kvtkVPkeihBs42jGcA
// Published April 15, 2018
public class DatabaseController {
    private final String TAG = "DatabaseController says: ";

    private final FirebaseFirestore db;

    private CollectionReference collection;
    private final OnCompleteListener writeListener;
    private final OnCompleteListener deleteListener;

    public DatabaseController(FirebaseFirestore db, Context ct) {
        this.db = db;

        this.writeListener = task -> {
            if(task.isSuccessful())
                Log.d(TAG, "Last write was a success");
            else
                Log.d(TAG, "Last write was a fail, cause: " + task.getException().getMessage()); // TODO: find a way to determine what the last write was. Should be easy.
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

        collection = collection.document(id).collection("comments");
        collection.document(c.getId()).set(c).addOnCompleteListener(writeListener);
    }

    public void removeComment(Comment c, String id) {   // TODO: consider making this method take the comment's id, rather than the object itself.
        collection = db.collection("QRcodes");

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
        batch.set(doc, data);

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

    public void deleteQRCode(QRCode qr) {
        collection = db.collection("QRCodes");
        collection.document(qr.getId()).delete().addOnCompleteListener(deleteListener);
    }

    public void writeProfile(Profile p) {
        collection = db.collection("users");
        collection.document(p.getUname()).set(p).addOnCompleteListener(writeListener);
    }

    public void deleteProfile(Profile p) {
        collection = db.collection("users");
        collection.document(p.getUname()).delete().addOnCompleteListener(deleteListener);
    }

    public Task<DocumentSnapshot> readProfile(String username) {
        collection = db.collection("users");
        return collection.document(username).get();
    }

    public Profile getProfile(Task<DocumentSnapshot> task) {
        DocumentSnapshot doc = task.getResult();
        if(doc.getData() == null) return null;
        return doc.toObject(Profile.class);
    }
}
