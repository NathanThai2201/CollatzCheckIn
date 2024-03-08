package com.example.collatzcheckin;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
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
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

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
    Uri imageUri;
    StorageReference storageReference;
    public EventViewFragment(Event event) {
        this.event = event;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        String[] words = event.getEventDate().split(" ");

        view = inflater.inflate(R.layout.fragment_event_view, container, false);

        Button viewAttendeeButton = view.findViewById(R.id.view_attendee);
        Button editEventButton = view.findViewById(R.id.edit_event);
        ImageView posterImage = view.findViewById(R.id.poster_image);
        String eventid = "id1";
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
        storageReference = FirebaseStorage.getInstance().getReference("posters/"+eventid);
        // Download the image manually and set it to the ImageView
        try {
            final File localFile = File.createTempFile("images", "jpg");
            storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    // Set the downloaded image to the ImageView
                    posterImage.setImageURI(Uri.fromFile(localFile));
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            // Handle exception
        }

        viewAttendeeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).showAttendeeList(event);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).showEventList();
            }
        });
        editEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).showEditEvent(event);
            }
        });
        return view;

    }

//    public ArrayList<String> fetchEvent() {
//        db = new EventDB();
//        HashMap<String, String> fetchData = db.getEvent("Main");
//        ArrayList<String> eventData = new ArrayList<>();
//        eventData.add(fetchData.get("eventTitle"));
//        eventData.add(fetchData.get("eventDescription"));
//        String date = fetchData.get("eventDate");
//        String[] parsedDate = date.split(" ");
//        eventData.add(parsedDate[1]);
//        eventData.add(parsedDate[2]);
//        eventData.add(parsedDate[parsedDate.length - 1]);
//
//        return eventData;
//    }
}