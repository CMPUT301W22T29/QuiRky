package com.example.quirky;

import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

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
            - dm.write(data);
     This method will create a new document every time you write.
 */
public class DatabaseManager {
    private final FirebaseFirestore db;
    private CollectionReference col_ref;
    private DocumentReference doc_ref;

    private final OnSuccessListener<DocumentReference> sl;
    private final OnFailureListener fl;

    private final String TAG = "Sample";

    public DatabaseManager() {
        db = FirebaseFirestore.getInstance();
        sl = docref -> Log.d("Sample", "DocumentSnapshot added with ID: " + docref.getId());
        fl = e -> Log.d("Sample", "Error adding document! ", e);

        col_ref = db.collection("users");
    }

    public DatabaseManager(String collection) {
        db = FirebaseFirestore.getInstance();
        sl = docref -> Log.d(TAG, "DocumentSnapshot added with ID: " + docref.getId());
        fl = e -> Log.d(TAG, "Error adding document! ", e);

        col_ref = db.collection(collection);
    }

    public void setCollection(String collection) {
        col_ref = db.collection((collection));
    }

    public void setDoc(String doc) {
        doc_ref = col_ref.document(doc);
    }

    public void write(Map<String, Object> data) {
        col_ref.add(data).addOnSuccessListener(sl).addOnFailureListener(fl);
    }


    // FIXME: Currently have two read methods: read() and readAlt(), implementing two separate methods of reading from the database
    public Map<String, Object> read(String doc) {
        Map<String, Object> data;

        col_ref.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                for(QueryDocumentSnapshot doc1 : task.getResult()) {
                    Log.d(TAG, doc1.getId() + " => " + doc1.getData());
                }
            } else {
                Log.w(TAG, "Error getting documents.", task.getException());
            }
        });

        // FIXME: this read method currently does not work.
        data = new HashMap<>();
        return data;
    }

    public Map<String, Object> readAlt(String doc) {
        Task<DocumentSnapshot> t;
        t = col_ref.document(doc).get();

        // FIXME: The Task object represents a task.
        // FIXME:   Calling col_ref.document(doc).get() will start a task that will take time to complete
        // FIXME:   Calling t.getResult() immediately may cause bugs if the task is not yet complete
        // FIXME:   Must implement some kind of stalling feature?

        return t.getResult().getData(); // TODO: t.getResult().getData() can return Null. Make sure this case is accounted for.
    }
}
