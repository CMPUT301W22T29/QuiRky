package com.example.quirky;

import android.util.Log;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
 *          - images:
 *              Holds an image for each player that scanned the code and chose to save the image.
 */
public class DatabaseManager {
    private final FirebaseFirestore db;
    private CollectionReference collection;

    private List<DocumentSnapshot> docs;

    private final OnSuccessListener<Void> writeSuccess;
    private final OnFailureListener writeFail;

    private final String TAG = "DatabaseManager says: ";

    // Default constructor
    public DatabaseManager() {
        db = FirebaseFirestore.getInstance();
        writeSuccess = docref -> Log.d(TAG, "The write was successful.");
        writeFail = e -> Log.d(TAG, "The write operation failed. ", e);
        collection = db.collection("users");
    }

    /**
     * Write comment writes a comment object to FireStore
     * @param comment
     *      - The comment object to be written
     * @param qrId
     *      - The QRCode the comment is written under
     */
    public void writeComment(Comment comment, String qrId) {
        collection = db.collection("QRcodes").document(qrId).collection("comments");
        HashMap<String, String> data = new HashMap<>();
        data.put("content", comment.getContent());
        data.put("user", comment.getUname());
        data.put("timestamp", comment.getTimestamp().toString());
        collection.document(comment.getTimestamp().toString()).set(data).addOnSuccessListener(writeSuccess).addOnFailureListener(writeFail);
    }

    /**
     * Write a user's profile to FireStore
     * @param p
     *      - The profile object to be written
     */
    public void writeUser(Profile p) {

        assert (p != null);

        collection = db.collection("users");

        String name = p.getUname();
        String email = p.getEmail();
        String phone = p.getPhone();

        HashMap<String, String> data = new HashMap<>();

        data.put("name", name);
        data.put("email", email);
        data.put("phone", phone);

        collection.document(name).set(data)
                .addOnSuccessListener(writeSuccess)
                .addOnFailureListener(writeFail);
    }

    /**
     * readComments() begins the read process from FireStore. Because this takes time, a task is returned.
     * The calling class must set a listener to the Task that calls getComments() to get the data it wants.
     *
     * @param qrCodeId
     *      - The id field of the QRCode that holds the desired comments
     * @return
     */
    public Task<QuerySnapshot> readComments(String qrCodeId) {
        collection = db.collection("QRcode").document(qrCodeId).collection("comments");
        return collection.get();
    }

    /**
     * getComments() is the second half of the read process, once the Task is complete the caller uses getComments() to actually get the data.
     * @param task
     *      - The task returned to by readComments(). If this is called with a different task, method will not work properly.
     * @return
     *      - All comments written to the QRCode stored in a ArrayList
     */
    public ArrayList<Comment> getComments(Task<QuerySnapshot> task) {
        // FIXME: android studio says following line may produce null pointer exception. Consider how.
        List<DocumentSnapshot> docs = task.getResult().getDocuments();

        // Here we assume docs is a filled List of Document Snapshots
        ArrayList<Comment> comments = new ArrayList<>();
        for(int i = 0; i < docs.size(); i++) {
            String content = docs.get(i).getString("contents");
            String user = docs.get(i).getString("user");
            Date timestamp = docs.get(i).getDate("timestamp");

            comments.add(new Comment(content, user, timestamp));
        }

        return comments;
    }


    /**
     * A mock comment reader to be used until the actual comment reading works properly.
     * @return
     *      - An arraylist of hardcoded comments.
     */
    public ArrayList<Comment> MockReadComments(String qrCodeId) {
        ArrayList<Comment> c = new ArrayList<>();
        c.add(new Comment("This is a comment on: " + qrCodeId, "Sum Guy", new Date()));
        c.add(new Comment("This is comment 2 on: " + qrCodeId, "Guy 2", new Date()));
        c.add(new Comment("I found this QRCode on a bench in a public park!", "Raymart", new Date()));

        return c;

    }
}
