package com.example.mytestingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import com.example.mytestingapp.Classes.LocalRequest;
import com.example.mytestingapp.Classes.SeekerRating;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SeekerRatingSubmit extends AppCompatActivity {


    String providerId;
    String userType;
    String price;
    String seekerEmail;


    Button submitBtn;

    EditText price_value, ratingNote, review;

    RatingBar ratingBar;

    FirebaseUser firebaseUser;

    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seeker_rating_submit);




        providerId = getIntent().getStringExtra("receiver id");
        userType = getIntent().getStringExtra("user type");
        price = getIntent().getStringExtra("price");


        ratingNote = findViewById(R.id.ratingNote);
        price_value = findViewById(R.id.price_value);
        review = findViewById(R.id.review);

        submitBtn = findViewById(R.id.submitButton);

        ratingBar = findViewById(R.id.rating_bar);

        price_value.setText("Final price to be paid by the seeker to the provider: " + price);

        ratingNote.setText("Rate the Provider");

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                String userId = firebaseUser.getUid();
//                reference = FirebaseDatabase.getInstance().getReference("Seekers");
//                reference.orderByChild("userID").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        if (snapshot.exists()){
//                            seekerEmail = snapshot.child(userId).child("email").getValue(String.class);
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });

                String one = review.getText().toString().trim();

                float two = ratingBar.getRating();

                SeekerRating seekerRating = new SeekerRating(userId, providerId, one, two);


                reference = FirebaseDatabase.getInstance().getReference().child("Providers").child(providerId).child("ratings");
                reference.child(userId).setValue(seekerRating);

                //TODO remove local request, local request proposals, arrival and completion flags.
                //Local request removal
                reference = FirebaseDatabase.getInstance().getReference("LocalRequests").child(userId);
                reference.removeValue();

                //Local request proposals removal
                reference = FirebaseDatabase.getInstance().getReference("LocalRequestsProposals").child(userId);
                reference.removeValue();

                //remove connection
                reference = FirebaseDatabase.getInstance().getReference("StartingConnections").child(userId + providerId);
                reference.removeValue();

                //arrival removal
                reference = FirebaseDatabase.getInstance().getReference("SeekerLocalRequestArrivalConfirm").child(userId);
                reference.removeValue();

                //completion removal
                reference = FirebaseDatabase.getInstance().getReference("SeekerLocalRequestCompletionConfirm").child(userId);
                reference.removeValue();


                Intent backintent = new Intent(SeekerRatingSubmit.this, SeekerMainHomeActivity.class);
                startActivity(backintent);
                finish();


            }
        });


    }
}