/*
 * Copyright (c) 2022. CMPUT301W22T29
 * Subject to MIT License
 * See full terms at https://github.com/CMPUT301W22T29/QuiRky/blob/main/LICENSE
 */

package com.example.quirky.controllers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import com.example.quirky.ListeningList;
import com.example.quirky.models.Comment;
import com.example.quirky.models.GeoLocation;
import com.example.quirky.models.Profile;
import com.example.quirky.models.QRCode;
import com.example.quirky.models.UserOwnedQRCode;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Jonathen Adsit
 * This is a controller class that does all the reading and writing to FireStore
 * TODO: Consider removing the coupling from the model classes,
*/

// Reading & Writing Custom Objects to FireStore taken from:
// https://youtu.be/jJnm3YKfAUI
// Made By:
// https://www.youtube.com/channel/UC_Fh8kvtkVPkeihBs42jGcA
// Published April 15, 2018
@SuppressWarnings({"unchecked", "ConstantConditions"})
public class DatabaseController {
    private static final String TAG = "DatabaseController says: ";
    private static final long MAX_DOWNLOAD_SIZE = 10485760; // 10485760 Bytes == 10 mB

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
     * TODO: Finish the doc comment
     * @param nearbyCode
     */
    public void writeNearbyQRCode(UserOwnedQRCode nearbyCode) {
        assert nearbyCode != null : "You can't write a null QRCode to the database!";

        GeoLocation location = nearbyCode.getLocation();
        int lat = location.getApproxLat();
        int lon = location.getApproxLong();
        CollectionReference nearbyCollection = firestore.collection(String.format("locations/latitude%d/longitude%d/qr/codes", lat, lon));

        nearbyCollection
                .whereEqualTo(FieldPath.of("location/exactLat"), nearbyCode.getLocation().getExactLat())
                .whereEqualTo(FieldPath.of("location/exactLong"), nearbyCode.getLocation().getExactLong())
                .whereEqualTo("id", nearbyCode.getId())
                .get().addOnCompleteListener(task -> {
                    if (task.getResult().getDocuments().size() == 0) {
                        nearbyCollection.document().set(nearbyCode).addOnCompleteListener(writeListener);
                    }
                });
    }

    /**
     * Write a QRCode to the database.
     * Checks if the QRCode already exists in the database.
     * If so, updates that document rather than creating a new one
     * @param newCode The QRCode to write/update
     */
    public void writeQRCode(QRCode newCode) {
        assert newCode != null : "You can't write a null QRCode to the database!";

        ListeningList<QRCode> code = new ListeningList<>();
        code.setOnAddListener(listeningList -> {
            if(listeningList.size() > 0) {
                updateQRCode(newCode, listeningList.get(0));
            } else {
                collection = firestore.collection("QRCodes");
                collection.document().set(newCode).addOnCompleteListener(writeListener);
            }
        });
        readQRCode( newCode.getId(), code);
    }

    private void updateQRCode(QRCode newCode, QRCode oldCode) {
        for(String player : newCode.getScanners())
            oldCode.addScanner(player);

        for(String title : newCode.getTitles())
            oldCode.addTitle(title);

        for(Comment comment : newCode.getComments())
            oldCode.addComment(comment);

        for(GeoLocation location : newCode.getLocations())
            oldCode.addLocation(location);

        collection = firestore.collection("QRCodes");
        collection.whereEqualTo("id", oldCode.getId()).get().addOnCompleteListener(task ->
                task.getResult().getDocuments().get(0).getReference().set(oldCode)
        );
    }

    /**
     * Write a profile to the database
     * Checks if the profile already exists in the database
     * If a document already contains the profile, overwrite it.
     * Otherwise, creates a new document for the profile.
     * @param p The profile to write
     */
    public void writeProfile(String original_username, Profile p) {
        assert p != null : "You can't write a null object to the database!";

        collection = firestore.collection("users");
        collection.whereEqualTo("uname", original_username).get().addOnCompleteListener(task -> {
            if(task.getResult().getDocuments().size() > 0) {
                task.getResult().getDocuments().get(0).getReference().set(p).addOnCompleteListener(writeListener);
            } else {
                collection = firestore.collection("users");
                collection.document().set(p).addOnCompleteListener(writeListener);
            }
        });
    }

    /**
     * Delete a QRCode from the database. Also deletes any associated photos from Firebase Storage
     * @param qr The QRCode to delete
     */
    public void deleteQRCode(String qr) {

        // Delete from FireStore. The Document ID is unknown so it must be read, then it can be referenced and deleted
        collection = firestore.collection("QRCodes");
        collection.whereEqualTo("id", qr).get().addOnCompleteListener(task -> {

            int size = task.getResult().getDocuments().size();
            if(size > 1)
                Log.d(TAG, "For some reason more than 1 QRCode had that ID! Only one was deleted!");
            else if (size < 1)
                Log.d(TAG, "A QRCode with that ID did not exist in the database!");

            DocumentSnapshot doc = task.getResult().getDocuments().get(0);
            doc.getReference().delete().addOnCompleteListener(deleteListener);

        });

        // Delete from Firebase Storage
        storage = firebase.getReference().child("photos").child(qr);
        storage.delete().addOnCompleteListener(deleteListener);
    }

