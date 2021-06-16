package com.example.mytestingapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mytestingapp.Adapters.ProviderReviewAdaptor;
import com.example.mytestingapp.Classes.ProviderRating;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class SeekerReviewListActivity extends AppCompatActivity {

    private String seekerID, seekerEmail;
    private TextView seekerName;
    private ListView listView;
    List<ProviderRating> ProvidersRatingList = new ArrayList<>();
    DatabaseReference reference;
    ProviderReviewAdaptor providerReviewAdaptor;

    ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            //ProviderRating providerRating = snapshot.getValue(ProviderRating.class);
            String providerEmail = snapshot.child("providerEmail").getValue(String.class);
            String review = snapshot.child("review").getValue(String.class);
            float rating = snapshot.child("starNumber").getValue(float.class);
            ProviderRating providerRating = new ProviderRating(seekerID,providerEmail,review,rating);
            ProvidersRatingList.add(providerRating);
            providerReviewAdaptor.notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            providerReviewAdaptor.notifyDataSetChanged();
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
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seeker_review_list);

        seekerID = getIntent().getStringExtra("seeker id");
        seekerEmail = getIntent().getStringExtra("seeker email");

        seekerName = findViewById(R.id.seekerEmailText);
        seekerName.setText("Reviews on " + seekerEmail);

        listView = findViewById(R.id.seekerReviewsList);
        providerReviewAdaptor = new ProviderReviewAdaptor(this, ProvidersRatingList);
        listView.setAdapter(providerReviewAdaptor);

        reference = FirebaseDatabase.getInstance().getReference("Seekers").child(seekerID).child("ratings");
        reference.keepSynced(true);

        reference.addChildEventListener(childEventListener);


    }
}