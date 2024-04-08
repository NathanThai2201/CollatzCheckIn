package com.example.collatzcheckin.utils;

import com.example.collatzcheckin.attendee.User;

/**
 * The FirebaseFindUserCallback interface provides a callback method
 * to handle the asynchronous retrieval of confirmation whether a user exists in
 * the Firebase Firestore database.
 */
public interface SignInUserCallback {
    /**
     * Called upon successful confirmation of a User object from the database.
     *
     * @param exists represnts whether a user exists or not
     */
    void onCallback(boolean exists);
}
