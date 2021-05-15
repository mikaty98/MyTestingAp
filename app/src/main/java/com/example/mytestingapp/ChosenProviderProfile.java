package com.example.mytestingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mytestingapp.Classes.Provider;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

public class ChosenProviderProfile extends AppCompatActivity {

    private EditText username,jobDescription,gender,age,id,email,phoneNumber;
    private CircleImageView profilePic;

    private Button acceptButton, backButton;

    private FirebaseDatabase rootNode;
    private DatabaseReference reference;

    private FirebaseStorage storage;
    private StorageReference storageReference;

    private Provider provider = new Provider();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chosen_provider_profile);

        Intent intent = getIntent();
        String providerEmail = intent.getStringExtra("provider email");
        String seekerEmail = intent.getStringExtra("seeker email");
        String userID = intent.getStringExtra("userID");

        int estimatedArrivaltime = intent.getIntExtra("estimatedArrivalTime", 1);
        int estimatedCompletionTime = intent.getIntExtra("estimatedCompletionTime", 1);
        int price = intent.getIntExtra("price", 1);



        String parts[] = providerEmail.split(".com");
        String trimmedEmail = parts[0];

        username = findViewById(R.id.username);
        jobDescription = findViewById(R.id.job_description);
        gender = findViewById(R.id.gender);
        age = findViewById(R.id.age);
        id = findViewById(R.id.id);
        email = findViewById(R.id.email);
        phoneNumber = findViewById(R.id.phone_number);
        profilePic = findViewById(R.id.profilePic);

        acceptButton = findViewById(R.id.acceptButton);
        backButton = findViewById(R.id.backButton);



        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("Providers");

        Query checkUser = reference.orderByChild("userID").equalTo(userID);



        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    provider.setEmail(snapshot.child(userID).child("email").getValue().toString());
                    provider.setId(snapshot.child(userID).child("id").getValue(String.class));
                    provider.setUserName(snapshot.child(userID).child("userName").getValue(String.class));
                    provider.setJobDesc(snapshot.child(userID).child("jobDesc").getValue(String.class));
                    provider.setGender(snapshot.child(userID).child("gender").getValue(String.class));
                    provider.setAge(snapshot.child(userID).child("age").getValue(String.class));
                    provider.setPhoneNumber(snapshot.child(userID).child("phoneNumber").getValue(String.class));


                    storageReference = FirebaseStorage.getInstance().getReference().child("images/"+userID);
                    final Bitmap[] bitmap = new Bitmap[1];
                    try{
                        File localfile = File.createTempFile( provider.getId(),".jpg");
                        storageReference.getFile(localfile)
                                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                        bitmap[0] = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                                        profilePic.setImageBitmap(bitmap[0]);
                                        provider.setImageBitmap(bitmap[0]);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                bitmap[0] = BitmapFactory.decodeFile("defaultProfilePic.jpeg");
                                profilePic.setImageBitmap(bitmap[0]);
                                provider.setImageBitmap(bitmap[0]);
                            }
                        });

                    }
                    catch (Exception e){
                        bitmap[0] = BitmapFactory.decodeFile("app/defaultProfilePic.jpeg");
                        profilePic.setImageBitmap(bitmap[0]);
                        provider.setImageBitmap(bitmap[0]);
                    }


                    username.setText("User Name:   "+provider.getUserName());
                    jobDescription.setText("Job Description:   "+provider.getJobDesc());
                    gender.setText("Gender:   "+provider.getGender());


                    age.setText("Age:   "+provider.getAge());
                    id.setText("ID Number:   "+provider.getId());
                    email.setText("Email:   "+provider.getEmail()+".com");
                    phoneNumber.setText("Phone Number:   "+provider.getPhoneNumber());

                    profilePic.setImageBitmap(provider.getImageBitmap());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                // go to chat room



            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });

    }






}