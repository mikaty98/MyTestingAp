package com.example.mytestingapp.ui.FragmentSystem;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mytestingapp.Classes.SeekerRating;
import com.example.mytestingapp.R;
import com.example.mytestingapp.SeekerLocalRequest;
import com.example.mytestingapp.SeekerLocalRequestWaitingList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class HomeFragment extends Fragment {


    private Button localService;
    private Button localWaiting,cancelRequest;
    private TextView welcomeUser;
    private String seekerID, seekerEmail, seekerUserName;

    private DatabaseReference reference;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        localService = view.findViewById(R.id.localService);
        localWaiting = view.findViewById(R.id.localWaiting);
        cancelRequest = view.findViewById(R.id.cancelRequestBtn);
        welcomeUser = view.findViewById(R.id.welcomeUser);

        seekerID = FirebaseAuth.getInstance().getUid();

        getSeekerInfo();


        localService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                reference = FirebaseDatabase.getInstance().getReference().child("LocalRequests");
                reference.orderByChild("seekerID").equalTo(seekerID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            Toast.makeText(getActivity(), "You can't request two services at the same time!", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(getActivity(), "Loading...", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(getActivity(), SeekerLocalRequest.class);
                            intent.putExtra("seeker email", seekerEmail);
                            startActivity(intent);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });

        localWaiting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                reference = FirebaseDatabase.getInstance().getReference().child("LocalRequests");
                reference.orderByChild("seekerID").equalTo(seekerID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {


                        if (dataSnapshot.exists()) {
                            Toast.makeText(getActivity(), "Loading...", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(getActivity(), SeekerLocalRequestWaitingList.class);
                            intent.putExtra("seeker email", seekerEmail);
                            startActivity(intent);

                        } else {
                            Toast.makeText(getActivity(), "You didn't request a service yet", Toast.LENGTH_SHORT).show();

                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });


            }
        });

        cancelRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {


                reference = FirebaseDatabase.getInstance().getReference().child("LocalRequests");
                reference.orderByChild("seekerID").equalTo(seekerID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {


                        if (dataSnapshot.exists())
                        {
                            new AlertDialog.Builder(v.getContext())
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setTitle("Cancel Your Request")
                                    .setMessage("Are you sure you want to cancel your request?")
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which)
                                        {
                                            reference = FirebaseDatabase.getInstance().getReference().child("LocalRequests");
                                            reference.child(seekerID).removeValue();
                                        }

                                    })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which)
                                        {
                                            return;

                                        }
                                    })
                                    .show();


                        }
                        else {
                            Toast.makeText(getActivity(), "You didn't request a service yet", Toast.LENGTH_SHORT).show();

                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });


            }
        });




        return view;
    }

    private void getSeekerInfo() {
        reference = FirebaseDatabase.getInstance().getReference("Seekers");
        Query checkuser = reference.orderByChild("userID").equalTo(seekerID);
        checkuser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    seekerEmail = snapshot.child(seekerID).child("email").getValue(String.class);
                    seekerUserName = snapshot.child(seekerID).child("userName").getValue(String.class);
                    welcomeUser.setText("WELCOME " + seekerUserName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}