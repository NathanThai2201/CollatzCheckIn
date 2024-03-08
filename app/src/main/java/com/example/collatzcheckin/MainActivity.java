package com.example.collatzcheckin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import java.net.Authenticator;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;

import com.example.collatzcheckin.admin.controls.events.AdminEventListFragment;
import com.example.collatzcheckin.admin.controls.events.AdminEventViewFragment;
import com.example.collatzcheckin.admin.controls.profile.UserListFragment;
import com.example.collatzcheckin.admin.controls.profile.UserViewFragment;
import com.example.collatzcheckin.attendee.User;
import com.example.collatzcheckin.attendee.profile.ProfileFragment;
import com.example.collatzcheckin.attendee.profile.UpdateProfileActivity;
import com.example.collatzcheckin.authentication.AnonAuthentication;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * MainActivity of the application, handles setting up the bottom nav fragment and the user
 */
public class MainActivity extends AppCompatActivity {

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

        if(authentication.updateUI(MainActivity.this)) {
            Intent i = new Intent(MainActivity.this, UpdateProfileActivity.class);
            startActivity(i);
        }
        String uuid = authentication.identifyUser();
        AttendeeDB db = new AttendeeDB();
        HashMap<String,String> userData = db.findUser(uuid);
        user = new User(userData.get("Uid"), userData.get("Name"), userData.get("Email"));

        EventDB eventDB = new EventDB();
        // creating the nav bar
        // adds functionality to allow attendee to navigate
        bottomNavigationView.setOnItemSelectedListener(item -> {

            int iconPressed= item.getItemId();

            // navigate to profile page
            if (iconPressed == R.id.profile) {
                replaceFragment(new ProfileFragment());
            }
            //TODO: navigate to home page (where users can browse events)
            if (iconPressed == R.id.home) {
                Intent i = new Intent(this,EventList.class);
                i.putExtra("user", user);
                startActivity(i);
            }
            //TODO: navigate to camera so users can scan QR code

            return true;
        });
    }

    /**
     * Switches fragments to correponding button on nav bar
     *
     * @param fragment Fragment to switch to
     */
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }




    public void showAdminEventList() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.event_frame_view, new AdminEventListFragment())
                .addToBackStack(null)
                .commit();
    }
    public void showAdminEventView(Event e) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.event_frame_view, new AdminEventViewFragment(e))
                .addToBackStack(null)
                .commit();
    }
    public void showUserList() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_user, new UserListFragment())
                .addToBackStack(null)
                .commit();
    }
    public void showAdminUserView(User user) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_user, new UserViewFragment(user))
                .addToBackStack(null)
                .commit();
    }
    public void showEditEvent(Event e) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.event_frame_view, new EditEventFragment(e))
                .addToBackStack(null)
                .commit();
    }

}