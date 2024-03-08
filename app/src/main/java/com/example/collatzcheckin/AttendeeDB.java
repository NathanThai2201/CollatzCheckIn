package com.example.collatzcheckin;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashMap;

public class AttendeeDB {
    private AttendeeDBConnecter userDB;
    private CollectionReference userRef;

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
     * @return CollectionReference
     */
    public CollectionReference getUserRef() {
        return userRef;
    }

    /**
     * Query to extract user data
     * @param uuid The unique idenitfier assigned to the user using Firebase Authenticator
     */
    public HashMap<String, String> findUser(String uuid) {
        HashMap<String, String> userData = new HashMap<>();
        userRef.document(uuid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("TAG", "DocumentSnapshot data: " + document.getData());
                        Log.d("TAG", "DocumentSnapshot data: " + document.getString("Name"));
                        userData.put("Name", document.getString("Name"));
                        userData.put("Email", document.getString("Email"));
                        userData.put("Uid", document.getString("Uid"));

                    } else {
                        Log.d("TAG", "No such document");
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });
        return userData;
    }

    /**
     * Query to add/update user data
     * @param user Object of type user that holds user data
     */
    public void addUser(User user) {
        HashMap<String, String> userData = new HashMap<>();
        userData.put("Name", user.getName());
        userData.put("Email", user.getEmail());
        userData.put("Uid", user.getUid());
        userData.put("Geo", user.getGeolocation());
        userData.put("Notif", user.getNotifications());
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
}