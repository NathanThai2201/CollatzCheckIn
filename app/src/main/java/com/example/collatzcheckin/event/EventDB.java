package com.example.collatzcheckin.event;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.collatzcheckin.attendee.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import static com.google.android.gms.common.util.CollectionUtils.mapOf;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import org.checkerframework.common.returnsreceiver.qual.This;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * This class is used to add events to the event database
 * in Firebase
 */
public class EventDB {
    private EventDBConnector eventDB;
    public CollectionReference eventRef;
    HashMap<String, String> attendees = new HashMap<>();
    /**
     * This is a constructor for the eventDB
     * */
    public EventDB() {
        this.eventDB = new EventDBConnector();
        this.eventRef = eventDB.db.collection("events");
    }
    /**
    * This method adds an event to Firestore database
    * @param event
    */
    public void addEvent(Event event) {
        HashMap<String, Object> eventData = new HashMap<>();
        eventData.put("Event Title", event.getEventTitle());
        eventData.put("Event Organizer", event.getEventOrganizer());
        eventData.put("Event Date", event.getEventDate());
        eventData.put("Event Description", event.getEventDescription());
        eventData.put("Event Poster", event.getEventPoster());
        eventData.put("Event Location", event.getEventLocation());
        eventData.put("Member Limit", Integer.toString(event.getMemberLimit()));
        eventData.put("Attendees", event.getAttendees());
        eventRef.document(event.getEventID()).set(eventData);
    }

    /**
     * This method signs a given user up for an event
     * @param event
     * @param uuid
     */
    public void userSignUp(String event, String uuid) {
        eventRef.document(event).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        attendees = (HashMap<String, String>) document.get(("Attendees"));
                        attendees.put(uuid, "0");
                        eventRef.document(event).update("Attendees", attendees);
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }
//
//    public void getAttendeeList(String event) {
//        eventRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot querySnapshots, @Nullable FirebaseFirestoreException error) {
//                if (error != null) {
//                    Log.e("Firestore", error.toString());
//                    return;
//                }
//
//                    for (QueryDocumentSnapshot doc : querySnapshots) {
//                        String organizer = doc.getString("Event Organizer");
//                        if (organizer.matches(event)) {
//
//                            attendees = (HashMap<String,String>) doc.get("Attendees");
//                            Log.w(TAG, String.valueOf(attendees));
//                        }
//                    }
//
//
//                }
//            });
//        }
}
