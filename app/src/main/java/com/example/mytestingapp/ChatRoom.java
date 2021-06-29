package com.example.mytestingapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.mytestingapp.Adapters.MessageAdapter;
import com.example.mytestingapp.Adapters.MessageAdapterProvider;
import com.example.mytestingapp.Classes.Chats;
import com.example.mytestingapp.Classes.ConnectedSandP;
import com.example.mytestingapp.Classes.Provider;
import com.example.mytestingapp.Classes.Seeker;
import com.example.mytestingapp.Classes.SeekerLocalRequestArrivalConfirm;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
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
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Single;

public class ChatRoom extends AppCompatActivity {

    TextView username;
    ImageView imageView;

    EditText msg_text;
    RecyclerView recyclerView;
    ImageButton SendBtn, callBtn, infoBtn, userBtn;
    int flag = 0;

    int flagg = 0;

    FirebaseUser firebaseUser, firebaseUser1;
    DatabaseReference reference;
    DatabaseReference reference1;
    Intent intent;

    private FirebaseStorage storage;
    private StorageReference storageReference;


    List<Chats> chatsList;
    MessageAdapterProvider messageAdapterProvider;


    private FirebaseDatabase rootNode;
    private DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference().child("SeekerLocalRequestArrivalConfirm");
    private DatabaseReference reference3;


    private FirebaseAuth mauth;








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        storage = FirebaseStorage.getInstance();

        SendBtn = findViewById(R.id.btn_send);
        callBtn = findViewById(R.id.callBtn);
        userBtn = findViewById(R.id.userBtn);
        msg_text = findViewById(R.id.text_send);

        imageView = findViewById(R.id.imageview_profile);
        username = findViewById(R.id.username1);

        infoBtn = findViewById(R.id.infoBtn);


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

