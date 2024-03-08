package com.example.collatzcheckin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.collatzcheckin.admin.controls.profile.UserListFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class EventView extends AppCompatActivity {
    Uri imageUri;
    StorageReference storageReference;
    TextView eventTitle;
    TextView eventMonth;
    TextView eventDay;
    TextView eventTime;
    TextView eventDescription;
    TextView eventLocation;
    ImageView posterImage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_view);
        Intent intent = getIntent();
        Event event = (Event) intent.getSerializableExtra("event");
        String[] parsedData = event.getEventDate().split(" ");
        eventTitle = findViewById(R.id.event_name);
        eventTitle.setText(event.getEventTitle());

        eventMonth = findViewById(R.id.event_month);
        eventMonth.setText(parsedData[0]);

        eventDay = findViewById(R.id.event_day);
        eventDay.setText(parsedData[1]);

        eventTime = findViewById(R.id.event_time);
        eventTime.setText(parsedData[parsedData.length - 1]);


        eventDescription = findViewById(R.id.event_description);
        eventDescription.setText(event.getEventDescription());



        eventLocation = findViewById(R.id.event_location);
        eventLocation.setText(event.getEventLocation());

        posterImage = findViewById(R.id.poster_image);
        String eventid = "id1";
        String eventid2 = "Concert";
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

        Button backButton =  findViewById(R.id.event_view_back_button);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button viewAttendees = findViewById(R.id.view_attendee);
        viewAttendees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewAttendeesList();
            }
        });

        Button editEvent = findViewById(R.id.edit_event);
        editEvent.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.event_edit_frame, new EditEventFragment(event))
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    public void viewAttendeesList() {
        Intent intent = new Intent(this, AttendeeList.class);
        startActivity(intent);
    }
}