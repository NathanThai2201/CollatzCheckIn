package com.example.collatzcheckin;

import androidx.appcompat.app.AppCompatActivity;

import com.example.collatzcheckin.attendee.User;
import com.example.collatzcheckin.attendee.AttendeeDB;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CreateEvent extends AppCompatActivity {
    View view;
    TextView eventTitle;
    TextView eventLocation;
    TextView eventDate;
    TextView eventDescription;
    TextView eventLimit;
    //User user;
    /**
     * Method to run on creation of the activity. Handles create event
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        Intent intent = getIntent();
        com.example.collatzcheckin.attendee.User user = (User) intent.getSerializableExtra("user");

        eventTitle = findViewById(R.id.edit_event_name);
        eventDate = findViewById(R.id.edit_event_date);
        eventLocation = findViewById(R.id.edit_event_location);
        eventDescription = findViewById(R.id.edit_event_description);
        eventLimit = findViewById(R.id.edit_event_limit);



        Button backButton = findViewById(R.id.back_button_create_event);


        Button addEventButton = findViewById(R.id.add_event);

        addEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get values
                String title = eventTitle.getText().toString();
                String date = eventDate.getText().toString();
                String location = eventLocation.getText().toString();
                String description = eventDescription.getText().toString();

                //Update user and event db
                EventDB db = new EventDB();
                AttendeeDB userDb = new AttendeeDB();
                Event event = new Event(title, user.getUid(), date, description, "URL", location, 333);
                db.addEvent(event);
                user.addOrganizingEvent(event);
                userDb.addUser(user);
                finish();
            }
        });

        //Return without creating an event
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}