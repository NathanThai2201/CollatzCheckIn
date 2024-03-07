package com.example.collatzcheckin;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
/**
 * This is a fragment class for viewing the attendee list for a given event
 */
public class AttendeeListFragment extends Fragment {

    View view;
    Event event;
    public AttendeeListFragment(Event event) {
        this.event = event;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_attendee_list, container, false);
        Button backButton = view.findViewById(R.id.back_button);

        //Switch back to event detail page
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).showEventView(event);
            }
        });
        return view;
    }
}