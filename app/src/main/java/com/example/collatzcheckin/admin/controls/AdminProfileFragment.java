package com.example.collatzcheckin.admin.controls;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.collatzcheckin.AdminMainActivity;
import com.example.collatzcheckin.R;
import com.example.collatzcheckin.attendee.AttendeeDB;
import com.example.collatzcheckin.attendee.AttendeeCallbackManager;
import com.example.collatzcheckin.attendee.User;
import com.example.collatzcheckin.attendee.profile.EditProfileActivity;
import com.example.collatzcheckin.authentication.AnonAuthentication;
import com.example.collatzcheckin.utils.FirebaseFindUserCallback;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.Serializable;

/**
 * ProfileFragment displays user their profile information
 */
public class AdminProfileFragment extends Fragment implements FirebaseFindUserCallback {

    Button update, remove, admin;
    User user;
    TextView name, email, geo, notification;
    ImageView pfp;
    private String uuid;
    private final AnonAuthentication authentication = new AnonAuthentication();
    private final AttendeeDB attendeeDB = new AttendeeDB();
    AttendeeCallbackManager attendeeFirebaseManager = new AttendeeCallbackManager();


    /**
     * This constructs an instance of AdminProfileFragment
     */
    public AdminProfileFragment(){
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

        View view = inflater.inflate(R.layout.admin_profile_fragment, container, false);
        initViews(view);
        uuid = authentication.identifyUser();
        // Call readData method from FirebaseManager class
        attendeeFirebaseManager.readData(uuid, this);


        //lauches a new activity and sends user data and recives updated info
        ActivityResultLauncher<Intent> launchEditProfileActivity = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {

                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        assert data != null;
                        user = (User) data.getSerializableExtra("updatedUser");
                        setData(user);
                    }
                }
        );

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                intent.putExtra("user", (Serializable) user);
                launchEditProfileActivity.launch(intent);

            }
        });

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StorageReference imageRef = FirebaseStorage.getInstance().getReference().child("images/" + uuid);
                imageRef.delete();
                user.setPfp(user.getGenpfp());
                setPfp(user);
                attendeeDB.addUser(user);
            }
        });
        return view;
    }

    /**
     * Callback method invoked when user data is retrieved from Firebase
     * Handles the retrieved user data by setting it in the UI
     *
     * @param user The User object containing the retrieved user data
     */
    @Override
    public void onCallback(User user) {
        // Handle the retrieved user data
        this.user = user;
        setData(user);
    }

    /**
     * Initializes the views used in the user interface
     *
     * @param view The root view of the fragment layout
     */
    private void initViews(View view) {
        update = view.findViewById(R.id.up_button);
        admin = view.findViewById(R.id.admin_button);
        remove = view.findViewById(R.id.remove);
        name = view.findViewById(R.id.nameText);
        email = view.findViewById(R.id.emailText);
        pfp = view.findViewById(R.id.pfp);
        geo = view.findViewById(R.id.geotext);
        notification = view.findViewById(R.id.notiftext);
    }

    /**
     * Sets data for the user interface based on the given User object
     *
     * @param user The User object whose data will be displayed
     */
    public void setData(User user) {
        name.setText(user.getName());
        email.setText(user.getEmail());
        if(user.getGeolocation()) {
            geo.setText("enabled");
        } else {
            geo.setText("disabled");
        }
        if(user.getNotifications()) {
            notification.setText("enabled");
        } else {
            notification.setText("disabled");
        }
        setPfp(user);
    }

    /**
     * Sets the profile picture for the user interface based on the given User object
     *
     * @param user The User object whose profile picture will be displayed
     */
    public void setPfp(User user) {
        Glide.with(this).load(user.getPfp()).into(pfp);
    }
}
