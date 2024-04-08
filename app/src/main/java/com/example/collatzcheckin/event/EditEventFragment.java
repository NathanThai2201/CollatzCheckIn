package com.example.collatzcheckin.event;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.example.collatzcheckin.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

/**
 * EditEventFragment of the application, allows users to edit their existing event
 */
public class EditEventFragment extends Fragment {
    private View view;
    Uri imageUri;
    StorageReference storageReference;
    ImageView posterImage;
    EventDB db;
    Event event;
    private TextInputEditText eventDescriptionInput;
    private TextInputEditText eventDateInput;
    private TextInputEditText eventLocationInput;
    public EditEventFragment(Event event) {
        this.event = event;
    }
    /**
     * Method to run on creation of the activity. Handles user profile editing abilities
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_edit_event, container, false);
        db = new EventDB();
        Button selectPosterButton = view.findViewById(R.id.select_poster_button);
        Button uploadPosterButton = view.findViewById(R.id.upload_poster_button);
        Button backButton = view.findViewById(R.id.back_button);
        Button submitButton = view.findViewById(R.id.submit_event_edit_button);
        eventDescriptionInput = view.findViewById(R.id.event_description_input);
        eventLocationInput = view.findViewById(R.id.event_location_input);
        eventDateInput = view.findViewById(R.id.event_date_input);
        posterImage = view.findViewById(R.id.poster_image);
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
        db.eventRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot querySnapshots, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("Firestore", error.toString());
                    return;
                }
                if (querySnapshots != null) {
                    for (QueryDocumentSnapshot doc : querySnapshots) {
                        String eventID = doc.getId();
                        if (eventID.equals(eventid)) {
                            String eventDate = doc.getString("Event Date");
                            eventDateInput.setText(eventDate);
                            String eventDescription = doc.getString("Event Description");
                            eventDescriptionInput.setText(eventDescription);
                            String eventLocation = doc.getString("Event Location");
                            eventLocationInput.setText(eventLocation);
                        }
                    }
                }
            }
        });
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitChanges();
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().finish();
//                ((MainActivity)getActivity()).showEventView(event);
            }
        });
        selectPosterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPoster();

            }
        });
        uploadPosterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPoster();

            }
        });
        return view;

    }
    private void uploadPoster() {
        //SimpleDateFormat date_formatter = new SimpleDateFormat("yyyy_MM-dd_HH_mm_ss", Locale.CANADA);
        //Date now = new Date();
        //String poster_filename = date_formatter.format(now);
        if (imageUri != null) {
            storageReference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(getContext(), "Successfully Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(getContext(), "Failed To Upload", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(getContext(), "Select an image first", Toast.LENGTH_LONG).show();
        }
    }
    private final ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null && data.getData() != null) {
                        imageUri = data.getData();
                        posterImage.setImageURI(imageUri);
                    }
                }
            });
    private void selectPoster() {
        Intent imageIntent = new Intent();
        imageIntent.setType("image/*");
        imageIntent.setAction(Intent.ACTION_GET_CONTENT);
        launcher.launch(imageIntent);
    }
    private void submitChanges(){
        String event_Description = eventDescriptionInput.getText().toString();
        String event_Date = eventDateInput.getText().toString();
        String event_Location = eventLocationInput.getText().toString();
        db.eventRef.document(event.getEventID()) // Assuming "Concert" is the document ID
                .update("Event Description", event_Description,
                        "Event Date", event_Date,
                        "Event Location", event_Location)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getContext(), "Changes saved successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Failed to save changes", Toast.LENGTH_SHORT).show();
                        Log.e("Firestore", "Error updating document", e);
                    }
                });
    }
}