package com.example.collatzcheckin.event;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.collatzcheckin.R;
import com.example.collatzcheckin.attendee.AttendeeDB;
import com.example.collatzcheckin.attendee.events.EventSignUp;
import com.example.collatzcheckin.authentication.AnonAuthentication;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CameraFragment# newInstance} factory method to
 * create an instance of this fragment.
 */
public class CameraFragment extends Fragment {
    View view;
    private Location currentLocation;
    AttendeeDB attendeeDB = new AttendeeDB();
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE_LOCATION = 1001;


    /**
     * Method to run on creation of the fragment. Responsible for displaying the camera fragment
     * @param inflater container
     * @param container
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.activity_camera, container, false);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext());
        //Open camera
        Button scanButton = view.findViewById(R.id.scan_button);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                scan();
            }
        });
        return view;
    }

    /**
     * Method to open up camera
     */
    //Open camera with settings

    private void updateUserLocation(String userId, double latitude, double longitude) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("user").document(userId).update("Geo","true");
        db.collection("user")
                .document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String geo = documentSnapshot.getString("Geo");
                        if (geo != null && geo.equals("true")) {
                            db.collection("user")
                                    .document(userId)
                                    .update("Latitude", String.valueOf(latitude),"Longitude",String.valueOf(longitude))
                                    .addOnSuccessListener(aVoid -> Log.d("UserLocation", "Location updated successfully"))
                                    .addOnFailureListener(e -> Log.e("UserLocation", "Error updating location", e));
                        }
                    }
                }).addOnFailureListener(e -> {
                    Log.e("ERRLOLOLOL","lol noooo",e);
                });
    }
    private void scan() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Press volume up to turn on flash");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(Capture.class);
        barLauncher.launch(options);

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // Check if permission is granted
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            // Request location updates
            fusedLocationProviderClient.requestLocationUpdates(new LocationRequest(), new LocationCallback() {
                @Override
                public void onLocationResult(@NonNull LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                    final AnonAuthentication authentication = new AnonAuthentication();
                    String uuid = authentication.identifyUser();
                    Location location = locationResult.getLastLocation();
                    if (location != null) {
                        // Get latitude and longitude
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        // Update user location in Firestore
                        updateUserLocation(uuid, latitude, longitude);
                    }
                }
            }, null);
        } else {
            // Permission not granted, request it
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_LOCATION);
        }
    }




    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result -> {
        if (result.getContents() != null) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Result");

            EventDB db = new EventDB();
            final AnonAuthentication authentication = new AnonAuthentication();
            String uuid = authentication.identifyUser();

            String res = result.getContents();

            //If scanning a share qr
            if (res.contains("_share")) {
                String eventId = res.substring(0, res.length() - 6);
                db.eventRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot querySnapshots, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.e("Firestore", error.toString());
                            return;
                        }
                        if (querySnapshots != null) {

                            for (QueryDocumentSnapshot doc : querySnapshots) {

                                // Get event from share qr
                                if (doc.getId().matches(eventId)) {
                                    String eventId = doc.getId();
                                    String eventOrganizer = doc.getString("Event Organizer");
                                    String eventTitle = doc.getString("Event Title");
                                    String eventDate = doc.getString("Event Date");
                                    String eventDescription = doc.getString("Event Description");
                                    String eventPoster = doc.getString("Event Poster");
                                    String eventLocation = doc.getString("Event Location");
                                    String memberLimit = doc.getString("Member Limit");
                                    HashMap<String, String> attendees = (HashMap<String, String>) doc.get("Attendees");
                                    int parsedMemberLimit = 0; // Default value, you can change it based on your requirements

                                    if (memberLimit != null && !memberLimit.isEmpty()) {
                                        parsedMemberLimit = Integer.parseInt(memberLimit);
                                    }

                                    //Open event view activity
                                    Event event = new Event(eventTitle, eventOrganizer, eventDate, eventDescription, eventPoster, eventLocation, parsedMemberLimit, eventId, attendees);
                                    Intent eventViewIntent = new Intent(getContext(), EventSignUp.class);
                                    eventViewIntent.putExtra("event", event);
                                    startActivity(eventViewIntent);
                                }
                            }
                        }
                    }
                });
            }
            //Otherwise scanning a check in qr
            else {

                db.eventRef.document(result.getContents())
                        .get()
                        .addOnSuccessListener(documentSnapshot -> {
                            if (documentSnapshot.exists()) {
                                // Document found
                                HashMap<String, String> attendees = (HashMap<String, String>) documentSnapshot.get("Attendees");
                                //User is signed up for event
                                if (attendees.containsKey(uuid)) {
                                    Log.d("TAG", "FOUND");
                                    String count = attendees.get(uuid);
                                    int parsedCount = Integer.parseInt(count) + 1;
                                    attendees.put(uuid, Integer.toString(parsedCount));
                                    db.eventRef.document(documentSnapshot.getId()).update("Attendees", attendees);
                                    builder.setMessage("You have successfully checked in to the event!");
                                    if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                                            != PackageManager.PERMISSION_GRANTED) {
                                        ActivityCompat.requestPermissions(requireActivity(),
                                                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                                REQUEST_CODE_LOCATION);
                                    } else {
                                        fusedLocationProviderClient.getLastLocation()
                                                .addOnSuccessListener(location -> {
                                                    if (location != null) {
                                                        // Get latitude and longitude
                                                        FirebaseFirestore.getInstance().collection("user").document(uuid).update("Geo","true");
                                                        double latitude = location.getLatitude();
                                                        double longitude = location.getLongitude();
                                                        // Update user location in Firestore
                                                        updateUserLocation(uuid, latitude, longitude);
                                                        // Show success message
                                                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                dialog.dismiss();
                                                            }
                                                        }).show();
                                                    }
                                                });

                                    }
                                } else {
                                    builder.setMessage("You have not signed up for this event.");
                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    }).show();
                                }
                            } else {
                                // Document does not exist
                                Log.d("TAG", "No such document");
                            }
                        })
                        .addOnFailureListener(e -> {
                            // Handle any errors
                            Log.d("TAG", "Error getting document", e);
                        });
            }
        }
    });
}