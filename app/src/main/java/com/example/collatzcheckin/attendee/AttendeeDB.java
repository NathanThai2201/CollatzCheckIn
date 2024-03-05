package com.example.collatzcheckin.attendee;


import android.util.Log;
import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
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

        public AttendeeDB() {
            this.userDB = new AttendeeDBConnecter();
            this.userRef = userDB.db.collection("user");
        }

    public CollectionReference getUserRef() {
        return userRef;
    }

    public HashMap<String, String> findUser(String uuid) {
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



        public void addUser(String uuid) {
            HashMap<String, String> userData = new HashMap<>();
            userData.put("Name", "user.getName()");
            userData.put("Email", "user.getEmail()");
            userData.put("Uid", "user.getUid()");
            userRef.document(uuid)
                    .set(userData)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("Firestore", "DocumentSnapshot successfully written!");
                        }
                    });
    }
}
