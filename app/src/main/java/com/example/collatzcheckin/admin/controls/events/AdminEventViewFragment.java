
package com.example.collatzcheckin.admin.controls.events;

        import android.os.Bundle;

        import androidx.fragment.app.Fragment;

        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.TextView;

        import com.example.collatzcheckin.Event;
        import com.example.collatzcheckin.MainActivity;
        import com.example.collatzcheckin.R;
        import com.example.collatzcheckin.admin.controls.AdministratorDB;


public class AdminEventViewFragment extends Fragment {
    private AdministratorDB db;
    private View view;
    private Event event;

    Button backButton;
    Button deleteButton;
    TextView eventTitle;
    TextView eventDescription;
    TextView eventLocation;
    TextView eventMonth;
    TextView eventDay;
    TextView eventTime;

    public AdminEventViewFragment(Event event) {
        this.event = event;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        String[] words = event.getEventDate().split(" ");

        view = inflater.inflate(R.layout.fragment_admin_event_view, container, false);

        deleteButton = view.findViewById(R.id.delete_event); // check this
        backButton = view.findViewById(R.id.back_button_event_view);
        eventTitle = view.findViewById(R.id.event_title);
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
                administratorDB.removeEvent(event);
//                ((MainActivity) getActivity()).showEventList();
            }
        });

//        backButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ((MainActivity) getActivity()).showEventList();
//            }
//        });
        return view;

    }
}