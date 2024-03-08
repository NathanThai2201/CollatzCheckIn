package com.example.collatzcheckin;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import org.checkerframework.common.returnsreceiver.qual.This;

import java.util.HashMap;
import java.util.Map;
/**
 * This class is used to add events to the event database
 * in Firebase
 */
public class EventDB {
    private EventDBConnector eventDB;
    public CollectionReference eventRef;
    /**
     * This is a constructor for the eventDB
     * */
    public EventDB() {
        this.eventDB = new EventDBConnector();
        this.eventRef = eventDB.db.collection("events");
    }
    /**
    * This class adds an event to Firestore database
    * @param event
    * This is an event to add
    */
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
