package com.example.mytestingapp.ui.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.mytestingapp.LocalRequest;
import com.example.mytestingapp.R;
import com.example.mytestingapp.ServiceAdaptor;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class ExploreFragment extends Fragment {


    ListView listView;
    List<String> serviceTitle = new ArrayList<>();
    List<String> locationList = new ArrayList<>();
    List<String> seekerEmail = new ArrayList<>();


    DatabaseReference reference;

    public ExploreFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explore, container, false);


        ServiceAdaptor serviceAdaptor = new ServiceAdaptor(getActivity(), serviceTitle,locationList,seekerEmail);
        listView = view.findViewById(R.id.serviceList);
        listView.setAdapter(serviceAdaptor);


        List<LocalRequest> localRequestList = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("LocalRequests");

        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String title = snapshot.child("requestTitle").getValue(String.class);
                String location = snapshot.child("suburb").getValue(String.class);
                String email = snapshot.child("city").getValue(String.class); //TODO change child value

                serviceTitle.add(title);
                locationList.add(location);
                seekerEmail.add(email);
                serviceAdaptor.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                serviceAdaptor.notifyDataSetChanged();
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



        return view;
    }
}