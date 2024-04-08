package com.example.collatzcheckin.admin.controls.profile;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.collatzcheckin.AdminMainActivity;
import com.example.collatzcheckin.MainActivity;
import com.example.collatzcheckin.R;
import com.example.collatzcheckin.admin.controls.AdministratorDB;
import com.example.collatzcheckin.admin.controls.events.AdminEventListFragment;
import com.example.collatzcheckin.attendee.AttendeeDB;
import com.example.collatzcheckin.attendee.User;
import com.example.collatzcheckin.authentication.AnonAuthentication;
import com.example.collatzcheckin.utils.PhotoUploader;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

/**
 * UserViewFragment displays the details of a specific user profile for administrators
 */
public class UserViewFragment extends Fragment implements AdministratorDB.RemoveCallback {
    private User user;
    private View view;
    private AdminMainActivity mActivity;

    Button backButton;
    Button deleteButton;
    Button deleteImageButton;

    ShapeableImageView pfp;
    PhotoUploader photoUploader = new PhotoUploader();
    Uri imagePath;
    TextView name, username, email;
    Switch geo, notif;
    AttendeeDB attendeeDB = new AttendeeDB();


    /**
     * Constructor for UserViewFragment
     *
     * @param user The User object whose details are to be displayed
     */
    public UserViewFragment(User user) {
        this.user = user;
    }

    /**
     * Called when the fragment is associated with an activity
     * This method is called once the fragment is associated with its activity
     * It verifies if the activity is an instance of AdminMainActivity and assigns it to mActivity
     *
     * @param context The context to attach to
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AdminMainActivity) {
            mActivity = (AdminMainActivity) context;
        }
    }

    /**
     * Called when the fragment is no longer associated with its activity
     * This method is called when the fragment is detached from its activity
     * It releases the reference to the activity to avoid potential memory leaks
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mActivity = null; // Release reference to activity
    }

    /**
     * Called to create the user interface view
     * This method inflates the layout for the fragment's UI and initializes UI elements
     * It also populates UI with user details, sets up listeners for buttons,
     * and handles button click events
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any views in the fragment
     * @param container          If non-null, this is the parent view that the fragment's UI should be attached to
     *                           The fragment should not add the view itself, but this can be used to generate the LayoutParams of the view
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here
     * @return The root view of the fragment layout
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_admin_user_view, container, false);


        pfp = view.findViewById(R.id.editpfp);
        Intent intent = requireActivity().getIntent();
        name = view.findViewById(R.id.editName);
        email = view.findViewById(R.id.editEmail);
        geo = (Switch) view.findViewById(R.id.enablegeo);
        notif = (Switch) view.findViewById(R.id.enablenotif);
        Log.d("AdminUserView", user.getPfp());
        if (user != null) {
            if (user.getName() != null && !user.getName().isEmpty()) {
                name.setText(user.getName());
            }
            if (user.getEmail() != null && !user.getEmail().isEmpty()) {
                email.setText(user.getEmail());
            }
            setPfp(user);
            geo.setChecked(user.isGeolocation());
            notif.setChecked(user.isNotifications());
        }


        deleteButton = view.findViewById(R.id.delete_user);

        deleteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                AdministratorDB administratorDB = new AdministratorDB();

                // Call the removeProfile method passing event as parameter
                administratorDB.removeProfile(user, UserViewFragment.this);
                UserListFragment userListFragment = new UserListFragment();
                ((AdminMainActivity) requireActivity()).replaceFragment(userListFragment);
            }
        });
        deleteImageButton = view.findViewById(R.id.remove_image);
        deleteImageButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                StorageReference imageRef = FirebaseStorage.getInstance().getReference().child("images/" + user.getUid());
                imageRef.delete();
                user.setPfp(user.getGenpfp());
                setPfp(user);
                attendeeDB.addUser(user);
            }
        });
        backButton = view.findViewById(R.id.back_button_user_view);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to the user list
                UserListFragment userListFragment = new UserListFragment();
                ((AdminMainActivity) requireActivity()).replaceFragment(userListFragment);
            }
        });


        return view;
    }
    /**
     * Callback method called when user profile is successfully removed
     */
    @Override
    public void onRemoved() {
        // Handle event removal success
        // For example, navigate back to the event list fragment
        if (getActivity() != null) {
            Intent intent = new Intent(getActivity(), AdminMainActivity.class);
            startActivity(intent);
        }
    }

    /**
     * Callback method called when user profile removal fails
     *
     * @param errorMessage The error message describing the cause of failure
     */
    @Override
    public void onRemoveFailure(String errorMessage) {
        // Handle event removal failure
        // For example, show an error message to the user
        Toast.makeText(requireContext(), "Failed to delete profile: " + errorMessage, Toast.LENGTH_SHORT).show();
    }

    /**
     * Set Profile picture
     *
     * @param user           selected User
     */
    public void setPfp(User user) {
        Glide.with(this).load(user.getPfp()).into(pfp);
    }

}
