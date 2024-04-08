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

import com.example.collatzcheckin.AdminMainActivity;
import com.example.collatzcheckin.MainActivity;
import com.example.collatzcheckin.R;
import com.example.collatzcheckin.attendee.AttendeeDB;
import com.example.collatzcheckin.attendee.User;
import com.example.collatzcheckin.event.Event;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * The UserListFragment displays a list of users for administrative purposes
 * It retrieves user data from Firestore and populates the list accordingly
 */
public class UserListFragment extends Fragment {
    ListView userList;

    ArrayAdapter<User> userArrayAdapter;

    ArrayList<User> userDataList;

    private View view;

    AttendeeDB db;

    /**
     * Method to run on creation of the fragment. Handles admin browse profiles
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
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
                        List<String> eventIds = (List<String>) doc.get("Events");
                        String pfp = doc.getString("Pfp");



                       userDataList.add(new User(uid, username, email, eventIds, pfp));

                    }

                    userArrayAdapter.notifyDataSetChanged();
                }
            }
        });

        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3) {
                // Retrieve the user object associated with the clicked item
                User user = (User) adapter.getItemAtPosition(position);
                Log.d("UserListFragment", "Clicked user: " + user);
                Log.d("UserListFragment", "Clicked user events: " + user.getEventIds());
                if (user != null) {
                    Log.d("UserListFragment", "User details: Name: " + user.getName() + ", UID: " + user.getUid() + ", Email: " + user.getEmail());
                } else {
                    Log.e("UserListFragment", "Null user object");
                }
                // Create a new instance of UserViewFragment with the selected user
                UserViewFragment userViewFragment = new UserViewFragment(user);

                // Replace the current fragment in MainActivity with UserViewFragment
                ((AdminMainActivity) requireActivity()).replaceFragment(userViewFragment);
            }
        });

        return view;
    }
}

