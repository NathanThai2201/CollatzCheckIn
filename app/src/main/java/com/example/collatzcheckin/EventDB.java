package com.example.collatzcheckin;

import com.google.firebase.firestore.CollectionReference;

import java.util.HashMap;

public class EventDB {
    private EventDBConnector eventDB;
    private CollectionReference eventRef;

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
        eventData.put("Member Limit", Integer.toString(event.getMemberLimit()));
        eventRef.document(event.getEventTitle()).set(eventData);

    }
}
