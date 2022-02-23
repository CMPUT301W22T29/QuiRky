package com.example.quirky;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
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
    private CollectionReference col_ref;
    private HashMap<String, Object> data;

    private final OnSuccessListener<Void> sl;
    private final OnFailureListener fl;

    private final String TAG = "DatabaseManager says: ";

    // Default constructor
    public DatabaseManager() {
        db = FirebaseFirestore.getInstance();
        sl = docref -> Log.d(TAG, "DocumentSnapshot added with ID.");

        fl = e -> Log.d("Sample", "Error adding document! ", e);

        col_ref = db.collection("users");
    }

    // Constructor to set collection
    public DatabaseManager(String collection) {
        db = FirebaseFirestore.getInstance();
        sl = docref -> Log.d(TAG, "DocumentSnapshot added with ID.");
        fl = e -> Log.d(TAG, "Error adding document! ", e);

        col_ref = db.collection(collection);
    }

    public void setCollection(String collection) {
        col_ref = db.collection((collection));
    }

    public void write(Map<String, Object> data, String doc) {
        col_ref.document(doc).set(data).addOnSuccessListener(sl).addOnFailureListener(fl);
    }

    // This method is for reading a specific document you know exists.
    // If you call it on a document that does not exist in the current collection, it throws an exception.
    // Use the generic read() method if you do not know what documents exist in the collection.
    public HashMap<String, Object> read(String doc) {

        Task<DocumentSnapshot> ReadTask = col_ref.document(doc).get();
        ReadTask.addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    data = (HashMap<String, Object>) task.getResult().getData();
                } else {
                    Log.w(TAG, "_*_Error reading the document.", task.getException());
                }
            }
        });

        // FIXME: data keeps being null, there is an error somewhere in the read. I think the listener is not calling it's onComplete method before we reach the end of this method, and so data is never initialized.
        if(data == null) {
            // This exception throw was just being used for debugging purposes.
            throw new ArithmeticException(TAG + "For some reason data is null...");
        }

        return data;
    }
}
