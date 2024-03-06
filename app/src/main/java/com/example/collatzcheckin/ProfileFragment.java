package com.example.collatzcheckin;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.Serializable;

public class ProfileFragment extends Fragment {

    Button update;
    Button remove;
    User user;

    public ProfileFragment(){
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, container, false);

        update = view.findViewById(R.id.up_button);
        remove = view.findViewById(R.id.remove);
        TextView name = view.findViewById(R.id.nameText);
        TextView username = view.findViewById(R.id.usernameText);
        TextView email = view.findViewById(R.id.emailText);
        ImageView pfp = view.findViewById(R.id.pfp);


        ActivityResultLauncher<Intent> launchEditProfileActivity = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {

                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        assert data != null;
                        User updatedUser = (User) data.getSerializableExtra("updatedUser");
                        assert updatedUser != null;
                        name.setText(updatedUser.getName());
                        username.setText(updatedUser.getUsername());
                        email.setText(updatedUser.getEmail());
                        if (updatedUser.getPfp() != null) {
                            pfp.setImageURI(Uri.parse(updatedUser.getPfp()));
                        }
                    }
                }
        );



        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                String user_name = name.getText().toString();
                String user_username = username.getText().toString();
                String user_email = email.getText().toString();
                user = new User(user_name,user_username,user_email);
                intent.putExtra("user", (Serializable) user);
                launchEditProfileActivity.launch(intent);

            }
        });
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pfp.setImageResource(R.drawable.baseline_person_24);
                StorageReference imageRef = FirebaseStorage.getInstance().getReference().child("images/" + user.getUsername());
                imageRef.delete();

            }
        });
        return view;
    }
}