    /**
     * Delete a profile from the database
     * @param username The username of the profile to delete
     */
    public void deleteProfile(String username) {
        // Delete from FireStore. The Document ID is unknown so it must be read, then it can be referenced and deleted
        collection = firestore.collection("users");
        collection.whereEqualTo("uname", username).get().addOnCompleteListener(task -> {
            if (task.getResult().getDocuments().size() < 1)
                Log.d(TAG, "A user with that name did not exist in the database!");

            DocumentSnapshot doc = task.getResult().getDocuments().get(0);
            doc.getReference().delete().addOnCompleteListener(deleteListener);
        });
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
        collection.whereEqualTo("name", username).get().addOnCompleteListener(task -> {
            int size = task.getResult().getDocuments().size();
            if(size < 1) {
                Log.d(TAG, "A user with that name did not exist! No data read!");
                data.addNone();
                return;
            }

            DocumentSnapshot doc = task.getResult().getDocuments().get(0);
            data.add( doc.toObject(Profile.class));
        });
    }

    /**
     * Checks if a user with the given username already exists in the database
     * This is an asynch operation and will not return the result
     * When the read completes, the boolean if placed in the listening list
     * @param username A username to check the database for
     * @param data A listening list containing booleans. If the username is taken, true is added to the list. False otherwise.
     */
    public void userExists(String username, ListeningList<Boolean> data) {
        collection = firestore.collection("users");
        collection.whereEqualTo("uname", username).get().addOnCompleteListener(task -> {
            int docs = task.getResult().getDocuments().size();
            Log.d(TAG, docs + " documents containing this players");
            if(task.getResult().getDocuments().size() > 0)
                data.add(true);
            else
                data.add(false);
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
        collection.whereEqualTo("uname", username).get().addOnCompleteListener(task -> {

            List<DocumentSnapshot> docs = task.getResult().getDocuments();
            if(docs.size() < 1) {
                data.add(false);
                return;
            }

            if(docs.get(0).contains("owner"))
                data.add( docs.get(0).getBoolean("owner") );
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
     * Read all QRCodes whose approximate longitude and latitude match location's
     *
     * QRCode references are stored in groups with other QRCode references that have the same floor
     * of their coordinate values. E.g. 2 QRCodes with coordinates (6.9, 7.2) and (6.0, 7.6)
     * respectively would be in the same group. This works out to a very very approximate 100km
     * radius. If a smaller radius is desired, the qr codes returned from this method can be
     * searched.
     * This method stores the data retrieved from the database in a ListeningList, which calls
     * onAdd() whenever something is added to it. Override onAdd() with whatever processing should
     * be performed on the data.
     * The data retrieved from the database is not the same as the QRCodes retrieved by
     * readQRCode(). Rather, it retrieves a location specific subset of the data contained by the
     * usual qr codes. Currently, it only retrieves the QRCode id and location.
     *
     * @param location The reference location in which the retrieved QRCodes will be near to.
     * @param data The container in which the retrieved data will be add
     */
    public void readNearbyCodes(GeoLocation location, ListeningList<UserOwnedQRCode> data) {
        int lat = location.getApproxLat();
        int lon = location.getApproxLong();
        collection = firestore.collection(String.format("locations/latitude%d/longitude%d/qr/codes", lat, lon));
        collection.get().addOnCompleteListener(task -> {
            Collection<UserOwnedQRCode> result = task.getResult().toObjects(UserOwnedQRCode.class);
            data.addAll(result);
        });
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
        collection.whereEqualTo("id", id).get().addOnCompleteListener(task -> {
            int size = task.getResult().getDocuments().size();
            if(size != 1) {
                Log.d(TAG, "That id did not match up with exactly 1 document! Adding Nothing");
                data.addNone();
            } else {
                QRCode result = task.getResult().getDocuments().get(0).toObject(QRCode.class);
                data.add(result);
            }
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
     * @param username The username of the profile to write the password to
     */
    public void writeLoginHash(String hash, String username) {
        // The Document ID of this user is not known, so it must be read first
        collection = firestore.collection("users");
        collection.whereEqualTo("uname", username).get().addOnCompleteListener(task -> {
            if(task.getResult().getDocuments().size() < 1)
                Log.d(TAG, "No user with name " + username + " exists in the database! Login hash not written!");
            else {
                DocumentReference doc = task.getResult().getDocuments().get(0).getReference();
                doc.update("loginhash", hash).addOnCompleteListener(writeListener);
            }


        });

    }

    /**
     * Search the database for a user with whose password field matches the given password.
     * This is an asynchronous operation, and as such will not return the read result.
     * When the read completes, the data is added to the passed ListeningList<>.
     * @param hash The password to search for
     * @param data A listening list containing Profiles. The read result is placed here.
     */
    public void readLoginHash(String hash, ListeningList<Profile> data) {

        assert ! hash.equals("") : "Must provide a hash code to read!";

        collection = firestore.collection("users");
        collection.whereEqualTo("loginhash", hash).get().addOnCompleteListener(task -> {
            QuerySnapshot q = task.getResult();
            if(q.size() != 1) {
                Log.d(TAG, "For some reason reading a login hash did not produce exactly 1 profile!");
                data.addNone();
            } else {
                Profile p = q.getDocuments().get(0).toObject(Profile.class);
                data.add(p);
                writeLoginHash("", p.getUname());
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

                // Create a ListeningList to read the Metadata of each file into
                ListeningList<StorageMetadata> metadata = new ListeningList<>();
                metadata.setOnAddListener(listeningList -> {

                    if(listeningList.size() < files.size())
                        return;

                    // Use the metadata of the files to determine the oldest one
                    StorageMetadata oldest = metadata.get(0);
                    for (int i = 0; i < metadata.size(); i++) {
                        if (metadata.get(i).getCreationTimeMillis() < oldest.getCreationTimeMillis())
                            oldest = metadata.get(i);
                    }

                    // Delete the oldest file
                    Log.d(TAG, "Deleting->|" + oldest.getPath());
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
