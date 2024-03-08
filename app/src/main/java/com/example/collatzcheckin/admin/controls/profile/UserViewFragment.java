package com.example.collatzcheckin.admin.controls.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.collatzcheckin.MainActivity;
import com.example.collatzcheckin.R;
import com.example.collatzcheckin.admin.controls.AdministratorDB;
import com.example.collatzcheckin.attendee.User;

public class UserViewFragment extends Fragment {
    private User user;
    private View view;

    Button backButton;
    Button deleteButton;
    TextView userName;
    TextView userEmail;
    TextView userUid;

    public UserViewFragment(User user) {
        this.user = user;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_admin_user_view, container, false);

        backButton = view.findViewById(R.id.back_button_user_view);
        userName = view.findViewById(R.id.user_name);
        userName.setText(user.getName());

        userEmail = view.findViewById(R.id.user_email);
        userEmail.setText(user.getEmail());

        userUid = view.findViewById(R.id.user_uid);
        userUid.setText(user.getUid());

        deleteButton = view.findViewById(R.id.delete_user);

        deleteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                AdministratorDB administratorDB = new AdministratorDB();

                // Call the removeEvent method passing event as parameter
                administratorDB.removeProfile(user);
                ((MainActivity) getActivity()).showUserList();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to the user list
                ((MainActivity) getActivity()).showUserList();
            }
        });

        return view;
    }
}
