package com.example.collatzcheckin.attendee.events;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.collatzcheckin.MainActivity;
import com.example.collatzcheckin.R;
import com.example.collatzcheckin.attendee.AttendeeDB;
import com.example.collatzcheckin.attendee.AttendeeCallbackManager;
import com.example.collatzcheckin.attendee.User;
import com.example.collatzcheckin.authentication.AnonAuthentication;
import com.example.collatzcheckin.event.EventDB;
import com.example.collatzcheckin.utils.FirebaseFindUserCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * SignUpConfirmationFragment confirms user details before the sign up
 */
public class SignUpConfirmationFragment extends DialogFragment implements FirebaseFindUserCallback {
    private static final String ARG_PARAM1 = "euid_param";
    private static Context mcontext;
    TextView name, email;
    AnonAuthentication authentication = new AnonAuthentication();
    AttendeeDB attendeeDB = new AttendeeDB();
    EventDB eventDB = new EventDB();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference eventRef = db.collection("events");
    private static final String MESSAGING_SCOPE = "https://www.googleapis.com/auth/firebase.messaging";
    private static final String[] SCOPES = { MESSAGING_SCOPE };
    User user;
    String euid;
    AttendeeCallbackManager attendeeFirebaseManager = new AttendeeCallbackManager();
    private SignedUp listener;

    /**
     * Required empty public constructor
     */
    public SignUpConfirmationFragment() {
        // Required empty public constructor
    }