        recyclerView = findViewById(R.id.recyclerview_messages);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);





        intent = getIntent();
        String receiverId = intent.getStringExtra("receiver id");
        int arrivalTime = intent.getIntExtra("arrival time", 60);
        int completionTime = intent.getIntExtra("completion time", 60);
        int price = intent.getIntExtra("price", 0);
        String userType = intent.getStringExtra("user type");

        firebaseUser1 = FirebaseAuth.getInstance().getCurrentUser();

        String myId = firebaseUser1.getUid();


        DatabaseReference mref2 = FirebaseDatabase.getInstance().getReference("Providers");

        mref2.child(myId).child("gotAccepted").setValue(true);





        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

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

                    readMessages(receiverId);


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

        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                DatabaseReference mref = FirebaseDatabase.getInstance().getReference("Seekers").child(receiverId).child("phoneNumber");
                mref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String phoneNumber = dataSnapshot.getValue(String.class);

                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" +phoneNumber));
                        startActivity(intent);


                        //do what you want with the likes
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });

        infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                DatabaseReference mref1 = FirebaseDatabase.getInstance().getReference("LocalRequests").child(receiverId);
                mref1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        String buildingName = dataSnapshot.child("buildingName").getValue(String.class);
                        String buildingNumber = dataSnapshot.child("buildingNumber").getValue(String.class);
                        String apartmentNumber = dataSnapshot.child("apartmentNumber").getValue(String.class);
                        String floorNumber = dataSnapshot.child("floorNumber").getValue(String.class);
                        String streetName = dataSnapshot.child("streetName").getValue(String.class);
                        String streetNumber = dataSnapshot.child("streetNumber").getValue(String.class);



                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (!isFinishing()){
                                    new AlertDialog.Builder(ChatRoom.this)
                                            .setTitle("Request Location Details")
                                            .setMessage("Street Name:  "+streetName+"\n\n"+ "Street Number:  "+streetNumber+"\n\n"+
                                                    "Building Name:  "+buildingName+"\n\n"+ "Building Number:  "+buildingNumber+"\n\n"+
                                                    "Apartment Number:  "+apartmentNumber+"\n\n" + "Floor Number:  "+floorNumber+"\n\n"
                                            )
                                            .setCancelable(false)
                                            .setPositiveButton("Got it", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which)
                                                {

                                                }
                                            }).show();
                                }
                            }
                        });



                        //do what you want with the likes
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        });


        userBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                DatabaseReference mref1 = FirebaseDatabase.getInstance().getReference("Seekers").child(receiverId);
                mref1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        String userName = dataSnapshot.child("userName").getValue(String.class);
                        String userEmail = dataSnapshot.child("email").getValue(String.class);

                        String birthDay = dataSnapshot.child("birthDay").getValue(String.class);
                        String birthMonth = dataSnapshot.child("birthMonth").getValue(String.class);
                        String birthYear = dataSnapshot.child("birthYear").getValue(String.class);

                        String birthDate = birthDay+"/"+birthMonth+"/"+birthYear;


                        String IdNumber = dataSnapshot.child("id").getValue(String.class);
                        String userGender = dataSnapshot.child("gender").getValue(String.class);
                        String phoneNumber = dataSnapshot.child("phoneNumber").getValue(String.class);



                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (!isFinishing()){
                                    new AlertDialog.Builder(ChatRoom.this)
                                            .setTitle("Service Seeker Details")
                                            .setMessage("Name:  "+userName+"\n\n"+ "Email:  "+userEmail+"\n\n"+
                                                    "Gender:  "+userGender+"\n\n" + "Birth Date:  "+birthDate+"\n\n" +
                                                    "Phone Number:  "+phoneNumber+"\n\n" +"ID Number:  "+IdNumber+"\n\n"
                                            )
                                            .setCancelable(false)
                                            .setPositiveButton("Got it", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which)
                                                {

                                                }
                                            }).show();
                                }
                            }
                        });



                        //do what you want with the likes
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        });


        SeekerLocalRequestArrivalConfirm seekerLocalRequestArrivalConfirm = new SeekerLocalRequestArrivalConfirm(receiverId);
        reference3 = FirebaseDatabase.getInstance().getReference("SeekerLocalRequestArrivalConfirm").child(receiverId);



        reference3.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName)
            {
                if (snapshot.exists())
                {
                    //Toast.makeText(ChatRoom.this,"DONEEEEEEEEEEEEEEEEEEEEEE",Toast.LENGTH_LONG).show();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (!isFinishing()){
                                new AlertDialog.Builder(ChatRoom.this)
                                        .setTitle("Arrival Confirmation")
                                        .setMessage("Please confirm your arrival")
                                        .setCancelable(false)
                                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which)
                                            {
                                                reference3.child("flag").setValue(1);

                                                Intent intent = new Intent(ChatRoom.this, ProviderLocalRequestEndBuffer2.class);
                                                intent.putExtra("receiver id", receiverId);
                                                intent.putExtra("completion time", completionTime);
                                                intent.putExtra("user type", userType);

                                                startActivity(intent);
                                                finish();
                                            }
                                        }).show();
                            }
                        }
                    });
                }
                else
                {
                   // Toast.makeText(ChatRoom.this,"NOOOOOOOOOOOOOOOOO",Toast.LENGTH_LONG).show();
                }


            }




            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }

    private void readMessages(String receiverId)
    {
         chatsList = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");


        reference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {

                if(snapshot.exists())
                {
                    chatsList.clear();

                    for (DataSnapshot ds: snapshot.getChildren())
                    {

                        Chats chats = ds.getValue(Chats.class);

                        if (chats.getSender().equals(firebaseUser.getUid()) && chats.getReceiver().equals(receiverId) ||
                               chats.getSender().equals(receiverId) && chats.getReceiver().equals(firebaseUser.getUid()))
                        {
                            chatsList.add(chats);
                        }

                        messageAdapterProvider = new MessageAdapterProvider(ChatRoom.this, chatsList, receiverId);

                        recyclerView.setAdapter(messageAdapterProvider);
                    }

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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