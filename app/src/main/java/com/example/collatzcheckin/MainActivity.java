package com.example.collatzcheckin;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;
import android.Manifest;
import com.example.collatzcheckin.attendee.AttendeeDB;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
//import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.RemoteMessage;
import com.example.collatzcheckin.R;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.collatzcheckin.admin.controls.events.AdminEventListFragment;
import com.example.collatzcheckin.admin.controls.events.AdminEventViewFragment;
import com.example.collatzcheckin.admin.controls.profile.UserListFragment;
import com.example.collatzcheckin.admin.controls.profile.UserViewFragment;
import com.example.collatzcheckin.attendee.User;
import com.example.collatzcheckin.attendee.events.BrowseEventsFragment;
import com.example.collatzcheckin.attendee.profile.ProfileFragment;
import com.example.collatzcheckin.attendee.profile.CreateProfileActivity;
import com.example.collatzcheckin.authentication.AnonAuthentication;
import com.example.collatzcheckin.event.CameraFragment;
import com.example.collatzcheckin.event.EventListFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.example.collatzcheckin.event.EditEventFragment;
import com.example.collatzcheckin.event.Event;
import com.example.collatzcheckin.event.EventDB;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * MainActivity of the application, handles setting up the bottom nav fragment and the user
 */
public class MainActivity extends AppCompatActivity {

    private final AnonAuthentication authentication = new AnonAuthentication();
    private User user;
    private Button viewAttendeeButton;
    private ArrayList<String> data;
    EventDB db = new EventDB();

    /**
     * Method to run on creation of the activity. Handles user authentication and creates the bottomnav fragment
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    private static Context context;
    private static final String TAG = "MainActivity";
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    Toast.makeText(this, "Notifications permission granted",Toast.LENGTH_SHORT)
                            .show();
                } else {
                    Toast.makeText(this, "CollatzCheckIn can't post notifications without permission",
                            Toast.LENGTH_LONG).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = getApplicationContext();

        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // requestPermission will show the native Android notification permission prompt.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelId  = getString(R.string.default_notification_channel_id);
            String channelName = getString(R.string.default_notification_channel_name);
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW));
        }
        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                Log.d(TAG, "Key: " + key + " Value: " + value);
            }
        }

        Intent i = new Intent(MainActivity.this, CreateProfileActivity.class);
        startActivity(i);

        // show home page
        replaceFragment(new EventListFragment());


        // creating the nav bar
        // adds functionality to allow attendee to navigate
        bottomNavigationView.setOnItemSelectedListener(item -> {
            // show home page
            //replaceFragment(new EventListFragment());
            int iconPressed= item.getItemId();
            // navigate to profile page
            if (iconPressed == R.id.profile) {
                replaceFragment(new ProfileFragment());
            }
            // navigate to page to browse events
            if (iconPressed == R.id.search) {
                replaceFragment(new BrowseEventsFragment());
            }
            if (iconPressed == R.id.scanner) {
                replaceFragment(new CameraFragment());
            }
            if (iconPressed == R.id.home) {
                replaceFragment(new EventListFragment());
            }
            return true;
        });

    }

    /**
     * Switches fragments to correponding button on nav bar
     *
     * @param fragment Fragment to switch to
     */
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
    
}
