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
    View view;
    Uri imageUri;
    StorageReference storageReference;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_event_view, container, false);

        Button viewAttendeeButton = view.findViewById(R.id.view_attendee);
        Button editEventButton = view.findViewById(R.id.edit_event);
        ImageView posterImage = view.findViewById(R.id.poster_image);
        String eventid = "id1";
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
                ((MainActivity)getActivity()).showAttendeeList();
            }
        });
        editEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).showEditEvent();
            }
        });
        return view;

    }
}