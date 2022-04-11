package com.example.quirky;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jonathen Adsit
 * This is a controller class that does all the reading and writing to FireStore
 * TODO: Consider removing the coupling from the model classes,
 *      by creating methods to set the collection/document to read from
 *      And creating methods to read a single field, an object from a document, and a group of objects from a collection.
 */

// Reading & Writing Custom Objects to FireStore taken from:
// https://youtu.be/jJnm3YKfAUI
// Made By:
// https://www.youtube.com/channel/UC_Fh8kvtkVPkeihBs42jGcA
// Published April 15, 2018
@SuppressWarnings({"unchecked", "ConstantConditions"})
public class DatabaseController {
    private final String TAG = "DatabaseController says: ";

    private final FirebaseFirestore db;

    private CollectionReference collection;
    private final OnCompleteListener writeListener;
    private final OnCompleteListener deleteListener;

    public DatabaseController() {
        this.db = FirebaseFirestore.getInstance();

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

    public void writeQRCode(QRCode qr) {
        assert qr != null : "You can't write a null object to the database!";

        collection = db.collection("QRCodes");
        collection.document(qr.getId()).set(qr).addOnCompleteListener(writeListener);
    }

    public void writeProfile(Profile p) {
        assert p != null : "You can't write a null object to the database!";
        collection = db.collection("users");
        collection.document(p.getUname()).set(p).addOnCompleteListener(writeListener);
    }

    public void deleteQRCode(String qr) {
        collection = db.collection("QRCodes");
        collection.document(qr).delete().addOnCompleteListener(deleteListener);
    }

    public void deleteProfile(String username) {
        collection = db.collection("users");
        collection.document(username).delete().addOnCompleteListener(deleteListener);
    }

    public void readProfile(String username, ListeningList<Profile> data) {
        collection = db.collection("users");
        collection.document(username).get().addOnCompleteListener(task -> {
            data.add( task.getResult().toObject(Profile.class) );
        });
    }

    public void isOwner(String username, ListeningList<Boolean> data) {
        collection = db.collection("users");
        collection.document(username).get().addOnCompleteListener(task -> {
            if(task.getResult().contains("owner"))
                data.add( task.getResult().getBoolean("owner") );
            else
                data.add( false );
        });
    }

    // This method can be used to Query the database for specific users, or read the whole database at once, by passing an empty string.
    public void readUsers(String search, ListeningList<Profile> data) {
        collection = db.collection("users");
        OnCompleteListener<QuerySnapshot> complete = task -> {
            Collection<Profile> result = task.getResult().toObjects(Profile.class);
            data.addAll(result);
        };

        if(search.equals("") || search.equals(" "))
            collection.get().addOnCompleteListener(complete);
        else
            collection.whereGreaterThanOrEqualTo("uname", search).get().addOnCompleteListener(complete);
    }

    public void readQRCode(String id, ListeningList<QRCode> data) {
        collection = db.collection("QRCodes");
        collection.document(id).get().addOnCompleteListener(task -> {
            QRCode result = task.getResult().toObject(QRCode.class);
            data.add(result);
        });
    }

    public void readAllQRCodes(ListeningList<QRCode> data) {
        collection = db.collection("QRCodes");
        collection.get().addOnCompleteListener(task -> {
            Collection<QRCode> result = task.getResult().toObjects(QRCode.class);
            data.addAll(result);
        });
    }

    public void writeLoginHash(String hash, String user) {
        collection = db.collection("users");
        collection.document(user).update("loginhash", hash).addOnCompleteListener(writeListener);
    }

    public void readLoginHash(String hash, ListeningList<Profile> data) {
        collection = db.collection("users");
        collection.whereEqualTo("loginhash", hash).get().addOnCompleteListener(task -> {
            QuerySnapshot q = task.getResult();
            if(q.size() != 1) {
                Log.d(TAG, "For some reason reading a login hash did not produce exactly 1 profile!");
                data.add(null);
            } else {
                Profile p = q.getDocuments().get(0).toObject(Profile.class);
                db.collection("users").document(p.getUname()).update("loginhash", "");
                data.add (p);
            }
        });
    }
}
