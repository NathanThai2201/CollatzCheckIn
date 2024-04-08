package com.example.collatzcheckin.admin.controls;

import com.google.firebase.firestore.FirebaseFirestore;

/**
 * The AdministratorDBConnector class provides connectivity to the Firestore database for administrative tasks
 */
public class AdministratorDBConnector {
    public FirebaseFirestore db;

    /**
     * Constructs a new AdministratorDBConnector and initializes the Firestore database instance
     */
    public  AdministratorDBConnector() {
        this.db = FirebaseFirestore.getInstance();
    }
}
