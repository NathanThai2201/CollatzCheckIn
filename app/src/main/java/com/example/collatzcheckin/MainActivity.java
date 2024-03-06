package com.example.collatzcheckin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private Button viewAttendeeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_view_organizer);
        showEventView();


//        Event e = new Event("Main", new Organizer("Michael Carey"), Calendar.getInstance().getTime().toString(),"Test event", "URL", 22);
//        EventDB eventDB = new EventDB();
//        eventDB.addEvent(e);


    }

    public void showAttendeeList() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.event_frame_view, new AttendeeListFragment())
                .addToBackStack(null)
                .commit();
    }

    public void showEventView() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.event_frame_view, new EventViewFragment())
                .addToBackStack(null)
                .commit();
    }

}