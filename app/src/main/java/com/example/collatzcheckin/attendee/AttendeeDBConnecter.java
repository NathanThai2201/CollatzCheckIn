package com.example.collatzcheckin.attendee;

import com.google.firebase.firestore.FirebaseFirestore;

/**
 * AttendeeDB initialized an instance of the Firebase
 */
public class AttendeeDBConnecter {
    public FirebaseFirestore db;

    /**
     * This constructs instance of database
     */
    public  AttendeeDBConnecter() {
        this.db = FirebaseFirestore.getInstance();
    }
}
