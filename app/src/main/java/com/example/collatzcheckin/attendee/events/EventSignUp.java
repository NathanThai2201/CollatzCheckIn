package com.example.collatzcheckin.attendee.events;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.collatzcheckin.R;
import com.example.collatzcheckin.authentication.AnonAuthentication;
import com.example.collatzcheckin.event.EditEventFragment;
import com.example.collatzcheckin.event.Event;
import com.example.collatzcheckin.event.EventArrayAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * EventSignUp shows the event details and allows the user to sign up for the event
 */
public class EventSignUp extends AppCompatActivity implements SignedUp {
    StorageReference storageReference;
    TextView eventTitle, eventMonth, eventDay, eventTime, eventDescription, eventLocation, eventFull, signedUp;
    ImageView posterImage;
    Button backButton, signupEvent;
    Event event;
    int signedupNum, signupLimit;
    AnonAuthentication authentication = new AnonAuthentication();
    String uuid;

    /**
     * Method to run on creation of the activity. Handles displaying status of sign up
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_sign_up);
        Intent intent = getIntent();
        event = (Event) intent.getSerializableExtra("event");
        String[] parsedData = event.getEventDate().split(" ");
        uuid = authentication.identifyUser();

        initViews();
        SetData(event);
        limitCheck(event);
        signUpCheck(event);

        String eventid = event.getEventID();
        storageReference = FirebaseStorage.getInstance().getReference("posters/"+eventid);
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


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        signupEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUpConfirmationFragment.newInstance(event.getEventID()).show(getSupportFragmentManager(), "Confirm");
            }
        });
    }
    /**
     * Initializes xml views
     */
    private void initViews() {
        eventTitle = findViewById(R.id.event_name);
        eventMonth = findViewById(R.id.event_month);
        eventDay = findViewById(R.id.event_day);
        eventTime = findViewById(R.id.event_time);
        eventDescription = findViewById(R.id.event_description);
        eventLocation = findViewById(R.id.event_location);
        posterImage = findViewById(R.id.poster_image);
        backButton =  findViewById(R.id.event_view_back_button);
        signupEvent = findViewById(R.id.sign_up);
        eventFull = findViewById(R.id.event_full);
        signedUp = findViewById(R.id.signed_up_success);
    }

    /**
     * Displays events specific information
     *
     * @param event           Event the user wants to view
     */
    private void SetData(Event event) {
        String[] parsedData = event.getEventDate().split(" ");

        eventTitle.setText(event.getEventTitle());
        if (parsedData.length >= 2) {
            eventMonth.setText(parsedData[0]);
            eventDay.setText(parsedData[1]);
        }

        if (parsedData.length > 2) {
            eventTime.setText(parsedData[parsedData.length - 1]);
        } else {
            // Handle the case where there is no time information
            eventTime.setText("No time information");
        }
        eventDescription.setText(event.getEventDescription());
        eventLocation.setText(event.getEventLocation());
    }

    /**
     * Checks if there is room in the events for another sign up
     *
     * @param event           Event the user wants to view
     */
    private void limitCheck(Event event) {
        signedupNum = event.getAttendees().size();
        signupLimit = event.getMemberLimit();
        if(signedupNum == signupLimit) {
            signupEvent.setVisibility(View.INVISIBLE);
            eventFull.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Checks if user has previously signed up
     *
     * @param event           Event the user wants to view
     */
    private void signUpCheck(Event event) {
        if(event.getAttendees().containsKey(uuid)) {
            signupEvent.setVisibility(View.INVISIBLE);
            signedUp.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Changes text to show user they have sign up when the confirm sign up
     */
    @Override
    public void updateText() {
        signupEvent.setVisibility(View.INVISIBLE);
        signedUp.setVisibility(View.VISIBLE);
    }
}
