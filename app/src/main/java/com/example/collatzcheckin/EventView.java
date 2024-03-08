package com.example.collatzcheckin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

public class EventView extends AppCompatActivity {

    TextView eventTitle;
    TextView eventMonth;
    TextView eventDay;
    TextView eventTime;
    TextView eventDescription;
    TextView eventLocation;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_view);
        Intent intent = getIntent();
        Event event = (Event) intent.getSerializableExtra("event");
        String[] parsedData = event.getEventDate().split(" ");
        eventTitle = findViewById(R.id.event_name);
        eventTitle.setText(event.getEventTitle());

        eventMonth = findViewById(R.id.event_month);
        eventMonth.setText(parsedData[0]);

        eventDay = findViewById(R.id.event_day);
        eventDay.setText(parsedData[1]);

        eventTime = findViewById(R.id.event_time);
        eventTime.setText(parsedData[parsedData.length - 1]);


        eventDescription = findViewById(R.id.event_description);
        eventDescription.setText(event.getEventDescription());



        eventLocation = findViewById(R.id.event_location);
        eventLocation.setText(event.getEventLocation());

        Button backButton =  findViewById(R.id.event_view_back_button);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button viewAttendees = findViewById(R.id.view_attendee);
        viewAttendees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewAttendeesList();
            }
        });







    }

    public void viewAttendeesList() {
        Intent intent = new Intent(this, AttendeeList.class);
        startActivity(intent);
    }
}