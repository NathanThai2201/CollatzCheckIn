package com.example.collatzcheckin.authentication;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AnonAuthentication {
    private FirebaseAuth mAuth;

    public AnonAuthentication() {
        mAuth = FirebaseAuth.getInstance();
    }

    public boolean validateUser() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {return false;}
        return true;
    }

    public String identifyUser() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (validateUser()) {
            String uuid = currentUser.getUid().toString();
            return uuid;
        } else {
            return null;
        }
    }


}
