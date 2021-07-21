package com.example.mytestingapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mytestingapp.Adapters.MessageAdapterProvider;
import com.example.mytestingapp.Adapters.ProviderMessageAdapter;
import com.example.mytestingapp.Classes.Chats;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProviderChatRoomFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProviderChatRoomFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    String receiverId;


    TextView username;
    ImageView imageView;

    EditText msg_text;
    RecyclerView recyclerView;
    ImageButton SendBtn, callBtn, infoBtn, userBtn;
    int flag = 0;

    int flagg = 0;

    FirebaseUser firebaseUser, firebaseUser1;
    DatabaseReference reference,reference5, reference6;
    DatabaseReference reference1;
    Intent intent;

    private FirebaseStorage storage;
    private StorageReference storageReference;


    List<Chats> chatsList;
    ProviderMessageAdapter messageAdapterProvider;


    private FirebaseDatabase rootNode;
    private DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference().child("SeekerLocalRequestArrivalConfirm");
    private DatabaseReference reference3;


    private FirebaseAuth mauth;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProviderChatRoomFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProviderChatRoomFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProviderChatRoomFragment newInstance(String param1, String param2) {
        ProviderChatRoomFragment fragment = new ProviderChatRoomFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_provider_chat_room, container, false);

        ProviderLocalRequestEnd1 providerLocalRequestEnd1 = (ProviderLocalRequestEnd1) getActivity();

        receiverId = providerLocalRequestEnd1.getReceiverId();
        int arrivalTime = providerLocalRequestEnd1.getArrivalTime();
        int completionTime = providerLocalRequestEnd1.getCompletionTime();
        int price = providerLocalRequestEnd1.getPrice();
        String userType = providerLocalRequestEnd1.getUserType();

        storage = FirebaseStorage.getInstance();

        SendBtn = view.findViewById(R.id.btn_send);
        callBtn = view.findViewById(R.id.callBtn);
        userBtn = view.findViewById(R.id.userBtn);
        msg_text = view.findViewById(R.id.text_send);

        imageView = view.findViewById(R.id.imageview_profile);
        username = view.findViewById(R.id.username1);

        infoBtn = view.findViewById(R.id.infoBtn);


        recyclerView = view.findViewById(R.id.recyclerview_messages);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);


        // user got accepted

        firebaseUser1 = FirebaseAuth.getInstance().getCurrentUser();
        String myId = firebaseUser1.getUid();
        DatabaseReference mref2 = FirebaseDatabase.getInstance().getReference("Providers");
        mref2.child(myId).child("gotAccepted").setValue(true);

        //delete proposals

        reference5 = FirebaseDatabase.getInstance().getReference("LocalRequestsProposals").child(receiverId);
        reference5.removeValue();

        reference6 = FirebaseDatabase.getInstance().getReference("LocalRequests").child(receiverId);
        reference6.child("picked").setValue("yes");



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
                   // Toast.makeText(ChatRoom.this, "You can't send an empty message!", Toast.LENGTH_SHORT).show();
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



                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (!getActivity().isFinishing()){
                                    new AlertDialog.Builder(getContext())
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



                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (!getActivity().isFinishing()){
                                    new AlertDialog.Builder(getContext())
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

        return view;
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

                        messageAdapterProvider = new ProviderMessageAdapter(ProviderChatRoomFragment.this, chatsList, receiverId);

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