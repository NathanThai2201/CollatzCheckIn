package com.example.collatzcheckin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_view_organizer);

        Event e = new Event("Main", new Organizer("Michael Carey"), Calendar.getInstance().getTime().toString(),"Test event", "URL", 22);
        EventDB eventDB = new EventDB();
        eventDB.addEvent(e);
        Button view_attendee = findViewById(R.id.view_attendee);

        view_attendee.setOnClickListener(v -> {
            setContentView(R.layout.attendee_list);
        });

    }
}