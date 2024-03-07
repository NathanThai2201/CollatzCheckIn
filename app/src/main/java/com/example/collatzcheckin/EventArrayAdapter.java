package com.example.collatzcheckin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class EventArrayAdapter extends ArrayAdapter<Event> {
    private ArrayList<Event> events;
    private Context context;

    public EventArrayAdapter(Context context, ArrayList<Event> events) {
        super(context, 0, events);
        this.events = events;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        return super.getView(position, convertView, parent);
        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.event_list_content, parent,false);
        }

        Event event = events.get(position);

        TextView eventTitle = view.findViewById(R.id.event_list_title);
        TextView eventDate = view.findViewById(R.id.event_list_date);

        String[] wordList = event.getEventDate().split(" ");
        String parsedString = wordList[1] + " " + wordList[2] + ", " + wordList[wordList.length - 1];

        eventTitle.setText(event.getEventTitle());
        eventDate.setText(parsedString);
        return view;
    }
}
