package com.example.collatzcheckin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_view_organizer);


        Button view_attendee = findViewById(R.id.view_attendee);

        view_attendee.setOnClickListener(v -> {
            setContentView(R.layout.attendee_list);
        });

    }
}