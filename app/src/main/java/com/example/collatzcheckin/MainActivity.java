package com.example.collatzcheckin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private Button viewAttendeeButton;
    private ArrayList<String> data;
    EventDB db = new EventDB();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_view_organizer);
        showEventList();

        ArrayAdapter<Event> eventArrayAdapter = new EventArrayAdapter(this, new ArrayList<Event>());

//        Event e = new Event("Main", new Organizer("Michael Carey"), Calendar.getInstance().getTime().toString(),"Test event", "URL", 22);
//        EventDB eventDB = new EventDB();
//        eventDB.addEvent(e);


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

}