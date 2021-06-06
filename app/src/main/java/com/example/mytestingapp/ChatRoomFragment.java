package com.example.mytestingapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
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

import com.example.mytestingapp.Adapters.MessageAdapter;
import com.example.mytestingapp.Classes.Chats;
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

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatRoomFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatRoomFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    String receiverId;

    TextView username;
    ImageView imageView;

    EditText msg_text;
    RecyclerView recyclerView;
    ImageButton SendBtn;
    int flag = 0;


    FirebaseUser firebaseUser;
    DatabaseReference reference;
    DatabaseReference reference1;

    private FirebaseStorage storage;
    private StorageReference storageReference;


    List<Chats> chatsList;
    MessageAdapter messageAdapter;








    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ChatRoomFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChatRoomFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatRoomFragment newInstance(String param1, String param2) {
        ChatRoomFragment fragment = new ChatRoomFragment();
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

        View view = inflater.inflate(R.layout.fragment_chat_room, container, false);


        LocalRequestEnd1 localRequestEnd1 = (LocalRequestEnd1) getActivity();
        receiverId = localRequestEnd1.getMyData();

        storage = FirebaseStorage.getInstance();

        SendBtn = view.findViewById(R.id.btn_send);
        msg_text = view.findViewById(R.id.text_send);



        imageView = view.findViewById(R.id.imageview_profile);
        username = view.findViewById(R.id.username1);

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

        recyclerView = view.findViewById(R.id.recyclerview_messages);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        reference = FirebaseDatabase.getInstance().getReference("Providers").child(receiverId);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists())
                {

                    flag = 1;

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


                    readMessages(receiverId);


                }




            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if (flag == 0)
        {

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


        }





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
                    //Toast.makeText(ChatRoom.this, "You can't send an empty message!", Toast.LENGTH_SHORT).show();
                }

                msg_text.setText("");
            }
        });




        // Inflate the layout for this fragment
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

                        messageAdapter = new MessageAdapter(ChatRoomFragment.this, chatsList, receiverId);

                        recyclerView.setAdapter(messageAdapter);
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