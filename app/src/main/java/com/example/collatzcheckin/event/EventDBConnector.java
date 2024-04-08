package com.example.collatzcheckin.event;

import com.google.firebase.firestore.FirebaseFirestore;
/**
 * This class initializes a connection to Firebase Firestore
 */
public class EventDBConnector {
    public FirebaseFirestore db;

/**
 * This is a constructor for EventDBConnector
 */
    public  EventDBConnector() {
        this.db = FirebaseFirestore.getInstance();
    }
}
