package com.example.collatzcheckin.event;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.collatzcheckin.R;
import com.example.collatzcheckin.attendee.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class AttendeeArrayAdapter extends ArrayAdapter<HashMap<String,String>> {
    private ArrayList<HashMap<String,String>> attendees;
    private Context context;


    /**
     * Constructor for EventArrayAdapter
     * @param context
     * @param attendees
     */
    public AttendeeArrayAdapter(Context context, ArrayList<HashMap<String,String>> attendees) {
        super(context, 0, attendees);
        this.attendees = attendees;
        this.context = context;
    }
    /**
     * Constructor for EventArrayAdapter
     * @param position
     * @param convertView
     * @param parent
     * @return View
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.attendee_list_content, parent,false);
        }

        HashMap<String, String> attendee = attendees.get(position);
        Set<String> keySet = attendee.keySet();
        String name = keySet.iterator().next();
        String count = attendee.get(name);

        //Updates textviews in the listview with correct text for an event
        TextView attendeeName = view.findViewById(R.id.attendee_name);
        TextView checkInCount = view.findViewById(R.id.attendee_checkin_count);

        attendeeName.setText(name);
        checkInCount.setText(count);

        return view;
    }
}
