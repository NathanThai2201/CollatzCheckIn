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

/**
 * ProfileFragment displays user their profile information
 */
public class ProfileFragment extends Fragment {

    Button update;
    Button remove;
    User user;
    TextView name, username, email, geo, notification;
    ImageView pfp;
    private String uuid;
    private final AnonAuthentication authentication = new AnonAuthentication();
    private final AttendeeDB attendeeDB = new AttendeeDB();
    HashMap<String, String> userData = new HashMap<>();

    /**
     * This constructs an instance of ProfileFragment
     */
    public ProfileFragment(){
    }

    /**
     * Called to create the user profile fragment's Inflates the fragment layout from
     * the specified XML resource, populates the item list from the bundle arguments, and sets up UI
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any views in
     *                           the fragment.
     * @param container          If non-null, this is the parent view that the fragment's UI should be
     *                           attached to. The fragment should not add the view itself, but this can
     *                           be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous
     *                           saved state as given here.
     * @return The root view of the fragment's layout hierarchy.
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, container, false);

        update = view.findViewById(R.id.up_button);
        remove = view.findViewById(R.id.remove);
        name = view.findViewById(R.id.nameText);
        username = view.findViewById(R.id.usernameText);
        email = view.findViewById(R.id.emailText);
        pfp = view.findViewById(R.id.pfp);
        geo = view.findViewById(R.id.geotext);
        notification = view.findViewById(R.id.notiftext);

        uuid = authentication.identifyUser();
        getUser(uuid);

        //lauches a new activity and sends user data and recives updated info
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
                        if(updatedUser.isGeolocation()) {
                            geo.setText("enabled");
                        } else {
                            geo.setText("disabled");
                        }

                        if(updatedUser.isNotifications()) {
                            notification.setText("enabled");
                        } else {
                            notification.setText("disabled");
                        }
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
                boolean geo_value;
                boolean notif_value;
                String user_name = name.getText().toString();
                String user_username = username.getText().toString();
                String user_email = email.getText().toString();
                String geo_settings = geo.getText().toString();
                String notif_settings = notification.getText().toString();
                if(geo_settings == "enabled") {
                    geo_value = true;
                } else {
                    geo_value = false;
                }
                if(notif_settings == "enabled") {
                    notif_value = true;
                } else {
                    notif_value = false;
                }
                user = new User(user_name,user_username,user_email, uuid, geo_value, notif_value);
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

    /**
     * Query to extract user data
     * @param uuid The unique idenitfier assigned to the user using Firebase Authenticator
     */
    private void getUser(String uuid) {
        if (uuid == null) {
            // Handle the case where uuid is null (e.g., log an error, throw an exception, or return)
            Log.e(TAG, "UUID is null in getUser");
            return;
        }
        CollectionReference ref = attendeeDB.getUserRef();
        DocumentReference docRef = ref.document(uuid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        name.setText(document.getString("Name"));
                        email.setText(document.getString("Email"));
                        notification.setText(document.getString("Notif"));
                        geo.setText(document.getString("Geo"));

                        if(document.getString("Geo") == "true") {
                            geo.setText("enabled");
                            Log.d(TAG, " such document" + document.getString("Geo"));
                        } else {
                            geo.setText("disabled");
                            Log.d(TAG, " such document" + document.getString("Geo"));
                        }

                        if(document.getString("Notif") == "true") {
                            notification.setText("enabled");
                        } else {
                            notification.setText("disabled");
                        }

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