    /**
     * Called when the fragment is attached to a context.
     *
     * @param context       The context to which the fragment is attached
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof SignedUp) {
            listener = (SignedUp) context;
        } else {
            throw new RuntimeException(context + " must implement AddBookDialogListener");
        }
        mcontext = context;
    }

    /**
     * Create a new instance of SignUpConfirmationFragment using the provided parameters.
     *
     * @param euid_param       This is the event ID
     * @return                 A new instance of fragment SignUpConfirmationFragment.
     */
    public static SignUpConfirmationFragment newInstance(String euid_param) {
        SignUpConfirmationFragment fragment = new SignUpConfirmationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, euid_param);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * OnCreateDialog create the dialog and implements functionality
     *
     * @param savedInstanceState       If the fragment is being re-initialized after previously
     *                                 being shut down, this Bundle contains the data it most
     *                                 recently supplied
     * @return                         The created AlertDialog that represents the sign-up confirmation dialog
     **/
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = getLayoutInflater().inflate(R.layout.fragment_sign_up_confirmation, null);
        initViews(view);
        euid = getArguments().getString(ARG_PARAM1);
        String uuid = authentication.identifyUser();
        attendeeFirebaseManager.readData(uuid, this);


        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        AlertDialog alertDialog = builder
                .setView(view)
                .setTitle("Confirm your details")
                .setNeutralButton("Cancel", null)
                .setPositiveButton("OK", ((dialog, which) -> {
                    createAlert(euid);
                    //add event in user table
                    attendeeDB.EventsSignUp(euid, uuid);
                    //add user in event table
                    eventDB.userSignUp(euid, uuid);
                    listener.updateText();
                    subscribeToEvent(getContext(),euid);
                }))
                .create();
        alertDialog.show();
        return alertDialog;
    }
    public void subscribeToEvent(Context context, String euid){
        FirebaseMessaging.getInstance().subscribeToTopic(euid)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = context.getString(R.string.msg_subscribed);
                        if (!task.isSuccessful()) {
                            msg = context.getString(R.string.msg_subscribe_failed);
                        }
                        Log.d("Notifications",msg);
                    }
                });

    }
    public int createAlert(String event) {
        final int[] numberOfAttendees = new int[1];
        eventRef.document(event).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> eventData = document.getData();
                        String eventTitle = eventData.get("Event Title").toString();
                        String eventOrganizer = eventData.get("Event Organizer").toString();
                        HashMap<String, String> attendeesMap = (HashMap<String, String>) eventData.get("Attendees");
                        if (attendeesMap != null) {
                            numberOfAttendees[0] = attendeesMap.size();
                            Log.d(TAG, "Event " + event + ": " + numberOfAttendees[0]);
                            if (numberOfAttendees[0]==0){
                                sendNotification(eventOrganizer, "Your event now has it's first attendee!",eventTitle);
                            }
                            if (numberOfAttendees[0]==9){
                                sendNotification(eventOrganizer, "Your event now has 10 attendees!",eventTitle);
                            }
                            if (numberOfAttendees[0]==99){
                                sendNotification(eventOrganizer, "Your event now has 100 attendees!",eventTitle);
                            }
                            if (numberOfAttendees[0]==999){
                                sendNotification(eventOrganizer, "Your event now has 1000 attendees!",eventTitle);
                            }
                        } else {
                            Log.d(TAG, "No 'Attendees' field for event " + event);
                        }
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
        return numberOfAttendees[0];
    }
    private static String getAccessToken(Context context) throws Exception {
        try {
            AssetManager assetManager = context.getAssets();
            InputStream inputStream = assetManager.open("service_account.json");
            Log.d(TAG, inputStream.toString());
            GoogleCredentials googleCredentials = GoogleCredentials
                    .fromStream(inputStream)
                    .createScoped(Arrays.asList(SCOPES));
            googleCredentials.refresh();
            Log.d(TAG, "3");
            Log.d(TAG, "JSON File Content: " + googleCredentials);
            Log.d(TAG, "Token: " + googleCredentials.getAccessToken().getTokenValue());
            return googleCredentials.getAccessToken().getTokenValue();
        } catch (Exception e) {
            Log.e(TAG, "IOException occurred: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private void sendNotification(String euid,String message,String title) {
        //Execute the network call in a async task
        new SignUpConfirmationFragment.NotificationTask(mcontext,euid, message, title).execute();
    }
    private static class NotificationTask extends AsyncTask<Void, Void, Void> {
        private final Context context;
        private final String euid;
        private final String message;
        private final String title;

        public NotificationTask(Context context, String euid, String message, String title) {
            this.context = context;
            this.euid = euid;
            this.message = message;
            this.title = title;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                JSONObject fullObject = new JSONObject();
                JSONObject jsonObject = new JSONObject();

                JSONObject notificationObj = new JSONObject();
                notificationObj.put("title", title);
                notificationObj.put("body", message);

                JSONObject dataObject = new JSONObject();
                dataObject.put("userId", "N/A");

                jsonObject.put("topic", euid);
                jsonObject.put("notification", notificationObj);
                jsonObject.put("data", dataObject);

                fullObject.put("message", jsonObject);
                callApi(fullObject, context);
            } catch (JSONException e) {
                Log.e(TAG, "Error creating JSON object", e);
            }
            return null;
        }
    }
    private static void callApi(JSONObject fullObject, Context context){
        try{
            Log.d(TAG,"yes");
            Log.d(TAG, String.valueOf(fullObject));
            MediaType JSON = MediaType.get("application/json; charset=utf-8");
            OkHttpClient client = new OkHttpClient();
            RequestBody body = RequestBody.create(fullObject.toString(), JSON);
            Request request = new Request.Builder()
                    .url("https://fcm.googleapis.com/v1/projects/collatzcheckin/messages:send")
                    .addHeader("Authorization", "Bearer " + getAccessToken(context))
                    .addHeader("Content-Type", "application/json")
                    .post(body)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    Log.d(TAG, "sent failure");
                    // Handle failure
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    try (ResponseBody responseBody = response.body()) {
                        // Process the response
                        Log.d(TAG, "sent success");
                    }
                }
            });
        } catch (Exception e) {
        }
    }

    /**
     * Fill the dialog with user details when is have been retrieved from Firebase
     *
     * @param user       User data that has been retrieved from Firebase
     **/
    @Override
    public void onCallback(User user) {
        if (isAdded()) { // Check if fragment is attached
            // Handle the retrieved user data
            this.user = user;
            setData(user);
        }
    }

    /**
     * Initializes data and views
     *
     * @param view           Views in the fragment
     */
    private void initViews(View view) {
        name = view.findViewById(R.id.name_text);
        email = view.findViewById(R.id.email_text);
    }

    /**
     * Set Text in Dialog Box to user details
     *
     * @param user       User details
     **/
    private void setData(User user) {
        name.setText(user.getName());
        email.setText(user.getEmail());
    }
}