package com.example.collatzcheckin.utils;

import com.example.collatzcheckin.attendee.User;

/**
 * The FirebaseFindUserCallback interface provides a callback method
 * to handle the asynchronous retrieval of a User object
 * from the Firebase Firestore database.
 */
public interface FirebaseFindUserCallback {
    /**
     * Called upon successful retrieval of a User object from the database.
     *
     * @param user The retrieved User object containing the user's information.
     */
    void onCallback(User user);
}
