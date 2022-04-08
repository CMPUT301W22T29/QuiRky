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

    public void writeQRCode(QRCode qr, String user) {
        assert(qr != null) : "You can't write a null object to the database!";

        // A batch write: a group of write operations to be done at once.
        // Batches are not written immediately. Can add many operations to a batch, then commit all writes at once.
        WriteBatch batch = db.batch();
        DocumentReference doc;

        // Set the document location
        collection = db.collection("QRcodes");
        doc = collection.document(qr.getId());

        // Write the score of the QRCode
        Map<String, Object> data = new HashMap<>();
        data.put("score", qr.getScore());
        batch.set(doc, data);

        // Add the comments to the batch. Skip any null comments.
        collection = doc.collection("comments");
        batch.set(collection.document("sample"), data);
        for(Comment c : qr.getComments()) {
            if(c==null) continue;
            batch.set(collection.document(c.getId()), c);
        }

        // Create an entry for the user in the QRCode's userdata folder.
        // Values are set to null, a separate method will set the location and photo to actual values.
        collection = doc.collection("userdata");

        data.clear();
        data.put("location", null);
        data.put("photo", null);

        batch.set(collection.document(user), data);

        // Issue the batch write
        batch.commit().addOnCompleteListener(writeListener);
    }

    public void deleteQRCode(String qr) {
        collection = db.collection("QRcodes");
        collection.document(qr).delete().addOnCompleteListener(deleteListener);
    }

    public void writeProfile(Profile p) {
        collection = db.collection("users");
        collection.document(p.getUname()).set(p).addOnCompleteListener(writeListener);
    }

    public void deleteProfile(String username) {
        collection = db.collection("users");
        collection.document(username).delete().addOnCompleteListener(deleteListener);
    }

    public ListeningList<Profile> readProfile(String username) {
        collection = db.collection("users");
        ListeningList<Profile> data = new ListeningList<>();
        collection.document(username).get().addOnCompleteListener(task -> {
            data.add( task.getResult().toObject(Profile.class) );
        });

        return data;
    }

    public ListeningList<Boolean> isOwner(String username) {
        collection = db.collection("users");
        ListeningList<Boolean> data = new ListeningList<>();
        collection.document(username).get().addOnCompleteListener(task -> {
            if(task.getResult().contains("owner"))
                data.add( task.getResult().getBoolean("owner") );
            else
                data.add( false );
        });

        return data;
    }

    public ListeningList<Profile> readUsers(String search) {
        collection = db.collection("users");
        ListeningList<Profile> data = new ListeningList<>();
        OnCompleteListener<QuerySnapshot> complete = task -> {
            Collection<Profile> result = task.getResult().toObjects(Profile.class);
            data.addAll(result);
        };

        if(search.equals("") || search.equals(" "))
            collection.get().addOnCompleteListener(complete);
        else
            collection.whereGreaterThanOrEqualTo("uname", search).get().addOnCompleteListener(complete);

        return data;
    }

    public QRCode readQRCode(String id) {   // TODO: Refactor QRCode class
        return new QRCode(id, QRCodeController.score(id), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }

    public ListeningList<QRCode> readAllQRCodes() {
        collection = db.collection("QRcodes");

        ListeningList<QRCode> data = new ListeningList<>();
        collection.get().addOnCompleteListener(task -> {
            Collection<QRCode> result = task.getResult().toObjects(QRCode.class);
            data.addAll(result);
        });

        return data;
    }

    public void writeLoginHash(String hash, String user) {
        collection = db.collection("users");
        collection.document(user).update("loginhash", hash).addOnCompleteListener(writeListener);
    }

    public ListeningList<Profile> readLoginHash(String hash) {
        collection = db.collection("users");

        ListeningList<Profile> data = new ListeningList<>();
        collection.whereEqualTo("loginhash", hash).get().addOnCompleteListener(task -> {
            QuerySnapshot q = task.getResult();
            if(q.size() != 1) {
                Log.d(TAG, "For some reason reading a login hash did not produce exactly 1 profile!");
            } else {
                data.add ( q.getDocuments().get(0).toObject(Profile.class) );
            }
        });

        return data;
    }
}
