package com.example.collatzcheckin.event;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.collatzcheckin.R;
import com.example.collatzcheckin.attendee.User;
import com.example.collatzcheckin.attendee.AttendeeDB;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.io.ByteArrayDataOutput;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateEvent extends AppCompatActivity {
    View view;
    Context context = this;
    ImageView posterImage;

    TextView eventTitle;
    TextView eventLocation;
    TextView eventDate;
    TextView eventDescription;
    TextView eventLimit;
    Uri imageUri;
    String id;
    Spinner eventDropDown;
    ArrayAdapter<String> eventTitlesAdapter;
    ArrayList<String> eventTitles;
    StorageReference storageReference;
    private static final String DATE_PATTERN = "^(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)\\s\\d{1,2}\\s\\d{4}\\s\\d{1,2}:\\d{2}$";    //User user;
    /**
     * Method to run on creation of the activity. Handles create event
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        Intent intent = getIntent();
        String uuid = intent.getStringExtra("uuid");
        ArrayList<Event> events = (ArrayList<Event>) intent.getSerializableExtra("events");
        eventTitles = new ArrayList<>();


        eventDropDown = findViewById(R.id.event_selection);
        eventTitle = findViewById(R.id.edit_event_name);
        eventDate = findViewById(R.id.edit_event_date);
        eventLocation = findViewById(R.id.edit_event_location);
        eventDescription = findViewById(R.id.edit_event_description);
        eventLimit = findViewById(R.id.edit_event_limit);
        posterImage = findViewById(R.id.poster_image);



        Button selectPosterButton = findViewById(R.id.select_poster_button);
        Button backButton = findViewById(R.id.back_button_create_event);
        Button addEventButton = findViewById(R.id.add_event);


        eventTitles.add("");    //Default title for not replacing an event (drop down menu)

        //Get all organized events
        for (Event e: events) {
            eventTitles.add(e.getEventTitle());
        }
        eventTitlesAdapter = new ArrayAdapter<>(this, R.layout.spinner_dropdown_item, eventTitles);
        eventDropDown.setAdapter(eventTitlesAdapter);

        //Generating a unique id for event
        id = generateRandomString(16);


        addEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get values
                String replaceValue = eventDropDown.getSelectedItem().toString();

                //Not replacing an event
                if (replaceValue.matches("")) {
                    createNewEvent(uuid);
                }
                else {
                    //Replacing an event
                    for (Event e: events) {
                        if (e.getEventTitle().matches(replaceValue)) {
                            replaceEvent(e);
                            break;
                        }
                    }

                }
                selfSubscribe(uuid, context);
            }
        });

        //Return without creating an event
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Get image for event poster
        selectPosterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPoster();
            }
        });

    }

    /**
     * Method to run if replacing an older event
     * @param event
     */
    private void replaceEvent(Event event) {
        String title = eventTitle.getText().toString();
        String date = eventDate.getText().toString();
        String location = eventLocation.getText().toString();
        String description = eventDescription.getText().toString();
        String limit = eventLimit.getText().toString();

        if(title.matches("") || date.matches("") || location.matches("") || description.matches("") ) {
            Toast.makeText(this, "Please Complete All Fields", Toast.LENGTH_LONG).show();
            return;
        }
        if (limit.matches("")){
            limit = "999999";
        }
        if (!isNumeric(limit)){
            Toast.makeText(this, "Please enter a number for your event limit", Toast.LENGTH_LONG).show();
            return;
        }
        if(!isValidDateFormatAndFutureDate(date)) {
            Toast.makeText(this, "Enter Date similar to Apr 27 2024 10:30 and no past dates", Toast.LENGTH_LONG).show();
            return;
        }

        String id = event.getEventID();
        EventDB db = new EventDB();
        AttendeeDB userDb = new AttendeeDB();
        storageReference = FirebaseStorage.getInstance().getReference("posters/"+id);
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

        ArrayList<String> attendees = new ArrayList<>(event.getAttendees().keySet());

        //Updating old event info with new values
        event.setEventTitle(title);
        event.setEventDescription(description);
        event.setEventDate(date);
        event.setEventLocation(location);
        event.setMemberLimit(Integer.parseInt(limit));
        event.setAttendees(new HashMap<>());

        //Wipe all past attendees
        AttendeeDB attendeeDB = new AttendeeDB();
        for (String attendee: attendees) {
            attendeeDB.userRef.document(attendee).update("Events", FieldValue.arrayRemove(id));
        }

        //Replace image
        if(imageUri != null) {
            uploadPoster(event.getEventID());
        }
        else {
            storageReference = FirebaseStorage.getInstance().getReference("posters/"+ event.getEventID());
            storageReference.delete();
        }
        db.addEvent(event);
        finish();


    }

    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    /**
     * Method to run if creating a new event
     * @param uuid
     */
    public void createNewEvent(String uuid) {
        String title = eventTitle.getText().toString();
        String date = eventDate.getText().toString();
        String location = eventLocation.getText().toString();
        String description = eventDescription.getText().toString();
        String limit = eventLimit.getText().toString();

        if(title.matches("") || date.matches("") || location.matches("") || description.matches("") ) {
            Toast.makeText(this, "Please Complete All Fields", Toast.LENGTH_LONG).show();
            return;
        }
        if (limit.matches("")){
            limit = "999999";
        }
        if (!isNumeric(limit)){
            Toast.makeText(this, "Please enter a number for your event limit", Toast.LENGTH_LONG).show();
            return;
        }
        if(!isValidDateFormatAndFutureDate(date)) {
            Toast.makeText(this, "Enter Date similar to Apr 27 2024 10:30 and no past dates", Toast.LENGTH_LONG).show();
            return;
        }


        //Update user and event db
        EventDB db = new EventDB();
        AttendeeDB userDb = new AttendeeDB();


        storageReference = FirebaseStorage.getInstance().getReference("posters/"+id);
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

        //Upload image
        if(imageUri != null) {
            uploadPoster(id);
        }

        //Creating a qr
        Bitmap qr = generateQR(id);
        //Creating a share qr
        Bitmap shareQr = generateQR(id+"_share");


        storageReference = FirebaseStorage.getInstance().getReference("/qr");
        StorageReference imgRef = storageReference.child(id + ".jpg");
        StorageReference shareQrRef = storageReference.child( id + "_share.jpg");

        //Converting to a byte array to upload qr
        byte[] qrData = convertToByteArray(qr);
        byte[] shareQrData = convertToByteArray(shareQr);
        UploadTask shareUploadTask = shareQrRef.putBytes(shareQrData);
        UploadTask uploadTask = imgRef.putBytes(qrData);
        String finalLimit = limit;
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.

                //Upload event with entered fields
                String uri = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
                Event event = new Event(title, uuid, date, description, uri, location, Integer.parseInt(finalLimit), id, new HashMap<String,String>());
                db.addEvent(event);
                finish();
            }
        });
    }

    /**
     * Converts bitmap to bytearray for uplodaing to firebase
     * @param qr
     * @return byte[] data
     */
    private byte[] convertToByteArray(Bitmap qr) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        qr.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        return data;
    }

    /**
     * uploads image to firebase
     * @param id
     */

    private void uploadPoster(String id) {
        storageReference = FirebaseStorage.getInstance().getReference("posters/"+id);
        storageReference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>(){
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot){
                        Toast.makeText(context, "Successfully Uploaded", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener(){
                    @Override
                    public void onFailure(@NonNull Exception e){

                        Toast.makeText(context, "Failed To Upload",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Open user photos
     */
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

    /**
     * Open user photos
     */
    private void selectPoster() {
        Intent imageIntent = new Intent();
        imageIntent.setType("image/*");
        imageIntent.setAction(Intent.ACTION_GET_CONTENT);
        Log.d("TAG", "selectPoster ");
        launcher.launch(imageIntent);
    }
    /**
     * Generates 'random' ID for a given length
     * @param length
     * @return sb (String)
     */
    public static String generateRandomString(int length) {
        String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            sb.append(randomChar);
        }
        return sb.toString();
    }
    /**
     * Generates qr code that is attached to an ID
     * @param id
     * @return bitmap (Bitmap)
     */
    public static Bitmap generateQR(String id) {
        MultiFormatWriter writer = new MultiFormatWriter();
        try {
            BitMatrix matrix = writer.encode(id, BarcodeFormat.QR_CODE, 600, 600);
            BarcodeEncoder encoder = new BarcodeEncoder();
            Bitmap bitmap = encoder.createBitmap(matrix);
            return  bitmap;
        }
        catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }
    public void selfSubscribe(String uuid,Context context){
        FirebaseMessaging.getInstance().subscribeToTopic(uuid)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Self signed up for event";
                        if (!task.isSuccessful()) {
                            msg = context.getString(R.string.msg_subscribe_failed);
                        }
                        Log.d("Failed self sign up",msg);
                    }
                });

    }

    // ChatGPT: How do I write a function that checks if the a date string is in the format MONTH DAY YEAR TIME
    /**

     Check if date is in valid format
     @param dateString String of date entered
     @return boolean true if format is right and false if it not bad*/
    public static boolean isValidDateFormat(String dateString) {

        Pattern pattern = Pattern.compile(DATE_PATTERN);
        Matcher matcher = pattern.matcher(dateString);
        return matcher.matches();
    }

    // ChatGPT: How do I write a function that checks if the a date string is in the format MONTH DAY YEAR TIME
    /**

     Checks if the given date string matches the format "MONTH DAY YEAR TIME" and is in the future
     @param dateString The date string to validate
     @return True if the date string matches the format and is in the future, otherwise false*/
    public static boolean isValidDateFormatAndFutureDate(String dateString) {
        if (!isValidDateFormat(dateString)) {
            return false;}

        SimpleDateFormat sdf = new SimpleDateFormat("MMM d yyyy HH:mm", Locale.ENGLISH);
        try {
            Date date = sdf.parse(dateString);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            Calendar now = Calendar.getInstance();

            return cal.after(now);
        } catch (ParseException e) {

            e.printStackTrace();
            return false;
        }
    }
}