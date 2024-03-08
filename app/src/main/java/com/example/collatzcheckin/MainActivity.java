package com.example.collatzcheckin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import com.example.collatzcheckin.admin.controls.events.AdminEventListFragment;
import com.example.collatzcheckin.admin.controls.events.AdminEventViewFragment;
import com.example.collatzcheckin.admin.controls.profile.UserListFragment;
import com.example.collatzcheckin.admin.controls.profile.UserViewFragment;
import com.example.collatzcheckin.attendee.AttendeeDB;
import com.example.collatzcheckin.attendee.User;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Button viewAttendeeButton;
    private ArrayList<String> data;
    EventDB db = new EventDB();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_view_organizer);
        showAdminEventList();

        //setContentView(R.layout.user_view_organizer);
        //showUserList();

//        Event e = new Event("Oilers vs Flames", new Organizer("Rogers"), "Mar 20 2024 20:00","Edmonton Oilers vs Calgary Flames", "URL", "Edmonton, AB", 10000);

        Event e2 = new Event("Testing", new Organizer("To Delete"), "APR 05 2024 21:00","Test Data for deletion", "URL", "Testing", 40000);
//

        EventDB eventDB = new EventDB();
//        eventDB.addEvent(e);

        eventDB.addEvent(e2);

        //User u = new User("Test Deletion UID", "Delete Me", "Delete@deleteme.com");
        //AttendeeDB attendeeDB = new AttendeeDB();
        //attendeeDB.addUser(u);




    }

    public void showAttendeeList(Event e) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.event_frame_view, new AttendeeListFragment(e))
                .addToBackStack(null)
                .commit();
    }
    public void showEventList() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.event_frame_view, new EventListFragment())
                .addToBackStack(null)
                .commit();
    }

    public void showEventView(Event e) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.event_frame_view, new EventViewFragment(e))
                .addToBackStack(null)
                .commit();
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
}