package com.example.collatzcheckin.attendee.profile;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.collatzcheckin.MainActivity;
import com.example.collatzcheckin.R;
import com.example.collatzcheckin.attendee.AttendeeDB;
import com.example.collatzcheckin.attendee.User;
import com.example.collatzcheckin.authentication.AnonAuthentication;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment{

    TextView emailText;
    User user;
    TextView nameText;
    Button updateProfile;
    private String uuid;
    private final AnonAuthentication authentication = new AnonAuthentication();
    private final AttendeeDB attendeeDB = new AttendeeDB();
    HashMap<String, String> userData = new HashMap<>();

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        CountDownLatch latch = new CountDownLatch(1);
        nameText = rootView.findViewById(R.id.username_text);
        emailText = rootView.findViewById(R.id.email_text);

        updateProfile = rootView.findViewById(R.id.updateButton);

        Boolean isCurrentUser = authentication.validateUser();


        if (isCurrentUser) {
            uuid = authentication.identifyUser();


            getUser(uuid);// Perform asynchronous task


            Log.d(TAG, "Drkflm e: " + userData.get("Email"));

        }


        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateProfileFragment.newInstance().show(getFragmentManager(), "Modify profile");
            }
        });

        return rootView;
    }


        private void getUser(String uuid) {
            CollectionReference ref = attendeeDB.getUserRef();
            DocumentReference docRef = ref.document(uuid);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            Log.d(TAG, "DocumentSnapshot data: " + document.getString("Name"));
                            userData.put("Name", document.getString("Name"));
                            userData.put("Email", document.getString("Email"));
                            userData.put("Uid", document.getString("Uid"));
                            nameText.setText(document.getString("Name"));
                            emailText.setText(document.getString("Email"));

                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });
    }


}