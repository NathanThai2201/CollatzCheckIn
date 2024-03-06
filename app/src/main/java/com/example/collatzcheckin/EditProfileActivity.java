package com.example.collatzcheckin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.UUID;

public class EditProfileActivity extends AppCompatActivity {


    Button cancel;
    Button confirm;
    ShapeableImageView pfp;
    Uri imagePath;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_edit);

        cancel = findViewById(R.id.cancel_button);
        confirm = findViewById(R.id.confirm_button);
        pfp = findViewById(R.id.editpfp);
        Intent intent = getIntent();
        User user = (User) intent.getSerializableExtra("user");
        EditText name = findViewById(R.id.editName);
        EditText username = findViewById(R.id.editUsername);
        EditText email = findViewById(R.id.editEmail);
        if (!user.getName().equals("")){
            name.setText(user.getName());
        }
        if (!user.getUsername().equals("")){
            username.setText(user.getUsername());
        }
        if (!user.getEmail().equals("")){
            email.setText(user.getEmail());
        }
        if (user.getPfp()!=null){
            pfp.setImageURI(Uri.parse(user.getPfp()));

        }
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String newName = name.getText().toString();
                user.setName(newName);
                String newUsername = username.getText().toString();
                user.setUsername(newUsername);
                String newEmail = email.getText().toString();
                user.setEmail(newEmail);


                if (imagePath!=null) {
                    FirebaseStorage.getInstance().getReference("images/" + user.getUsername()).putFile(imagePath).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(EditProfileActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    user.setPfp(imagePath.toString());
                }

                Intent resultIntent = new Intent();
                resultIntent.putExtra("updatedUser", user);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

        pfp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoIntent = new Intent(Intent.ACTION_GET_CONTENT);
                photoIntent.setType("image/*");
                startActivityForResult(photoIntent,1);
            }
        });
    }
    @Override
    protected void onActivityResult(int request_Code, int resultCode, @Nullable Intent data) {
        super.onActivityResult(request_Code, resultCode, data);
        if (request_Code == 1 && resultCode == RESULT_OK && data != null) {
            imagePath = data.getData();
            getImageInImageView();
        }
    }
    private void getImageInImageView() {
        Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imagePath);
            } catch (IOException e) {
                e.printStackTrace();
        }
        pfp.setImageBitmap(bitmap);


        }
    }


