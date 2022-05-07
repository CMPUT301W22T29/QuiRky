/*
 * Copyright (c) 2022. CMPUT301W22T29
 * Subject to MIT License
 * See full terms at https://github.com/CMPUT301W22T29/QuiRky/blob/main/LICENSE
 */

package com.example.quirky.controllers;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.quirky.ListeningList;
import com.example.quirky.OnAddListener;
import com.example.quirky.R;
import com.example.quirky.models.Profile;
import com.example.quirky.models.QRCode;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
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
    private final long MAX_DOWNLOAD_SIZE = 10485760; // 10485760 Bytes == 10 mB

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
                Log.d(TAG, "Last write was a fail, cause: " + task.getException().getMessage());
        };

        this.deleteListener = task -> {
            if(task.isSuccessful())
                Log.d(TAG, "Deletion from database success!");
            else
                Log.d(TAG, "Deletion from database failed!!");
        };
    }

    /**
     * Write a QRCode to the database. Use this method to update any of the fields a QRCode has.
     * @param qr The QRCode to write/update
     */
    public void writeQRCode(QRCode qr) {
        assert qr != null : "You can't write a null object to the database!";

        collection = firestore.collection("QRCodes");
        collection.document(qr.getId()).set(qr).addOnCompleteListener(writeListener);
    }

    /**
     * Write a profile to the database. Use this method to also update a profile in the database, if any field has changed
     * @param p The profile to write/update
     */
    public void writeProfile(Profile p) {
        assert p != null : "You can't write a null object to the database!";
        collection = firestore.collection("users");
        collection.document(p.getUname()).set(p).addOnCompleteListener(writeListener);
    }

    /**
     * Delete a QRCode from the database. Also deletes any associated photos from Firebase Storage
     * @param qr The QRCode to delete
     */
    public void deleteQRCode(String qr) {
        // Delete from FireStore
        collection = firestore.collection("QRCodes");
        collection.document(qr).delete().addOnCompleteListener(deleteListener);

        // Delete from Firebase Storage
        storage = firebase.getReference().child("photos").child(qr);
        storage.delete().addOnCompleteListener(deleteListener);
    }

    /**
     * Delete a profile from the database
     * @param username The username of the profile to delete
     */
    public void deleteProfile(String username) {
        collection = firestore.collection("users");
        collection.document(username).delete().addOnCompleteListener(deleteListener);
    }

    /**
     * Read the profile with the given username from the database.
     * This is an asynchronous operation, and as such will not return the read result.
     * When the read completes, the data is added to the passed ListeningList<>.
     * @param username The username of the profile to read
     * @param data A listening list containing profiles. The read result is added to this list.
     */
    public void readProfile(String username, ListeningList<Profile> data) {
        assert (!username.equals("") && !username.equals(" ")) : "Tried calling readProfile() with an empty username! Did you mean to use readAllUsers()?";

        collection = firestore.collection("users");
        collection.document(username).get().addOnCompleteListener(task -> {
            data.add( task.getResult().toObject(Profile.class) );
        });
    }

    /**
     * Check if the given user is an admin.
     * This is an asynchronous operation, and as such will not return the read result.
     * When the read completes, the data is added to the passed ListeningList<>.
     * @param username The username to check for
     * @param data A listening list containing Booleans. The read result is added to this list.
     */
    public void isOwner(String username, ListeningList<Boolean> data) {
        collection = firestore.collection("users");
        collection.document(username).get().addOnCompleteListener(task -> {
            if(task.getResult().contains("owner"))
                data.add( task.getResult().getBoolean("owner") );
            else
                data.add( false );
        });
    }

    /**
     * Read a collection of users from the database. This method may be used to read every single player, or
     * this method may be used to search for players by username. Pass an empty string to read the whole population.
     * This is an asynchronous operation, and as such will not return the read result.
     * When the read completes, the data is added to the passed ListeningList<>.
     * @param search The username to use to search for players. If this is empty, read will return every player.
     * @param data A listening list containing Profiles. Read result is added here.
     */
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

    /**
     * Read the QRCode with the given ID from the database.
     * This is an asynchronous operation, and as such will not return the read result.
     * When the read completes, the data is added to the passed ListeningList<>.
     * @param id The ID of the QRCode to read
     * @param data A listening list containing QRCodes. The read result is added here.
     */
    public void readQRCode(String id, ListeningList<QRCode> data) {
        collection = firestore.collection("QRCodes");
        collection.document(id).get().addOnCompleteListener(task -> {
            QRCode result = task.getResult().toObject(QRCode.class);
            data.add(result);
        });
    }

    /**
     * Read every QRCode from the database.
     * This is an asynchronous operation, and as such will not return the read result.
     * When the read completes, the data is added to the passed ListeningList<>.
     * @param data A listening list containing QRCodes.
     */
    public void readAllQRCodes(ListeningList<QRCode> data) {
        collection = firestore.collection("QRCodes");
        collection.get().addOnCompleteListener(task -> {
            Collection<QRCode> result = task.getResult().toObjects(QRCode.class);
            data.addAll(result);
        });
    }

    /**
     * Write a one-use login password to a profile in the database.
     * The password should be the hash of the QRCode the player will scan to login to their account.
     * @param hash The password to write to the account
     * @param user The username of the profile to write the password to
     */
    public void writeLoginHash(String hash, String user) {
        collection = firestore.collection("users");
        collection.document(user).update("loginhash", hash).addOnCompleteListener(writeListener);
    }

    /**
     * Search the database for a user with whose password field matches the given password.
     * This is an asynchronous operation, and as such will not return the read result.
     * When the read completes, the data is added to the passed ListeningList<>.
     * @param hash The password to search for
     * @param data A listening list containing Profiles. The read result is placed here.
     */
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
                data.add(p);
            }
        });
    }

    /**
     * Read the 5 most recent photos taken from the database.
     * This is an asynchronous operation, and as such will not return the read result.
     * When the read completes, the data is added to the passed ListeningList<>.
     * @param photos A listening list containing Bitmaps. The photos read are placed here
     */
    public void getRecentPhotos(ListeningList<Bitmap> photos) {
        storage = firebase.getReference().child("recent");
        Collection<Bitmap> results = new ArrayList<>();

        // Get the list of all photos from the Database
        Task<ListResult> task_read_list = storage.list(5);

        task_read_list.addOnCompleteListener(task -> {

            // Loop through each file in the list
            List<StorageReference> files = task.getResult().getItems();
            if(files.size() == 0) {
                photos.addNone();
                return;
            }

            for(StorageReference fileRef : files) {
                Task<byte[]> read_photo = fileRef.getBytes(MAX_DOWNLOAD_SIZE);
                read_photo.addOnCompleteListener(task1 -> {
                    byte[] bytes = task1.getResult();
                    Bitmap photo = BitmapFactory.decodeByteArray( bytes, 0, bytes.length);
                    results.add(photo);

                    if(results.size() == files.size())
                        photos.addAll(results);
                });
            }
        });
    }

    /**
     * Read a limited number of photos from the database
     * This is an asynchronous operation, and as such will not return the read result.
     * When the read completes, the data is added to the passed ListeningList<>.
     * @param id The ID of the QRCode to get the photos from
     * @param photos A listening list containing Bitmaps. The photos read are placed here.
     * @param maxPhotos The maximum number of photos to be read.
     */
    public void readPhotos(String id, ListeningList<Bitmap> photos, int maxPhotos) {
        storage = firebase.getReference().child("photos").child(id);
        Collection<Bitmap> results = new ArrayList<>();

        // Get the list of all photos from the Database
        Task<ListResult> task_read_list = storage.list(maxPhotos);

        task_read_list.addOnCompleteListener(task -> {

            // Loop through each file in the list
            List<StorageReference> files = task.getResult().getItems();
            if(files.size() == 0) {
                photos.addNone();
                return;
            }

            for(StorageReference fileRef : files) {

                // Reading the bytes of the file is a separate read operation
                Task<byte[]> read_photo = fileRef.getBytes(MAX_DOWNLOAD_SIZE);
                read_photo.addOnCompleteListener(task1 -> {

                    // Turn the byte array into a Bitmap object and add it to the results
                    byte[] bytes = task1.getResult();
                    Bitmap photo = BitmapFactory.decodeByteArray( bytes, 0, bytes.length);
                    results.add(photo);

                    // Once done, add all results to the ListeningList at once, so as to only call the Listener once, rather than each time a read finishes.
                    if(results.size() == files.size())
                        photos.addAll(results);
                });
            }
        });
    }

    /**
     * Write a photo to a QRCode in the database. The photo is also written to the Most Recent Photos,
     * overwriting the oldest of the 5 photos.
     * @param CodeId The ID of the QRCode the photo should be written under
     * @param photo A bitmap representing the photo.
     */
    public void writePhoto(String CodeId, Bitmap photo, Context ct) {

        MemoryController mc = new MemoryController(ct);

        String PhotoId = QRCodeController.SHA256(QRCodeController.getRandomString(20));
        Uri location = mc.savePhoto(photo, PhotoId, 20);

        storage = firebase.getReference().child("photos").child(CodeId).child(PhotoId);
        UploadTask uploadTask = storage.putFile(location);
        uploadTask.addOnCompleteListener(writeListener);

        deleteOldestRecentPhoto();

        storage = firebase.getReference().child("recent").child(PhotoId);
        UploadTask writeRecent = storage.putFile(location);
        writeRecent.addOnCompleteListener(writeListener);
    }

    private void deleteOldestRecentPhoto() {
        storage = firebase.getReference().child("recent");

        // Get a list of the files in the recent folder
        storage.listAll().addOnCompleteListener(task -> {
            List<StorageReference> files = task.getResult().getItems();

            if(files.size() >= 5) {

                // Read the metadata of each file from Firebase. Use a ListeningList to store results
                ListeningList<StorageMetadata> metadata = new ListeningList<>();
                metadata.setOnAddListener(listeningList -> {

                    // Use the metadata of the files to determine the oldest one
                    StorageMetadata oldest = metadata.get(0);
                    for (int i = 0; i < 5; i++) {
                        if (metadata.get(i).getCreationTimeMillis() < oldest.getCreationTimeMillis())
                            oldest = metadata.get(i);
                    }

                    // Delete the oldest file
                    firebase.getReference().child("recent").child( oldest.getName() ).delete();
                });

                // The actual read of metadata from Firebase
                for (StorageReference fileRef : files)
                    fileRef.getMetadata().addOnCompleteListener(task1 -> metadata.add(task1.getResult()));
            }
        });
    }

    /**
     * Helper function to delete all photos in the remote storage
     * Calls recursive method deleteAllPhotos(StorageReference) to loop through each subfolder
     * Wrote this method because Firebase Console is bugged and will not let me manually delete files.
     */
    public void deleteAllPhotos() {
        deleteAllPhotos( firebase.getReference().child("photos"));
        deleteAllPhotos( firebase.getReference().child("recent"));
    }
    private void deleteAllPhotos(StorageReference root) {
        root.listAll().addOnCompleteListener(task -> {
            List<StorageReference> list = task.getResult().getItems();
            if(list.size() == 0)
                return;
            for(StorageReference ref : list) {
                deleteAllPhotos(ref);
                ref.delete();
            }
        });
    }
}
