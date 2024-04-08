
package com.example.collatzcheckin.admin.controls.events;

        import android.net.Uri;
        import android.os.Bundle;

        import androidx.fragment.app.Fragment;

        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.example.collatzcheckin.AdminMainActivity;
        import com.example.collatzcheckin.MainActivity;
        import com.example.collatzcheckin.event.Event;

        import com.example.collatzcheckin.R;
        import com.example.collatzcheckin.admin.controls.AdministratorDB;
        import com.example.collatzcheckin.admin.controls.profile.UserListFragment;
        import com.google.android.gms.tasks.OnSuccessListener;
        import com.google.firebase.storage.FileDownloadTask;
        import com.google.firebase.storage.FirebaseStorage;
        import com.google.firebase.storage.StorageReference;

        import java.io.File;
        import java.io.IOException;


/**
 * The AdminEventViewFragment class displays detailed information about a specific event
 * It allows administrators to view event details and delete the event if needed
 */
public class AdminEventViewFragment extends Fragment implements AdministratorDB.RemoveCallback{
    private AdministratorDB db;
    private View view;
    private Event event;

    Button backButton;
    Button deleteButton;
    Button imageButton;
    TextView eventTitle;
    TextView eventDescription;
    TextView eventLocation;
    TextView eventMonth;
    TextView eventDay;
    TextView eventTime;
    ImageView posterImage;
    StorageReference storageReference;

    /**
     * Constructs a new AdminEventViewFragment with the specified event
     *
     * @param event The event object to be displayed
     */
    public AdminEventViewFragment(Event event) {
        this.event = event;
    }


    /**
     * Called to create the view hierarchy associated with the fragment
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any views in the fragment
     * @param container          If non-null, this is the parent view that the fragment's UI should be attached to
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state
     * @return The root view of the fragment layout
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        String[] words = event.getEventDate().split(" ");

        view = inflater.inflate(R.layout.fragment_admin_event_view, container, false);

        deleteButton = view.findViewById(R.id.delete_event);
        imageButton = view.findViewById(R.id.remove_event_poster);
        backButton = view.findViewById(R.id.back_button_event_view);
        eventTitle = view.findViewById(R.id.event_title);
        posterImage = view.findViewById(R.id.poster_image);

        storageReference = FirebaseStorage.getInstance().getReference("posters/"+event.getEventID());
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

        eventTitle.setText(event.getEventTitle());

        eventDescription = view.findViewById(R.id.event_description);
        eventDescription.setText(event.getEventDescription());

        eventLocation = view.findViewById(R.id.event_location);
        eventLocation.setText(event.getEventLocation());
        eventMonth = view.findViewById(R.id.event_month);
        eventMonth.setText(words[0]);

        eventDay = view.findViewById(R.id.event_day);
        eventDay.setText(words[1]);

        eventTime = view.findViewById(R.id.event_time);
        eventTime.setText(words[words.length - 1]);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdministratorDB administratorDB = new AdministratorDB();

                // Call the removeEvent method passing event as parameter
                administratorDB.removeEvent(event, AdminEventViewFragment.this);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdminEventListFragment adminEventListFragment = new AdminEventListFragment();
                ((AdminMainActivity) requireActivity()).replaceFragment(adminEventListFragment);
            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StorageReference imageRef = FirebaseStorage.getInstance().getReference().child("posters/" + event.getEventID());
                imageRef.delete();
                posterImage.setImageResource(R.drawable.ic_launcher_background);
            }
        });

        return view;

    }

    /**
     * Callback method invoked when the event is successfully removed
     * Handles event removal success
     */
    @Override
    public void onRemoved() {
        // Handle event removal success
        // For example, navigate back to the event list fragment
        AdminEventListFragment adminEventListFragment = new AdminEventListFragment();
        ((AdminMainActivity) requireActivity()).replaceFragment(adminEventListFragment);
    }

    /**
     * Callback method invoked when event removal fails
     * Handles event removal failure
     *
     * @param errorMessage The error message indicating the reason for failure
     */
    @Override
    public void onRemoveFailure(String errorMessage) {
        // Handle event removal failure
        // For example, show an error message to the user
        Toast.makeText(requireContext(), "Failed to delete event: " + errorMessage, Toast.LENGTH_SHORT).show();
    }
}