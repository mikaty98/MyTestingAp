package com.example.mytestingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.mytestingapp.Classes.Provider;
import com.example.mytestingapp.Classes.Seeker;
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
import java.util.HashMap;

public class ChatRoom extends AppCompatActivity {

    TextView username;
    ImageView imageView;

    EditText msg_text;
    RecyclerView recyclerView;
    ImageButton SendBtn;


    FirebaseUser firebaseUser;
    DatabaseReference reference;
    DatabaseReference reference1;
    Intent intent;

    private FirebaseStorage storage;
    private StorageReference storageReference;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        storage = FirebaseStorage.getInstance();

        SendBtn = findViewById(R.id.btn_send);
        msg_text = findViewById(R.id.text_send);



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
        String receiverId = intent.getStringExtra("receiver id");

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        reference = FirebaseDatabase.getInstance().getReference("Providers").child(receiverId);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists())
                {

                    Provider provider = snapshot.getValue(Provider.class);
                    username.setText(provider.getUserName());

                    storageReference = FirebaseStorage.getInstance().getReference().child("images/"+receiverId);
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



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        reference1 = FirebaseDatabase.getInstance().getReference("Seekers").child(receiverId);


        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists())
                {

                    Seeker seeker = snapshot.getValue(Seeker.class);
                    username.setText(seeker.getUserName());

                    storageReference = FirebaseStorage.getInstance().getReference().child("images/"+receiverId);
                    final Bitmap[] bitmap = new Bitmap[1];
                    try{
                        File localfile = File.createTempFile(seeker.getId(),".jpg");
                        storageReference.getFile(localfile)
                                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                        bitmap[0] = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                                        imageView.setImageBitmap(bitmap[0]);
                                        seeker.setImageBitmap(bitmap[0]);

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                bitmap[0] = BitmapFactory.decodeFile("defaultProfilePic.jpeg");
                                imageView.setImageBitmap(bitmap[0]);
                                seeker.setImageBitmap(bitmap[0]);
                            }
                        });

                    }
                    catch (Exception e){
                        bitmap[0] = BitmapFactory.decodeFile("app/defaultProfilePic.jpeg");
                        imageView.setImageBitmap(bitmap[0]);
                        seeker.setImageBitmap(bitmap[0]);
                    }

                    imageView.setImageBitmap(seeker.getImageBitmap());

                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        SendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = msg_text.getText().toString();

                if(!msg.equals(""))
                {
                    sendMessage(firebaseUser.getUid(), receiverId, msg);
                }
                else
                {
                    Toast.makeText(ChatRoom.this, "You can't send an empty message!", Toast.LENGTH_SHORT).show();
                }

                msg_text.setText("");
            }
        });



    }


    private void sendMessage(String sender, String receiver, String message)
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);

        reference.child("Chats").push().setValue(hashMap);




    }


}