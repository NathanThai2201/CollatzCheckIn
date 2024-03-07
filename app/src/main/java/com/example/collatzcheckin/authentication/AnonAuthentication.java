package com.example.collatzcheckin.authentication;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;


import static com.google.android.material.internal.ContextUtils.getActivity;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.collatzcheckin.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.Executor;

/**
 * AnonAuthentication handles user authentication
 */
public class AnonAuthentication {
    private final FirebaseAuth mAuth;
    Context context;

    /**
     * This constructs an instance of the Firebase Authenticator
     */
    public AnonAuthentication() {
        mAuth = FirebaseAuth.getInstance();
    }

    /**
     * This checks if the user has previously signed in or not
     * @return boolean value, where 'true' represents that there is an account and 'false' is not
     */
    public boolean validateUser() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {return false;}
        return true;
    }

    /**
     * Gets the FirebaseUser object which has user information stored
     * @return FirebaseUser object
     */
    public FirebaseUser getUser() {
        return mAuth.getCurrentUser();
    }

    public String identifyUser() {
        if (validateUser()) {
            return getUser().getUid();
        } else {
            return null;
        }
    }

    /**
     * Checks if it's the user first time and they need to create a profile
     * @param context The activiity the authetication is being performed on
     * @return boolean value, where true is they need to create a profile
     */
    public boolean updateUI(Context context) {
        this.context = context;
        final Boolean[] createProfile = {false};

        if (getUser() == null) {
            mAuth.signInAnonymously()
                    .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInAnonymously:success");
                                Boolean create = true;
                                createProfile[0] = true;
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInAnonymously:failure", task.getException());
                                Toast.makeText(context, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                updateUI(context);
                                createProfile[0] = false;
                            }
                        }
                    });
        }
        return createProfile[0];
    }


}
