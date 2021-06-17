package com.example.mytestingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.mytestingapp.Classes.Seeker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

public class SeekerEditProfileActivity extends AppCompatActivity {
    private String userID;
    private EditText username, gender, age, id, phoneNumber;
    private CircleImageView profilePic;
    private Button saveBtn;
    private DatabaseReference reference;
    private StorageReference storageReference;
    private Seeker seeker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seeker_edit_profile);

        profilePic = findViewById(R.id.profilePic);
        username = findViewById(R.id.username_reg);
        gender = findViewById(R.id.gender_reg);
        age = findViewById(R.id.age_reg);
        id = findViewById(R.id.id_reg);
        phoneNumber = findViewById(R.id.phone_number_reg);

        saveBtn = findViewById(R.id.saveBtn);

        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        
        reference = FirebaseDatabase.getInstance().getReference("Seekers");
        Query checkUser = reference.orderByChild("userID").equalTo(userID);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    seeker = snapshot.child(userID).getValue(Seeker.class);
                    getProfilePic();
                    username.setText(seeker.getUserName());
                    gender.setText(seeker.getGender());
                    age.setText(seeker.getAge());
                    id.setText(seeker.getId());
                    phoneNumber.setText(seeker.getPhoneNumber());
                    profilePic.setImageBitmap(seeker.getImageBitmap());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference.child(userID).setValue(seeker);
                
            }
        });
    }

    private void getProfilePic() {
        storageReference = FirebaseStorage.getInstance().getReference().child("images/" + userID);
        final Bitmap[] bitmap = new Bitmap[1];
        try {
            File localfile = File.createTempFile(userID, ".jpg");
            storageReference.getFile(localfile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            bitmap[0] = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                            profilePic.setImageBitmap(bitmap[0]);
                            seeker.setImageBitmap(bitmap[0]);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    bitmap[0] = BitmapFactory.decodeFile("defaultProfilePic.jpeg");
                    profilePic.setImageBitmap(bitmap[0]);
                    seeker.setImageBitmap(bitmap[0]);
                }
            });

        } catch (Exception e) {
            bitmap[0] = BitmapFactory.decodeFile("app/defaultProfilePic.jpeg");
            profilePic.setImageBitmap(bitmap[0]);
            seeker.setImageBitmap(bitmap[0]);
        }

    }

}