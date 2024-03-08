package com.example.collatzcheckin;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.collatzcheckin.authentication.AnonAuthentication;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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

public class EventList extends Fragment {
    ListView eventList;
    ArrayAdapter<Event> eventArrayAdapter;
    ArrayList<Event> eventDataList;
    private final AnonAuthentication authentication = new AnonAuthentication();


    View view;
    EventDB db;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.event_view_organizer, container, false);

        db = new EventDB();
        eventList = view.findViewById(R.id.event_list_view);
        eventDataList = new ArrayList<>();
        eventArrayAdapter = new EventArrayAdapter(getContext(), eventDataList);
        eventList.setAdapter(eventArrayAdapter);
        String uuid = authentication.identifyUser();
        User user = getUser(uuid);
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
                        if (!user.getUid().equals(doc.getString("Event Organizer"))) {
                            continue;
                        }
                        String eventTitle = doc.getId();
                        String eventOrganizer = (doc.getString("Event Organizer"));
                        String eventDate = doc.getString("Event Date");
                        String eventDescription = doc.getString("Event Description");
                        String eventPoster = doc.getString("Event Poster");
                        String eventLocation = doc.getString("Event Location");
                        String memberLimit = doc.getString("Member Limit");

                        eventDataList.add(new Event(eventTitle, eventOrganizer, eventDate, eventDescription, eventPoster, eventLocation, Integer.parseInt(memberLimit)));
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
//                change(event);
            }
        });


        Button createEventButton = view.findViewById(R.id.create_event);
        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeCreateEvent(user);
            }
        });
        return view;
    }

//    public void change(Event event) {
//        Intent myIntent = new Intent(this, EventView.class);
//        myIntent.putExtra("event", event);
//        startActivity(myIntent);
//    }

    public void changeCreateEvent(User user) {
        Intent myIntent = new Intent(getContext(), CreateEvent.class);
        myIntent.putExtra("user", user);
        startActivity(myIntent);
    }


    public User getUser(String uuid) {
        AttendeeDB db = new AttendeeDB();

        HashMap<String,String> userData= db.findUser(uuid);
        return new User(userData.get("Uid"), userData.get("Name"), userData.get("Email"));
    }

}