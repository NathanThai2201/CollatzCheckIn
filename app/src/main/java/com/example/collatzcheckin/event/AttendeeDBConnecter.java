package com.example.collatzcheckin.event;

import com.google.firebase.firestore.FirebaseFirestore;

public class AttendeeDBConnecter {
    public FirebaseFirestore db;

    /**
     * This constructs instance of database
     */
    public  AttendeeDBConnecter() {
        this.db = FirebaseFirestore.getInstance();
    }
}
