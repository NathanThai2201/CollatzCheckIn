package com.example.collatzcheckin.utils;


import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.collatzcheckin.attendee.AttendeeDB;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

/**
 * This class handles the photo upload to Firebase Storage
 */
public class PhotoUploader {

    private final AttendeeDB attendeeDB = new AttendeeDB();
    public PhotoUploader(){};

    public void uploadProfile(String uuid, Uri imagePathProfile, OnSuccessListener<String> onSuccessListener) {
        if (imagePathProfile != null) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference("images/" + uuid);
            storageReference.putFile(imagePathProfile)
                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Log.d(TAG, uri.toString());
                                        attendeeDB.saveProfilePhoto(uri.toString(), uuid);
                                        onSuccessListener.onSuccess(uri.toString());
                                    }
                                });

                            } else {
                                Log.d(TAG, "Upload failed");
                            }
                        }
                    });
        }
    }


    public void uploadGenProfile(String uuid, String name, OnSuccessListener<String> onSuccessListener) {
        String nameLetter = String.valueOf(name.charAt(0));
        nameLetter = nameLetter.toUpperCase();

        StorageReference pfpRef = FirebaseStorage.getInstance().getReference().child("generatedpfp/" + nameLetter + ".png");

        try {
            final File localFile = File.createTempFile("temp", "png");

            pfpRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    // File successfully downloaded
                    pfpRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Log.d(TAG, uri.toString());
                            attendeeDB.saveProfilePhoto(uri.toString(), uuid);
                            attendeeDB.saveGenProfilePhoto(uri.toString(), uuid);
                            onSuccessListener.onSuccess(uri.toString());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Handle any errors getting the download URL
                            e.printStackTrace();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Handle any errors downloading the file
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            // Handle exception
        }
    }

    public void updateGenProfile(String uuid, String name, OnSuccessListener<String> onSuccessListener) {
        String nameLetter = String.valueOf(name.charAt(0));
        nameLetter = nameLetter.toUpperCase();

        StorageReference pfpRef = FirebaseStorage.getInstance().getReference().child("generatedpfp/" + nameLetter + ".png");

        try {
            final File localFile = File.createTempFile("temp", "png");

            pfpRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    // File successfully downloaded
                    pfpRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Log.d(TAG, uri.toString());
                            attendeeDB.saveGenProfilePhoto(uri.toString(), uuid);
                            onSuccessListener.onSuccess(uri.toString());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Handle any errors getting the download URL
                            e.printStackTrace();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Handle any errors downloading the file
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            // Handle exception
        }
    }

}
