package com.example.collatzcheckin;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

public class AttendeeListFragment extends Fragment {

    View view;
    Event event;
    public AttendeeListFragment(Event event) {
        this.event = event;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_admin_attendee_list, container, false);

        ListView listView = view.findViewById(R.id.attendee_list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Handle click event here
                // For example, you can get the clicked item from the adapter
                Object item = parent.getItemAtPosition(position);
                // Do something with the clicked item
            }
        });

        Button backButton = view.findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).showEventView(event);
            }
        });

        return view;
    }
}