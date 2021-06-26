package com.example.mytestingapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mytestingapp.Adapters.ProviderReviewAdaptor;
import com.example.mytestingapp.Adapters.SeekerReviewAdaptor;
import com.example.mytestingapp.Classes.ProviderRating;
import com.example.mytestingapp.Classes.SeekerRating;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ProviderReviewListActivity extends AppCompatActivity {

    private String providerID,providerName;
    private TextView providerNameText;
    private ListView listView;
    List<SeekerRating> seekerRatingList = new ArrayList<>();
    DatabaseReference reference;
    SeekerReviewAdaptor seekerReviewAdaptor;

    ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            String seekerName = snapshot.child("seekerName").getValue(String.class);
            String review = snapshot.child("review").getValue(String.class);
            float rating = snapshot.child("starNumber").getValue(float.class);
            SeekerRating seekerRating = new SeekerRating(seekerName, providerID, review, rating);
            seekerRatingList.add(seekerRating);
            seekerReviewAdaptor.notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            seekerReviewAdaptor.notifyDataSetChanged();
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
        setContentView(R.layout.activity_provider_review_list);

        providerID = getIntent().getStringExtra("provider id");
        providerName = getIntent().getStringExtra("provider Name");

        providerNameText = findViewById(R.id.providerNameText);
        providerNameText.setText("Reviews on " + providerName);

        listView = findViewById(R.id.providerReviewsList);
        seekerReviewAdaptor = new SeekerReviewAdaptor(this, seekerRatingList);
        listView.setAdapter(seekerReviewAdaptor);

        reference = FirebaseDatabase.getInstance().getReference("Providers").child(providerID).child("ratings");
        reference.keepSynced(true);

        reference.addChildEventListener(childEventListener);

    }
}