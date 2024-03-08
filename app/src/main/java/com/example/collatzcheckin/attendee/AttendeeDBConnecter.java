package com.example.collatzcheckin.attendee;

import com.google.firebase.firestore.FirebaseFirestore;

public class AttendeeDBConnecter {
    public FirebaseFirestore db;

    public  AttendeeDBConnecter() {
        this.db = FirebaseFirestore.getInstance();
    }
}
