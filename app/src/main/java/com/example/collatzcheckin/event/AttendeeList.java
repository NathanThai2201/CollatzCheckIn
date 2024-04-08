package com.example.collatzcheckin.event;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.collatzcheckin.R;
import com.example.collatzcheckin.attendee.AttendeeDB;
import com.example.collatzcheckin.attendee.User;
import com.example.collatzcheckin.event.AttendeeArrayAdapter;
import com.example.collatzcheckin.event.Event;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AttendeeList extends AppCompatActivity {
    /**
     * Method to run on creation of the activity. Handles attendee list
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */

    ArrayList<HashMap<String,String>> attendees;
    AttendeeArrayAdapter attendeeAdapter;
    ListView attendeeList;

    /**
     * Method to run on creation of the activity. responsible for displaying all sign ups and check ins to the event
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendee_list);

        //Retrieve event info
        Intent intent = getIntent();
        Event event = (Event) intent.getSerializableExtra("Event");
        AttendeeDB attendeeDB = new AttendeeDB();
        String id = event.getEventID();

        //Initialize attendee array adapter
        attendees = new ArrayList<>();
        attendeeAdapter = new AttendeeArrayAdapter(this, attendees);
        attendeeList = findViewById(R.id.attendee_list);
        attendeeList.setAdapter(attendeeAdapter);

        List<String> keyList = new ArrayList<>(event.getAttendees().keySet());

        //Pull all attendees of the event and update array adapter/ list view
        attendeeDB.userRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot querySnapshots, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("Firestore", error.toString());
                    return;
                }
                if (querySnapshots != null) {
                    attendees.clear();
                    for (QueryDocumentSnapshot doc : querySnapshots) {
                        String id = doc.getId();
                        if (keyList.contains(id)) {
                            HashMap<String,String> tmp = new HashMap<>();
                            tmp.put(doc.getString("Name"), event.getAttendees().get(id));
                            attendees.add(tmp);
                        }
                    }
                    attendeeAdapter.notifyDataSetChanged();

                }
            }
        });


        //Change to map activity
        Button map = findViewById(R.id.map);
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewMap(event);
            }
        });




        //Switch back to event detail page
        Button backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    /**
     * Method to switch to map activity
     * @param event
     */
    public void viewMap(Event event) {
        Intent intent = new Intent(this, MapActivity.class);
        intent.putExtra("Event", event);
        startActivity(intent);
    }

}