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

    Our Database works in the following way:
        The database has Collections.
            Collections are like directories.
        The Collections have Documents
            Documents store the data.
        Documents store data in field-value pairs, like a dictionary

        The Hierarchy is:
        Database
            Collections
                Documents
                    Data (Key-Value Pairs)

    To use the DatabaseManager:
        Create an instance of the class
            - DatabaseManager dm = new DatabaseManager();
        Set the collection you want to write to
            - dm.setCollection("users");
        Create a Map and fill it with data
            - Map<String, String> data = new HashMap();
            - data.put("key, "value");
        Use the write method
            - dm.write(data, "document_name");
     This method will create a new document every time you write.
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
        collection = db.collection("users");
        HashMap<String, String> data = new HashMap<>();
        data.put("name", p.getName());
        data.put("email", p.getEmail());
        data.put("phone", p.getPhone());
        collection.document(p.getName()).set(data).addOnSuccessListener(writeSuccess).addOnFailureListener(writeFail);
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
