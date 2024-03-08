package com.example.collatzcheckin.admin.controls.profile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.collatzcheckin.MainActivity;
import com.example.collatzcheckin.R;
import com.example.collatzcheckin.attendee.AttendeeDB;
import com.example.collatzcheckin.attendee.User;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class UserListFragment extends Fragment {
    ListView userList;

    ArrayAdapter<User> userArrayAdapter;

    ArrayList<User> userDataList;

    private View view;

    AttendeeDB db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_admin_user_list, container, false);
        db = new AttendeeDB();

        userList = view.findViewById(R.id.user_list_view);
        userDataList = new ArrayList<>();
        userArrayAdapter = new UserArrayAdapter(getContext(), userDataList);
        userList.setAdapter(userArrayAdapter);

        db.userRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot querySnapshots, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("Firestore", error.toString());
                    return;
                }
                if (querySnapshots != null) {
                   userDataList.clear();
                    for (QueryDocumentSnapshot doc : querySnapshots) {
                        String user_id = doc.getId();
                        String username = doc.getString("Name");
                        String uid = doc.getString("Uid");
                        String email = doc.getString("Email");


                       userDataList.add(new User(uid, username, email));
                    }

                    userArrayAdapter.notifyDataSetChanged();
                }
            }
        });

//        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3) {
//                User user = (User) adapter.getItemAtPosition(position);
//                // Handle user click, for example:
//                // Show user details or navigate to user profile
//                ((MainActivity) getActivity()).showAdminUserView(user);
//            }
//        });

        return view;
    }
}

