package com.example.collatzcheckin;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class EditEventFragment extends Fragment {
    private View view;
    Uri imageUri;
    StorageReference storageReference;
    ImageView posterImage;
    EventDB db;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_edit_event, container, false);
        db = new EventDB();
        Button selectPosterButton = view.findViewById(R.id.select_poster_button);
        Button uploadPosterButton = view.findViewById(R.id.upload_poster_button);
        Button backButton = view.findViewById(R.id.back_button);
        posterImage = view.findViewById(R.id.poster_image);
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
                        if (eventID == eventid2) {
                            String eventTitle = doc.getString("Event Title");
                            Organizer eventOrganizer = new Organizer(doc.getString("Event Organizer"));
                            String eventDate = doc.getString("Event Date");
                            String eventDescription = doc.getString("Event Description");
                            String eventPoster = doc.getString("Event Poster");
                            String eventLocation = doc.getString("Event Location");
                            String memberLimit = doc.getString("Member Limit");
                        }
                    }
                }
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).showEventView();
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
        storageReference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>(){
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot){
                        Toast.makeText(getContext(), "Successfully Uploaded",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener(){
                    @Override
                    public void onFailure(@NonNull Exception e){

                        Toast.makeText(getContext(), "Failed To Upload",Toast.LENGTH_SHORT).show();
                    }
                });
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

}