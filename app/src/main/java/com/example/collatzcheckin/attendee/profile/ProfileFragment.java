package com.example.collatzcheckin.attendee.profile;


import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.collatzcheckin.R;
import com.example.collatzcheckin.attendee.AttendeeDB;
import com.example.collatzcheckin.attendee.User;
import com.example.collatzcheckin.authentication.AnonAuthentication;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.Serializable;
import java.util.HashMap;

public class ProfileFragment extends Fragment {

    Button update;
    Button remove;
    User user;
    TextView name, username, email;
    ImageView pfp;
    private String uuid;
    private final AnonAuthentication authentication = new AnonAuthentication();
    private final AttendeeDB attendeeDB = new AttendeeDB();
    HashMap<String, String> userData = new HashMap<>();

    public ProfileFragment(){
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, container, false);

        update = view.findViewById(R.id.up_button);
        remove = view.findViewById(R.id.remove);
        name = view.findViewById(R.id.nameText);
        username = view.findViewById(R.id.usernameText);
        email = view.findViewById(R.id.emailText);
        pfp = view.findViewById(R.id.pfp);

        uuid = authentication.identifyUser();
        getUser(uuid);


        ActivityResultLauncher<Intent> launchEditProfileActivity = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {

                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        assert data != null;
                        User updatedUser = (User) data.getSerializableExtra("updatedUser");
                        assert updatedUser != null;
                        name.setText(updatedUser.getName());
                        username.setText(updatedUser.getUsername());
                        email.setText(updatedUser.getEmail());
                        if (updatedUser.getPfp() != null) {
                            pfp.setImageURI(Uri.parse(updatedUser.getPfp()));
                        }
                    }
                }
        );



        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                String user_name = name.getText().toString();
                String user_username = username.getText().toString();
                String user_email = email.getText().toString();
                user = new User(user_name,user_username,user_email, uuid);
                intent.putExtra("user", (Serializable) user);
                launchEditProfileActivity.launch(intent);

            }
        });
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pfp.setImageResource(R.drawable.baseline_person_24);
                StorageReference imageRef = FirebaseStorage.getInstance().getReference().child("images/" + user.getUsername());
                imageRef.delete();

            }
        });
        return view;
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
                        userData.put("Uid", uuid);
                        user = new User(uuid, document.getString("Name"), document.getString("Uid"));
                        name.setText(document.getString("Name"));
                        email.setText(document.getString("Email"));

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
