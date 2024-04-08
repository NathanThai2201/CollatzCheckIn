package com.example.collatzcheckin.attendee;


import android.util.Log;
import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import androidx.annotation.NonNull;

import com.example.collatzcheckin.utils.FirebaseFindUserCallback;
import com.example.collatzcheckin.utils.SignInUserCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * AttendeeDB interacts with the database collectction that holds user info
 */
public class AttendeeDB {
    private AttendeeDBConnecter userDB;
    public CollectionReference userRef;

    /**
     * This constructs instance of database and sets the collection to 'user'
     */
    public AttendeeDB() {
        this.userDB = new AttendeeDBConnecter();
        this.userRef = userDB.db.collection("user");
    }

    /**
     * This returns the object 'CollectionReference' which holds information about the
     * collection that is being interacted with
     * @return  CollectionReference object for the 'user' collection
     */
    public CollectionReference getUserRef() {
        return userRef;
    }

    /**
     * Query to extract user data
     * @param uuid              The unique idenitfier assigned to the user using Firebase Authenticator
     * @param callback          The callback to be invoked with the retrieved User object
     */
    public void findUser(String uuid, FirebaseFindUserCallback callback) {
            userRef.document(uuid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            User user = new User(document.getString("Name"),
                                    document.getString("Email"),
                                    uuid,
                                    Boolean.parseBoolean(document.getString("Geo")),
                                    Boolean.parseBoolean(document.getString("Notif")),
                                    document.getString("Pfp"),
                                    document.getString("GenPfp"));

                            Log.d(TAG, "Success");
                            callback.onCallback(user);
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });
        }

    /**
     * Checks if a user exist based on the provided UUID.
     *
     * @param uuid     The unique identifier assigned to the user
     * @param callback The callback to be invoked with the validation result (true if user exists, false otherwise).
     */
    public void isValidUser(String uuid, SignInUserCallback callback) {
        userRef.document(uuid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "Success");
                        callback.onCallback(true);
                    } else {
                        Log.d(TAG, "No such document");
                        callback.onCallback(false);
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                    callback.onCallback(false);
                }
            }
        });
    }

    /**
     * Query to add/update user data
     * @param user Object of type user that holds user data
     */
        public void addUser(User user) {
            HashMap<String, Object> userData = new HashMap<>();
            String[] str = {};

            userData.put("Name", user.getName());
            userData.put("Email", user.getEmail());
            userData.put("Uid", user.getUid());
            userData.put("Geo", (user.getGeolocation()).toString());
            userData.put("Longitude", String.valueOf(user.getLongitude()));
            userData.put("Latitude", String.valueOf(user.getLatitude()));
            userData.put("Notif", (user.getNotifications()).toString());
            userData.put("Pfp", user.getPfp());
            userData.put("GenPfp", user.getGenpfp());
            userData.put("Admin", (String.valueOf(user.isAdmin())));
            userData.put("Events", new ArrayList<String>());
            if (user.getGeolocation()) {
                userData.put("Longitude", String.valueOf(user.getLongitude()));
                userData.put("Latitude", String.valueOf(user.getLatitude()));
            }

            Log.d("Firestore", "DocumentSnapshot successfully written!");
            userRef.document(user.getUid())
                    .set(userData)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("Firestore", "DocumentSnapshot successfully written!");
                        }
                    });
        }
        public void updateUser(User user){
            DocumentReference ref = userDB.db.collection("user").document(user.getUid());
            ref.update("Name",user.getName());
            ref.update("Email",user.getEmail());
            ref.update("Geo",user.getGeolocation().toString());
            ref.update("Notif",user.getNotifications().toString());
            ref.update("Pfp",user.getPfp());
            ref.update("GenPfp",user.getGenpfp());
            if (user.getGeolocation()){
                ref.update("Latitude",String.valueOf(user.getLatitude()));
                ref.update("Longitude",String.valueOf(user.getLongitude()));
            } else {
                ref.update("Latitude",String.valueOf(0.00));
                ref.update("Longitude",String.valueOf(0.00));

            }

        }


    /**
     * Query to extract user data
     * @param uuid The unique identifier assigned to the user using Firebase Authenticator
     */
    public HashMap<String, String> locateUser(String uuid) {
        HashMap<String, String> userData = new HashMap<>();
        userRef.document(uuid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        Log.d(TAG, "DocumentSnapshot data: " + document.getString("Name"));
                        userData.put("Name", document.getString("Name"));
                        userData.put("Email", document.getString("Email"));
                        userData.put("Uid", document.getString("Uid"));
                        userData.put("Admin", document.getString("Admin"));

                        Log.d(TAG, "DocumentSnapshot data: " + userData.get("Name"));
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
        return userData;
    }

    /**
     * Updates the 'Events' array field for a user in the database.
     *
     * @param euid              The unique identifier of the event that user will sign up for
     * @param uuid              The unique identifier of the user
     */
    public void EventsSignUp(String euid, String uuid) {
        userRef.document(uuid).update("Events", FieldValue.arrayUnion(euid));
    }

    /**
     * Updates the 'Pfp' field for a user in the database.
     *
     * @param location      The location of the profile photo to be saved
     * @param uuid          The unique identifier of the user
     */
    public void saveProfilePhoto(String location, String uuid) {
        userRef.document(uuid).update("Pfp", location);
    }

    /**
     * Updates the deterministically generated pfp for a user in the database.
     *
     * @param location The location of the generic profile photo to be saved.
     * @param uuid     The unique identifier of the user.
     */
    public void saveGenProfilePhoto(String location, String uuid) {
        userRef.document(uuid).update("GenPfp", location);
    }


}
