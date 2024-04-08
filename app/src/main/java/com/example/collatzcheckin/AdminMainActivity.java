package com.example.collatzcheckin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;


import java.util.ArrayList;

import com.example.collatzcheckin.admin.controls.AdminBrowseFragment;
import com.example.collatzcheckin.admin.controls.AdminProfileFragment;
import com.example.collatzcheckin.admin.controls.events.AdminEventListFragment;
import com.example.collatzcheckin.admin.controls.events.AdminEventViewFragment;
import com.example.collatzcheckin.admin.controls.profile.UserListFragment;
import com.example.collatzcheckin.admin.controls.profile.UserViewFragment;
import com.example.collatzcheckin.attendee.User;
import com.example.collatzcheckin.attendee.profile.ProfileFragment;
import com.example.collatzcheckin.authentication.AnonAuthentication;
import com.example.collatzcheckin.event.CameraFragment;
import com.example.collatzcheckin.event.EventListFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.example.collatzcheckin.event.EditEventFragment;
import com.example.collatzcheckin.event.Event;
import com.example.collatzcheckin.event.EventDB;

/**
 * AdminMainActivity of the application, handles setting up the bottom nav fragment for the admin
 */
public class AdminMainActivity extends AppCompatActivity {

    private final AnonAuthentication authentication = new AnonAuthentication();
    private User user;
    private Button viewAttendeeButton;
    private ArrayList<String> data;
    EventDB db = new EventDB();

    /**
     * Method to run on creation of the activity. Handles user authentication and creates the bottomnav fragment
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        replaceFragment(new EventListFragment());
        // creating the nav bar
        // adds functionality to allow attendee to navigate
        bottomNavigationView.setOnItemSelectedListener(item -> {
            // show home page
            replaceFragment(new EventListFragment());
            int iconPressed= item.getItemId();
            // navigate to profile page
            if (iconPressed == R.id.profile) {
                replaceFragment(new AdminProfileFragment());
            }
            // navigate to page to browse events
            if (iconPressed == R.id.search) {
                replaceFragment(new AdminBrowseFragment());
            }
            if (iconPressed == R.id.scanner) {
                replaceFragment(new CameraFragment());
            }
            if (iconPressed == R.id.home) {
                replaceFragment(new EventListFragment());
            }
            return true;
        });

    }

    /**
     * Switches fragments to correponding button on nav bar
     *
     * @param fragment Fragment to switch to
     */
    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

}