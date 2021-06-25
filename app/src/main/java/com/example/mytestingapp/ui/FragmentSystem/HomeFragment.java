package com.example.mytestingapp.ui.FragmentSystem;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mytestingapp.Classes.SeekerRating;
import com.example.mytestingapp.R;
import com.example.mytestingapp.SeekerGlobalRequest;
import com.example.mytestingapp.SeekerGlobalRequestWaitingList;
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
    private Button localWaiting;
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
        welcomeUser = view.findViewById(R.id.welcomeUser);

        seekerID = FirebaseAuth.getInstance().getUid();

        getSeekerInfo();


        localService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                reference = FirebaseDatabase.getInstance().getReference().child("LocalRequests");
                reference.orderByChild("seekerEmail").equalTo(seekerEmail).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {


                        if (dataSnapshot.exists()) {
                            Toast.makeText(getActivity(), "You can't request two local services at the same time!", Toast.LENGTH_SHORT).show();


                        } else {
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
                reference.orderByChild("seekerEmail").equalTo(seekerEmail).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {


                        if (dataSnapshot.exists()) {
                            Toast.makeText(getActivity(), "Loading...", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(getActivity(), SeekerLocalRequestWaitingList.class);
                            intent.putExtra("seeker email", seekerEmail);
                            startActivity(intent);
                            getActivity().finish();

                        } else {
                            Toast.makeText(getActivity(), "You didn't request a local service yet", Toast.LENGTH_SHORT).show();

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
                    welcomeUser.setText("Welcome " + seekerUserName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}