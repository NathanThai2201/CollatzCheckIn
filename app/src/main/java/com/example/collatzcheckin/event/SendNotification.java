package com.example.collatzcheckin.event;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
//import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.RemoteMessage;

import com.example.collatzcheckin.R;
import com.example.collatzcheckin.attendee.AttendeeDB;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class SendNotification extends AppCompatActivity {
    private static Context context;
    private EditText messageInput;
    private EditText titleInput;
    private static final String MESSAGING_SCOPE = "https://www.googleapis.com/auth/firebase.messaging";
    private static final String[] SCOPES = { MESSAGING_SCOPE };
    private static final String TAG = "SendNotifs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_notification_activity);
        Intent intent = getIntent();
        Event event = (Event) intent.getSerializableExtra("Event");
        AttendeeDB attendeeDB = new AttendeeDB();
        String euid = event.getEventID();
        messageInput = findViewById(R.id.notification_text);
        titleInput = findViewById(R.id.notification_title);
        context = this;

        Button sendNotificationButton = findViewById(R.id.send_notification_button);
        sendNotificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = messageInput.getText().toString();
                String title = titleInput.getText().toString();
                sendNotification(euid, message,title);
                Toast.makeText(SendNotification.this, "Notification sent", Toast.LENGTH_SHORT).show();
            }
        });

        Button backButton = findViewById(R.id.back_button_notification);
        //Switch back to event detail page
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private static String getAccessToken() throws Exception {
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
        new NotificationTask(euid, message, title).execute();
    }
    private static class NotificationTask extends AsyncTask<Void, Void, Void> {
        private final String euid;
        private final String message;
        private final String title;

        public NotificationTask(String euid, String message, String title) {
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
                callApi(fullObject);
            } catch (JSONException e) {
                Log.e(TAG, "Error creating JSON object", e);
            }
            return null;
        }
    }
    private static void callApi(JSONObject fullObject){
        try{
            Log.d(TAG,"yes");
            Log.d(TAG, String.valueOf(fullObject));
            MediaType JSON = MediaType.get("application/json; charset=utf-8");
            OkHttpClient client = new OkHttpClient();
            RequestBody body = RequestBody.create(fullObject.toString(), JSON);
            Request request = new Request.Builder()
                    .url("https://fcm.googleapis.com/v1/projects/collatzcheckin/messages:send")
                    .addHeader("Authorization", "Bearer " + getAccessToken())
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
}