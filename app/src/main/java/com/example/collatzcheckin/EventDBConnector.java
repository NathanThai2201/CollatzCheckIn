package com.example.collatzcheckin;

import com.google.firebase.firestore.FirebaseFirestore;
/**
 * This class initializes a connection to Firebase Firestore
 */
public class EventDBConnector {
    public FirebaseFirestore db;

    public  EventDBConnector() {
        this.db = FirebaseFirestore.getInstance();
    }
}
