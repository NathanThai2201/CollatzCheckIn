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

    public HashMap<String, String> getEvent(String eventName) {
        HashMap<String, String> userData = new HashMap<>();
        eventRef.document(eventName).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                Log.d("TAG", task.toString());
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("TAG", "DocumentSnapshot data: " + document.getData());
                        Log.d("TAG", "DocumentSnapshot data: " + document.getString("Name"));
                        userData.put("Name", document.getString("Name"));
                        userData.put("Email", document.getString("Email"));
                        userData.put("Uid", document.getString("Uid"));

                    } else {
                        Log.d("TAG", "No such document");
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });

        return userData;
    }
}
