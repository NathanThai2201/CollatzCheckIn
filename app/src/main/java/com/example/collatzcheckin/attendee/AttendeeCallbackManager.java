package com.example.collatzcheckin.attendee;

import com.example.collatzcheckin.utils.FirebaseFindUserCallback;
import com.example.collatzcheckin.utils.SignInUserCallback;

/**
 * AttendeeCallbackManager manages callbacks when retrieving user info from Firebase
 * so it can be displayed on relevant pages
 */
public class AttendeeCallbackManager {
    private final AttendeeDB attendeeDB = new AttendeeDB();

    /**
     * Reads user data from Firebase based on the provided UUID and invokes
     * the callback to handle the retrieved User object.
     *
     * @param uuid     The UUID of the user to find.
     * @param callback The callback to be invoked with the retrieved User object.
     */
    public void readData(String uuid, FirebaseFindUserCallback callback) {
        attendeeDB.findUser(uuid, new FirebaseFindUserCallback() {
            @Override
            public void onCallback(User user) {
                callback.onCallback(user);
            }
        });
    }

    /**
     * Checks the validity of a user based on the provided UUID and invokes
     * the callback to handle the validation result.
     *
     * @param uuid     The UUID of the user to check.
     * @param callback The callback to be invoked with the validation result (true if user exists, false otherwise).
     */
    public void userCheck(String uuid, SignInUserCallback callback) {
        attendeeDB.isValidUser(uuid, new SignInUserCallback() {
            @Override
            public void onCallback(boolean exists) {
                callback.onCallback(exists);
            }

        });
    }
}
