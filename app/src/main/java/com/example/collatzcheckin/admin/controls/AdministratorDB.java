package com.example.collatzcheckin.admin.controls;

import android.content.DialogInterface;
import android.media.Image;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.collatzcheckin.AdminMainActivity;
import com.example.collatzcheckin.admin.controls.events.AdminEventListFragment;
import com.example.collatzcheckin.attendee.AttendeeDB;
import com.example.collatzcheckin.event.Event;
import com.example.collatzcheckin.attendee.User;
import com.example.collatzcheckin.event.EventDB;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AdministratorDB class handles database operations related to administrators' tasks
 */
public class AdministratorDB {
    private AdministratorDBConnector adminDB;
    private CollectionReference eventRef;
    private CollectionReference profileRef;
    private ArrayList<String> signedUp;


    /**
     * Constructor for AdministratorDB
     * Initializes the database connector and collection references
     */
    public AdministratorDB() {
        this.adminDB = new AdministratorDBConnector();
        this.eventRef = adminDB.db.collection("events");
        this.profileRef = adminDB.db.collection("user");

    }

    /**
     * Callback interface to handle removal operations
     */
    public interface RemoveCallback {
        /**
         * Called when the removal operation is successful
         */
        void onRemoved();
        /**
         * Called when the removal operation fails
         *
         * @param errorMessage The error message describing the cause of failure
         */
        void onRemoveFailure(String errorMessage);
    }


    /**
     * Removes the specified event from the database
     *
     * @param event    The event to be removed
     * @param callback Callback to handle the result of the removal operation
     */
    public void removeEvent(Event event, RemoveCallback callback) {
        String eventID = event.getEventID(); // Get the ID of the event


        boolean checkVar = false;
        // Code to remove event from EventDB


        Log.d("AdminDB", "event.getAttendees(): " + event.getAttendees());
        if (event.getAttendees() != null) {
            signedUp= new ArrayList<>(event.getAttendees().keySet()); // get list of sign ups
            checkVar = true;
        }


        AttendeeDB attendeeDB = new AttendeeDB();

        boolean finalCheckVar = checkVar;
        eventRef.document(eventID).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) { // Document successfully deleted
                        StorageReference imageRef = FirebaseStorage.getInstance().getReference().child("posters/" + event.getEventID());
                        imageRef.delete();
                        StorageReference qrRef = FirebaseStorage.getInstance().getReference().child("qr/" + event.getEventID() + ".jpg");
                        qrRef.delete();
                        StorageReference shareRef = FirebaseStorage.getInstance().getReference().child("qr/" + event.getEventID() + "_share.jpg");
                        shareRef.delete();
                        if (finalCheckVar) {
                            // remove references of event to be deleted from all users
                            for (String attendee : signedUp) {
                                Log.d("AdminDB", "Attendees " + attendee);
                                attendeeDB.userRef.document(attendee).update("Events", FieldValue.arrayRemove(eventID));
                            }
                        }
                        callback.onRemoved();
                        System.out.println("DocumentSnapshot successfully deleted!");
                        }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle any errors
                        callback.onRemoveFailure("Didnt work");
                        System.out.println("Error deleting document: " + e.getMessage());
                    }
                });
    }


    /**
     * Removes the specified user profile from the database
     *
     * @param user     The user profile to be removed
     * @param callback Callback to handle the result of the removal operation
     */
    public void removeProfile(User user, RemoveCallback callback) {
        // Code to remove profile from AttendeeDB
        String userUid = user.getUid(); // Get the UID of the user
        Log.d("User UID", userUid);


        List<String> attending = user.getEventIds(); // get list of sign ups

        EventDB eventDB = new EventDB();

        profileRef.document(userUid).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) { // Document successfully deleted
                        StorageReference imageRef = FirebaseStorage.getInstance().getReference().child("images/" + user.getUid());
                        imageRef.delete();
                        for (String eventID : attending){
                            Log.d("attending", eventID);
                            eventRef.document(eventID)
                                    .get()
                                    .addOnSuccessListener(documentSnapshot -> {
                                        if (documentSnapshot.exists()) {
                                            // Document found

                                            HashMap<String,String> attendees = (HashMap<String,String>) documentSnapshot.get("Attendees");
                                            HashMap<String,String> attendeesNew = new HashMap<String,String>();

                                            for (Map.Entry<String, String> entry : attendees.entrySet()) {
                                                String key = entry.getKey();
                                                String value = entry.getValue();

                                                if (!key.equals(userUid)) {
                                                    attendeesNew.put(key, value);
                                                }
                                            }
                                            eventRef.document(eventID).update("Attendees", attendeesNew);

                                        } else {
                                            // Document does not exist
                                            Log.d("TAG", "No such document");
                                        }
                                    });
                            //eventRef.document(eventID).update("Attendees", FieldValue.arrayRemove(userUid));

                        }
                        callback.onRemoved();
                        System.out.println("DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle any errors
                        callback.onRemoveFailure("Didnt work");
                        System.out.println("Error deleting document: " + e.getMessage());
                    }
                });
    }
}
