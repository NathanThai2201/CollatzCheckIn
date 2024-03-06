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
 * A simple {@link Fragment} subclass.
 * Use the {@link UpdateProfileFragment#newInstance} factory method to
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
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment UpdateProfileFragment.
     */
    public static UpdateProfileFragment newInstance(User user) {
        UpdateProfileFragment fragment = new UpdateProfileFragment();
        Bundle args = new Bundle();
        args.putSerializable("user", user);
        fragment.setArguments(args);
        return fragment;
    }



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