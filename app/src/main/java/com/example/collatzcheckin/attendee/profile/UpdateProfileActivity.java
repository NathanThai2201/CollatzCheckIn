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
import com.example.collatzcheckin.attendee.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;

public class UpdateProfileActivity extends AppCompatActivity {

    private Button doneButton;
    private Button cancelButton;
    private EditText userName;
    private EditText userEmail;
    private String create_profile = "create_profile";
    private String userUuid;
    private FirebaseFirestore db;
    private CollectionReference usersRef;
    private User user;
    private FirebaseAuth mAuth;

    //if true then activity is for creating profile, if false it is for updating
    private boolean create;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        db = FirebaseFirestore.getInstance();
        usersRef = db.collection("users");

        doneButton = findViewById(R.id.done_button);
        cancelButton = findViewById(R.id.cancel_button);
        userName = findViewById(R.id.username);
        userEmail = findViewById(R.id.email);

        Intent intent = getIntent();

        create = intent.getBooleanExtra(create_profile, false);

        userUuid = currentUser.getUid().toString();
//        getUser(userUuid, usersRef);

//        if (currentUser != null && !create) {
//            userUuid = currentUser.getUid().toString();
//            getUser(userUuid, usersRef);
//        }

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String nameEdit = userName.getText().toString();
//                String emailEdit = userEmail.getText().toString();
//
//                user = new User(userUuid, nameEdit, emailEdit);
//
//                addUser(user);

                finish();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


//    private void addUser(User userDetails) {
//        usersRef
//                .document(userDetails.getUid())
//                .set(userDetails)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Log.d("Firestore", "DocumentSnapshot successfully written!");
//                        return null;
//                    }
//                });
//    }

//    private void getUser(String uuid, CollectionReference ref) {
//        DocumentReference docRef = ref.document(uuid);
//        ref.document(uuid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                User user1 = documentSnapshot.toObject(User.class);
//                userName.setText(user1.getName());
//                userEmail.setText(user1.getEmail());
//                return user1;
//            }
//        });
//    }
}