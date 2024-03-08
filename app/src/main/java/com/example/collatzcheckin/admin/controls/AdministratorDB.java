package com.example.collatzcheckin.admin.controls;

import android.media.Image;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.collatzcheckin.Event;
import com.example.collatzcheckin.attendee.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.List;

public class AdministratorDB {
    private AdministratorDBConnector adminDB;
    private CollectionReference eventRef;
    private CollectionReference profileRef;
    private CollectionReference imageRef;
    public AdministratorDB() {
        this.adminDB = new AdministratorDBConnector();
        this.eventRef = adminDB.db.collection("events");
        this.profileRef = adminDB.db.collection("user"); // Some of these paths do not exist. Need to address before push
        this.imageRef = adminDB.db.collection("images");
    }

    public List<Event> getAllEvents() {
        // Code to retrieve all events from EventDB
        List<Event> events = new ArrayList<>();
        eventRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null) {
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        Event event = document.toObject(Event.class);
                        events.add(event);
                    }
                }
            } else {
                // Handle errors here
            }
        });
        return events;
    }

    public List<User> getAllProfiles() {
        // Code to retrieve all profiles from AttendeeDB
        List<User> profiles = new ArrayList<>();
        profileRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null) {
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        User profile = document.toObject(User.class);
                        profiles.add(profile);
                    }
                }
            } else {
                // Handle errors here
            }
        });
        return profiles;
    }


    public List<Image> getAllImages() {
        // Code to retrieve all images from ImageDB
        List<Image> images = new ArrayList<>();
        imageRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null) {
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        // Assuming images are stored as paths or URLs in Firestore
                        String imageUrl = document.getString("imageUrl");
                        // Load image using imageUrl and add to images list
                    }
                }
            } else {
                // Handle errors here
            }
        });
        return images;
    }

    // Methods to remove events, profiles, and images
    public void removeEvent(Event event) {
        // Code to remove event from EventDB
        String eventTitle = event.getEventTitle(); // Get the title of the event
        eventRef.document(eventTitle).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Document successfully deleted
                        System.out.println("DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle any errors
                        System.out.println("Error deleting document: " + e.getMessage());
                    }
                });
    }


    public void removeProfile(User user) {
        // Code to remove profile from AttendeeDB
        String userUid = user.getUid(); // Get the UID of the user
        Log.d("User UID", userUid);

        profileRef.document(userUid).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Document successfully deleted
                        System.out.println("DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle any errors
                        System.out.println("Error deleting document: " + e.getMessage());
                    }
                });
    }


    public void removeImage(Image image) {
        // Code to remove image from ImageDB

    }
}
