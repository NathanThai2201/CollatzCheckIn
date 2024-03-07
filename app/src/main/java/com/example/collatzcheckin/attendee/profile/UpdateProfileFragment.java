package com.example.collatzcheckin.attendee.profile;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.collatzcheckin.R;
import com.example.collatzcheckin.attendee.AttendeeDB;
import com.example.collatzcheckin.attendee.User;

/**
 * A simple UpdateProfileFragment subclass.
 * Use the UpdateProfileFragment factory method to
 * create an instance of this fragment.
 */
public class UpdateProfileFragment extends DialogFragment {
    private static User user;
    EditText editName;
    EditText editEmail;
    Button updateButton;
    AttendeeDB attendeeDB = new AttendeeDB();

    public UpdateProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    /**
     * Create a new instance of this fragment using the provided parameters.
     * @return A new instance of fragment UpdateProfileFragment.
     */
    public static UpdateProfileFragment newInstance(User user) {
        UpdateProfileFragment fragment = new UpdateProfileFragment();
        Bundle args = new Bundle();
        args.putSerializable("user", user);
        fragment.setArguments(args);
        return fragment;
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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_update_profile, container, false);
        editName = rootView.findViewById(R.id.username_text);
        editEmail= rootView.findViewById(R.id.email_text);
        updateButton = rootView.findViewById(R.id.updateButton);

        if(getArguments() != null){
            user = (User) getArguments().getSerializable("user");
            editName.setText(user.getName());
            editEmail.setText(user.getEmail());
        }

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName = editName.getText().toString();
                String newEmail = editEmail.getText().toString();
                user.setName(newName);
                user.setEmail(newEmail);
                attendeeDB.addUser(user);

            }
        });
        return rootView;
    }
}