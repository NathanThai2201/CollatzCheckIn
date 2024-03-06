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

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.HashMap;


public class EventViewFragment extends Fragment {
    private EventDB db ;
    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_event_view, container, false);
        Button viewAttendeeButton = view.findViewById(R.id.view_attendee);
        ArrayList<String> data = fetchEvent();
        viewAttendeeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).showAttendeeList();
            }
        });
        return view;

    }

    public ArrayList<String> fetchEvent() {
        db = new EventDB();
        HashMap<String, String> fetchData = db.getEvent("Main");
        ArrayList<String> eventData = new ArrayList<>();
        eventData.add(fetchData.get("eventTitle"));
        eventData.add(fetchData.get("eventDescription"));
        String date = fetchData.get("eventDate");
        String[] parsedDate = date.split(" ");
        eventData.add(parsedDate[1]);
        eventData.add(parsedDate[2]);
        eventData.add(parsedDate[parsedDate.length - 1]);

        return eventData;
    }
}