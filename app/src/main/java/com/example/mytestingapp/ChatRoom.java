package com.example.mytestingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.mytestingapp.Classes.Provider;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

public class ChatRoom extends AppCompatActivity {

    TextView username;
    ImageView imageView;

    FirebaseUser firebaseUser;
    DatabaseReference reference;
    Intent intent;

    private FirebaseStorage storage;
    private StorageReference storageReference;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        storage = FirebaseStorage.getInstance();



        imageView = findViewById(R.id.imageview_profile);
        username = findViewById(R.id.username1);

       // Toolbar toolbar = findViewById(R.id.toolbar2);
        //setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle("");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //toolbar.setNavigationOnClickListener(new View.OnClickListener() {
           // @Override
          //  public void onClick(View v) {
            //    finish();
            //}
        //});

        intent = getIntent();
        String providerId = intent.getStringExtra("provider id");

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Providers").child(providerId);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Provider provider = snapshot.getValue(Provider.class);
                username.setText(provider.getUserName());

                storageReference = FirebaseStorage.getInstance().getReference().child("images/"+providerId);
                final Bitmap[] bitmap = new Bitmap[1];
                try{
                    File localfile = File.createTempFile( provider.getId(),".jpg");
                    storageReference.getFile(localfile)
                            .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    bitmap[0] = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                                    imageView.setImageBitmap(bitmap[0]);
                                    provider.setImageBitmap(bitmap[0]);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            bitmap[0] = BitmapFactory.decodeFile("defaultProfilePic.jpeg");
                            imageView.setImageBitmap(bitmap[0]);
                            provider.setImageBitmap(bitmap[0]);
                        }
                    });

                }
                catch (Exception e){
                    bitmap[0] = BitmapFactory.decodeFile("app/defaultProfilePic.jpeg");
                    imageView.setImageBitmap(bitmap[0]);
                    provider.setImageBitmap(bitmap[0]);
                }

                imageView.setImageBitmap(provider.getImageBitmap());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setSupportActionBar(Toolbar toolbar) {

    }
}