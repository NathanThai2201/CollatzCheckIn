package com.example.collatzcheckin;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This is a fragment class for viewing the event detail page as a user
 */
public class EventViewFragment extends Fragment {
    private EventDB db ;
    private View view;
    private Event event;

    Button backButton;
    TextView eventTitle;
    TextView eventDescription;
    TextView eventLocation;
    TextView eventMonth;
    TextView eventDay;
    TextView eventTime;
    public EventViewFragment(Event event) {
        this.event = event;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        //Parse date
        String[] words = event.getEventDate().split(" ");

        view = inflater.inflate(R.layout.fragment_event_view, container, false);

        Button viewAttendeeButton = view.findViewById(R.id.view_attendee);

        //Setting all the textviews for the given event
        backButton = view.findViewById(R.id.back_button_event_view);
        eventTitle = view.findViewById(R.id.event_title);
        eventTitle.setText(event.getEventTitle());

        eventDescription = view.findViewById(R.id.event_description);
        eventDescription.setText(event.getEventDescription());

        eventLocation = view.findViewById(R.id.event_location);
        eventLocation.setText(event.getEventLocation());
        eventMonth = view.findViewById(R.id.event_month);
        eventMonth.setText(words[0]);

        eventDay = view.findViewById(R.id.event_day);
        eventDay.setText(words[1]);

        eventTime = view.findViewById(R.id.event_time);
        eventTime.setText(words[words.length - 1]);

        //Switch to attendee list
        viewAttendeeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).showAttendeeList(event);
            }
        });

        //Switch back to event list
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).showEventList();
            }
        });
        return view;

    }

}