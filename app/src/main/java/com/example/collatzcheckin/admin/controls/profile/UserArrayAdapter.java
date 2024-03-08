package com.example.collatzcheckin.admin.controls.profile;

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

public class UserArrayAdapter extends ArrayAdapter<User> {
    private ArrayList<User> users;
    private Context context;

    public UserArrayAdapter(Context context, ArrayList<User> users) {
        super(context,0, users);
        this.users = users;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        return super.getView(position, convertView, parent);
        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.list_item_user, parent,false);
        }
        User user = users.get(position);

        TextView userName = view.findViewById(R.id.user_name_display);

        //String[] wordList = event.getEventDate().split(" ");
        //String parsedString = wordList[0] + " " + wordList[1] + ", " + wordList[wordList.length - 1];

        userName.setText(user.getName());
        return view;
    }
}
