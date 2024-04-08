package com.example.collatzcheckin.event;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.collatzcheckin.R;
import com.example.collatzcheckin.attendee.AttendeeDB;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.osmdroid.config.Configuration;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.Map;

public class MapActivity extends AppCompatActivity {

    private MapView mMapView;
    FirebaseFirestore db;
    ArrayList<GeoPoint> locations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Intent intent = getIntent();
        Event event = (Event) intent.getSerializableExtra("Event");
        String id = event.getEventID();
        db = FirebaseFirestore.getInstance();
        DocumentReference ref = db.collection("events").document(id);

        Configuration.getInstance().load(this, getPreferences(MODE_PRIVATE));

        mMapView = findViewById(R.id.map);
        mMapView.setTileSource(org.osmdroid.tileprovider.tilesource.TileSourceFactory.MAPNIK);
        mMapView.setBuiltInZoomControls(true);

        locations = new ArrayList<>();

        Button exit = findViewById(R.id.back);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    DocumentSnapshot document = task.getResult();
                                                    if (document.exists()) {
                                                        Map<String, String> idCheckedIn = (Map<String, String>) document.get("Attendees");
                                                        if (idCheckedIn != null && !idCheckedIn.isEmpty()) {
                                                            // Iterate through the map of user IDs and associated numbers
                                                            for (Map.Entry<String, String> entry : idCheckedIn.entrySet()) {
                                                                String uuid = entry.getKey();
                                                                int checked_in = Integer.parseInt(entry.getValue());
                                                                if (checked_in > 0) {
                                                                    DocumentReference userRef = db.collection("user").document(uuid);
                                                                    userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                            if (task.isSuccessful()) {
                                                                                DocumentSnapshot userDocument = task.getResult();
                                                                                if (userDocument.exists()) {
                                                                                    if (userDocument.getString("Geo").equals("true")) {
                                                                                        double latitude = Double.valueOf((userDocument.getString("Latitude")));
                                                                                        double longitude = Double.valueOf((userDocument.getString("Longitude")));
                                                                                        GeoPoint location = new GeoPoint(latitude, longitude);
                                                                                        locations.add(location);
                                                                                        // Create marker and add it here if you want
                                                                                        Marker marker = new Marker(mMapView);
                                                                                        marker.setPosition(location);
                                                                                        marker.setTitle(userDocument.getString(("Name")));
                                                                                        mMapView.getOverlays().add(marker);
                                                                                        mMapView.getController().setZoom(15);
                                                                                        mMapView.getController().setCenter(location);
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    });
                                                                }

                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        });



    }
}
