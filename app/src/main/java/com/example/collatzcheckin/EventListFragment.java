package com.example.collatzcheckin;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class EventListFragment extends Fragment {
    ListView eventList;
    ArrayAdapter<Event> eventArrayAdapter;
    ArrayList<Event> eventDataList;
    private View view;
    EventDB db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_event_list, container, false);
        db = new EventDB();

        eventList = view.findViewById(R.id.event_list_view);
        eventDataList = new ArrayList<>();
        eventArrayAdapter = new EventArrayAdapter(getContext(), eventDataList);
        eventList.setAdapter(eventArrayAdapter);

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
                        String eventTitle = doc.getId();
                        String eventOrganizer = doc.getString("Event Organizer");
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
                ((MainActivity)getActivity()).showEventView(event);
                // assuming string and if you want to get the value on click of list item
                // do what you intend to do on click of listview row
            }
        });








        return view;
    }
}
