package com.example.collatzcheckin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.collatzcheckin.attendee.User;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class EventList extends AppCompatActivity {
    ListView eventList;
    ArrayAdapter<Event> eventArrayAdapter;
    ArrayList<Event> eventDataList;
    View view;
    EventDB db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_view_organizer);
        Intent intent = getIntent();
        User user = (User) intent.getSerializableExtra("user");

        db = new EventDB();
        eventList = findViewById(R.id.event_list_view);
        eventDataList = new ArrayList<>();
        eventArrayAdapter = new EventArrayAdapter(this, eventDataList);
        eventList.setAdapter(eventArrayAdapter);

        db.eventRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot querySnapshots, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("Firestore", error.toString());
                    return;
                }
                if (querySnapshots != null) {
                    eventDataList.clear();

                    for (QueryDocumentSnapshot doc : querySnapshots) {
                        String eventOrganizer = doc.getString("Event Organizer");
                        if (user.getUid() != null && eventOrganizer != null && !user.getUid().equals(eventOrganizer)) {
                            continue;
                        }
                        String eventTitle = doc.getId();
                        String eventDate = doc.getString("Event Date");
                        String eventDescription = doc.getString("Event Description");
                        String eventPoster = doc.getString("Event Poster");
                        String eventLocation = doc.getString("Event Location");
                        String memberLimit = doc.getString("Member Limit");
                        int parsedMemberLimit = 0; // Default value, you can change it based on your requirements

                        if (memberLimit != null && !memberLimit.isEmpty()) {
                            parsedMemberLimit = Integer.parseInt(memberLimit);
                        }
                        eventDataList.add(new Event(eventTitle, eventOrganizer, eventDate, eventDescription, eventPoster, eventLocation, parsedMemberLimit));
                    }

                    eventArrayAdapter.notifyDataSetChanged();
                }
            }
        });
        eventList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3)
            {

                Event event = (Event)adapter.getItemAtPosition(position);
                change(event);
            }
        });


        Button createEventButton = findViewById(R.id.create_event);
        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeCreateEvent(user);
            }
        });
        Button backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Log.d("yes","no");
               finish();
            }
        });
    }

    public void change(Event event) {
        Intent myIntent = new Intent(this, EventView.class);
        myIntent.putExtra("event", event);
        startActivity(myIntent);
    }

    public void changeCreateEvent(User user) {
        Intent myIntent = new Intent(this, CreateEvent.class);
        myIntent.putExtra("user", user);
        startActivity(myIntent);
    }

//    public void showAttendeeList(Event e) {
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.event_frame_view, new AttendeeListFragment(e))
//                .addToBackStack(null)
//                .commit();
//    }
//    public void showEventList() {
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.event_frame_view, new EventListFragment())
//                .addToBackStack(null)
//                .commit();
//    }
//
//    public void showEventView(Event e) {
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.event_frame_view, new EventViewFragment(e))
//                .addToBackStack(null)
//                .commit();
//    }
//
//    public void showCreateEvent() {
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.event_frame_view, new AddEventFragment())
//                .addToBackStack(null)
//                .commit();
//    }

}