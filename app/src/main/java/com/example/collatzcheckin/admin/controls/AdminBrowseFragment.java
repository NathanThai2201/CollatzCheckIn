package com.example.collatzcheckin.admin.controls;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.collatzcheckin.AdminMainActivity;
import com.example.collatzcheckin.R;

import com.example.collatzcheckin.admin.controls.events.AdminEventListFragment;
import com.example.collatzcheckin.admin.controls.profile.UserListFragment;
import com.example.collatzcheckin.admin.controls.profile.UserViewFragment;
import com.example.collatzcheckin.attendee.events.BrowseEventsFragment;


/**
 * AdminBrowseFragment is a Fragment responsible for displaying buttons to navigate to different
 * administrative features
 */
public class AdminBrowseFragment extends Fragment {

    private View view;
    Button eventButton;
    Button profilesButton;
    Button searchButton;


    /**
     * Inflates the layout for this fragment, sets up UI components, and handles button clicks
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any views in
     *                           the fragment
     * @param container          If non-null, this is the parent view that the fragment's UI should be
     *                           attached to. The fragment should not add the view itself, but this can
     *                           be used to generate the LayoutParams of the view
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous
     *                           saved state as given here
     * @return The root view of the fragment's layout hierarchy
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.admin_browse, container, false);

        eventButton = view.findViewById(R.id.events_button);
        profilesButton = view.findViewById(R.id.profiles_button);
        searchButton = view.findViewById(R.id.signup_button);

        eventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdminEventListFragment adminEventListFragment = new AdminEventListFragment();
                ((AdminMainActivity) requireActivity()).replaceFragment(adminEventListFragment);
            }
        });
        profilesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserListFragment userListFragment = new UserListFragment();
                ((AdminMainActivity) requireActivity()).replaceFragment(userListFragment);
            }
        });
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BrowseEventsFragment browseEventsFragment = new BrowseEventsFragment();
                ((AdminMainActivity) requireActivity()).replaceFragment(browseEventsFragment);
            }
        });
        return view;
    }
}

