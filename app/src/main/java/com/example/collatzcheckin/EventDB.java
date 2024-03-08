package com.example.collatzcheckin;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class EventDB {
    private EventDBConnector eventDB;
    public CollectionReference eventRef;

    public EventDB() {
        this.eventDB = new EventDBConnector();
        this.eventRef = eventDB.db.collection("events");
    }

    public void addEvent(Event event) {
        HashMap<String, String> eventData = new HashMap<>();
        eventData.put("Event Organizer", event.getEventOrganizer());
        eventData.put("Event Date", event.getEventDate());
        eventData.put("Event Description", event.getEventDescription());
        eventData.put("Event Poster", event.getEventPoster());
        eventData.put("Event Location", event.getEventLocation());
        eventData.put("Member Limit", Integer.toString(event.getMemberLimit()));
        eventRef.document(event.getEventTitle()).set(eventData);

    }
}
