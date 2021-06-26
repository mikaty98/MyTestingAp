package com.example.mytestingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import com.example.mytestingapp.Classes.Provider;
import com.example.mytestingapp.Classes.ProviderRating;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProviderRatingSubmit extends AppCompatActivity {


    String seekerId;
    String userType;
    String price;
    String providerName;

    Button submitBtn;

    EditText price_value, ratingNote, review;

    RatingBar ratingBar;

    FirebaseUser firebaseUser;

    DatabaseReference reference;
    private Provider provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_rating_submit);


        seekerId = getIntent().getStringExtra("receiver id");
        userType = getIntent().getStringExtra("user type");
        price = getIntent().getStringExtra("price");


        ratingNote = findViewById(R.id.ratingNote);
        price_value = findViewById(R.id.price_value);
        review = findViewById(R.id.review);

        submitBtn = findViewById(R.id.submitButton);

        ratingBar = findViewById(R.id.rating_bar);

        price_value.setText("Final price to be paid by the seeker to the provider: " + price);

        ratingNote.setText("Rate the seeker");

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                String userId = firebaseUser.getUid();
                reference = FirebaseDatabase.getInstance().getReference("Providers");
                reference.orderByChild("userID").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            providerName = snapshot.child(userId).child("userName").getValue(String.class);
                            String one = review.getText().toString().trim();
                            float two = ratingBar.getRating();

                            ProviderRating providerRating = new ProviderRating(seekerId,providerName,one,two);

                            reference = FirebaseDatabase.getInstance().getReference().child("Seekers").child(seekerId).child("ratings");
                            reference.child(userId).setValue(providerRating);

                            //completion removal
                            reference = FirebaseDatabase.getInstance().getReference("ProviderLocalRequestCompletionConfirm").child(userId);
                            reference.removeValue();

                            //setting busy to false
                            reference = FirebaseDatabase.getInstance().getReference("Providers");
                            reference.child(userId).child("sentProposal").setValue(false);

                            Intent intent = new Intent(ProviderRatingSubmit.this, ProviderHomeActivity.class);
                            startActivity(intent);
                            finish();


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });


            }
        });


    }
}