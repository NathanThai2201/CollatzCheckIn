package com.example.collatzcheckin.admin.controls;

import com.google.firebase.firestore.FirebaseFirestore;

public class AdministratorDBConnector {
    public FirebaseFirestore db;
    public  AdministratorDBConnector() {
        this.db = FirebaseFirestore.getInstance();
    }
}
