package com.example.collatzcheckin;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
/**
 *  This class extends ArrayAdapter to work with Event data types
 */
public class EventArrayAdapter extends ArrayAdapter<Event> {
    private ArrayList<Event> events;
    private Context context;


    /**
     * Constructor for EventArrayAdapter
     * @param context
     * @param events
            */
    public EventArrayAdapter(Context context, ArrayList<Event> events) {
        super(context, 0, events);
        this.events = events;
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
            view = LayoutInflater.from(context).inflate(R.layout.event_list_content, parent,false);
        }

        Event event = events.get(position);

        //Updates textviews in the listview with correct text for an event
        TextView eventTitle = view.findViewById(R.id.event_list_title);
        TextView eventDate = view.findViewById(R.id.event_list_date);

        //Parse date string
        String[] wordList = event.getEventDate().split(" ");
        String parsedString = "";

        if (wordList.length >= 2) {
            parsedString = wordList[0] + " " + wordList[1] + ", " + wordList[wordList.length - 1];
        } else {
            // Handle the case where wordList doesn't have enough elements
        }

        eventTitle.setText(event.getEventTitle());
        eventDate.setText(parsedString);
        return view;
    }
}
