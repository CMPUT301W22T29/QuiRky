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

/*
DatabaseManager:
    This class manages all writing and reading from our database at FireStore.
    Create an instance of this class whenever you want to write or read from the database
    TODO: replace your comments with proper Javadoc
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

    public void writeComment(Comment comment, String qrId) {
        collection = db.collection("QRcodes").document(qrId).collection("comments");
        HashMap<String, String> data = new HashMap<>();
        data.put("content", comment.getContent());
        data.put("user", comment.getUname());
        data.put("timestamp", comment.getTimestamp().toString());
        collection.document(comment.getTimestamp().toString()).set(data).addOnSuccessListener(writeSuccess).addOnFailureListener(writeFail);
    }

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

    public Task<QuerySnapshot> readComments(String qrCodeId) {
        collection = db.collection("QRcode").document(qrCodeId).collection("comments");
        return collection.get();
    }
}
