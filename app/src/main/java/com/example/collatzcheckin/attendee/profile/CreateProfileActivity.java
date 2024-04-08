package com.example.collatzcheckin.attendee.profile;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.POST_NOTIFICATIONS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
;
import com.example.collatzcheckin.R;
import com.example.collatzcheckin.attendee.AttendeeCallbackManager;
import com.example.collatzcheckin.attendee.AttendeeDB;
import com.example.collatzcheckin.attendee.User;
import com.example.collatzcheckin.authentication.AnonAuthentication;
import com.example.collatzcheckin.utils.PhotoUploader;
import com.example.collatzcheckin.utils.SignInUserCallback;
import com.google.android.gms.tasks.OnSuccessListener;

/**
 * UpdateProfileActivity of the application, handles creating a new user profile
 */
public class CreateProfileActivity extends AppCompatActivity implements SignInUserCallback {

    private String uuid;
    private final AnonAuthentication authentication = new AnonAuthentication();
    AttendeeCallbackManager attendeeFirebaseManager = new AttendeeCallbackManager();
    private final AttendeeDB attendeeDB = new AttendeeDB();
    private User user;
    PhotoUploader photoUploader = new PhotoUploader();
    private static final int PERMISSION_REQUEST_CODE = 200;

    /**
     * Method to run on creation of the activity. Handles user profile creation
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        uuid = authentication.identifyUser();
        if(!authentication.validateUser()) {
            authentication.updateUI(this, new OnSuccessListener<String>() {
                @Override
                public void onSuccess(String s) {
                    uuid = authentication.identifyUser();
                    onCallback(false);
                }
            });
        } else {
            attendeeFirebaseManager.userCheck(uuid, this);
        }
    }

    /**
     * Ends activity if this a returing user or create a new profile for new user
     *
     * @param exists       User exists in Firebase
     **/
    @Override
    public void onCallback(boolean exists) {
        if(exists) {
            finish();
        } else {
            requestPermission();
        }
    }

    /**
     * Creates guest profile for new user and stores in Firebase
     **/
    private void validation() {
        user = new User(uuid, "Guest", "");
        boolean hasLocationPermission = ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        user.setGeolocation(hasLocationPermission);
        attendeeDB.addUser(user);
        photoUploader.uploadGenProfile(uuid, "Guest", new OnSuccessListener<String>() {
            @Override
            //dummy method
            public void onSuccess(String s) {
                String test = "test";
            }
        });
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION, POST_NOTIFICATIONS}, PERMISSION_REQUEST_CODE);

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("Tag", "Location permission granted");
            } else {
                Log.d("TAg","Location Denied");
            }
            if (grantResults.length > 1 && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Log.d("TAg","Notifications Allowed");
            } else {
                Log.d("TAg","Notifications Denied");

            }
            validation();
            finish();
        }
    }
}
