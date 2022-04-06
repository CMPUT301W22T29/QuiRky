package com.example.quirky;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

import static java.lang.Math.toIntExact;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
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
 *      Has a document for each QRCode that has been scanned with the app.
 *          Each document contains:
 *              - The score of the QRCode
 *              - Two inner collections:
 *                  - comments:
 *                      Holds a document for each comment the QRCode has.
 *                      The documents has 3 fields: content, user, and time
 *                  - userdata:
 *                      Holds a document for each user that has scanned this code.
 *                      The document contains: the photo the user took, and the location the user found the code.
 *
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
    private final Context ct;

    private CollectionReference collection;
    private final OnCompleteListener writeListener;
    private final OnCompleteListener deleteListener;

    public DatabaseController(Context ct) {
        this.db = FirebaseFirestore.getInstance();
        this.ct = ct;

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

    /**
     * Add a comment to a QRCode in the database
     * @param c The comment to write
     * @param id The QRCode the comment is written on
     */
    public void addComment(Comment c, String id) {
        collection = db.collection("QRcodes");

        collection = collection.document(id).collection("comments");
        collection.document(c.getId()).set(c).addOnCompleteListener(writeListener);
    }

    /**
     * Remove a Comment from a QRCode
     * @param c The comment to be removed
     * @param id The id of the QRCode the comment is on
     */
    public void removeComment(Comment c, String id) {   // TODO: consider making this method take the comment's id, rather than the object itself.
        collection = db.collection("QRcodes");

        collection = collection.document(id).collection("comments");
        collection.document(c.getId()).delete().addOnCompleteListener(deleteListener);
    }

    /**
     * Write a QRCode to the database. Will also add the QRCode to the account of the app-holder
     * @param qr The QRCode to be written
     */
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

    /**
     * Create or Update the Location a player scanned a QRCode
     * @param qrid The ID of the QRCode
     * @param user The User who scanned the code
     * @param location The location the user scanned the code
     */
    public void writeQRCodeLocation(String qrid, String user, GeoPoint location) {
        collection = db.collection("QRcodes").document(qrid).collection("userdata");
        Map<String, Object> data = new HashMap<>();
        data.put("location", location);
        collection.document(user).update(data).addOnCompleteListener(writeListener);
    }

    /**
     * Create or Update the Photo a player took of a QRCode
     * @param qrid The ID of the QRCode
     * @param user The User who scanned the code
     * @param photo The photo the user took of the QRCode
     */
    public void writeQRCodePhoto(String qrid, String user, Drawable photo) {
        collection = db.collection("QRcodes").document(qrid).collection("userdata");
        Map<String, Object> data = new HashMap<>();
        data.put("photo", photo);
        collection.document(user).update(data).addOnCompleteListener(writeListener);
    }

    /**
     * Delete a QRCode completely from the Database. Only the Owner should be able to do this.
     * TODO: Implement a method to remove a QRCode from a players profile
     * @param qr The QRCode to delete
     */
    public void deleteQRCode(String qr) {
        collection = db.collection("QRcodes");
        collection.document(qr).delete().addOnCompleteListener(deleteListener);
    }

    /**
     * Write a new profile to the Database.
     * @param p The profile to be written
     */
    public void writeProfile(Profile p) {
        collection = db.collection("users");
        collection.document(p.getUname()).set(p).addOnCompleteListener(writeListener);
    }

    /**
     * Delete an account from the Database. Only the Owner should be able to do this.
     * @param username The username of the profile to delete
     */
    public void deleteProfile(String username) {
        collection = db.collection("users");
        collection.document(username).delete().addOnCompleteListener(deleteListener);
    }

    /**
     * Begin reading a profile from the Database.
     * This is an Asynchronous operation, and such this method not return the result. The result can be obtained from getProfile()
     * @param username The username of the profile to be read
     * @return A task representing the read operation. This must be passed to getProfile(task)
     */
    public Task<DocumentSnapshot> readProfile(String username) {
        collection = db.collection("users");
        return collection.document(username).get();
    }

    /**
     * Get the results of the readProfile(). This method should only be called once the Task returned from ReadProfile has completed.
     * @param task The task returned from readProfile(). Calling with any other task will result in errors.
     * @return The profile with the username given to readProfile(). Will return null if the username does not exist.
     */
    public Profile getProfile(Task<DocumentSnapshot> task) {
        DocumentSnapshot doc = task.getResult();
        if(doc.getData() == null) {
            Log.d(TAG, "getProfile() returned a null Profile");
            return null;
        }
        return doc.toObject(Profile.class);
    }

    /**
     * Determine if the user passed to readProfile() is an owner user.
     * @param task The task returned by readProfile(). Calling with any other task will result in errors.
     * @return If the read user is an owner or not.
     */
    public boolean isOwner(Task<DocumentSnapshot> task) {
        DocumentSnapshot doc = task.getResult();
        if(doc.contains("owner"))
            return doc.getBoolean("owner");
        else
            return false;
    }

    /**
     * Begin querying the database for profiles with a similar username to the given string.
     * This is an Asynchronous operation, and such this method not return the result. The result can be obtained from getProfile()
     * @param search The string to search for similar usernames with
     * @return A task representing the read operation. This must be passed to getUserSearchQuery(task)
     */
    public Task<QuerySnapshot> startUserSearchQuery(String search) {
        collection = db.collection("users");
        return collection.whereGreaterThanOrEqualTo("uname", search).get();
    }

    /**
     * Get the results of the startUserSearchQuery(); This method should only be called once the Task returned from it has completed.
     * @param task The task returned from startUserSearchQuery(). Calling with any other task will result in errors.
     * @return An ArrayList of profiles with usernames similar to the string given to startUserSearchQuery().
     */
    public ArrayList<Profile> getUserSearchQuery(Task<QuerySnapshot> task) {
        QuerySnapshot result = task.getResult();
        return (ArrayList<Profile>) result.toObjects(Profile.class);
    }

    /**
     * Begin reading the entire 'users' collection from the database
     * This is an Asynchronous operation, and such this method not return the result. The result can be obtained from getProfile()
     * @return A task representing the read operation. This must be passed to getAllProfiles(task)
     */
    public Task<QuerySnapshot> readAllProfiles() {
        collection = db.collection("users");
        return collection.get();
    }

    /**
     * Get the results of the readAllProfiles(); This method should only be called once the Task returned from it has completed.
     * @param task The task returned from readAllProfiles(). Calling with any other task will result in errors.
     * @return An ArrayList of all profiles in the database
     */
    public ArrayList<Profile> getAllProfiles(Task<QuerySnapshot> task) {
        QuerySnapshot result = task.getResult();
        return (ArrayList<Profile>) result.toObjects(Profile.class);
    }

    /**
     * Begin reading a QRCode from the database.
     * This is an Asynchronous operation, and such this method not return the result. The result can be obtained from getProfile()
     * @param id The id of the QRCode to be read
     * @return A task representing the read operation. This must be passed to getQRCode(task)
     */
    public Task<DocumentSnapshot> readQRCode(String id) {
        collection = db.collection("QRcodes");
        return collection.document(id).get();
    }

    /**
     * Get the results of the readQRCode(); This method should only be called once the Task returned from it has completed.
     * @param task The task returned from readQRCode(). Calling with any other task will result in errors.
     * @return The QRCode with the ID that was passed to readQRCode(). Returns null if such a QRCode does not exist in the database, or if the score of the QRCode breaks the integer limit.jfdkla;
     */
    // Conversion from Long -> int taken from
    // https://stackoverflow.com/a/32869210
    // Posted by:
    // https://stackoverflow.com/users/1162647/pierre-antoine
    // Sept. 30, 2015
    public QRCode getQRCode(Task<DocumentSnapshot> task) {
        DocumentSnapshot doc = task.getResult();
        try {
            long score = doc.getLong("score");
            return new QRCode(doc.getId(), toIntExact(score));
        } catch (ArithmeticException e) {
            Log.d(TAG, "Somehow a QRCode score broke the integer limit LMAOOOOO");
            return null;
        } catch (NullPointerException e) {
            Log.d(TAG, "getQRCode returned a Null object");
            return null;
        }
    }

    /**
     * Begin reading every QRCode from the database. This is an asynchronous operation and as such does not return the data.
     * @return A task representing the read operation. Pass this task to getAllQRCodes() to get the data, once the task completes.
     */
    public Task<QuerySnapshot> readAllQRCodes() {
        collection = db.collection("QRcodes");
        return collection.get();
    }

    /**
     * Finish reading every QRCode from the database.
     * @param task The task returned by readAllQRCodes. Calling with any other task will result in errors.
     * @return An ArrayList containing every QRCode in the database.
     */
    public ArrayList<QRCode> getAllQRCodes(Task<QuerySnapshot> task) {
        QuerySnapshot result = task.getResult();
        ArrayList<QRCode> codes = new ArrayList<>();
        for(DocumentSnapshot doc : result.getDocuments()) {
            String id = doc.getId();
            int score = toIntExact(doc.getLong("score"));
            codes.add(new QRCode(id, score));
        }

        return codes;
    }

    /* - - The Methods in this block are related to each other - - */
    /**
     * Begin reading the userdata of a QRCode from the database.
     * This is an Asynchronous operation, and such this method not return the result. The result can be obtained from getProfile()
     * @param qrcode The ID of the QRCode in question
     * @return A task representing the read operation. This can be passed to one of the following methods to get user-specific data about the QRCode
     *          1. getQRCodeLocations(task), to get all the locations the QRCode has been scanned at
     *          2. getQRCodePhotos(task), to get all the photos users have taken of the QRCode
     *          3. getQRCodeScanners, to get the usernames of all players who have scanned the QRCode
     */
    public Task<QuerySnapshot> readQRCodeUserData(String qrcode) {
        collection = db.collection("QRCodes").document(qrcode).collection("userdata");
        return collection.get();
    }

    /**
     * Get all the photos players have taken of a QRCode
     * @param task The task returned by readQRCodeUserData(). Calling with any other task will result in errors.
     * @return A list of GeoPoints objects, every place the QRCode has been scanned.
     */
    public ArrayList<GeoPoint> getQRCodeLocations(Task<QuerySnapshot> task) {
        QuerySnapshot query = task.getResult();
        ArrayList<GeoPoint> locations = new ArrayList<>();
        for(DocumentSnapshot doc : query.getDocuments()) {
            if(doc == null) continue;
            locations.add((GeoPoint) doc.get("location"));      // FIXME: this will 1000% crash, need to think about how to get a GeoPoint object from a lat & long in firestore

        }
        return locations;
    }

    /**
     * Get all the photos players have taken of a QRCode
     * @param task The task returned by readQRCodeUserData(). Calling with any other task will result in errors.
     * @return A list of drawable objects
     */
    public ArrayList<Drawable> getQRCodePhotos(Task<QuerySnapshot> task) {
        QuerySnapshot query = task.getResult();
        ArrayList<Drawable> photos = new ArrayList<>();
        for(DocumentSnapshot doc : query.getDocuments()) {
            if(doc == null) continue;
            photos.add((Drawable) doc.get("photo"));        // FIXME: this will 1000% crash, need to think about how store Drawables to FireStore
        }
        return photos;
    }

    /**
     * Get the users who have scanned the QRCode
     * @param task The task returned by readQRCodeUserData(). Calling with any other task will result in errors.
     * @return A list of strings, the usernames of players who scanned the code.
     */
    public ArrayList<String> getQRCodeScanners(Task<QuerySnapshot> task) {

        QuerySnapshot query = task.getResult();
        ArrayList<Profile> scanners = (ArrayList<Profile>) query.toObjects(Profile.class);
        Log.d(TAG, "getQRCodeScanners read " + query.size() + " players");

        ArrayList<String> usernames = new ArrayList<>();
        for(Profile p : scanners)
            usernames.add(p.getUname());
        return usernames;
    }
    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

    /**
     * Write a hash code to the Database that a player can use to log in with.
     * @param hash The hash to be scanned that will log in a player.
     * @param user The account the hash will log the player into.
     */
    public void writeLoginHash(String hash, String user) {
        collection = db.collection("users");
        collection.document(user).update("loginhash", hash).addOnCompleteListener(writeListener);
    }

    /**
     * Begin searching the database for the account with the specified login hash.
     * This is an Asynchronous operation, and such this method not return the result. The result can be obtained from getProfile()
     * @param hash The password of the account to be logged into.
     * @return A task representing the read operation. This must be passed to getProfileWithHash() once the task completes.
     */
    public Task<QuerySnapshot> readLoginHash(String hash) {
        collection = db.collection("users");
        return collection.whereEqualTo("loginhash", hash).get();
    }

    /**
     * Get the account with the hash passed to by readLoginHash().
     * This will also delete the data field that holds the login password from the database. This makes login QRCodes one use only.
     * @param task The task returned by readLoginHash(). Calling with any other task will result in errors.
     * @return The account with the hash password passed to readLoginHash(). If no account has such password, return null.
     */
    public Profile getProfileWithHash(Task<QuerySnapshot> task) {
        QuerySnapshot results = task.getResult();

        if(results.isEmpty()) {
            Log.d(TAG, "readLoginHash did not find any users with that password! Returned null!");
            return null;
        }
        else if (results.size() > 1) {
            Log.d(TAG, "For some reason there are multiple profiles with the same password. readLoginHash() returned null.");
            return null;
        }

        Profile p = results.toObjects(Profile.class).get(0);

        collection = db.collection("users");
        collection.document(p.getUname()).update("loginhash", 0).addOnCompleteListener(deleteListener);

        return p;
    }

    /**
     * Start checking if a user exists in the database. This is an asynchronous operation, and as such this method will not return the data.
     * @param username The username to check for
     * @return A task representing the read operation. Pass this to checkProfileExists() once the task completes
     */
    public Task<DocumentSnapshot> startCheckProfileExists(String username) {
        collection = db.collection("users");
        return collection.document(username).get();
    }

    /**
     * Check if a user exists in the database.
     * @param task The task returned by startCheckProfileExists(). Calling with any other task will result in errors.
     * @return True if the user exists in the database. False otherwise.
     */
    public boolean checkProfileExists(Task<DocumentSnapshot> task) {
        return task.getResult().getData() == null;
    }
    public ArrayList<GeoPoint> fakeGetLocations() {
        //Task<QuerySnapshot> task = readQRCodeUserData(QRCode.);
        //ArrayList<GeoPoint> geoPoints = getQRCodeLocations();
        ArrayList<GeoPoint> geoPoints = new ArrayList<>();
        GeoPoint geoPoint = new GeoPoint((double)53.5232, (double)-113.5263);
        geoPoints.add(geoPoint);
        GeoPoint geoPoint2 = new GeoPoint((double)53.5232, (double)-113.5261);
        geoPoints.add(geoPoint2);
        GeoPoint geoPoint3 = new GeoPoint((double)53.5234, (double)-113.5262);
        geoPoints.add(geoPoint3);
        return geoPoints;
    }



    /**
     * Begin reading the comments of a QRCode.
     * Because this is an asynchronous operation, the results are not returned here.
     * @param qrId The ID of the QRCode to read from
     * @return A task representing the read operation. Pass this to getComments() to complete the read
     */
    public Task<QuerySnapshot> readComments(String qrId) {
        collection = db.collection("QRcodes").document(qrId).collection("comments");
        return collection.get();
    }

    /**
     * Finish reading the comments of a QRCode.
     * @param task The task returned by readComments(). Calling with any other task will result in errors
     * @return The comments on the QRCode, in an ArrayList
     */
    public ArrayList<Comment> getComments(Task<QuerySnapshot> task) {
        QuerySnapshot q = task.getResult();
        return (ArrayList<Comment>) q.toObjects(Comment.class);
    }
}
