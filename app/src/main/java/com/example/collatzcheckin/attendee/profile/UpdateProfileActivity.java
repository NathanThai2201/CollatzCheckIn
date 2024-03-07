package com.example.collatzcheckin.attendee.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.collatzcheckin.MainActivity;
import com.example.collatzcheckin.R;
import com.example.collatzcheckin.attendee.AttendeeDB;
import com.example.collatzcheckin.attendee.User;
import com.example.collatzcheckin.authentication.AnonAuthentication;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;

/**
 * UpdateProfileActivity of the application, handles creating a new user profile
 */
public class UpdateProfileActivity extends AppCompatActivity {

    private Button doneButton;
    private EditText userName;
    private EditText userEmail;
    private String userUuid;
    private final AnonAuthentication authentication = new AnonAuthentication();
    private final AttendeeDB attendeeDB = new AttendeeDB();
    private User user;
    private boolean isVaild = true;

    /**
     * Method to run on creation of the activity. Handles user profile creation
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        doneButton = findViewById(R.id.done_button);
        userName = findViewById(R.id.username);
        userEmail = findViewById(R.id.email);
        userUuid = authentication.identifyUser();

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameEdit = userName.getText().toString();
                String emailEdit = userEmail.getText().toString();

                //simpleerror checking
                if(nameEdit.length() < 1) {
                    userName.setError("Please enter your name.");
                    isVaild = false;
                } else {
                    userName.setError(null);
                }

                if(emailEdit.length() < 1) {
                    userEmail.setError("Please enter your email.");
                    isVaild = false;
                } else {
                    userEmail.setError(null);
                }

                if(isVaild) {
                    user = new User(userUuid, nameEdit, emailEdit);
                    attendeeDB.addUser(user);
                    finish();
                }

            }
        });
    }

}