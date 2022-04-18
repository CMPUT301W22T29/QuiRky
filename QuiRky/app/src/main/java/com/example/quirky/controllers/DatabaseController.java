/*
 * Copyright (c) 2022. CMPUT301W22T29
 * Subject to MIT License
 * See full terms at https://github.com/CMPUT301W22T29/QuiRky/blob/main/LICENSE
 */

package com.example.quirky.controllers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.quirky.ListeningList;
import com.example.quirky.models.Profile;
import com.example.quirky.models.QRCode;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

    private final FirebaseFirestore firestore;
    private CollectionReference collection;

    private final FirebaseStorage firebase;
    private StorageReference storage;

    private final OnCompleteListener writeListener;
    private final OnCompleteListener deleteListener;

    public DatabaseController() {
        this.firestore = FirebaseFirestore.getInstance();
        this.firebase = FirebaseStorage.getInstance();

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

        collection = firestore.collection("QRCodes");
        collection.document(qr.getId()).set(qr).addOnCompleteListener(writeListener);
    }

    public void writeProfile(Profile p) {
        assert p != null : "You can't write a null object to the database!";
        collection = firestore.collection("users");
        collection.document(p.getUname()).set(p).addOnCompleteListener(writeListener);
    }

    public void deleteQRCode(String qr) {
        // Delete from FireStore
        collection = firestore.collection("QRCodes");
        collection.document(qr).delete().addOnCompleteListener(deleteListener);

        // Delete from Firebase Storage
        storage = firebase.getReference().child("photos").child(qr); // FIXME: I think firebase.getReference(String) does not do what you think it does. You may need to use firebase.getReference().child(photos).child(id); instead
        storage.delete().addOnCompleteListener(deleteListener);
    }

    public void deleteProfile(String username) {
        collection = firestore.collection("users");
        collection.document(username).delete().addOnCompleteListener(deleteListener);
    }

    public void readProfile(String username, ListeningList<Profile> data) {
        assert (!username.equals("") && !username.equals(" ")) : "Tried calling readProfile() with an empty username! Did you mean to use readAllUsers()?";

        collection = firestore.collection("users");
        collection.document(username).get().addOnCompleteListener(task -> {
            data.add( task.getResult().toObject(Profile.class) );
        });
    }

    public void isOwner(String username, ListeningList<Boolean> data) {
        collection = firestore.collection("users");
        collection.document(username).get().addOnCompleteListener(task -> {
            if(task.getResult().contains("owner"))
                data.add( task.getResult().getBoolean("owner") );
            else
                data.add( false );
        });
    }

    // This method can be used to Query the database for specific users, or read the whole database at once, by passing an empty string.
    public void readAllUsers(String search, ListeningList<Profile> data) {
        collection = firestore.collection("users");
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
        collection = firestore.collection("QRCodes");
        collection.document(id).get().addOnCompleteListener(task -> {
            QRCode result = task.getResult().toObject(QRCode.class);
            data.add(result);
        });
    }

    public void readAllQRCodes(ListeningList<QRCode> data) {
        collection = firestore.collection("QRCodes");
        collection.get().addOnCompleteListener(task -> {
            Collection<QRCode> result = task.getResult().toObjects(QRCode.class);
            data.addAll(result);
        });
    }

    public void writeLoginHash(String hash, String user) {
        collection = firestore.collection("users");
        collection.document(user).update("loginhash", hash).addOnCompleteListener(writeListener);
    }

    public void readLoginHash(String hash, ListeningList<Profile> data) {
        collection = firestore.collection("users");
        collection.whereEqualTo("loginhash", hash).get().addOnCompleteListener(task -> {
            QuerySnapshot q = task.getResult();
            if(q.size() != 1) {
                Log.d(TAG, "For some reason reading a login hash did not produce exactly 1 profile!");
                data.add(null);
            } else {
                Profile p = q.getDocuments().get(0).toObject(Profile.class);
                firestore.collection("users").document(p.getUname()).update("loginhash", "");
                data.add (p);
            }
        });
    }

    public void recentPhotos(ListeningList<Bitmap> photos) {
    }

    public void readPhotos(String id, ListeningList<Bitmap> photos) {
        storage = firebase.getReference().child("photos").child(id);
        Task<ListResult> task_read_list = storage.list(10);

        Collection<Bitmap> results = new ArrayList<>();
        task_read_list.addOnCompleteListener(task -> {
            List<StorageReference> files = task.getResult().getItems();
            for(StorageReference fileRef : files) {
                fileRef.getBytes(1048576).addOnCompleteListener(task_read_photo -> {
                    byte[] photo = task_read_photo.getResult();
                    results.add( BitmapFactory.decodeByteArray( photo, 0, photo.length) );

                    if(results.size() == files.size())
                        photos.addAll(results);
                });
            }
        });
    }

    // ByteBuffer code sourced from
    // https://stackoverflow.com/a/34165515
    // Written by user
    // https://stackoverflow.com/users/4853690/%e6%9c%b1%e8%a5%bf%e8%a5%bf
    // Published Dec. 8, 2015
    public void writePhoto(String id, Bitmap photo) {
        storage = firebase.getReference().child("photos");

        ByteBuffer buff = ByteBuffer.allocate( photo.getByteCount() );
        photo.copyPixelsToBuffer(buff);

        UploadTask task = storage.child(id).putBytes( buff.array() );
        task.addOnCompleteListener(writeListener);
    }
}
