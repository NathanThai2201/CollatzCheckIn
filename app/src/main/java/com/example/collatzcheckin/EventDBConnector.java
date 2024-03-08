package com.example.collatzcheckin;

import com.google.firebase.firestore.FirebaseFirestore;

public class EventDBConnector {
    public FirebaseFirestore db;

    public  EventDBConnector() {
        this.db = FirebaseFirestore.getInstance();
    }
}
